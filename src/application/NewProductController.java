package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;

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
    
    @FXML
    private void handlePickImage() {
        // Configurar el FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de Imagen", "*.png", "*.jpg", "*.gif", "*.bmp"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );

        // Mostrar el FileChooser y obtener la imagen seleccionada
        File selectedFile = fileChooser.showOpenDialog(null);

        // Verificar si se seleccionó un archivo
        if (selectedFile != null) {
            // Cargar la imagen seleccionada en el ImageView
            Image selectedImage = new Image(selectedFile.toURI().toString());
            imageView.setImage(selectedImage);
        }
    }
    @FXML
    private void handleAdd() {}
    
    
}
                                           