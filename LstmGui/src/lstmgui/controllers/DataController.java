/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.controllers;

import com.ml.data.DataManager;
import com.ml.nn.LstmBlock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
public class DataController implements Initializable {
    
    public LstmBlock selectedBlock;

    @FXML
    public VBox lstmList;
    
    @FXML
    public VBox dataSetList;
    
    @FXML
    public TextArea dataText;
    
    @FXML
    public TextField lstmName;
    
    @FXML
    public ScrollPane lstmListScroll;
    
    @FXML
    public AnchorPane scrollAnchorPane;
    
    @FXML
    public TextField delimiterField;
    
    @FXML
    public void addLstm(){
        LstmBlock block;
        if(delimiterField.getText().isEmpty())
            block = new LstmBlock(dataText.getText().replace("\\n", "\n"), 0.0001);
        else
            block = new LstmBlock(dataText.getText().replace("\\n", "\n"), delimiterField.getText(), 0.0001);
        block.name = lstmName.getText();
        stateManager.addBlock(block);
        refreshLstmBlocks();
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
                                
                                dataText.setText("");
                                String out = selectedBlock.getStringData();
                                out = out.replace("\\n", "\n");
                                dataText.appendText(out);
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
