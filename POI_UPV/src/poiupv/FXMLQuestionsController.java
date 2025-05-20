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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MPOHJOL
 */
public class FXMLQuestionsController implements Initializable {

      
    private Label someLabel; // fx:id must match what's in the FXML

    private void handleSomething() {
        someLabel.setText("Hello from FXMLQuestions!");
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void FXMLDocumentController(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }
   
  }
  
