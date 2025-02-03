package com.omadi.g.Controller;

import com.omadi.g.App;
import com.omadi.g.Controller.AddEventController;
import com.omadi.g.Model.Evenement;
import com.omadi.g.DAO.EvenementDao;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EvenementController implements Initializable {

    @FXML
    private FlowPane buttonContainer;

    private ObservableMap<Evenement, Pane> eventPaneMap = FXCollections.observableHashMap();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reloadEvents();
    }

    public void reloadEvents() {
        EvenementDao evenementDao = new EvenementDao();
        ObservableList<Evenement> evenements = evenementDao.getAllEvenements(); // Assurez-vous que cette méthode retourne les données actualisées

        eventPaneMap.clear();
        buttonContainer.getChildren().clear();

        for (Evenement evenement : evenements) {
            Pane pane = createEventPane(evenement);
            eventPaneMap.put(evenement, pane);
            buttonContainer.getChildren().add(pane);
        }

        Pane addButton = createAddButton();
        buttonContainer.getChildren().add(addButton);
    }

    private Pane createEventPane(Evenement evenement) {
        TextField tfTitre = new TextField(evenement.GetTitre());
        tfTitre.setStyle("-fx-background-color: transparent; -fx-text-fill: #4a4747;"
                + " -fx-alignment: CENTER;-fx-font-family: 'Yu Gothic UI Semibold';");
        Pane pane = new Pane();
        pane.getChildren().add(tfTitre);
        pane.getStyleClass().add("Button");

        tfTitre.setLayoutY(55);
        tfTitre.setPrefWidth(131.5);
        tfTitre.setPrefHeight(25);

        tfTitre.setLayoutY(49.0);
        tfTitre.setEditable(false);
        tfTitre.setCursor(Cursor.DEFAULT);

        pane.setPrefHeight(113.0);
        pane.setPrefWidth(131.0);
        pane.setLayoutX(34.0);
        pane.setLayoutY(168.0);

        pane.setOnMouseEntered(event -> {
            pane.setStyle("-fx-background-color: #72FAA8; -fx-background-radius: 10px;  -fx-cursor:hand;");
        });
        pane.setOnMouseExited(event -> {
            pane.setStyle("-fx-background-color: white; -fx-background-radius: 5px; -fx-cursor:hand;");
        });
        pane.setOnMouseClicked(event -> {
            ClickEvent(evenement);
        });

        String formattedDate = formatDate(evenement.getDate_Evenement());
        TextField tfDate = new TextField(formattedDate);
        tfDate.setPrefWidth(131.5);
        tfDate.setPrefHeight(25);
        tfDate.setLayoutX(25);
        tfDate.setLayoutY(-4);
        tfDate.setStyle("-fx-border-radius:5px ; -fx-background-color: transparent; "
                + "-fx-effect :  ;"
                + " -fx-alignment:ENTER;"
                + "-fx-background-radius:5px;"
                + "-fx-font-family: 'Verdana';-fx-font-size: 8px;");

        tfDate.setTextFormatter(new TextFormatter<>(change -> {
            tfDate.setPadding(new Insets((tfDate.getPrefHeight() - tfDate.getFont().getSize()) / 2, 5, 0, 5));
            return change;
        }));
        tfDate.setEditable(false);
        tfDate.setCursor(Cursor.DEFAULT);
        pane.getChildren().add(tfDate);

        TextField tfStatut = new TextField();
        tfStatut.setPrefHeight(15);
        tfStatut.setPrefWidth(65);
        tfStatut.setLayoutX(0);
        tfStatut.setLayoutY(88.5);
        tfStatut.setStyle("  -fx-background-color: green; "
                + "-fx-border-radius : 0,5px; -fx-background-radius:2px");
        tfStatut.setEditable(false);
        tfStatut.setCursor(Cursor.DEFAULT);

        LocalDate currentDate = LocalDate.now();
        if (evenement.getDate_Evenement().isAfter(currentDate)) {
            tfStatut.setText("en cours");
            tfStatut.setStyle("-fx-background-color: transparent; -fx-border-radius: 0,5px; -fx-background-radius: 2px; -fx-text-fill: #3F4E92;");
        } else {
            tfStatut.setText("terminé");
            tfStatut.setStyle("-fx-background-color: transparent ;  -fx-text-fill: #FF7373;-fx-border-radius: 0,5px; -fx-background-radius: 2px;");
        }
        pane.getChildren().add(tfStatut);

        Button delButton = new Button();
        delButton.setText("");
        delButton.setPrefHeight(10);
        delButton.setPrefWidth(10);
        delButton.setLayoutX(-18);
        delButton.setLayoutY(-19);
        delButton.setStyle("-fx-background-color: white; -fx-background-radius: 5px;"
                + "-fx-alignment:CENTER_RIGHT;");
        ImageView imgDel = new ImageView(new Image("file:/C:/Users/madio/Downloads/deleteIcon (1).png"));
        imgDel.setFitHeight(10);
        imgDel.setFitWidth(10);
        StackPane imageContainer = new StackPane(imgDel);
        imageContainer.setPrefSize(42, 44); // Taille du conteneur d'image (facultatif)

        // Ajouter le StackPane au bouton
        delButton.setGraphic(imageContainer);
        pane.getChildren().add(delButton);
        delButton.setOnAction(event -> DeletEvent(evenement));
        delButton.setOnMouseEntered(event -> {
            delButton.setStyle("-fx-background-color: white; -fx-background-radius: 10px;");
        });
        delButton.setOnMouseEntered(event -> {
            delButton.setStyle("-fx-background-color: #FF7373; -fx-background-radius: 10px;");
        });
        delButton.setOnMouseExited(event -> {
            delButton.setStyle("-fx-background-color: white; -fx-background-radius: 5px;");
        });
        tfTitre.setMouseTransparent(true);
        tfDate.setMouseTransparent(true);
        tfStatut.setMouseTransparent(true);

        return pane;
    }

    private Pane createAddButton() {
        Pane addButton = new Pane();
        addButton.setPrefHeight(113.0);
        addButton.setPrefWidth(131.0);
        addButton.setLayoutX(34.0);
        addButton.setLayoutY(168.0);
        addButton.setStyle("-fx-background-color:transparent; "
                + "-fx-background-radius: 5px;"
                + "-fx-alignment:CENTER_RIGHT;"
                + "-fx-effect: dropshadow(gaussian, rgba(171, 171, 171, 0.65), 13, 0, 0, 5); -fx-cursor:hand; ");
        addButton.setOnMouseClicked(event -> {
            try {
                AddEvent();
            } catch (IOException ex) {
                Logger.getLogger(EvenementController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        ImageView imageView = new ImageView(new Image("file:/C:/Users/madio/Downloads/AddIcon.png"));
        imageView.setFitHeight(140.0);
        imageView.setFitWidth(150.0);
        imageView.setLayoutX(-9);
        imageView.setLayoutY(-17);
        addButton.getChildren().add(imageView);

        addButton.setOnMouseEntered(event -> {
            addButton.setStyle("-fx-background-color: transparent; -fx-background-radius: 10px; -fx-cursor:hand;");
            imageView.setImage(new Image("file:/C:/Users/madio/Downloads/AddIcon.png")); // Changez l'image
        });

        addButton.setOnMouseExited(event -> {
            addButton.setStyle("-fx-background-color:#72FAA8; -fx-background-radius: 5px; -fx-cursor:hand;");
            imageView.setImage(new Image("file:/C:/Users/madio/Downloads/AddIcon.png")); // Rétablir l'image originale
        });

        return addButton;
    }

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'le' d MMMM yyyy", Locale.FRENCH);
        return date.format(formatter);
    }

    void ClickEvent(Evenement evenement) {
        try {
            // Charger le fichier FXML pour obtenir la racine
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/omadi/g/Participant_Event.fxml"));
            Parent root = loader.load();

            // Obtenir le contrôleur et lui passer l'objet `Evenement`
            Participant_EventController controller = loader.getController();
            controller.setEvenement(evenement);

            // Créer une nouvelle scène avec la racine chargée
            Scene scene = new Scene(root);

            // Créer un nouveau stage et attacher la scène
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Détails de l'Événement: " + evenement.GetTitre());

            // Optionnel: Définir une taille initiale
            stage.setWidth(1303);
            stage.setHeight(758);

            // Afficher le stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ClickAcc(ActionEvent event) throws IOException {
        App.setRoot("Acceuil");
    }

    private void AddEvent() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/omadi/g/AddEvent.fxml"));
            Parent root = fxmlLoader.load();

            AddEventController addEventController = fxmlLoader.getController();
            addEventController.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Événement");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            reloadEvents();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de l'ouverture du formulaire.", Alert.AlertType.ERROR);
        }
    }

    void DeletEvent(Evenement evenement) {
        if (evenement != null) {
            try {
                Logger.getLogger(EvenementController.class.getName()).log(Level.INFO, "Tentative de suppression de l'événement avec ID : {0}", evenement.getId());
                EvenementDao.deleEvent(evenement);

                // Supprimer l'événement de eventPaneMap et de buttonContainer.getChildren()
                Optional.ofNullable(eventPaneMap.get(evenement))
                        .ifPresent(pane -> {
                            eventPaneMap.remove(evenement);
                            buttonContainer.getChildren().remove(pane);
                        });

                showAlert("Succès", "Evenement supprimé avec succès.", Alert.AlertType.INFORMATION);
            } catch (SQLException e) {
                showAlert("Erreur", "Une erreur est survenue lors de la suppression de l'événement.", Alert.AlertType.ERROR);
                Logger.getLogger(EvenementController.class.getName()).log(Level.SEVERE, "Erreur lors de la suppression de l'événement : ", e);
            }
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
