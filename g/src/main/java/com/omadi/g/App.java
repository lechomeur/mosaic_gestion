package com.omadi.g;

import com.omadi.g.Model.Utilisateur;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import javafx.scene.image.Image;


/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Utilisateur utilisateurConnecte = null;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Connexion"), 640, 480);
        scene.getStylesheets().add(getClass().getResource("Style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Mosaic");
        URL iconURL = App.class.getResource("logo_de_mosaic.png");
        if (iconURL != null) {
            stage.getIcons().add(new Image(App.class.getResourceAsStream("logo_de_mosaic.png")));
        } else {
            System.out.println("Image non trouvé .");
        }

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    private void loadProperties() {
        Properties properties = new Properties();
        InputStream is = App.class.getResourceAsStream("conf/properties.conf");
        try {
            properties.load(is);
            Config.DB_LOGIN = properties.getProperty("DB_LOGIN");
            Config.DB_MDP = properties.getProperty("DB_MDP");
            Config.DB_HOST = properties.getProperty("DB_HOST");
            Config.DB_NAME = properties.getProperty("DB_NAME");
        } catch (IOException ex) {
            
        }

    }

}