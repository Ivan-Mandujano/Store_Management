package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import java.io.File;
//URL
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

//Json
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//Apache HTTP
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

public class NewUserController implements Initializable {
	//BUTTONS
    @FXML
    private Button pickImageButton;
    @FXML
    private Button add;
    //IMAGE
    @FXML
    private ImageView imageView;
    //TEXT FIELDS
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirm;
    //ERROR LABELS
    @FXML
    private Label emptyFirst;
    @FXML
    private Label emptyLast;
    @FXML
    private Label emptyEmail;
    @FXML
    private Label emptyPassword;
    @FXML
    private Label emptyConfirm;
    @FXML
    private Label emptyAccess;
    @FXML
    private ChoiceBox <String> accesslevel;
    private String[] acceso = {"Employee", "Administrator"};
    
    //Path a la imagen del producto
    private static String imagePath;

    @FXML
    private void handlePickImage() {
        // Configurar el File Chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg","*.bmp")
        );

        // Mostrar el FileChooser y obtener la imagen seleccionada
        File selectedFile = fileChooser.showOpenDialog(null);

        // Verificar si se seleccionó un archivo
        if (selectedFile != null) {
            Image selectedImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(selectedImage);

            // Almacenar la ruta de la imagen
            imagePath = selectedFile.getAbsolutePath();
        }
    }
    private void register() {
        Main m = new Main();
        boolean Correct = false;
        try {
            // URL del servicio de login
            URL url = new URL("http://127.0.0.1:5000/register");

            // Crear la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            String FirstName = firstname.getText().toString();
            String LastName = lastname.getText().toString();
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            String Level;
            if(accesslevel.getValue().equals("Administrator")) {
            	Level = "1";
            }else {
            	Level = "0";
            }
            System.out.println(Email + " " + Password);
            String jsonInputString = String.format("{\"first_name\":\"%s\",\"last_name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\",\"access_level\":\"%s\"}", FirstName, LastName, Email, Password, Level);

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
                 ObjectMapper objectMapper = new ObjectMapper();
                 JsonNode jsonResponse = objectMapper.readTree(response.toString());

                 String status = jsonResponse.get("status").asText();
                 String message = jsonResponse.get("message").asText();
                 String mensaje;
                 if ("success".equals(status)) {
                     mensaje = "The new user was added successfully.";
                     borrarCampos();
                     mostrarPopup(mensaje);
                 } else {
                     System.out.println("Error: " + message);
                     mensaje = "An error has ocurred during the process.";
                     mostrarPopup(mensaje);
                 }
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAdd() {
    	Boolean check = checkSend();
    	if(check) {
    		register();
    	}
    }
    private void mostrarPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New User");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void borrarCampos() {
        borrarTextField(firstname, lastname, email);
        borrarLabels(emptyFirst, emptyLast, emptyEmail, emptyPassword, emptyConfirm, emptyAccess);
        borrarContra(password,confirm);
        borrarImagen();
    }
    private void borrarContra(PasswordField... campos) {
    	for (PasswordField campo: campos) {
    		campo.clear();
    	}
    }
    private void borrarTextField(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }

    private void borrarTextArea(TextArea... areas) {
        for (TextArea area : areas) {
            area.clear();
        }
    }

    private void borrarLabels(Label... labels) {
        for (Label label : labels) {
            label.setText("");
        }
    }
    private void borrarImagen() {
        imageView.setImage(null);
    }
    private Boolean checkSend() {
    	Main m = new Main();
    	Boolean NoErrors = true;
    	if(firstname.getText().isEmpty()) {
    		emptyFirst.setText("Please enter the first name");
    		NoErrors=false;
    	}
    	else {
    		emptyFirst.setText("");
    	}
    	if(lastname.getText().isEmpty()) {
    		emptyLast.setText("Please enter the last name");
    		NoErrors=false;
    	}
    	else {
    		emptyLast.setText("");
    	}
    	if(email.getText().isEmpty()) {
    		emptyEmail.setText("Please enter the email");
    		NoErrors=false;
    	}else {
    		emptyEmail.setText("");
    	}
    	if(password.getText().isEmpty()) {
    		emptyPassword.setText("Please enter the password");
    		NoErrors=false;
    	}else {
    		emptyPassword.setText("");
    	}
    	if(confirm.getText().isEmpty()) {
    		emptyConfirm.setText("Please enter the confirmation");
    		NoErrors=false;
    	}else {
    		emptyConfirm.setText("");
    	}if(accesslevel.getValue().isEmpty()) {
    		emptyAccess.setText("Please enter the access level");
    		NoErrors=false;
    	}else {
    		emptyAccess.setText("");
    	}if (password.getText().trim().equals(confirm.getText().trim())) {
    	    emptyConfirm.setText("");
    	} else {
    	    emptyConfirm.setText("Password does not match confirmation");
    	    NoErrors = false;
    	}if(imagePath.isEmpty()) {
    		NoErrors=false;
    	}
		return NoErrors;
    	
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		accesslevel.getItems().addAll(acceso);
	    accesslevel.setValue(acceso[0]);

	}
    
}
                                           