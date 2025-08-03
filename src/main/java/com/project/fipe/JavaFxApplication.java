package com.project.fipe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        // Obtém o contexto do Spring a partir da classe principal
        context = SpringBootApp.getContext();
        if (context == null) {
            throw new IllegalStateException("Spring context is not initialized!");
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega o arquivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/index.fxml"));
            loader.setControllerFactory(context::getBean); // Usa o contexto Spring para injetar os controladores
            Parent root = loader.load();

            // Configura e exibe a cena
            Scene scene = new Scene(root);
            primaryStage.setTitle("Tabela FIPE");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar o arquivo FXML: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        // Fecha o contexto Spring ao sair
        if (context != null) {
            context.close();
        }
    }
}
