/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.model;
import com.ml.math.MathV;
import com.ml.nn.LstmBlock;
import com.ml.other.ProgressHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lstmgui.SwitcherController;
import lstmgui.model.User;
/**
 * this class was made to manage the static state
 * @author jakob
 */
public class stateManager {
    private static double xOffset;
    private static double yOffset;
    private static boolean dissableDrag;
    public static Stage stage;
    private static ArrayList<LstmBlock> blocks = new ArrayList<LstmBlock>();
    private static String networkFilesLocation = "network_files/";
    public static SwitcherController switcher = null;
    
    public static User loggedInUser;
    
    public static Thread learningThread = null;

    /**
     * returns if the learning thread is running
     * @return
     */
    public static boolean learningInProgress(){
        if(learningThread != null)
            return learningThread.isAlive();
        return false;
    }
    /**
     * change stage to fxml view
     */
    public static void changeFile(URL location){
        Parent root;
        try {
            root = FXMLLoader.load(location);
            Scene scene = new Scene(root);      
            stage.setScene(scene);
            stage.show();
            
            applyWindowMovement(root,stage);
            
        } catch (IOException ex) {
            Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Attaches mouse listener to move window if the tot bar is dragged
     * 
     * @param root the parent ( usually the root )
     * @param stage the stage to move
     */
    private static void applyWindowMovement(Parent root, Stage stage){
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
    
    public static void displayError(Exception e){
        new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            System.out.println(e.getMessage());
            e.printStackTrace();
    }
    
    public static void displayError(String e){
        new Alert(Alert.AlertType.ERROR, e).showAndWait();
            System.out.println(e);
    }
    
    public static void addBlock(LstmBlock block){
        blocks.add(block);
        saveBlock(block);
    }
    
    public static LstmBlock[] getBlocks(){
        openBlocksFromFolder();
        LstmBlock[] out = new LstmBlock[blocks.size()];
        for(int i = 0; i < out.length; i++){
            out[i] = blocks.get(i);
        }
        return out;
    }
    
    public static void saveBlocks(){
        for(int i = 0; i < blocks.size(); i++){
            saveBlock(blocks.get(i));
        }
    }
    
    public static void openBlocksFromFolder(){
        File folder = new File(networkFilesLocation);
        String[] existingLstms = new String[blocks.size()];
        for(int j = 0; j < blocks.size(); j++){
            existingLstms[j] = blocks.get(j).name + ".lstm";
        }
        
        if(folder.exists()){
            File[] networkFiles = folder.listFiles();
            for(int i = 0; i < networkFiles.length; i++){
                if(networkFiles[i].getName().endsWith(".lstm")){

                    if(existingLstms != null && MathV.indexOf(existingLstms, networkFiles[i].getName()) != -1){
                        continue;
                    }
                    
                    ObjectInputStream is = null;
                    try {
                        is = new ObjectInputStream(new FileInputStream(networkFiles[i]));
                        blocks.add((LstmBlock)is.readObject());
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }else{
            Logger.getLogger(stateManager.class.getName()).log(Level.FINE, null, "folder doesnt exist");
        }
    }

    public static void saveBlock(LstmBlock block) {
        // removes the handler, because it is not serializable 
        // and shouldnt be saved with the network
        ProgressHandler handler = block.chain.getHandler();// error for some reason
        block.onProgress(null);
        
        if(!new File(networkFilesLocation).exists())
            new File(networkFilesLocation).mkdirs();
        
        File networkFile = new File(networkFilesLocation + block.name + ".lstm");
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(networkFile));
            os.writeObject(block);
            os.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(stateManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        block.onProgress(handler);
    }
    
    public static String getSHA(String input){ 
  
        try { 
  
            // Static getInstance method is called with hashing SHA 
            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
  
            // digest() method called 
            // to calculate message digest of an input 
            // and return array of byte 
            byte[] messageDigest = md.digest(input.getBytes()); 
  
            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest); 
  
            // Convert message digest into hex value 
            String hashtext = no.toString(16); 
  
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            } 
  
            return hashtext; 
        } 
  
        // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) { 
            System.out.println("Exception thrown"
                               + " for incorrect algorithm: " + e); 
  
            return null; 
        } 
    }
}
