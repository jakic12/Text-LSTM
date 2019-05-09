package lstmgui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lstmgui.model.UserManager;
import lstmgui.model.stateManager;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void closeButtonClicked(ActionEvent event) {
        System.exit(0);
    }
    
    @FXML
    void keyPressedInForm(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            loginButtonClicked(null);
        }
    }

    @FXML
    void loginButtonClicked(ActionEvent event) {
        if(checkEmptyFields()){
            int result = UserManager.checkUser(usernameField.getText(), passwordField.getText());
            if(result >= 0){
                stateManager.loggedInUser = UserManager.getUsers()[result];
                stateManager.changeFile(getClass().getResource("/lstmgui/switcher.fxml"));
            }else if(result == -2){
                stateManager.displayError("passwords dont match");
            }else{
                stateManager.displayError("user doesnt exist");
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
        }else{
            return true;
        }
    }
    
    @FXML
    void registerButtonClicked(ActionEvent event) {
        stateManager.changeFile(getClass().getResource("/lstmgui/views/register.fxml"));
    }

}
