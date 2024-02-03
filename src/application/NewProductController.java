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

public class NewProductController {
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
    private TextField name;
    @FXML
    private TextField category;
    @FXML
    private TextArea description;
    @FXML
    private TextField price;
    @FXML
    private TextField quantity;
    //ERROR LABELS
    @FXML
    private Label EmptyName;
    @FXML
    private Label EmptyCategory;
    @FXML
    private Label EmptyDescription;
    @FXML
    private Label EmptyPrice;
    @FXML
    private Label EmptyQuantity;
    
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
    		SendtoDatabase();
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
        borrarTextField(name, category, price, quantity);
        borrarTextArea(description);
        borrarLabels(EmptyName, EmptyCategory, EmptyPrice, EmptyQuantity);
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
    private static void uploadImage(String prod_id) throws IOException, ParseException {
        File imageFile = new File(imagePath);

        if (!imageFile.exists()) {
            System.out.println("Image file not found.");
            return;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("http://127.0.0.1:5000/prod_images/new");

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody("image_data", imageFile);
            builder.addTextBody("prod_id", prod_id);  // Replace with the actual prod_id value

            HttpEntity multipartEntity = builder.build();
            httpPost.setEntity(multipartEntity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                ClassicHttpResponse classicResponse = response;
                int statusCode = classicResponse.getCode();
                System.out.println("Response Code: " + statusCode);

                HttpEntity responseEntity = response.getEntity();
                String responseBody = EntityUtils.toString(responseEntity);
                System.out.println("Response: " + responseBody);
            }
        }
    }
    private void SendtoDatabase() {
        try {
            String prodName = name.getText();
            String prodCategory = category.getText();
            String prodDescription = description.getText();
            String prodPrice = price.getText();
            String prodQuantity = quantity.getText();

            // Solicitud
            String postData = "prod_name=" + prodName +
                              "&prod_category=" + prodCategory +
                              "&prod_description=" + prodDescription +
                              "&prod_price=" + prodPrice +
                              "&prod_quantity=" + prodQuantity;

            URL url = new URL("http://127.0.0.1:5000/product/new");

            // Abre la conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configura la conexión
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = postData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Obtiene la respuesta del servidor
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Lee la respuesta del servidor utilizando Jackson
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    // Parsea la respuesta JSON con Jackson
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonResponse = objectMapper.readTree(response.toString());

                    String status = jsonResponse.get("status").asText();
                    String message = jsonResponse.get("message").asText();
                    String mensaje;
                    if ("success".equals(status)) {
                        // Extrae el prod_id del response
                        String prodId = jsonResponse.get("prod_id").asText();
                        System.out.println("Prod_ID: " + prodId);
                     // Llama al método para subir la imagen
                        uploadImage(prodId);
                        mensaje = "The new product was added successfully.";
                        borrarCampos();
                        mostrarPopup(mensaje);
                    } else {
                        System.out.println("Error: " + message);
                        mensaje = "An error has ocurred during the process.";
                        mostrarPopup(mensaje);
                    }
                }
            } else {
                System.out.println("Error in HTTP request. Response Code: " + responseCode);
            }

            // Cierra la conexión
            connection.disconnect();

            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Boolean checkSend() {
    	Main m = new Main();
    	Boolean NoErrors = true;
    	if(name.getText().isEmpty()) {
    		EmptyName.setText("Please enter the name");
    		NoErrors=false;
    	}
    	else {
    		EmptyName.setText("");
    	}
    	if(category.getText().isEmpty()) {
    		EmptyCategory.setText("Please enter the category");
    		NoErrors=false;
    	}
    	else {
    		EmptyCategory.setText("");
    	}
    	if(description.getText().isEmpty()) {
    		EmptyDescription.setText("Please enter the description");
    		NoErrors=false;
    	}else {
    		EmptyDescription.setText("");
    	}
    	if(price.getText().isEmpty()) {
    		EmptyPrice.setText("Please enter the description");
    		NoErrors=false;
    	}else {
    		EmptyPrice.setText("");
    	}
    	if(quantity.getText().isEmpty()) {
    		EmptyQuantity.setText("Please enter the description");
    		NoErrors=false;
    	}else {
    		EmptyQuantity.setText("");
    	}if(imagePath.isEmpty()) {
    		NoErrors=false;
    	}
		return NoErrors;
    	
    }
    
}
                                           