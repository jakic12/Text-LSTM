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
import lstmgui.model.stateManager;


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
        stage.initStyle(StageStyle.UNDECORATED);
        stateManager.stage = stage;
        stateManager.changeFile(getClass().getResource("/lstmgui/views/login.fxml"));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
