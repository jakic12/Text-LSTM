package lstmgui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    void loginButtonClicked(ActionEvent event) {
        int result = UserManager.checkUser(usernameField.getText(), passwordField.getText());
        if(result == 1){
            stateManager.changeFile(getClass().getResource("/lstmgui/switcher.fxml"));
        }else if(result == 0){
            System.out.println("passwords dont match");
        }else{
            System.out.println("user doesnt exist");
        }
    }
    
    @FXML
    void registerButtonClicked(ActionEvent event) {
        stateManager.changeFile(getClass().getResource("/lstmgui/views/register.fxml"));
    }

}
