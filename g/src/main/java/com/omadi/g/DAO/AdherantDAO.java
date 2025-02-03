package com.omadi.g.DAO;

import com.omadi.g.MariaDB;
import com.omadi.g.Model.Adherant;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdherantDAO {
public static ObservableList<Adherant> getAdherantsByAssociation(int associationId, Integer year, String cotis) throws SQLException {
    ObservableList<Adherant> adherants = FXCollections.observableArrayList();
    Connection connection = null;

    try {
        connection = MariaDB.getINSTANCE().getConnection();
        if (connection == null || connection.isClosed()) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
            return adherants;
        }

        Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

        
        StringBuilder sql = new StringBuilder("SELECT a.*, assoc.Nom AS AssociationNom FROM adhesion a ")
                            .append("JOIN association assoc ON a.Association_Id = assoc.Id WHERE 1=1");
     
        if (associationId > 0) {
            sql.append(" AND a.Association_Id = ?");
        }
        if (year != null) {
            sql.append(" AND YEAR(a.Date_Adhesion) = ?");
        }
        if (cotis != null) {
            if (cotis.equalsIgnoreCase("Payé")) {
                sql.append(" AND a.Montant IS NOT NULL");
            } else if (cotis.equalsIgnoreCase("Impayé")) {
                sql.append(" AND a.Montant IS NULL or a.Montant = 0");
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (associationId > 0) {
                ps.setInt(paramIndex++, associationId);
            }
            if (year != null) {
                ps.setInt(paramIndex++, year);
            }

            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String nom = rs.getString("Nom");
                    String prenom = rs.getString("Prenom");
                    String adresse = rs.getString("Adresse");
                    String mail = rs.getString("Mail");
                    int montant = rs.getInt("Montant");
                    if (rs.wasNull()) {
                        montant = -1; // Indicateur pour les montants nuls
                    }
                    String cheque_Espece = rs.getString("Cheque_Espece");
                    String telephone = rs.getString("Telephone");
                    Date date_adhesion = rs.getDate("Date_Adhesion");
                    String associationNom = rs.getString("AssociationNom");

                    Adherant adherant = new Adherant(id, nom, prenom, adresse, mail, montant, cheque_Espece, telephone, date_adhesion, associationNom, associationId );
                    adherants.add(adherant);
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching adherants.", ex);
        ex.printStackTrace();
    }

   
    FXCollections.sort(adherants, (a1, a2) -> a2.getDate_adhesion().compareTo(a1.getDate_adhesion()));

    return adherants;
}




    public static ObservableList<Integer> getAllAdhesionYears() throws SQLException {
        ObservableList<Integer> years = FXCollections.observableArrayList();
        Connection connection = null;

        try {
            connection = MariaDB.getINSTANCE().getConnection();
            if (connection == null || connection.isClosed()) {
                Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
                return years;
            }

            Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

            String sql = "SELECT DISTINCT YEAR(Date_Adhesion) AS annee FROM adhesion";

            // Préparer la requête SQL
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int year = rs.getInt("annee");
                    years.add(year);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching adhesion years.", ex);
            ex.printStackTrace();
        }

        return years;
    }
public static int getAdherantCount(int associationId, Integer year, String cotisFilter) {
    int count = 0;
    Connection connection = null;

    try {
        connection = MariaDB.getINSTANCE().getConnection();
        if (connection == null || connection.isClosed()) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
            return count;
        }

        Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

       
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM adhesion WHERE 1=1");

        if (associationId > 0) {
            sql.append(" AND Association_Id = ?");
        }
        if (year != null) {
            sql.append(" AND YEAR(Date_Adhesion) = ?");
        }
        if (cotisFilter != null) {
            if (cotisFilter.equalsIgnoreCase("Payé")) {
                sql.append(" AND Montant IS NOT NULL");
            } else if (cotisFilter.equalsIgnoreCase("Impayé")) {
                sql.append(" AND Montant IS NULL OR Montant = 0");
            }
        }

   
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (associationId > 0) {
                ps.setInt(paramIndex++, associationId);
            }
            if (year != null) {
                ps.setInt(paramIndex++, year);
            }

          
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching adherant count.", ex);
        ex.printStackTrace();
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while closing the connection.", ex);
                ex.printStackTrace();
            }
        }
    }

    return count;
}




    public static int getNbCpay(int associationId, Integer year) {
    int count = 0;
    Connection connection = null;

    try {
        connection = MariaDB.getINSTANCE().getConnection();
        if (connection == null || connection.isClosed()) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
            return count;
        }

        Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

        String sql = "SELECT COUNT(Montant) FROM adhesion WHERE (Association_Id = ? OR ? = 0)";
        if (year != null) {
            sql += " AND YEAR(date_adhesion) = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, associationId);
            ps.setInt(2, associationId);
            if (year != null) {
                ps.setInt(3, year);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching NbCpay count.", ex);
        ex.printStackTrace();
    }

    return count;
}


public static int getCNpay(int associationId, Integer year) {
    int count = 0;
    Connection connection = null;

    try {
        connection = MariaDB.getINSTANCE().getConnection();
        if (connection == null || connection.isClosed()) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
            return count;
        }

        Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

        String sql = "SELECT COUNT(*) FROM adhesion WHERE Montant IS NULL OR Montant=0  AND (Association_Id = ? OR ? = 0)";
        if (year != null) {
            sql += " AND YEAR(date_adhesion) = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, associationId);
            ps.setInt(2, associationId);
            if (year != null) {
                ps.setInt(3, year);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching CNpay count.", ex);
        ex.printStackTrace();
    }

    return count;
}


public static int getCotisTot(int associationId, Integer year, String cotisFilter) {
    int totalCotisation = 0;
    Connection connection = null;

    try {
        connection = MariaDB.getINSTANCE().getConnection();
        if (connection == null || connection.isClosed()) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
            return totalCotisation;
        }

        Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

      
        StringBuilder sql = new StringBuilder("SELECT SUM(Montant) FROM adhesion WHERE 1=1");

        if (associationId > 0) {
            sql.append(" AND Association_Id = ?");
        }
        if (year != null) {
            sql.append(" AND YEAR(Date_Adhesion) = ?");
        }
        if (cotisFilter != null) {
            if (cotisFilter.equalsIgnoreCase("Payé")) {
                sql.append(" AND Montant IS NOT NULL");
            } else if (cotisFilter.equalsIgnoreCase("Impayé")) {
                sql.append(" AND Montant IS NULL");
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (associationId > 0) {
                ps.setInt(paramIndex++, associationId);
            }
            if (year != null) {
                ps.setInt(paramIndex++, year);
            }

         
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalCotisation = rs.getInt(1);
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching total cotisation.", ex);
        ex.printStackTrace();
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while closing the connection.", ex);
                ex.printStackTrace();
            }
        }
    }

    return totalCotisation;
}

public static void addAdherant(Adherant adherant) throws SQLException {
    String query = "INSERT INTO adhesion (Nom, Prenom, Adresse, Mail, Telephone, Montant, Cheque_Espece, Association_Id, Date_adhesion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";

    try (Connection conn =  MariaDB.getINSTANCE().getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, adherant.getNom());
        stmt.setString(2, adherant.getPrenom());
        stmt.setString(3, adherant.getAdresse());
        stmt.setString(4, adherant.getMail());
        stmt.setString(5, adherant.getTelephone());
        stmt.setInt(6, adherant.getMontant());
        stmt.setString(7, adherant.getCheque_Espece());
        stmt.setInt(8, adherant.getAssociationId());

        stmt.executeUpdate();
    }
}

    public static void deleteAdherant(Adherant adherant) throws SQLException {
        String sql = "DELETE FROM adhesion  WHERE Id = ?";
        Connection connexion =  MariaDB.getINSTANCE().getConnection();
        try (PreparedStatement statement = connexion.prepareStatement(sql)) {
            statement.setInt(1, adherant.getId());
            statement.executeUpdate();
        }
    }
   public static void updateAdherant(Adherant adherant, Adherant originalAdherant) throws SQLException {
    StringBuilder updateQuery = new StringBuilder("UPDATE adhesion SET ");
    List<Object> parameters = new ArrayList<>();
    boolean first = true;

    // Comparer chaque champ avec les valeurs d'origine
    if (!adherant.getNom().equals(originalAdherant.getNom())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Nom = ?");
        parameters.add(adherant.getNom());
        first = false;
    }
    if (!adherant.getPrenom().equals(originalAdherant.getPrenom())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Prenom = ?");
        parameters.add(adherant.getPrenom());
        first = false;
    }
    if (!adherant.getAdresse().equals(originalAdherant.getAdresse())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Adresse = ?");
        parameters.add(adherant.getAdresse());
        first = false;
    }
    if (!adherant.getMail().equals(originalAdherant.getMail())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Mail = ?");
        parameters.add(adherant.getMail());
        first = false;
    }
    if (adherant.getMontant() != originalAdherant.getMontant()) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Montant = ?");
        parameters.add(adherant.getMontant());
        first = false;
    }
    if (!adherant.getCheque_Espece().equals(originalAdherant.getCheque_Espece())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Cheque_Espece = ?");
        parameters.add(adherant.getCheque_Espece());
        first = false;
    }
    if (!adherant.getTelephone().equals(originalAdherant.getTelephone())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Telephone = ?");
        parameters.add(adherant.getTelephone());
        first = false;
    }
    if (!adherant.getDate_adhesion().equals(originalAdherant.getDate_adhesion())) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Date_adhesion = ?");
        parameters.add(new java.sql.Date(adherant.getDate_adhesion().getTime()));
        first = false;
    }
    if (adherant.getAssociationId() != originalAdherant.getAssociationId()) {
        if (!first) updateQuery.append(", ");
        updateQuery.append("Association_Id = ?");
        parameters.add(adherant.getAssociationId());
        first = false;
    }

    // Ajouter la clause WHERE
    updateQuery.append(" WHERE Id = ?");
    parameters.add(adherant.getId());

    try (Connection connection = MariaDB.getINSTANCE().getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(updateQuery.toString())) {
        
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }

        int rowsUpdated = preparedStatement.executeUpdate();
        if (rowsUpdated == 0) {
            throw new SQLException("La mise à jour de l'adhérent a échoué, aucun enregistrement affecté.");
        }
    } catch (SQLException e) {
        throw new SQLException("Erreur lors de la mise à jour de l'adhérent.", e);
    }
}
   public static ObservableList<Adherant> searchAdherantsByNomPrenom(int associationId, String searchTerm) throws SQLException {
    ObservableList<Adherant> adherants = FXCollections.observableArrayList();
    Connection connection = null;

    try {
        connection = MariaDB.getINSTANCE().getConnection();
        if (connection == null || connection.isClosed()) {
            Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Database connection is null or closed.");
            return adherants;
        }

        Logger.getLogger(AdherantDAO.class.getName()).log(Level.INFO, "Database connection established.");

        StringBuilder sql = new StringBuilder("SELECT a.*, assoc.Nom AS AssociationNom FROM adhesion a ")
                            .append("JOIN association assoc ON a.Association_Id = assoc.Id WHERE 1=1");
     
        if (associationId > 0) {
            sql.append(" AND a.Association_Id = ?");
        }
        if (searchTerm != null && !searchTerm.isEmpty()) {
            sql.append(" AND (a.Nom LIKE ? OR a.Prenom LIKE ?)");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (associationId > 0) {
                ps.setInt(paramIndex++, associationId);
            }
            if (searchTerm != null && !searchTerm.isEmpty()) {
                ps.setString(paramIndex++, "%" + searchTerm + "%");
                ps.setString(paramIndex++, "%" + searchTerm + "%");
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("Id");
                    String nom = rs.getString("Nom");
                    String prenom = rs.getString("Prenom");
                    String adresse = rs.getString("Adresse");
                    String mail = rs.getString("Mail");
                    int montant = rs.getInt("Montant");
                    if (rs.wasNull()) {
                        montant = -1; // Indicateur pour les montants nuls
                    }
                    String cheque_Espece = rs.getString("Cheque_Espece");
                    String telephone = rs.getString("Telephone");
                    Date date_adhesion = rs.getDate("Date_Adhesion");
                    String associationNom = rs.getString("AssociationNom");

                    Adherant adherant = new Adherant(id, nom, prenom, adresse, mail, montant, cheque_Espece, telephone, date_adhesion, associationNom, associationId);
                    adherants.add(adherant);
                }
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while fetching adherants.", ex);
        ex.printStackTrace();
    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(AdherantDAO.class.getName()).log(Level.SEVERE, "Error while closing the connection.", ex);
                ex.printStackTrace();
            }
        }
    }

    return adherants;
}



}
