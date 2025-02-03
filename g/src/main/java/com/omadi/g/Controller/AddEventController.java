/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.Controller;

import com.omadi.g.Model.Evenement;
import com.omadi.g.DAO.EvenementDao;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author madio
 */
public class AddEventController implements Initializable  {
    
   
    @FXML
    private DatePicker DPdate;

    @FXML
    private TextField tfLieu;

    @FXML
    private TextField tfTitre;
        
    @FXML
    private Button tfAdd;
    
    private EvenementController parentController;
    
 public void setParentController(EvenementController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfLieu.setPromptText("Lieu");
        tfTitre.setPromptText("Titre");
    }
     @FXML
    private void AddEvent() {
    String titre = tfTitre.getText();
    String lieu = tfLieu.getText();
    LocalDate date = DPdate.getValue();

    if (titre.isEmpty() || lieu.isEmpty() || date == null) {
        System.out.println("Veuillez remplir tous les champs.");
        return;
    }

    Evenement evenement = new Evenement(0,titre, date, lieu);

    try {
        EvenementDao.addEvent(evenement); // Assurez-vous que cette méthode ajoute correctement l'événement à la base de données
        parentController.reloadEvents();
        System.out.println("Evenement ajouté avec succès.");
          closeWindow();
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'ajout de l'événement : " + e.getMessage());
    } finally {
        tfAdd.setDisable(false);
    }

    }
    private void closeWindow() {
    
    Stage stage = (Stage) DPdate.getScene().getWindow();
    stage.close(); // Fermer la fenêtre
}
    
}
