module com.omadi.g {
    requires javafx.controls;
    requires javafx.fxml;
      requires java.sql;
        requires java.base;

    opens com.omadi.g to javafx.fxml;
    exports com.omadi.g;
    opens com.omadi.g.Controller to javafx.fxml;
     opens com.omadi.g.Model to javafx.base;
    requires kernel;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
}
