/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Controller;

import com.omadi.g.DAO.EvenementDao;
import com.omadi.g.Model.Evenement;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author madio
 */
public class EditEventController {

    @FXML
    private DatePicker DPdate;

    @FXML
    private Button edit;

    @FXML
    private TextField tfLieu;

    @FXML
    private TextField tfTitre;

    @FXML
    void EditEvent(ActionEvent event) {
        evenement.SetTitre(tfTitre.getText());
        evenement.SetLieu(tfLieu.getText());
        evenement.SetDate_Ev(DPdate.getValue());

        try {
            boolean success = EvenementDao.updateEvenement(evenement);
            if (success) {
                showAlert("Succès", "L'événement a été mis à jour avec succès.", Alert.AlertType.INFORMATION);

                // Close the edit window
                Stage stage = (Stage) edit.getScene().getWindow();
                stage.close();
            } else {
                showAlert("Erreur", "Échec de la mise à jour de l'événement.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de l'événement.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }


private Evenement evenement;

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
         this.evenement = evenement;

        // Initialize UI components with evenement data
        if (evenement != null) {
            tfTitre.setText(evenement.GetTitre());
            tfLieu.setText(evenement.getLieu());
            DPdate.setValue(evenement.getDate_Evenement());
        }
    }
}
