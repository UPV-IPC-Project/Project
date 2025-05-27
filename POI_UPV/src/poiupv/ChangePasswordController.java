/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import com.sun.jdi.connect.spi.Connection;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.NavDAOException;
import model.Navigation;
import model.User;

/**
 * FXML Controller class
 *
 * @author MPOHJOL
 */
public class ChangePasswordController implements Initializable {

    /**
     * Initializes the controller class.
     * 
     * 
     */
    @FXML
    private Button backButton;
    @FXML
    private TextField newPasswordLabel1;
    @FXML
    private TextField newPasswordLabel2;
    @FXML
    private Button saveButton;
    @FXML
    private Label userMessage;

    private User currentUser; // assume this is passed from elsewhere
        
    public void setCurrentUser(User user) {
    this.currentUser = user;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        saveButton.setOnAction(event -> handleChangePassword());
    }    
    
     private void handleChangePassword() {
        String pass1 = newPasswordLabel1.getText();
        String pass2 = newPasswordLabel2.getText();
        
         if (pass1.isEmpty() || pass2.isEmpty()) {
             userMessage.setText("Both password fields must be filled.");
             return;
         }

         if (!pass1.equals(pass2)) {
              userMessage.setText("Passwords do not match.");
             return;
         }

         if (!User.checkPassword(pass1)) {
              userMessage.setText("Password must be 8–20 characters long and include upper/lowercase letters, a digit, and a special character.");
             return;
         }

         try {
             currentUser.setPassword(pass1);
             userMessage.setText("Password changed successfully.");
         } catch (Exception e) {
              userMessage.setText("Could not update password. Please try again.");
         }
        
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
