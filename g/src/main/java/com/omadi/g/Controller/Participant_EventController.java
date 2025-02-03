/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Controller;

import com.omadi.g.App;
import static com.omadi.g.Controller.EvenementController.formatDate;
import com.omadi.g.DAO.Event_participantDAO;
import com.omadi.g.Model.Adherant;
import com.omadi.g.Model.Association;
import com.omadi.g.Model.Evenement;
import com.omadi.g.Model.Participant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

// Importation des classes nécessaires pour l'exportation en Excel
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author madio
 */
public class Participant_EventController implements Initializable {

    @FXML
    private Label eventLieu;
    @FXML
    private Button DelPartt;
    @FXML
    private Button addPart;
    @FXML
    private Button EditPart;
    @FXML
    private Label eventDate;
    @FXML
    private Label eventTitre;
    @FXML
    private TableView<Participant> tableView;

    @FXML
    private TableColumn<Participant, Integer> colId;
    @FXML
    private TableColumn<Participant, String> colNom;
    @FXML
    private TableColumn<Participant, String> colPrenom;
    @FXML
    private TableColumn<Participant, Integer> colAge;
    @FXML
    private TableColumn<Participant, String> colTel;
    @FXML
    private TableColumn<Participant, Integer> colMontant;
    @FXML
    private TableColumn<Participant, String> colCE;
    @FXML
    private TableColumn<Participant, String> colAdherant;
    @FXML
    private TableColumn<Participant, String> colGenre;
    @FXML
    private TableColumn<Participant, String> colMail;

    private Evenement evenement;
    private Evenement evenementActuel;

    @FXML
    private ComboBox<String> tfCE;
    @FXML
    private ComboBox<String> tfGenre;
    @FXML
    private TextField tfMontant;
    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField tfTel;
    @FXML
    private TextField TfAge;
    @FXML
    private TextField TfMail;
    @FXML
    private ChoiceBox<String> montant;
    @FXML
    private TextField nbParticipe;
    @FXML
    private TextField MontantTot;
    @FXML
    private TextField MontanNpay;
    @FXML
    private TextField MonatntPay;
    @FXML
    private TextField nbFille;
    @FXML
    private TextField nbGarçon;
    @FXML
    private TextField rech;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("Montant"));
        colCE.setCellValueFactory(new PropertyValueFactory<>("Cheque_Espece"));
        colGenre.setCellValueFactory(new PropertyValueFactory<>("Genre"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("Mail"));
        colAdherant.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            String nom = participant.getNom();
            String prenom = participant.getPrenom();
            String adherantStatus = "non";

            try {
                if (Event_participantDAO.isAdherant(nom, prenom)) {
                    adherantStatus = "oui";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new SimpleStringProperty(adherantStatus);
        });
        tfCE.setPromptText("Moyen de paiement");
        tfCE.setItems(FXCollections.observableArrayList("Espèce", "Chèque"));

        tfGenre.setPromptText("Genre");
        tfGenre.setItems(FXCollections.observableArrayList("homme", "femme", "non binaire"));

        montant.setItems(FXCollections.observableArrayList("montant", "payé", "impayé"));
        montant.setValue("montant"); // Valeur par défaut

        montant.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            try {
                filterParticipantsByPayment(newVal);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        // Ajoutez un ChangeListener pour détecter les changements de texte en temps réel
        rech.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Appelez la méthode de recherche avec l'ID de l'événement et le terme de recherche
                int eventId = evenement.getId(); // Assurez-vous que evenement est défini et non null
                ObservableList<Participant> participants = Event_participantDAO.searchParticipantsByNomPrenom(eventId, newValue);

                // Mettez à jour la TableView avec les résultats de la recherche
                tableView.setItems(participants);
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérez l'erreur de manière appropriée, par exemple afficher une boîte de dialogue d'erreur
                showAlert("Erreur", "Erreur lors de la recherche des participants.", Alert.AlertType.ERROR);
            }
        });

        // Ajoutez ici les autres colonnes, chargements, etc.
    }

    private void filterParticipantsByPayment(String paymentStatus) throws SQLException {
        ObservableList<Participant> filteredParticipants;

        if ("payé".equals(paymentStatus)) {
            filteredParticipants = Event_participantDAO.getParticipantsByPaymentStatus(evenement.getId(), true);
        } else if ("impayé".equals(paymentStatus)) {
            filteredParticipants = Event_participantDAO.getParticipantsByPaymentStatus(evenement.getId(), false);
        } else {
            // Si aucune option n'est sélectionnée ou option non reconnue, afficher tous les participants
            filteredParticipants = Event_participantDAO.getParticipantsByEvent(evenement.getId());
        }

        tableView.setItems(filteredParticipants);
    }

    public void ChargerParticipant(Evenement evenement) {
        ObservableList<Participant> participants = FXCollections.observableArrayList();

        try {
            // Obtenez les participants de l'événement
            participants = Event_participantDAO.getParticipantsByEvent(evenement.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(participants);
        ClickTableView();
    }

    public void setEvenement(Evenement evenement) {
        String formattedDate = formatDate(evenement.getDate_Evenement());
        this.evenement = evenement;
        ChargerParticipant(evenement);
        if (eventTitre != null) {
            eventTitre.setText(evenement.GetTitre() + "," + "" + " " + evenement.getLieu() + " " + " " + formattedDate);
        }
        if (eventDate != null) {
            eventDate.setText(evenement.getDate_Evenement().toString());
            eventLieu.setText(evenement.getLieu());
            try {
                ObservableList<Participant> participants = Event_participantDAO.getParticipantsByEvent(0);
                tableView.setItems(participants);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (evenement != null) {
            int eventId = evenement.getId();
            // Utilisez eventId comme nécessaire
            try {
                ChargerParticipant(evenement);
                int participantCount = Event_participantDAO.getNParticipantByEvent(eventId);
                nbParticipe.setText("Nombre de Participant : " + participantCount);

                int MontantTotal = Event_participantDAO.getMontantTotalByEvent(eventId);
                MontantTot.setText("Montant Total : " + MontantTotal + "€");

                int NbPartcipantPaye = Event_participantDAO.getNbParticipantsPayesByEvent(eventId);
                MonatntPay.setText("Nombre de participant qui ont payé : " + NbPartcipantPaye);

                int NbPartcipantNonPaye = Event_participantDAO.getNombreParticipantsNonPayesByEvent(eventId);
                MontanNpay.setText("Participant qui n'ont pas payé : " + NbPartcipantNonPaye);

                int Nbfemmes = Event_participantDAO.getNombreFemmesByEvent(eventId);
                nbFille.setText("Nombre de Femmes : " + Nbfemmes);

                int NbHommes = Event_participantDAO.getNombreHommesByEvent(eventId);
                nbGarçon.setText("Nombre d'Hommes : " + NbHommes);

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors du chargement du nombre des données.", Alert.AlertType.ERROR);
            }
        } else {
            // Gérez le cas où evenement est null, par exemple, affichez un message d'erreur ou faites quelque chose d'autre
            System.err.println("evenement is null");
        }
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'le' d MMMM yyyy", Locale.FRENCH);
        return date.format(formatter);
    }

    private void ChargerParticipants() {
        ObservableList<Participant> participants = FXCollections.observableArrayList();

        try {
            participants = Event_participantDAO.getParticipantsByEvent(evenement.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des participants.", Alert.AlertType.ERROR);
        }

        tableView.setItems(participants);
    }

    private void updateStatistics() {
        if (evenement == null) {
            return;
        }

        try {
            int eventId = evenement.getId();

            int participantCount = Event_participantDAO.getNParticipantByEvent(eventId);
            nbParticipe.setText("Nombre de Participants : " + participantCount);

            int MontantTotal = Event_participantDAO.getMontantTotalByEvent(eventId);
            MontantTot.setText("Montant Total : " + MontantTotal + "€");

            int NbPartcipantPaye = Event_participantDAO.getNbParticipantsPayesByEvent(eventId);
            MonatntPay.setText("Nombre de participants qui ont payé : " + NbPartcipantPaye);

            int NbPartcipantNonPaye = Event_participantDAO.getNombreParticipantsNonPayesByEvent(eventId);
            MontanNpay.setText("Participants qui n'ont pas payé : " + NbPartcipantNonPaye);

            int Nbfemmes = Event_participantDAO.getNombreFemmesByEvent(eventId);
            nbFille.setText("Nombre de Femmes : " + Nbfemmes);

            int NbHommes = Event_participantDAO.getNombreHommesByEvent(eventId);
            nbGarçon.setText("Nombre d'Hommes : " + NbHommes);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour des statistiques.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void ClickAcc(ActionEvent event) throws IOException {
        App.setRoot("Evenement");
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void ClickEditPart(ActionEvent event) {
        // Récupérer le participant sélectionné dans la TableView
        Participant selectedParticipant = tableView.getSelectionModel().getSelectedItem();
        if (selectedParticipant == null) {
            showAlert("Erreur", "Veuillez sélectionner un participant à modifier.", Alert.AlertType.ERROR);
            return;
        }

        // Récupérer les valeurs des champs de texte
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String telephone = tfTel.getText();
        String mail = TfMail.getText(); // L'email peut être null

        // Initialiser les éléments de la ChoiceBox si ce n'est pas déjà fait
        if (tfGenre.getItems().isEmpty()) {
            ObservableList<String> genres = FXCollections.observableArrayList("Homme", "Femme", "Non binaire");
            tfGenre.setItems(genres);
        }

        if (tfCE.getItems().isEmpty()) {
            ObservableList<String> modesPaiement = FXCollections.observableArrayList("Espèce", "Chèque");
            tfCE.setItems(modesPaiement);
        }

        // Récupérer les éléments sélectionnés
        String genre = tfGenre.getSelectionModel().getSelectedItem();
        String ce = tfCE.getSelectionModel().getSelectedItem();

        // Afficher les sélections dans la console
        if (genre != null) {
            System.out.println("Genre sélectionné : " + genre);
        }

        if (ce != null) {
            System.out.println("Mode de paiement sélectionné : " + ce);
        }

        // Analyser l'âge
        Integer age = null;
        if (!TfAge.getText().isEmpty()) {
            try {
                age = Integer.parseInt(TfAge.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur de format", "L'âge doit être un nombre entier.", Alert.AlertType.ERROR);
                return;
            }
        }

        // Analyser le montant
        Integer montant = null;
        if (!tfMontant.getText().isEmpty()) {
            try {
                montant = Integer.parseInt(tfMontant.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur de format", "Le montant doit être un nombre entier.", Alert.AlertType.ERROR);
                return;
            }
        } else {
            // Afficher une alerte si aucun montant n'est saisi
            showAlert("Erreur de saisie", "Veuillez saisir une valeur dans le champ Montant.", Alert.AlertType.ERROR);
            return;
        }

        // Mettre à jour les informations du participant sélectionné
        selectedParticipant.setNom(nom);
        selectedParticipant.setPrenom(prenom);
        selectedParticipant.setTel(telephone);
        selectedParticipant.setMail(mail);
        selectedParticipant.setAge(age);
        selectedParticipant.setMontant(montant);
        selectedParticipant.setCheque_Espece(ce);
        selectedParticipant.setGenre(genre);

        try {
            // Mettre à jour le participant dans la base de données
            Event_participantDAO.updateParticipant(selectedParticipant);

            // Rafraîchir la TableView des participants
            ChargerParticipants();
            updateStatistics();

            // Réinitialiser les champs du formulaire après la mise à jour
            tfNom.clear();
            tfPrenom.clear();
            TfAge.clear();
            TfMail.clear();
            tfTel.clear();
            tfMontant.clear();
            tfCE.setValue(null);
            tfGenre.setValue(null);

            // Afficher une alerte de succès
            showAlert("Succès", "Participant mis à jour avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour du participant.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void ClickDeletPart(ActionEvent event) {
        Participant selectedParticipant = tableView.getSelectionModel().getSelectedItem();
        if (selectedParticipant == null) {
            showAlert("Erreur", "Veuillez sélectionner un participant à supprimer.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Supprimer la participation du participant à l'événement
            Event_participantDAO.removeParticipantFromEvent(selectedParticipant.getId(), evenement.getId());

            // Rafraîchir la TableView des participants
            ChargerParticipants();
            updateStatistics();

            // Afficher une alerte de succès
            showAlert("Succès", "Participation du participant supprimée avec succès.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la suppression de la participation du participant.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void ClickAddPart(ActionEvent event) {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String telephone = tfTel.getText();
        String mail = TfMail.getText(); // L'email peut être null

        // Initialiser les éléments de la ChoiceBox si ce n'est pas déjà fait
        if (tfGenre.getItems().isEmpty()) {
            ObservableList<String> genres = FXCollections.observableArrayList("Homme", "Femme", "Non binaire");
            tfGenre.setItems(genres);
        }

        if (tfCE.getItems().isEmpty()) {
            ObservableList<String> modesPaiement = FXCollections.observableArrayList("Espèce", "Chèque");
            tfCE.setItems(modesPaiement);
        }

        // Récupérer les éléments sélectionnés
        String genre = tfGenre.getSelectionModel().getSelectedItem();
        String ce = tfCE.getSelectionModel().getSelectedItem();

        // Afficher les sélections dans la console
        if (genre != null) {
            System.out.println("Genre sélectionné : " + genre);
        }

        if (ce != null) {
            System.out.println("Mode de paiement sélectionné : " + ce);
        }

        // Analyser l'âge
        Integer age = null;
        if (!TfAge.getText().isEmpty()) {
            try {
                age = Integer.parseInt(TfAge.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur de format", "L'âge doit être un nombre entier.", Alert.AlertType.ERROR);
                return;
            }
        } else {
            // Afficher une alerte si l'âge est vide
            showAlert("Champ requis", "Veuillez saisir l'âge du participant.", Alert.AlertType.ERROR);
            return;
        }

        // Analyser le montant
        Integer montant = null;
        if (!tfMontant.getText().isEmpty()) {
            try {
                montant = Integer.parseInt(tfMontant.getText());
            } catch (NumberFormatException e) {
                showAlert("Erreur de format", "Le montant doit être un nombre entier.", Alert.AlertType.ERROR);
                return;
            }
        } else {
            // Afficher une alerte si aucun montant n'est saisi
            showAlert("Erreur de saisie", "Veuillez saisir une valeur dans le champ Montant.", Alert.AlertType.ERROR);
            return;
        }

        Participant participant = new Participant(0, nom, prenom, age, telephone, montant, ce, mail, genre);
        try {
            // Ajouter le participant et récupérer son ID
            int participantId = Event_participantDAO.addParticipant(participant);

            // Associer le participant à l'événement
            Event_participantDAO.addParticipantToEvent(participantId, evenement.getId());

            // Rafraîchir la TableView des participants
            ChargerParticipants();
            updateStatistics();

            // Réinitialiser les champs du formulaire
            tfNom.clear();
            tfPrenom.clear();
            TfAge.clear();
            TfMail.clear();
            tfTel.clear();
            tfMontant.clear();
            tfCE.setValue(null);
            tfGenre.setValue(null);

            // Afficher une alerte de succès
            showAlert("Succès", "Participant ajouté avec succès à l'événement.", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ajout du participant.", Alert.AlertType.ERROR);
        }
    }

    public void initData(Object parameter) {
        if (parameter instanceof Evenement) {
            this.evenement = (Evenement) parameter;
            eventTitre.setText(evenement.GetTitre());

        }
    }

    @FXML
    void clickChoose(ActionEvent event) {
        String selectedGenre = tfGenre.getSelectionModel().getSelectedItem();
        String selectedCE = tfCE.getSelectionModel().getSelectedItem();

        if (selectedGenre != null && selectedCE != null) {
            // Utilisation des sélections ici
            System.out.println("Genre sélectionné : " + selectedGenre);
            System.out.println("Mode de paiement sélectionné : " + selectedCE);
        } else {
            System.out.println("Sélection non valide.");
        }
    }
    private int participantid = 0;

    private void selectParticipant(Participant participant) {

        tfNom.setText(participant.getNom());
        tfPrenom.setText(participant.getPrenom());
        tfGenre.setValue(participant.getGenre());
        TfMail.setText(participant.getMail());
        TfAge.setText(String.valueOf(participant.getAge()));
        tfTel.setText(participant.getTelephone());
        tfMontant.setText(participant.getMontant() == 0 ? "" : String.valueOf(participant.getMontant()));
        tfCE.setValue(participant.getCheque_Espece());
        participantid = participant.getId();
    }

    private void ClickTableView() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Participant participant = tableView.getSelectionModel().getSelectedItem();
                if (participant != null) {
                    selectParticipant(participant);
                }
            }
        });
    }

    @FXML
    void chercheParticipant(ActionEvent event) {
        String searchTerm = rech.getText().trim();

        // Vérifiez si le champ de recherche n'est pas vide
        if (!searchTerm.isEmpty()) {
            try {
                // Appel à votre DAO pour récupérer les participants correspondant au terme de recherche
                ObservableList<Participant> participants = Event_participantDAO.searchParticipantsByNomPrenom(evenement.getId(), searchTerm);

                // Mettez à jour votre TableView avec les participants trouvés
                tableView.setItems(participants);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la recherche des participants.", Alert.AlertType.ERROR);
            }
        } else {
            // Si le champ de recherche est vide, chargez tous les participants de l'événement
            ChargerParticipants(); // Vous devez implémenter cette méthode pour charger tous les participants
        }
    }

    @FXML
    void ClickEditEvent(ActionEvent event) {
        try {
            // Chargez le formulaire de modification
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/omadi/g/EditEvent.fxml"));
            Parent root = fxmlLoader.load();

            // Récupérez le contrôleur du formulaire de modification
            EditEventController editEventController = fxmlLoader.getController();

            editEventController.setEvenement(evenement);

            // Configurez la fenêtre modale
            Stage stage = new Stage();
            stage.setTitle("Modifier un Événement");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);

            // Montrez la fenêtre et attendez qu'elle soit fermée
            stage.showAndWait();

            // Après la fermeture, mettez à jour l'événement et les participants
            updateEventDetails();
            updateStatistics();
            ChargerParticipants();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'ouverture du formulaire.", Alert.AlertType.ERROR);
        }
    }

    private void updateEventDetails() {
        if (evenement != null) {
            String formattedDate = formatDate(evenement.getDate_Evenement());
            eventTitre.setText(evenement.GetTitre() + "," + "" + " " + evenement.getLieu() + " " + " " + formattedDate);

        }
    }

    @FXML
    private FileChooser fileChooser = new FileChooser();

    @FXML
    public void downfile(ActionEvent event) {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        // Appelez la méthode saveFile() pour ouvrir la boîte de dialogue de sauvegarde
        saveFile();
    }

    public void saveFile() {
        fileChooser.setTitle("Enregistrer le fichier");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx")
        );

        // Ouvrir la boîte de dialogue de sauvegarde
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            String extension = getFileExtension(file);
            if ("xlsx".equalsIgnoreCase(extension)) {
                exportToExcel(file);
            } else {
                showAlert("Erreur", "Format de fichier non supporté", AlertType.ERROR);
            }
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return "";
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    private void exportToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Participants");

            // En-têtes de colonnes
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Nom");
            headerRow.createCell(2).setCellValue("Prenom");
            headerRow.createCell(3).setCellValue("Age");
            headerRow.createCell(4).setCellValue("Telephone");
            headerRow.createCell(5).setCellValue("Montant");
            headerRow.createCell(6).setCellValue("Cheque/Espece");
            headerRow.createCell(7).setCellValue("Genre");
            headerRow.createCell(8).setCellValue("Mail");
            headerRow.createCell(9).setCellValue("Adherant");

            // Données des participants
            ObservableList<Participant> participants = tableView.getItems();
            int rowNum = 1;
            for (Participant participant : participants) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(participant.getId());
                row.createCell(1).setCellValue(participant.getNom());
                row.createCell(2).setCellValue(participant.getPrenom());
                row.createCell(3).setCellValue(participant.getAge());
                row.createCell(4).setCellValue(participant.getTelephone());
                row.createCell(5).setCellValue(participant.getMontant());
                row.createCell(6).setCellValue(participant.getCheque_Espece());
                row.createCell(7).setCellValue(participant.getGenre());
                row.createCell(8).setCellValue(participant.getMail());

                // Obtenir la valeur Adherant en utilisant la même logique que dans setCellValueFactory
                String nom = participant.getNom();
                String prenom = participant.getPrenom();
                String adherantStatus = "non";

                try {
                    if (Event_participantDAO.isAdherant(nom, prenom)) {
                        adherantStatus = "oui";
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                row.createCell(9).setCellValue(adherantStatus);
            }

            // Ajuster la largeur des colonnes
            for (int i = 0; i < 10; i++) {
                sheet.autoSizeColumn(i);
            }

            // Enregistrement du fichier Excel
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

            showAlert("Succès", "Fichier Excel exporté avec succès", Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'exportation du fichier Excel", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
