/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.controllers;

import com.ml.nn.LstmBlock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lstmgui.model.stateManager;

/**
 * FXML Controller class
 *
 * @author jakob
 */
public class DashboardController implements Initializable {
    private LstmBlock selectedBlock;
    
    
    @FXML
    private ScrollPane lstmListScroll;

    @FXML
    private AnchorPane scrollAnchorPane;

    @FXML
    private VBox lstmList;

    @FXML
    private TextArea outTextArea;
    
    @FXML
    private TextField numberOfTimeStepsField;

    @FXML
    private TextField startDataField;
    
    @FXML
    private CheckBox cleanOut;
    
    @FXML
    private void refreshLstmBlocksButton(){
        refreshLstmBlocks();
    }
    
    @FXML
    private void editLstms(){
        stateManager.switcher.setMainElement("data");
    }

    @FXML
    void forwardButtonClicked(ActionEvent event) {
        if(this.selectedBlock == null){
            stateManager.displayError("no block selected");
            return;
        }
        
        if(startDataField.getText().isEmpty()){
            stateManager.displayError("starting data field is empty");
            return;
        }
        
        if(numberOfTimeStepsField.getText().isEmpty()){
            stateManager.displayError("number of steps field is empty");
            return;
        }
        
        try{
            Integer.parseInt(numberOfTimeStepsField.getText());
        }catch(NumberFormatException e){
            stateManager.displayError("number of steps is not valid");
            return;
        }
        
        if(cleanOut.isSelected()){
            outTextArea.setText(this.selectedBlock.forwardWithVectorify(startDataField.getText(), Integer.parseInt(numberOfTimeStepsField.getText())));
        }else{
            outTextArea.setText(this.selectedBlock.forward(startDataField.getText(), Integer.parseInt(numberOfTimeStepsField.getText())));
        }
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lstmListScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        refreshLstmBlocks();
    }    
    
    private void refreshLstmBlocks(){
        lstmList.getChildren().clear();
        LstmBlock[] existingBlocks = stateManager.getBlocks();
        
        for(int i = 0; i < existingBlocks.length; i++){
            String name = "lstm " + i;
            if(existingBlocks[i].name != null)
                name = existingBlocks[i].name;
            
            Button b = new Button(name);
            VBox.setVgrow(b, Priority.ALWAYS);
            b.setPrefWidth(Integer.MAX_VALUE);
            b.setMinHeight(50);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for(int j = 0; j < lstmList.getChildren().size(); j++){
                        
                        lstmList.getChildren().get(j).setStyle("");
                        
                        if(event.getSource() == lstmList.getChildren().get(j)){ // finds the button that was clicked on
                            if(stateManager.getBlocks()[j] == selectedBlock){ // if it is allready selected, deselect it
                                lstmList.getChildren().get(j).setStyle("");
                                selectedBlock = null;
                            }else{
                                lstmList.getChildren().get(j).setStyle("-fx-background-color:#66bb6a;");
                                selectedBlock = stateManager.getBlocks()[j];
                                System.out.println(selectedBlock.name);
                            }
                            
                        }
                    }
                    event.consume();
                }
            });
            
            lstmList.getChildren().add(b);
            //lstmList.setPrefHeight(60*(i+1));
            scrollAnchorPane.setPrefHeight(60*(i+1));
        }
    }
    
}
