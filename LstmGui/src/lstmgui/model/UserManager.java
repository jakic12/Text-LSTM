/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import lstmgui.model.User;

import lstmgui.model.UserManager.permission;

/**
 *
 * @author e19
 */
public class UserManager {
    public static String userFile = "users.u";
    
    public static enum permission{
        USER, // standard user
        ADMIN // rights to manage users
    }
    
    public static User[] getUsers(){
        File in = new File(userFile);
        User[] outUser = null;
        if(!in.exists())
            return new User[]{new User("admin", "admin", permission.ADMIN)};
        else{
            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(new FileInputStream(in));
                outUser = (User[])(is.readObject());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if(is != null)
                        is.close();
                } catch (IOException ex) {
                    Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return outUser;
    }
    
    public static void saveUsers(User[] users){
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(new File(userFile)));
            os.writeObject(users);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(os != null)
                    os.close();
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void addUser(String u, String p) throws UserExistsException{
        if(checkUser(u,p) == -1){
            ArrayList<User> users = new ArrayList<User>(Arrays.asList(getUsers()));
            users.add(new User(u,p,permission.USER));
            System.out.println(users.size());
            saveUsers(users.toArray(new User[users.size()]));
        }else{
            throw new UserExistsException();
        }
    }
    
    public static User editUser(User user, String u, String p, permission per){
        User[] users = getUsers();
        for(int i = 0; i < users.length; i++){
            if(users[i].equals(user)){
                users[i].setUsername(u);
                users[i].setPass(u);
                users[i].setUserType(per);
                
                saveUsers(users);
                return users[i];
            }
        }
        return null;
    }
    
    public static User editUser(User user, String u, permission per){
        User[] users = getUsers();
        for(int i = 0; i < users.length; i++){
            if(users[i].equals(user)){
                users[i].setUsername(u);
                users[i].setUserType(per);
                
                saveUsers(users);
                return users[i];
            }
        }
        return null;
    }
    /**
     * 
     * @param u
     * @param p
     * @return returns -1 if user doesnt exist, -2 if password is wrong, the index of the user if password and username are correct
     */
    public static int checkUser(String u, String p){
        p = stateManager.getSHA(p);
        int out = -1;
        User[] users = getUsers();
        for(int i = 0; i < users.length; i++){
            if(users[i].username.equals(u)){
                out = -2;
                if(users[i].pass.equals(p)){
                    out = i;
                }
            }
        }
        return out;
    }

    public static void printUsers() {
        User[] users = getUsers();
        for(User a : users){
            System.out.println(a.id);
            System.out.println(a.username);
            System.out.println(a.pass);
        }
    }
}
