/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.ml.nn.LstmBlock;


/**
 *
 * @author jakob
 */
public class LstmGui extends Application {
    
    private double xOffset = 0; 
    private double yOffset = 0;
    private boolean dissableDrag = false;
    
    @Override
    public void start(Stage stage) throws Exception {
        // Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/lstmgui/views/register.fxml"));
        stage.initStyle(StageStyle.UNDECORATED);
        
        windowMovement(root, stage);
        
        Scene scene = new Scene(root);
                
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    /**
     * Attaches mouse listener to move window if the tot bar is dragged
     * 
     * @param root the parent ( usually the root )
     * @param stage the stage to move
     */
    public void windowMovement(Parent root, Stage stage){
        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
                if(event.getSceneY() > 30){
                    dissableDrag = true;
                }else{
                    dissableDrag = false;
                }
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(!dissableDrag){
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            }
        });
    }
    
}
