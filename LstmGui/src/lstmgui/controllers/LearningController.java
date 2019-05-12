/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.controllers;

import com.ml.data.DataManager;
import com.ml.math.MathV;
import com.ml.nn.LstmBlock;
import com.ml.other.ProgressHandler;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lstmgui.SwitcherController;
import lstmgui.model.stateManager;

/**
 * FXML Controller class
 *
 * @author jakob
 */
public class LearningController implements Initializable {
    
    LstmBlock selectedBlock = null;

    @FXML
    private VBox lstmList;
    
    @FXML
    private AnchorPane scrollAnchorPane;
    
    @FXML
    private ScrollPane lstmListScroll;
    
    @FXML
    private ProgressBar trainingProgressBar;
    
    @FXML
    private LineChart<String, Number> errorLineChart;
    
    @FXML
    private Label errorLabel;
    
    @FXML
    private TextField learningRateField;
    
    @FXML
    private TextField epochsField;
            
    @FXML
    private TextField iterationsField;
    
    @FXML
    private TextField graphEveryField;
    
    @FXML
    private TextField autosaveEveryField;

    @FXML
    private Button stopButton;
    
    @FXML
    private void refreshLstmBlocksButton(){
        refreshLstmBlocks();
    }

    @FXML
    private void stopButtonAction(){
        selectedBlock.chain.stopLearning = true;
    }
    
    @FXML
    private void changeSettings(){
        if(this.selectedBlock != null){
            System.out.println(learningRateField.getText());
            this.selectedBlock.chain.cell.setLearningRate(Double.parseDouble(learningRateField.getText()));
            System.out.println(this.selectedBlock.chain.cell.learningRate);
        }
    }
    
    @FXML
    private void editLstms(){
        stateManager.switcher.setMainElement("data");
    }
    
    @FXML
    private void startLearning(){
        if(this.selectedBlock == null)
            return;
        
        if(stateManager.learningInProgress()){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Stop the old learning thread?");
            alert.setContentText("A learning thread is already running, stop the old one and start a new one?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                stateManager.learningThread.stop();
            } else {
                return;
            }
        }
        
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        errorLineChart.getData().clear();
        errorLineChart.getData().add(series);
        
        int epochs = (epochsField.getText().isEmpty())? 1000 : Integer.parseInt(epochsField.getText());
        int iterations = (iterationsField.getText().isEmpty())? 10 : Integer.parseInt(iterationsField.getText());
        int graphEvery = (graphEveryField.getText().isEmpty())? 100 : Integer.parseInt(graphEveryField.getText());
        int autosaveFrequency = (autosaveEveryField.getText().isEmpty())? 500 : Integer.parseInt(autosaveEveryField.getText());
        
        Thread learningThread = new Thread(new Runnable(){
            @Override
            public void run() {
                refreshStopButtonVisibility();
                if(selectedBlock != null){
                    selectedBlock.onProgress(new ProgressHandler() {
                        @Override
                        public void progress(int epoch, double error) {
                            //update progress bar and line chart
                            double progress = ((double)epoch/epochs)*100;
                            trainingProgressBar.setProgress(progress/100);
                            
                            if(epoch % graphEvery == 0){
                                Platform.runLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        series.getData().add(new XYChart.Data<String, Number>(epoch + "", error));
                                        errorLabel.setText(error + "");
                                    }
                                });
                            }
                            
                            // save the block every `autosaveFrequency` times
                            if(epoch % autosaveFrequency == 0){
                                stateManager.saveBlock(selectedBlock);
                            }
                        }

                        @Override
                        public void end(double error) {
                            refreshStopButtonVisibility(false);
                            System.out.println("finished!");
                            System.out.println(selectedBlock.forwardWithVectorify(selectedBlock.getStringData().charAt(0),100));
                            System.out.println(selectedBlock.forward(selectedBlock.getStringData().charAt(0) + "",100));
                            stateManager.saveBlock(selectedBlock);
                        }
                    });

                    selectedBlock.train(epochs,iterations);
                }
            }
        });
        
        learningThread.start();
        stateManager.learningThread = learningThread;
    }

    private void refreshStopButtonVisibility(){
        if(stateManager.learningInProgress())
            stopButton.setVisible(true);
        else
            stopButton.setVisible(false);
    }

    private void refreshStopButtonVisibility(boolean state) {
        stopButton.setVisible(state);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopButton.managedProperty().bind(stopButton.visibleProperty());
        lstmListScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        refreshLstmBlocks();
        refreshStopButtonVisibility();
        
        epochsField.setText("1000");
        iterationsField.setText("10");
        graphEveryField.setText("100");
        autosaveEveryField.setText("500");
        learningRateField.setText("0.0001");
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
            scrollAnchorPane.setPrefHeight(60*(i+1));
        }
    }
    
}
