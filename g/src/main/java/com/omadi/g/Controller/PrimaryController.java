package com.omadi.g.Controller;

import com.omadi.g.Model.Adherant;
import com.omadi.g.DAO.AdherantDAO;
import com.omadi.g.App;
import com.omadi.g.Model.Association;
import com.omadi.g.DAO.AssociationDAO;
import com.omadi.g.DAO.Event_participantDAO;
import com.omadi.g.Model.Participant;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import java.sql.Date;
import java.time.LocalDate;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.Alert.AlertType;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrimaryController implements Initializable {

    @FXML
    private TableView<Adherant> tableView;

    @FXML
    private TableColumn<Adherant, String> colAdr;

    @FXML
    private TableColumn<Adherant, String> colCE;

    @FXML
    private TableColumn<Adherant, Integer> colCotis;

    @FXML
    private TableColumn<Adherant, Date> colDate;

    @FXML
    private TableColumn<Adherant, String> colMail;

    @FXML
    private TableColumn<Adherant, String> colNom;

    @FXML
    private TableColumn<Adherant, String> colPrenom;

    @FXML
    private TableColumn<Adherant, String> colTel;

    @FXML
    private TableColumn<Adherant, Integer> colId;
    private int selectedAdherantId = 0;

    private ObservableList<Adherant> adherantList = FXCollections.observableArrayList();

    private ObservableList<Association> Assoc = FXCollections.observableArrayList();
    @FXML
    private ChoiceBox<Association> assoc;
    @FXML
    private ChoiceBox<String> cotis;
    @FXML
    private ChoiceBox<Integer> DateAn;
    @FXML
    private TextField rech;
    @FXML
    private TextField nbAh;

    @FXML
    private TextField cotiP;

    @FXML
    private TextField cotiNp;
    @FXML
    private ImageView imgAcc;

    @FXML
    private ImageView imgLog;

    @FXML
    private ImageView imgStat;

    @FXML
    private TextField cotiTot;

    @FXML
    private TableColumn<Adherant, String> colAssoc;
    @FXML
    private TextField tfNom;
    @FXML
    private TextField tfPrenom;
    @FXML
    private TextField TfAdr;
    @FXML
    private TextField TfMail;
    @FXML
    private TextField tfCotis;
    @FXML
    private ComboBox<String> tfCE;
    @FXML
    private TextField tfTel;
    @FXML
    private ComboBox<Association> tfAsso;
    @FXML
    private Button EditAdh;
    @FXML
    private Button DelAh;
    @FXML
    private Button addAdh;
    private int selectedAssociationId = 0;

    @FXML
    void ClickAcc(ActionEvent event) throws IOException {
        App.setRoot("Acceuil");
    }

    @FXML
    void rechercher(ActionEvent event) throws SQLException {
        String searchTerm = rech.getText().trim();

        // Vérifiez si le champ de recherche n'est pas vide
        if (!searchTerm.isEmpty()) {
            try {
                // Appel à votre DAO pour récupérer les participants correspondant au terme de recherche
                ObservableList<Adherant> adherants = AdherantDAO.searchAdherantsByNomPrenom(selectedAssociationId, searchTerm);

                // Mettez à jour votre TableView avec les participants trouvés
                tableView.setItems(adherants);
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Erreur lors de la recherche des participants.", Alert.AlertType.ERROR);
            }
        } else {
            // Si le champ de recherche est vide, chargez tous les participants de l'événement
            getAdherantsByAssociation(0, null, null); // Vous devez implémenter cette méthode pour charger tous les participants
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ClickAddAdh(ActionEvent event) {
        try {

            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String adresse = TfAdr.getText().trim();
            String email = TfMail.getText().trim();
            String tel = tfTel.getText().trim();
            String cotisStr = tfCotis.getText().trim();
            Integer cotisation = cotisStr.isEmpty() ? null : Integer.parseInt(cotisStr);

            Association selectedAssociation = (Association) tfAsso.getValue();
            int associationId = selectedAssociation == null ? 0 : selectedAssociation.GetId();

            String moyenPaiement = (String) tfCE.getValue();

            java.util.Date date_adhesion = new java.util.Date();

            if (nom.isEmpty() || prenom.isEmpty() || associationId == 0) {
                showAlert("Erreur", "Les champs 'Nom', 'Prénom' et 'Association' sont obligatoires.", Alert.AlertType.ERROR);
                return;
            }

            if (cotisation == null) {
                moyenPaiement = "";
            }

            java.sql.Date sqlDate = new java.sql.Date(date_adhesion.getTime());
            String associationNom1 = null;

            Adherant newAdherant = new Adherant(0, nom, prenom, adresse, email, cotisation, moyenPaiement, tel, sqlDate, associationNom1, associationId);

            AdherantDAO.addAdherant(newAdherant);
            adherantList.add(newAdherant);

            showAlert("Succès", "L'adhérant a été ajouté avec succès.", Alert.AlertType.INFORMATION);

            tfNom.clear();
            tfPrenom.clear();
            TfAdr.clear();
            TfMail.clear();
            tfTel.clear();
            tfCotis.clear();
            tfCE.setValue("Moyen de paiement"); // Remettre le texte initial
            tfAsso.setValue(null); // Remettre à null ou sélectionnez "Sélectionnez une association"

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un montant de cotisation valide.", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'adhérant.", Alert.AlertType.ERROR);
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    @FXML
    void ClickDeletAdh(ActionEvent event) {
        Adherant adherant = tableView.getSelectionModel().getSelectedItem();
        if (adherant != null) {
            try {

                AdherantDAO.deleteAdherant(adherant);
                tableView.getItems().remove(adherant);
                showAlert("Succès", "L'adhérent a été supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Une erreur est survenue lors de la suppression de l'adhérent.", Alert.AlertType.ERROR);
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            showAlert("Information", "Veuillez sélectionner un adhérent à supprimer.", Alert.AlertType.INFORMATION);
        }
        tfNom.clear();
        tfPrenom.clear();
        TfAdr.clear();
        TfMail.clear();
        tfTel.clear();
        tfCotis.clear();
        tfCE.setValue("Moyen de paiement"); // Remettre le texte initial
        tfAsso.setValue(null);
    }

    @FXML
    private void ClickEditAdh(ActionEvent event) {
        Adherant selectedAdherant = tableView.getSelectionModel().getSelectedItem();
        if (selectedAdherant != null) {
            selectedAdherantId = selectedAdherant.getId();

            // Capture the original values
            Adherant originalAdherant = new Adherant(
                    selectedAdherant.getId(),
                    selectedAdherant.getNom(),
                    selectedAdherant.getPrenom(),
                    selectedAdherant.getAdresse(),
                    selectedAdherant.getMail(),
                    selectedAdherant.getMontant(),
                    selectedAdherant.getCheque_Espece(),
                    selectedAdherant.getTelephone(),
                    selectedAdherant.getDate_adhesion(),
                    selectedAdherant.getAssociationNom(),
                    selectedAdherant.getAssociationId()
            );

            Association selectedAssociation = tfAsso.getValue();
            int associationId = selectedAssociation != null ? selectedAssociation.GetId() : originalAdherant.getAssociationId();

            Adherant updatedAdherant = new Adherant(
                    selectedAdherantId,
                    tfNom.getText().trim(),
                    tfPrenom.getText().trim(),
                    TfAdr.getText().trim(),
                    TfMail.getText().trim(),
                    Integer.parseInt(tfCotis.getText().trim()),
                    (String) tfCE.getValue(),
                    tfTel.getText().trim(),
                    originalAdherant.getDate_adhesion(),
                    selectedAssociation != null ? selectedAssociation.GetNom() : originalAdherant.getAssociationNom(),
                    associationId
            );

            try {
                AdherantDAO.updateAdherant(updatedAdherant, originalAdherant);

                showAlert("Succès", "Les informations de l'adhérent ont été mises à jour avec succès.", Alert.AlertType.INFORMATION);
                ObservableList<Adherant> adherants = tableView.getItems();
                int index = adherants.indexOf(selectedAdherant);
                if (index != -1) {
                    adherants.set(index, updatedAdherant);
                }

                tableView.refresh();

            } catch (SQLException e) {
                showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de l'adhérent.", Alert.AlertType.ERROR);
                Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, e);
            }
        } else {
            showAlert("Information", "Veuillez sélectionner un adhérent à modifier.", Alert.AlertType.INFORMATION);
        }
        tfNom.clear();
        tfPrenom.clear();
        TfAdr.clear();
        TfMail.clear();
        tfTel.clear();
        tfCotis.clear();
        tfCE.setValue("Moyen de paiement"); // Remettre le texte initial
        tfAsso.setValue(null);
    }

    private void ClickTableView() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Adherant adherant = tableView.getSelectionModel().getSelectedItem();
                if (adherant != null) {
                    selectAdherant(adherant);
                }
            }
        });
    }

    private void selectAdherant(Adherant adherant) {

        tfNom.setText(adherant.getNom());
        tfPrenom.setText(adherant.getPrenom());
        TfAdr.setText(adherant.getAdresse());
        TfMail.setText(adherant.getMail());
        tfTel.setText(adherant.getTelephone());
        tfCotis.setText(adherant.getMontant() == 0 ? "" : String.valueOf(adherant.getMontant()));
        tfCE.setValue(adherant.getCheque_Espece());

        Association selectedAssociation = tfAsso.getItems().stream()
                .filter(a -> a.GetNom().equals(adherant.getAssociationNom()))
                .findFirst()
                .orElse(null);
        tfAsso.setValue(selectedAssociation);

        selectedAdherantId = adherant.getId();
    }

    private void getAdherantsByAssociation(int associationId, Integer year, String selectedCotis) throws SQLException {
        ObservableList<Adherant> adherants = AdherantDAO.getAdherantsByAssociation(associationId, year, selectedCotis);
        FXCollections.sort(adherants, (a1, a2) -> a2.getDate_adhesion().compareTo(a1.getDate_adhesion()));
        tableView.setItems(adherants);
        Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Adherents ajoutés pour l'association ID : {0}", associationId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("Nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("Prenom"));
        colAdr.setCellValueFactory(new PropertyValueFactory<>("Adresse"));
        colMail.setCellValueFactory(new PropertyValueFactory<>("Mail"));
        colCotis.setCellValueFactory(new PropertyValueFactory<>("Montant"));
        colCE.setCellValueFactory(new PropertyValueFactory<>("Cheque_Espece"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date_adhesion"));
        colAssoc.setCellValueFactory(new PropertyValueFactory<>("associationNom"));

        getAdhesionYears();

        try {
            getAssoc();
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }

        assoc.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTable();
            updateStats();
        });

        DateAn.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateTable();
            updateStats();
        });
        cotis.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            updateStats();
        });
        cotis.setItems(FXCollections.observableArrayList("Cotisation", "Payé", "Impayé"));
        cotis.setValue("Cotisation");

        updateTable();
        updateStats();

        try {
            getAdherantsByAssociation(0, null, null);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }

        colCotis.setCellFactory(new Callback<TableColumn<Adherant, Integer>, TableCell<Adherant, Integer>>() {
            public TableCell<Adherant, Integer> call(TableColumn<Adherant, Integer> param) {
                return new TableCell<Adherant, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else if (item == -1) {
                            setText("");
                        } else {
                            setText(item.toString());
                        }
                    }
                };
            }
        });
        tfCE.setPromptText("Moyen de paiement");
        tfCE.setItems(FXCollections.observableArrayList("Espèce", "Chèque"));

        try {
            ObservableList<Association> associations = AssociationDAO.getAllAssoc(0);

            tfAsso.setPromptText("Association");
            tfAsso.setItems(associations);

            Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Association ajouté : {0}", associations);
        } catch (SQLException e) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, e);
        }
        ClickTableView();
        rech.setPromptText("rechercher");

        rech.textProperty().addListener((observable, oldValue, newValue) -> {
            try {

                String searchTerm = rech.getText().trim();
                ObservableList<Adherant> adherants = AdherantDAO.searchAdherantsByNomPrenom(selectedAdherantId, searchTerm);

                // Mettez à jour la TableView avec les résultats de la recherche
                tableView.setItems(adherants);
            } catch (SQLException e) {
                e.printStackTrace();
                // Gérez l'erreur de manière appropriée, par exemple afficher une boîte de dialogue d'erreur
                showAlert("Erreur", "Erreur lors de la recherche des participants.", Alert.AlertType.ERROR);
            }
        });

    }

    private void NbAdherant(int associationId, Integer year, String selectedCotis) {
        int count = AdherantDAO.getAdherantCount(associationId, year, selectedCotis);
        String formattedCount = "nombre d'adherant : " + count;
        nbAh.setText(formattedCount);
        Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Nombre d'adhesion mis à jour: {0}", formattedCount);
    }

    private void getNbCpay(int associationId, Integer year) {
        int count = AdherantDAO.getNbCpay(associationId, year);
        String formattedCount = "Nombre de cotisation payé  : " + count;
        cotiP.setText(formattedCount);
        Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Nombre de cotisation payé: {0}", formattedCount);
    }

    private void getNbCNpay(int associationId, Integer year) {
        int count = AdherantDAO.getCNpay(associationId, year);
        String formattedCount = "Nombre de cotisation non payé  : " + count;
        cotiNp.setText(formattedCount);
        Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Nombre de cotisation non payé:", formattedCount);
    }

    private void getCotisT(int associationId, Integer year, String cotisFilter) {

        int total = AdherantDAO.getCotisTot(associationId, year, cotisFilter);

        cotiTot.setText("Cotisation totale : " + total + " €");
        Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Cotisation totale mise à jour : {0} €", total);
    }

    private void getAssoc() throws SQLException {
        try {
            ObservableList<Association> a = AssociationDAO.getAllAssoc(0);
            a.add(0, new Association(0, "Tous"));
            assoc.setItems(a);
            if (!a.isEmpty()) {
                assoc.setValue(a.get(0));
            }
            Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Association ajouté : {0}", a);
        } catch (SQLException e) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void getAdhesionYears() {
        try {
            Set<Integer> uniqueYears = new HashSet<>();
            ObservableList<Integer> years = AdherantDAO.getAllAdhesionYears();
            int currentYear = LocalDate.now().getYear();

            uniqueYears.addAll(years);
            uniqueYears.add(currentYear); // Ajouter l'année actuelle

            years = FXCollections.observableArrayList(uniqueYears); // Convertir l'ensemble en liste
            Collections.sort(years);

            years.add(0, null); // Ajouter l'option "Toutes les Dates" au début
            DateAn.setItems(years);

            DateAn.setConverter(new javafx.util.StringConverter<Integer>() {
                @Override
                public String toString(Integer year) {
                    return year == null ? "Toutes les Dates" : year.toString();
                }

                @Override
                public Integer fromString(String string) {
                    return "Toutes les Dates".equals(string) ? null : Integer.valueOf(string);
                }
            });

            DateAn.setValue(null); // Sélectionner par défaut "Toutes les Dates"

            Logger.getLogger(PrimaryController.class.getName()).log(Level.INFO, "Années d'adhésion ajoutées : {0}", years);
        } catch (SQLException e) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, "Erreur lors de la récupération des années d'adhésion", e);
        }
    }

    private void updateTable() {
        Association selectedAssoc = assoc.getSelectionModel().getSelectedItem();
        Integer selectedYear = DateAn.getSelectionModel().getSelectedItem();
        String selectedCotis = cotis.getValue();
        int associationId = (selectedAssoc != null) ? selectedAssoc.GetId() : 0;

        try {
            getAdherantsByAssociation(associationId, selectedYear, selectedCotis);
        } catch (SQLException ex) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateStats() {

        Association selectedAssoc = assoc.getValue();
        int associationId = (selectedAssoc != null) ? selectedAssoc.GetId() : 0; // Utiliser 0 si rien n'est sélectionné

        Integer selectedYear = DateAn.getValue();
        String selectedCotis = cotis.getValue();
        String selectedCotisFilter = cotis.getValue().toString();

        NbAdherant(associationId, selectedYear, selectedCotis);
        getNbCpay(associationId, selectedYear);
        getNbCNpay(associationId, selectedYear);
        getCotisT(associationId, selectedYear, selectedCotisFilter);

        updateTable();
    }

    @FXML
   void downloadfile(ActionEvent event) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Adherants");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        // Créer une ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Nom", "Prenom", "Adresse", "Mail", "Montant", "Cheque/Espece", "Telephone", "Date Adhesion", "Association"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }

        // Obtenir la liste filtrée des adhérents affichés
        ObservableList<Adherant> filteredAdherants = tableView.getItems();

        // Ajouter les données des adhérents
        int rowNum = 1;
        for (Adherant adherant : filteredAdherants) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(String.valueOf(adherant.getId()));
            row.createCell(1).setCellValue(adherant.getNom());
            row.createCell(2).setCellValue(adherant.getPrenom());
            row.createCell(3).setCellValue(adherant.getAdresse());
            row.createCell(4).setCellValue(adherant.getMail());
            row.createCell(5).setCellValue(adherant.getMontant());
            row.createCell(6).setCellValue(adherant.getCheque_Espece());
            row.createCell(7).setCellValue(adherant.getTelephone());
            row.createCell(8).setCellValue(adherant.getDate_adhesion().toString());
            row.createCell(9).setCellValue(adherant.getAssociationNom());
        }

        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("adherants.xlsx");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel", "*.xlsx"));
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());

        if (file != null) {
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
                showAlert("Succès", "Les données ont été exportées avec succès en Excel.", Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                showAlert("Erreur", "Une erreur est survenue lors de l'exportation en Excel.", Alert.AlertType.ERROR);
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
