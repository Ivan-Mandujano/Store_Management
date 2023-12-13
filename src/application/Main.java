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

public class Main extends Application {
    private static Stage stg;
    @Override
    public void start(Stage stage) throws Exception{
        stg = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 350, 450);
        stage.setResizable(false);
        stage.setTitle("StoreApp");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}