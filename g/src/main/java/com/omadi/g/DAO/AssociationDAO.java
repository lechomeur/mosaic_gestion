/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.DAO;

import com.omadi.g.MariaDB;
import com.omadi.g.Model.Association;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author madio
 */
public class AssociationDAO {
    public static ObservableList<Association> getAllAssoc(int associationId) throws SQLException {
        ObservableList<Association> ass = FXCollections.observableArrayList();
        String sql = "SELECT * FROM association";
        if (associationId > 0) {
            sql += " WHERE Id = ?";
        }
       

        try (Connection connection = MariaDB.getINSTANCE().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
                
             ResultSet rs = ps.executeQuery()) {
             if (associationId > 0) {
                ps.setInt(1, associationId);
            }

            if (connection != null && !connection.isClosed()) {
                Logger.getLogger(AssociationDAO.class.getName()).log(Level.INFO, "Connexion  de la base de donnée établie ");

                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String nom = rs.getString("Nom");

                    Association a = new Association(id, nom);
                    ass.add(a);
                }
            } else {
                Logger.getLogger(AssociationDAO.class.getName()).log(Level.SEVERE, "La connexion de la base de donnée de a été fermé .");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AssociationDAO.class.getName()).log(Level.SEVERE, "Error while fetching associations.", ex);
        }

        return ass;
    
    }

    public static int getAdherantCount() {
        int count = 0;
        Connection connection = null;

        try {
            String sql = "SELECT COUNT(*) FROM adhesion";
            MariaDB mariaDB = MariaDB.getINSTANCE();
            connection = mariaDB.getConnection();

            if (connection != null && !connection.isClosed()) {
                Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Connexion etabli.");

                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    count = rs.getInt(1);
                }
            } else {
                Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Erreur de connexion dans la base .");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Erreur lors de la recherche du nombre d'adhérents.", ex);
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Connexion fermé de la base de donnée .");
                } catch (SQLException ex) {
                    Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Erreur lors de la fermeture de la connexion.", ex);
                }
            }
        }
        return count;
    }
}
