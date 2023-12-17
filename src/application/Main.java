package application;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import javafx.stage.StageStyle;

public class Main extends Application {
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage stage = primaryStage;

        // Llamada inicial para cargar la vista de inicio
        loadView(stage, "LoginScene.fxml", "StoreApp Login", 350, 450);
    }

    // Función para cambiar de vista
    public void loadView(Stage primaryStage, String fxmlFile, String title, double width, double height) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = fxmlLoader.load();

            // Añadir funcionalidad de arrastrar la ventana
            root.setOnMousePressed(event -> {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            });

            root.setOnMouseDragged(event -> {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            });

            // Configurar el escenario y la ventana
            //primaryStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(root, width, height);
            primaryStage.setResizable(false);
            Image icon = new Image("Images/logo.png");
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle(title);
            primaryStage.setScene(scene);

            // Mostrar la nueva vista
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}