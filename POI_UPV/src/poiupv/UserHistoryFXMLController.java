/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package poiupv;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Session;
import model.User;

/**
 * FXML Controller class
 *
 * @author chick
 */
public class UserHistoryFXMLController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private ListView<String> sessionListView;
    userSession currentUser = userSession.getInstance();
    User user;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        user = currentUser.getUser();
        label.setText(user.getNickName()+ " Session History");
        List<Session> temp = user.getSessions();
        ObservableList<String> sessionList = FXCollections.observableArrayList();
        for (int i = 0; i < temp.size(); i++){
            String s = temp.get(i).getTimeStamp().format(DateTimeFormatter.ISO_LOCAL_DATE) + " " + temp.get(i).getTimeStamp().format(DateTimeFormatter.ISO_LOCAL_TIME) + "-  Correct: " + temp.get(i).getHits() + " Incorrect: " + temp.get(i).getFaults();
            sessionList.add(s);
            
        }
        sessionListView.setItems(sessionList);
        
        
        
    }    

    @FXML
    private void closePressed(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
}
