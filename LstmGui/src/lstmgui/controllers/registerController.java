package lstmgui.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lstmgui.model.UserExistsException;
import lstmgui.model.UserManager;

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
        UserManager.printUsers();
    }

    @FXML
    void registerButtonClicked(ActionEvent event) {
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
    
    private void displayError(String e){
        new Alert(Alert.AlertType.ERROR, e).showAndWait();
            System.out.println(e);
    }

}
