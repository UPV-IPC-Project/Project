/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import model.Navigation;
import model.User;
import model.NavDAOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;



/**
 * FXML Controller class
 *
 * @author KKKOKKON
 */
public class RegisterController implements Initializable {

    @FXML
    private DatePicker birthdateField;
    @FXML
    private Button chooseImageButton;
    @FXML
    private Button backButton;
    @FXML
    private Button registerButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label userMessage;
    @FXML
    private ImageView avatarImage;
    
    private Image Image1 = null;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void chooseImageClicked(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                Image1 = new Image(new FileInputStream(selectedFile));
                avatarImage.setImage(Image1);
            } catch (FileNotFoundException e) {
                userMessage.setText("Could not load selected image.");
                
            }
        }
    }

    @FXML
    private void backClicked(ActionEvent event) {
        try {
            PoiUPVApp.switchScene("login.fxml", "Login");
        } catch (Exception e) {
            userMessage.setText("Failed to return to login.");
            
        }
    }

    @FXML
    private void registerClicked(ActionEvent event) throws Exception {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String nickname = nameField.getText().trim();
        String email = emailField.getText().trim();
        LocalDate birthdate = birthdateField.getValue();
        
    if (!User.checkNickName(nickname)) {
        userMessage.setText("Invalid nickname. Nickname must be 6–15 characters (letters, - or _).");
        return;
    }
    
    if (!User.checkEmail(email)) {
        userMessage.setText("Invalid email format.");
        return;
    }
    
    if (!User.checkPassword(password)) {
        userMessage.setText("Password must be 8–20 chars, include upper/lowercase, digit, and symbol.");
        return;
        }

    if (!password.equals(confirmPassword)) {
        userMessage.setText("Passwords do not match.");
        return;
    }
    
    if (birthdate == null || birthdate.isAfter(LocalDate.now().minusYears(12))) {
        userMessage.setText("You must be at least 12 years old.");
        return;
        }
    
    try {
        Navigation nav = Navigation.getInstance();

        if (nav.exitsNickName(nickname)) {
            userMessage.setText("Nickname already taken.");
            return;
        }

        nav.registerUser(nickname, email, password, Image1, birthdate);
        userMessage.setText("Registration successful. You can now log in.");

            
        PoiUPVApp.switchScene("login.fxml", "Login");

        } catch (NavDAOException e) {
            userMessage.setText("Error accessing database.");
            
        }
    
        
    }
    }
    

