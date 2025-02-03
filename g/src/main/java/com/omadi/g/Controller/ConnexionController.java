/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.omadi.g.Controller;

import com.omadi.g.App;
import com.omadi.g.DAO.UtilisateurDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * FXML Controller class
 *
 * @author madio
 */
public class ConnexionController implements Initializable {


    @FXML
    private ImageView img;
    @FXML
    private TextField tflogin;
    @FXML
    private PasswordField pf;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Image image = new Image(App.class.getResourceAsStream("logo_de_mosaic.png"));
        //img.setImage(image);
    }  
     
    
    @FXML
    private void clickConnexion(ActionEvent event) throws SQLException {
        App.utilisateurConnecte = UtilisateurDAO.getUtilisateurIdentifie(tflogin.getText(), pf.getText());
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Connexion à la base");
        if (App.utilisateurConnecte != null) {
            try {

                App.setRoot("Acceuil");

            } catch (IOException ex) {
                Logger.getLogger(ConnexionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Identification échouée");
            a.showAndWait();
        }
    }

}
