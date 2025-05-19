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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.NavDAOException;
import model.Navigation;
import model.User;

/**
 * FXML Controller class
 *
 * @author KKKOKKON
 */
public class LoginController implements Initializable {

    @FXML
    private PasswordField passWord;
    @FXML
    private TextField userName;
    @FXML
    private Button loginButton;
    @FXML
    private Text userMessage;
    @FXML
    private Button registerButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void loginClicked(ActionEvent event) throws Exception {
        String name = userName.getText();
        String password = passWord.getText();
        
        try {
            Navigation nav = Navigation.getInstance();
            User user = nav.authenticate(name, password); 

        if (user != null) {
            userMessage.setText("Login successful.");
            PoiUPVApp.switchScene("FXMLDocument.fxml", "Navigation tool");
        } else {
            userMessage.setText("Invalid username or password.");
        }
        } catch (NavDAOException e) {
            userMessage.setText("Error accessing database.");
    }
        
      
    }

    @FXML
    private void registerClicked(ActionEvent event) throws NavDAOException, Exception {
        PoiUPVApp.switchScene("register.fxml", "Register");
        
    }
    }
        
    
    

