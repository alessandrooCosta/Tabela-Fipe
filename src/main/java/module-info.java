module fipe {
    requires javafx.controls;
    requires javafx.fxml;
    requires spring.boot;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires java.net.http;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens com.project.fipe.controle to javafx.fxml; // inseri como teste

    exports com.project.fipe;
    exports com.project.fipe.controle;
    exports com.project.fipe.Model; // Esta é a linha crucial que falta
}

