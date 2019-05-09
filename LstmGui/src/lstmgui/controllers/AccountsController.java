/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.controllers;

import com.ml.nn.LstmBlock;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import lstmgui.model.User;
import lstmgui.model.UserManager;
import lstmgui.model.UserManager.permission;
import lstmgui.model.stateManager;

/**
 * FXML Controller class
 *
 * @author jakob
 */
public class AccountsController implements Initializable {
    
    private User selectedUser;
    
    @FXML
    private ScrollPane userListScroll;

    @FXML
    private AnchorPane scrollAnchorPane;

    @FXML
    private VBox accountList;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField passwordField1;
    
    @FXML
    private ComboBox<permission> userTypeDropdown;

    @FXML
    void deleteUserBtnClicked(ActionEvent event) {

    }

    @FXML
    void saveBtnClicked(ActionEvent event) {
        if(usernameField.getText().isEmpty()){
            usernameField.setText(selectedUser.username);
        }else if(!usernameField.getText().equals(selectedUser.username)){
            for(User a : UserManager.getUsers()){
                if(a.username.equals(usernameField.getText())){
                    stateManager.displayError("user with that username already exists");
                    usernameField.setText(selectedUser.username);
                    return;
                }
            }
        }
        
        if(userTypeDropdown.getValue() == null){
            userTypeDropdown.setValue(selectedUser.userType);
        }
        
        if(passwordField.getText().isEmpty()){
            selectedUser = UserManager.editUser(selectedUser,usernameField.getText(), userTypeDropdown.getValue());
            refreshUserList();
            return;
        }
        
        if(!passwordField1.getText().equals(passwordField.getText())){
            stateManager.displayError("passwords dont match");
            return;
        }
        
        selectedUser = UserManager.editUser(selectedUser,usernameField.getText(), passwordField.getText(), userTypeDropdown.getValue());
        refreshUserList();
    }
    
    private void refreshSelected(){
        User[] users = UserManager.getUsers();
        for(int j = 0; j < accountList.getChildren().size(); j++){
            if(users[j].equals(selectedUser)){
                accountList.getChildren().get(j).setStyle("-fx-background-color:#66bb6a;");
            }else{
                accountList.getChildren().get(j).setStyle("");
            }
        }
    }
    
    private void refreshUserList(){
        accountList.getChildren().clear();//this doesnt delete
        User[] users = UserManager.getUsers();
        
        for(int i = 0; i < users.length; i++){
            String name = "lstm " + i;
            
            Button b = new Button(users[i].username);
            VBox.setVgrow(b, Priority.ALWAYS);
            b.setPrefWidth(Integer.MAX_VALUE);
            b.setMinHeight(50);
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    for(int j = 0; j < accountList.getChildren().size(); j++){
                        if(event.getSource() == accountList.getChildren().get(j)){ // finds the button that was clicked on
                            if(users[j] == selectedUser){ // if it is allready selected, deselect it
                                selectedUser = null;
                                usernameField.setText("");
                                userTypeDropdown.setValue(null);
                            }else{
                                selectedUser = users[j];
                                System.out.println(selectedUser.username);
                                usernameField.setText(selectedUser.username);
                                userTypeDropdown.setValue(selectedUser.userType);
                            }
                        }
                    }
                    refreshSelected();
                    event.consume();
                }
            });
            
            accountList.getChildren().add(b);
            //lstmList.setPrefHeight(60*(i+1));
            scrollAnchorPane.setPrefHeight(60*(i+1));
        }
        refreshSelected();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        refreshUserList();
        userListScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        userTypeDropdown.setItems(FXCollections.observableArrayList(permission.values()));
    }    
    
}
