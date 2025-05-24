/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.stage.Stage;
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
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker birthdatePicker;

    private userSession currentUser = userSession.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        User user = currentUser.getUser();
        if (user != null) {
            emailField.setText(user.getEmail());
            birthdatePicker.setValue(user.getBirthdate());
        }
    }

    @FXML
    private void saveChanges(ActionEvent event) throws IOException {
        User user = currentUser.getUser();
        if (user != null) {
        user.setEmail(emailField.getText());
        user.setBirthdate(birthdatePicker.getValue());
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
}

