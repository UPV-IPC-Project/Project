/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javax.imageio.ImageIO;
import model.User;


/**
 * FXML Controller class
 *
 * @author MPOHJOL
 */
public class editProfile implements Initializable {

    @FXML
    private Button backFromEditButton1;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker birthdatePicker;

    private userSession currentUser = userSession.getInstance();
    @FXML
    private ImageView profileImage;
    @FXML
    private Button saveButton;
    @FXML
    private Button editButton;
    @FXML
    private Button editPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = currentUser.getUser();
        if (user != null) {
            emailField.setText(user.getEmail());
            birthdatePicker.setValue(user.getBirthdate());
        }
    }

    @FXML
    private void handleChangeAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Window stage = null;
        
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            Image newAvatar = new Image(selectedFile.toURI().toString());
            profileImage.setImage(newAvatar);
            // Store newAvatar somewhere (e.g. in a field)        
        }
}
    
    @FXML
    private void saveChanges(ActionEvent event) throws IOException {
        User user = currentUser.getUser();
        if (user != null) {
        user.setEmail(emailField.getText());
        user.setBirthdate(birthdatePicker.getValue());
        user.setAvatar(profileImage.getImage());
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile_1.fxml"));
        Parent newRoot = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(newRoot));
        stage.show();
    }
    
    @FXML
    private void BackToProfileView(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile_1.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }

    @FXML
    private void changePasswordButton(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("ChangePassword.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }
}

