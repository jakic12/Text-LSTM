package lstmgui.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lstmgui.model.UserExistsException;
import lstmgui.model.UserManager;
import lstmgui.model.stateManager;

public class registerController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField1;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        stateManager.changeFile(getClass().getResource("/lstmgui/views/login.fxml"));
    }
    
    @FXML
    void keyPressedInForm(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            registerButtonClicked(null);
        }
    }

    @FXML
    void registerButtonClicked(ActionEvent event) {
        if(checkEmptyFields()){
            String username = usernameField.getText();
            String pass = passwordField.getText();
            if(pass.equals(passwordField1.getText())){
                try {
                    UserManager.addUser(username, pass);
                } catch (UserExistsException ex) {
                    displayError("user already exists!");
                }
            }else{
                displayError("passwords dont match");
            }
        }
    }
    
    boolean checkEmptyFields(){
        if(usernameField.getText().isEmpty()){
            stateManager.displayError("username field empty");
            return false;
        }else if(passwordField.getText().isEmpty()){
            stateManager.displayError("password field empty");
            return false;
        }else if(passwordField1.getText().isEmpty()){
            stateManager.displayError("you must repeat the password");
            return false;
        }else{
            return true;
        }
    }
    
    private void displayError(String e){
        new Alert(Alert.AlertType.ERROR, e).showAndWait();
            System.out.println(e);
    }

}
