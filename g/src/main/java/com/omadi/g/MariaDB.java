package com.omadi.g;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MariaDB {
    private static MariaDB INSTANCE = null;
    private Connection connection;

    // Paramètres de connexion
    private static final String DB_HOST = "localhost:3307";
    private static final String DB_NAME = "gestion_mosaic_2024";
    private static final String DB_LOGIN = "root";
    private static final String DB_MDP = "";

    private MariaDB() {
        try {
            
            Class.forName("org.mariadb.jdbc.Driver");
            connect();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, "Driver MariaDB non trouvé", ex);
        }
    }
       private void connect() {
        try {
            String chaineConnexion = "jdbc:mariadb://" + DB_HOST + "/" + DB_NAME;
            connection = DriverManager.getConnection(chaineConnexion, DB_LOGIN, DB_MDP);
        } catch (SQLException ex) {
            Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, "Erreur de connexion à la base de données", ex);
        }
    }

    public static MariaDB getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new MariaDB();
        }
        return INSTANCE;
    }

    public Connection getConnection() {
    try {
        if (connection == null || connection.isClosed()) {
            connect();
        }
    } catch (SQLException ex) {
        Logger.getLogger(MariaDB.class.getName()).log(Level.SEVERE, "Erreur lors de la vérification de la connexion à la base de données", ex);
    }
    return connection;
}
}
