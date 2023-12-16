package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
//URL
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

//Json
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class LoginController  {

    public LoginController() {

    }

    @FXML
    private Button loginButton;
    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;



    public void userLogIn(ActionEvent event) throws IOException {
        checkLogin();

    }

    private void checkLogin() throws IOException {
        Main m = new Main();
        boolean Correct = false;
        try {
            // URL del servicio de login
            URL url = new URL("http://127.0.0.1:5000/login");

            // Crear la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String username1 = username.getText().toString();
            String password1 = password.getText().toString();
            System.out.println(username1 + " " + password1);
            String jsonInputString = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", username1, password1);

            // Escribir los datos en el cuerpo de la solicitud
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Obtener la respuesta del servidor
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                 StringBuilder response = new StringBuilder();
                 String responseLine;
                 while ((responseLine = br.readLine()) != null) {
                     response.append(responseLine.trim());
                 }
                // Imprimir la respuesta recibida
                System.out.println("Response: " + response.toString());

                // Analizar la respuesta JSON
                String jsonResponse = response.toString();
                try {
                    // Crear un objeto ObjectMapper de Jackson
                    ObjectMapper objectMapper = new ObjectMapper();

                    // Leer el JSON como un JsonNode
                    JsonNode jsonNode = objectMapper.readTree(jsonResponse);

                    // Extraer valores del JsonNode
                    String message = jsonNode.get("message").asText();
                    String status = jsonNode.get("status").asText();
                    // Imprimir los valores extraídos
                    if ("success".equals(status)) {
                        Correct = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Correct) {
            wrongLogIn.setText("Success!");
            wrongLogIn.setTextFill(Color.GREEN);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            // Cargar el nuevo FXML
            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
            // Crear una nueva escena con el nuevo FXML
            Scene nuevaEscena = new Scene(root, 1000, 800);
            // Establecer la nueva escena en el Stage
            stage.setScene(nuevaEscena);
            stage.centerOnScreen();

            // Mostrar el Stage
            stage.show();
        }

        else if(username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogIn.setText("Please enter your data.");
        }


        else {
            wrongLogIn.setText("Wrong username or password!");
        }
    }


}