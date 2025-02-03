package com.omadi.g.DAO;

import com.omadi.g.MariaDB;
import com.omadi.g.Model.Utilisateur;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilisateurDAO {

    public static Utilisateur getUtilisateurIdentifie(String login, String mdp) throws SQLException {
        Utilisateur u = null;
        Connection connection = null;
        try {
            String sql = "SELECT nom, prenom FROM utilisateur WHERE login = ? AND mdp = SHA2(?, 256)";
            MariaDB mariaDB = MariaDB.getINSTANCE();
            connection = mariaDB.getConnection();

            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, login);
                ps.setString(2, mdp);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    u = new Utilisateur(login, nom, prenom);
                }
            } else {
                Logger.getLogger(UtilisateurDAO.class.getName()).log(Level.SEVERE, "Database connection is null.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UtilisateurDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return u;
    }
}
