/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.omadi.g.Controller;

import com.omadi.g.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author madio
 */
public class AcceuilController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
    }    
    

    @FXML
    private void onClickAdh(ActionEvent event) throws IOException {
         App.setRoot("Adherant");
    }
    @FXML
    void onClickEvent(ActionEvent event) throws IOException {
             App.setRoot("Evenement");
    }
    
}
