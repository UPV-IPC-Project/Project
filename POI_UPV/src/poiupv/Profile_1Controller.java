/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.User;


/**
 * FXML Controller class
 *
 * @author MPOHJOL
 */
public class Profile_1Controller implements Initializable {

    @FXML
    private Button backToMainButton;
    @FXML
    private Button buttonEdit;
    @FXML
    private ImageView profileImage;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label birthdateLabel;

    /**
     * Initializes the controller class.
     */
       private userSession currentUser = userSession.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        model.User user = currentUser.getUser();
        if (user != null) {
            nameLabel.setText(user.getNickName());
            emailLabel.setText(user.getEmail());

            // Format birthdate nicely, e.g., 24 May 1990
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
            birthdateLabel.setText(user.getBirthdate().format(formatter));

           Image img = user.getAvatar();
            if (img != null) {
                profileImage.setImage(img);
            } else {
                // Optionally set a default image if none found
                profileImage.setImage(new Image("file:default-avatar.png"));
            }
        }
    }

    
    @FXML
    private void goToEditProfile(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }
    
 
    @FXML
    private void backToMainView(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }
    
}
