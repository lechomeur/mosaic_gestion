package com.omadi.g.DAO;

import com.omadi.g.MariaDB;
import com.omadi.g.Model.Evenement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EvenementDao {

    public ObservableList<Evenement> getAllEvenements() {
        ObservableList<Evenement> evenements = FXCollections.observableArrayList();
        String query = "SELECT * FROM evenement";
        Connection connexion = MariaDB.getINSTANCE().getConnection();
        try (PreparedStatement statement = connexion.prepareStatement(query); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("Id");
                String titre = resultSet.getString("Titre");
                LocalDate dateEvenement = resultSet.getDate("Date_Evenement").toLocalDate();
                String lieu = resultSet.getString("Lieu");

                Evenement evenement = new Evenement(id,titre, dateEvenement, lieu);
                evenements.add(evenement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (ObservableList<Evenement>) evenements;
    }

    public Optional<LocalDate> getDateEvenementById(int id) {
        String query = "SELECT Date_Evenement FROM evenement WHERE Id = ?";
        Connection connexion = MariaDB.getINSTANCE().getConnection();
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                LocalDate dateEvenement = resultSet.getDate("Date_Evenement").toLocalDate();
                return Optional.of(dateEvenement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static void deleEvent(Evenement evenement) throws SQLException {
          String query = "DELETE FROM evenement WHERE Id = ?";
        try (Connection conn = MariaDB.getINSTANCE().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, evenement.getId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Evenement supprimé avec succès de la base de données.");
            } else {
                System.out.println("Erreur : Aucun événement supprimé de la base de données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void addEvent(Evenement evenement) throws SQLException {
        String query = "INSERT INTO evenement (Titre, Date_Evenement,lieu)"
                + "VALUES (?,?,?)";

        try (Connection conn = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, evenement.GetTitre());
            stmt.setDate(2, Date.valueOf(evenement.getDate_Evenement()));
            stmt.setString(3, evenement.getLieu());
            stmt.executeUpdate();
        }

    }
    public static boolean updateEvenement(Evenement evenement) throws SQLException {
        String updateQuery = "UPDATE evenement SET titre = ?, lieu = ?, date_evenement = ? WHERE id = ?";

        try (Connection connection = MariaDB.getINSTANCE().getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {
             
            statement.setString(1, evenement.GetTitre());
            statement.setString(2, evenement.getLieu());
            statement.setDate(3, java.sql.Date.valueOf(evenement.getDate_Evenement()));
            statement.setInt(4, evenement.getId()); // Assurez-vous que l'événement a une méthode getId()

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        }
    }
}
