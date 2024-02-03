package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import java.io.File;
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

public class NewUserController {
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
    @FXML
    private void handleAdd() {
    	Boolean check = checkSend();
    	if(check) {
    		
    	}
    }
    private void mostrarPopup(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New product");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void borrarCampos() {
        borrarTextField(firstname, lastname, email, password,confirm);
        borrarLabels(emptyFirst, emptyLast, emptyEmail, emptyPassword, emptyConfirm);
        borrarImagen();
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
    		emptyFirst.setText("Please enter the name");
    		NoErrors=false;
    	}
    	else {
    		emptyFirst.setText("");
    	}
    	if(lastname.getText().isEmpty()) {
    		emptyLast.setText("Please enter the category");
    		NoErrors=false;
    	}
    	else {
    		emptyLast.setText("");
    	}
    	if(email.getText().isEmpty()) {
    		emptyEmail.setText("Please enter the description");
    		NoErrors=false;
    	}else {
    		emptyEmail.setText("");
    	}
    	if(password.getText().isEmpty()) {
    		emptyPassword.setText("Please enter the description");
    		NoErrors=false;
    	}else {
    		emptyPassword.setText("");
    	}
    	if(confirm.getText().isEmpty()) {
    		emptyConfirm.setText("Please enter the description");
    		NoErrors=false;
    	}else {
    		emptyConfirm.setText("");
    	}if(imagePath.isEmpty()) {
    		NoErrors=false;
    	}
		return NoErrors;
    	
    }
    
}
                                           