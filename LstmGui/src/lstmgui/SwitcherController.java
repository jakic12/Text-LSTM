/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.swing.JScrollPane;
import lstmgui.model.stateManager;

/**
 * FXML Controller class
 *
 * @author jakob
 */
public class SwitcherController implements Initializable {

    // to store all window instances
    HashMap<String, Parent> rootPanes = new HashMap<String, Parent>();

    @FXML
    private AnchorPane anchorpane;
    
    @FXML
    private BorderPane borderpane;
    
    @FXML
    private void closeButtonPressed(){
        System.exit(0);
    }
    
    @FXML
    private void minimizeButtonPressed(ActionEvent event){
        Stage stage = (Stage)anchorpane.getScene().getWindow();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        
        stage.setIconified(true);
    }
    
    /**
     * method that handles panel switching
     * calls {@link #setMainElement(java.lang.String)} with the name of the button to lower case
     * !Must be called from buttons only!
     * @param event 
     */
    @FXML
    private void switchMainPanel(ActionEvent event){
        setMainElement(((Button)event.getSource()).getText().toLowerCase());
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setMainElement("dashboard");
        stateManager.switcher = this;
    }   
    /** 
     * sets the center of the border pane to the view you want
     * @param name the name of the view (without extension, must be in views folder) 
     */
    public void setMainElement(String name){
        Parent root = rootPanes.get(name);
        if(root != null)
            borderpane.setCenter(rootPanes.get(name));
        else{
            try {
                System.out.println("views/" + name + ".fxml");
                root = FXMLLoader.load(getClass().getResource("views/" + name + ".fxml"));
                rootPanes.put(name, root);
                borderpane.setCenter(root);
            } catch (IOException e) {
                displayError(e);
            }
        }
    }
    

    private void displayError(Exception e){
        new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            System.out.println(e.getMessage());
            e.printStackTrace();
    }
    
    private void displayError(String err){
        new Alert(Alert.AlertType.ERROR, err).showAndWait();
    }
    
}
