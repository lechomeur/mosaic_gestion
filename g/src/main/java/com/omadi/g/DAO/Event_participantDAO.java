/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.omadi.g.DAO;

import com.omadi.g.MariaDB;
import com.omadi.g.Model.Adherant;
import com.omadi.g.Model.Participant;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author madio
 */
public class Event_participantDAO {

    public static ObservableList<Participant> getParticipantsByEvent(int evenementId) throws SQLException {
        ObservableList<Participant> participants = FXCollections.observableArrayList();

        String query = "SELECT "
                + "p.Id, "
                + "p.Nom, "
                + "p.Prenom, "
                + "p.Age, "
                + "p.Telephone, "
                + "p.Montant, "
                + "p.Cheque_Espece, "
                + "p.Adherant, "
                + "p.Genre, "
                + "p.Mail "
                + "FROM participant p "
                + "JOIN participation pa ON p.Id = pa.id_participant "
                + "WHERE pa.id_evenement = ?";

        try (Connection connection = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, evenementId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Participant participant = new Participant(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getInt("Age"),
                        rs.getString("Telephone"),
                        rs.getInt("Montant"),
                        rs.getString("Cheque_Espece"),
                        rs.getString("Adherant"),
                        rs.getString("Genre"),
                        rs.getString("Mail")
                );
                participants.add(participant);
            }
        }
        return participants;
    }

    public static boolean isAdherant(String nom, String prenom) throws SQLException {
        String query = "SELECT COUNT(*) AS count "
                + "FROM adhesion "
                + "WHERE Nom = ? AND Prenom = ? AND Montant IS NOT NULL";
        try (Connection connection = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }

    public static int addParticipant(Participant participant) throws SQLException {
          String query = "INSERT INTO participant (Nom, Prenom, Age, Telephone, Montant, Cheque_Espece, Adherant, Genre, Mail) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    int participantId = -1;

    try (Connection conn = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setString(1, participant.getNom());
        stmt.setString(2, participant.getPrenom());
        stmt.setObject(3, participant.getAge(), java.sql.Types.INTEGER);
        stmt.setString(4, participant.getTelephone());
        stmt.setObject(5, participant.getMontant(), java.sql.Types.INTEGER); // Use setObject to handle null
        stmt.setString(6, participant.getCheque_Espece());
        stmt.setString(7, participant.getAdherant());
        stmt.setString(8, participant.getGenre());
        stmt.setString(9, participant.getMail());

        stmt.executeUpdate();

        ResultSet generatedKeys = stmt.getGeneratedKeys();
        if (generatedKeys.next()) {
            participantId = generatedKeys.getInt(1);
        }
    }
    return participantId;
    }

    public static void addParticipantToEvent(int participantId, int eventId) throws SQLException {
        String query = "INSERT INTO participation (id_participant, id_evenement) VALUES (?, ?)";

        try (Connection conn = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, participantId);
            stmt.setInt(2, eventId);

            stmt.executeUpdate();
        }
    }

    public static void updateParticipant(Participant participant) throws SQLException {
        String query = "UPDATE participant SET "
                + "Nom = ?, Prenom = ?, Age = ?, Telephone = ?, Montant = ?, Cheque_Espece = ?, Adherant = ?, Genre = ?, Mail = ? "
                + "WHERE Id = ?";

        try (Connection conn = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, participant.getNom());
            stmt.setString(2, participant.getPrenom());
            stmt.setInt(3, participant.getAge());
            stmt.setString(4, participant.getTelephone());
            stmt.setInt(5, participant.getMontant());
            stmt.setString(6, participant.getCheque_Espece());
            stmt.setString(7, participant.getAdherant());
            stmt.setString(8, participant.getGenre());
            stmt.setString(9, participant.getMail());
            stmt.setInt(10, participant.getId());

            stmt.executeUpdate();
        }
    }

    public static void removeParticipantFromEvent(int participantId, int eventId) throws SQLException {
        String query = "DELETE FROM participation WHERE id_participant = ? AND id_evenement = ?";

        try (Connection conn = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, participantId);
            stmt.setInt(2, eventId);

            stmt.executeUpdate();
        }
    }

    public static ObservableList<Participant> getParticipantsByPaymentStatus(int evenementId, boolean hasPaid) throws SQLException {
        ObservableList<Participant> participants = FXCollections.observableArrayList();
        String query;

        if (hasPaid) {
            query = "SELECT p.Id, p.Nom, p.Prenom, p.Age, p.Telephone, p.Montant, p.Cheque_Espece, p.Adherant, p.Genre, p.Mail "
                    + "FROM participant p "
                    + "JOIN participation pa ON p.Id = pa.id_participant "
                    + "WHERE pa.id_evenement = ? AND p.Montant IS NOT NULL AND p.Montant > 0";
        } else {
            query = "SELECT p.Id, p.Nom, p.Prenom, p.Age, p.Telephone, p.Montant, p.Cheque_Espece, p.Adherant, p.Genre, p.Mail "
                    + "FROM participant p "
                    + "JOIN participation pa ON p.Id = pa.id_participant "
                    + "WHERE pa.id_evenement = ? AND (p.Montant IS NULL OR p.Montant <= 0)";
        }

        try (Connection connection = MariaDB.getINSTANCE().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, evenementId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Participant participant = new Participant(
                        rs.getInt("Id"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getInt("Age"),
                        rs.getString("Telephone"),
                        rs.getInt("Montant"),
                        rs.getString("Cheque_Espece"),
                        rs.getString("Adherant"),
                        rs.getString("Genre"),
                        rs.getString("Mail")
                );
                participants.add(participant);
            }
        }
        return participants;
    }
    public static int getNParticipantByEvent(int eventId) throws SQLException {
    String query = "SELECT COUNT(*) AS count FROM participation WHERE id_evenement = ?";
    int participantCount = 0;

    try (Connection connection = MariaDB.getINSTANCE().getConnection(); 
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, eventId);
        
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            participantCount = rs.getInt("count");
        }
    }
    return participantCount;
}
    public static int getMontantTotalByEvent(int eventId) throws SQLException {
  String query = "SELECT COALESCE(SUM(p.Montant), 0) AS totalMontant " +
                   "FROM Participant p " +
                   "JOIN participation pa ON p.Id = pa.id_participant " +
                   "WHERE pa.id_evenement = ?";

    int totalMontant = 0;

    try (Connection connection = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            totalMontant = rs.getInt("totalMontant");
        }
    }

    return totalMontant;
}
    public static int getNbParticipantsPayesByEvent(int eventId) throws SQLException {
    String query = "SELECT COUNT(*) AS countParticipantsPayes " +
                   "FROM Participant p " +
                   "JOIN participation pa ON p.Id = pa.id_participant " +
                   "WHERE pa.id_evenement = ? AND (p.Montant > 0 OR p.Montant IS NOT NULL)";

    int countParticipantsPayes = 0;

    try (Connection connection = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            countParticipantsPayes = rs.getInt("countParticipantsPayes");
        }
    }

    return countParticipantsPayes;
}
    public static int getNombreParticipantsNonPayesByEvent(int eventId) throws SQLException {
    String query = "SELECT COUNT(*) AS countParticipantsNonPayes " +
                   "FROM Participant p " +
                   "JOIN participation pa ON p.Id = pa.id_participant " +
                   "WHERE pa.id_evenement = ? AND (p.Montant IS NULL OR p.Montant <= 0)";

    int countParticipantsNonPayes = 0;

    try (Connection connection = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            countParticipantsNonPayes = rs.getInt("countParticipantsNonPayes");
        }
    }

    return countParticipantsNonPayes;
}
    public static int getNombreFemmesByEvent(int eventId) throws SQLException {
    String query ="SELECT COUNT(*) AS countFemmes " +
                   "FROM Participant p " +
                   "INNER JOIN Participation pa ON p.Id = pa.id_participant " +
                   "WHERE UPPER(p.Genre) = 'Femme' AND pa.id_evenement = ?";

    int countFemmes = 0;

    try (Connection connection = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            countFemmes = rs.getInt("countFemmes");
        }
    }

    return countFemmes;
}
        public static int getNombreHommesByEvent(int eventId) throws SQLException {
    String query = "SELECT COUNT(*) AS countHomme " +
                   "FROM Participant p " +
                   "INNER JOIN Participation pa ON p.Id = pa.id_participant " +
                   "WHERE UPPER(p.Genre) = 'HOMME' AND pa.id_evenement = ?";

    int countHommes = 0;

    try (Connection connection = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, eventId);

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            countHommes = rs.getInt("countHomme");
        }
    }

    return countHommes;
}
      public static ObservableList<Participant> searchParticipantsByNomPrenom(int eventId, String searchTerm) throws SQLException {
    ObservableList<Participant> participants = FXCollections.observableArrayList();

    String sql = "SELECT p.id AS Id, p.nom AS Nom, p.prenom AS Prenom, " +
                 " Age, " +
                 "p.Telephone, " +
                 "p.Montant, " +
                 "p.Cheque_Espece, " +
                 "p.Adherant, " +
                 "p.Genre, " +
                 "p.Mail " +
                 "FROM Participant p " +
                 "INNER JOIN Participation pa ON p.id = pa.id_participant " +
                 "WHERE pa.id_evenement = ? " +
                 "AND (p.nom LIKE ? OR p.prenom LIKE ?)";

    try (Connection conn = MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, eventId);
        stmt.setString(2, "%" + searchTerm + "%");
        stmt.setString(3, "%" + searchTerm + "%");

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                // Création d'un nouvel objet Participant à partir des résultats de la requête
                Participant participant = new Participant(
                    rs.getInt("Id"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getInt("Age"),
                    rs.getString("Telephone"),
                    rs.getInt("Montant"),
                    rs.getString("Cheque_Espece"),
                    rs.getString("Adherant"),
                    rs.getString("Genre"),
                    rs.getString("Mail")
                );
                participants.add(participant); // Ajout du participant à la liste observable
            }
        }
    }

    return participants;
}


}




