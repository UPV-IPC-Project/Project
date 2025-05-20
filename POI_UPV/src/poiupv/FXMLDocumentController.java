/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poiupv;

import static java.awt.Color.green;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Answer;
import model.NavDAOException;
import model.Problem;
import model.Session;
import poiupv.Poi;
import model.Navigation;
import static model.Navigation.getInstance;
import model.User;

/**
 *
 * @author jsoler
 * @author aprice Alesan Price
 */
public class FXMLDocumentController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
    // ======================================
    // la variable zoomGroup se utiliza para dar soporte al zoom
    // el escalado se realiza sobre este nodo, al escalar el Group no mueve sus nodos
    private Group zoomGroup;

    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    private MenuButton map_pin;
    private MenuItem pin_info;
    @FXML
    private Label mousePosition;
    @FXML
    private Button buttonProfile;
    // Begins a new session
    @FXML
    private Button newSessionButton;
    // Displays questions when there is a session active
    @FXML
    private Label questionLabel;
    // a-d anser choices
    @FXML
    private RadioButton aAnswer;
    @FXML
    private RadioButton bAnswer;
    @FXML
    private RadioButton cAnswer;
    @FXML
    private RadioButton dAnswer;
    // Displays if the user is correct or not
    @FXML
    private Label correctLabel;
    @FXML
    private Button endSessionButton;
    @FXML
    private Button historyButton;
    
    
    @FXML
    private ToggleGroup ANSWERCHOICES;
    @FXML
    private Button checkButton;
    @FXML
    private Button nextButton;
    // Current user
    userSession currentUser = userSession.getInstance();
    User user;
    /**
     * 
     * SESSION VARIABLES
     * 
     */
    // Contains the timestamp of the session.
    LocalDateTime timestamp;
    // True if a session is open
    boolean activeSession;
    // Counts number of correct answers in the current session.
    int hits;
    // Counts the number of wrong answers in the current session.
    int faults;
    // number of the CURRENT problem starts at 1
    int count;
    // List of problems
    ArrayList<Problem> problems;
    // List of answers (changes each question)
    ArrayList<Answer> answers;
    // List of answer buttons (just the buttons)
    RadioButton[] radioButtons;
    // Instance navigation
    Navigation navigation;

    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del 
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.1);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.1);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();

        // Animación del scroll hasta la mousePosistion del item seleccionado
        double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
        double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
        double scrollH = itemSelected.getPosition().getX() / mapWidth;
        double scrollV = itemSelected.getPosition().getY() / mapHeight;
        final Timeline timeline = new Timeline();
        final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
        final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
        final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
        timeline.getKeyFrames().add(kf);
        timeline.play();

        // movemos el objto map_pin hasta la mousePosistion del POI
//        double pinW = map_pin.getBoundsInLocal().getWidth();
//        double pinH = map_pin.getBoundsInLocal().getHeight();
        map_pin.setLayoutX(itemSelected.getPosition().getX());
        map_pin.setLayoutY(itemSelected.getPosition().getY());
        pin_info.setText(itemSelected.getDescription());
        map_pin.setVisible(true);
    }

    private void initData() {
        data=map_listview.getItems();
        data.add(new Poi("1F", "Edificion del DSIC", 275, 250));
        data.add( new Poi("Agora", "Agora", 575, 350));
        data.add( new Poi("Pista", "Pista de atletismo y campo de futbol", 950, 350));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questionLabel.setWrapText(true);
        // TODO
        initData();
        //==========================================================
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.5);
        zoom_slider.setMax(1.5);
        zoom_slider.setValue(1.0);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que 
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        contentGroup.getChildren().add(zoomGroup);
        zoomGroup.getChildren().add(map_scrollpane.getContent());
        map_scrollpane.setContent(contentGroup);
        RadioButton[] list = { aAnswer, bAnswer, cAnswer, dAnswer };
        radioButtons = list;
        endSessionState();
        checkButton.setDisable(true);
        user = currentUser.getUser();
        try {
            navigation = getInstance();
        } catch (NavDAOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //
        

    }

    @FXML
    private void showPosition(MouseEvent event) {
        mousePosition.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) event.getX() + ",          Y: " + (int) event.getY());
    }

    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    @FXML
    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    @FXML
    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                }
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                map_listview.getItems().add(poi);
            }
        }
    }

    private void FXMLQuestionsController(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLQuestions.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }

    @FXML
    private void goToProfile(ActionEvent event) throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile_1.fxml"));
    Parent newRoot = loader.load();

    // Replace the current scene
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(new Scene(newRoot));
    stage.show();
    }

    /**
     * METHOD THAT BEGINS QUESTION SESSIONS
     * Creates a new session, generates questions, keeps track of how many correct/incorrect 
     * answers have been given.
     * 
     * @param event New Session button selected
     */
    @FXML
    private void newSessionStarted(ActionEvent event) {
        endSessionButton.setDisable(false);
        newSessionButton.setDisable(true);
        checkButton.setVisible(true);
        nextButton.setVisible(true);
        List<Problem> temp = navigation.getProblems();
        problems = new ArrayList<Problem>(temp);
        Collections.shuffle(problems);
        activeSession = true;
        hits = 0;
        faults = 0;
        count = 0;
        newQuestion();
        
        
    }


    /**
     * Ends the session
     * @param event end button pressed 
     */
    @FXML
    private void endPressed(ActionEvent event) {
        endSession();
        
    }

    /**
     * Displays a new stage of the user's history.
     * @param event session history button pressed
     */
    @FXML
    private void historyPressed(ActionEvent event) {
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserHistoryFXML.fxml"));
            Parent root = loader.load();
            Stage popupStage = new Stage();
            popupStage.setTitle("Session History");
            popupStage.setScene(new Scene(root));
            popupStage.initModality(Modality.APPLICATION_MODAL); // blocks interaction with the rest of the program
            
            popupStage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }
    /**
     * Checks the validity of the user's answer, enables going to the next question
     * @param event 
     */
    @FXML
    private void checkAnswerPressed(ActionEvent event) {
        // Checks the validity of the user's answer
        Answer selectedAnswer = (Answer) ANSWERCHOICES.getSelectedToggle().getUserData();
        correctLabel.setVisible(true);
        if (selectedAnswer.getValidity()){
            // Case where the user is correct
            correctLabel.setText("Your answer is correct!");
            correctLabel.setStyle("-fx-text-fill:green");
            hits++;
            
            
        }
        else{
            for (int i = 0; i < answers.size(); i++){
                if (answers.get(i).getValidity())
                {
                    correctLabel.setText("Incorrect. The answer is: " + answers.get(i).getText());
                    correctLabel.setStyle("-fx-text-fill:red");
                    faults++;
                }
            }
        }
        // Enable next question button.
        if (count < problems.size() - 1){
            nextButton.setDisable(false);
        }
        
    }

    @FXML
    private void nextQuestionPressed(ActionEvent event) {
        count++;
        newQuestion();
    }
    
    
    /**
     * 
     * HELPER METHODS
     * 
     * 
     */
    private void endSessionState(){
        questionLabel.setText("Start a new session to begin practice.");
        deselectAnswers(false);
        checkButton.setVisible(false);
        nextButton.setVisible(false);
        correctLabel.setVisible(false);
        nextButton.setDisable(true);
        newSessionButton.setDisable(false);
        endSessionButton.setDisable(true);
    }
    /**
     * Sets up the interface for a new question
     */
    private void newQuestion(){
        correctLabel.setVisible(false);
        questionLabel.setText((count+1) + ". " + problems.get(count).getText());
        List<Answer> temp = problems.get(count).getAnswers();
        
        answers = new ArrayList<Answer>(temp);
        Collections.shuffle( answers );
        deselectAnswers(true);
        nextButton.setDisable(true);
        
        /// Populates answer choices
        /// links the answer choices to the corresponding Answer object
        for (int i = 0; i < radioButtons.length; i++){
            radioButtons[i].setDisable(false);
            radioButtons[i].setText(  answers.get(i).getText() );
            radioButtons[i].setUserData(answers.get(i));
        }
        // Doesn't allow the user to select check unless something is selected.
        checkButton.disableProperty().bind(
        ANSWERCHOICES.selectedToggleProperty().isNull()
        
        );
        
        
    }
    
    /**
     * 
     * Deselects the answer choices
     * @param visible if false, makes them invisible
     */
    private void deselectAnswers(boolean visible){
        for (RadioButton radioButton : radioButtons) {
            radioButton.setSelected(false);
            if (!visible) {
                radioButton.setVisible(false);
            } else {
                radioButton.setVisible(true);
            }
        }
    }
    /**
     * Ends the session and stores it.
     */
    private void endSession(){
        timestamp = LocalDateTime.now();
        activeSession = false;
        endSessionState();
        user.addSession(hits, faults);
        currentUser = userSession.getInstance();
        currentUser.setUser(user);
        
    }


    
    
    
    

}
