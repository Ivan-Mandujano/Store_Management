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
        }
    }
    @FXML
    private void handleAdd() {
    	Boolean check = checkSend();
    	if(check) {
    		
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
    	}
		return NoErrors;
    	
    }
    
}
                                           