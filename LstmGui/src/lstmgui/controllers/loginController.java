package lstmgui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    }

    @FXML
    void registerButtonClicked(ActionEvent event) {

    }

}
