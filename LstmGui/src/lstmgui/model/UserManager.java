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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author e19
 */
public class UserManager {
    public static String userFile = "users.u";
    
    private static User[] getUsers(){
        File in = new File(userFile);
        User[] outUser = null;
        if(!in.exists())
            return new User[0];
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
            users.add(new User(u,p));
            System.out.println(users.size());
            saveUsers(users.toArray(new User[users.size()]));
        }else{
            throw new UserExistsException();
        }
    }
    /**
     * 
     * @param u
     * @param p
     * @return returns -1 if user doesnt exist, 0 if password is wrong, 1 if password and username are correct
     */
    public static int checkUser(String u, String p){
        p = stateManager.getSHA(p);
        int out = -1;
        User[] users = getUsers();
        for(int i = 0; i < users.length; i++){
            if(users[i].username.equals(u)){
                out = 0;
                if(users[i].pass.equals(p)){
                    out = 1;
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

class User implements Serializable{
    String id;
    public String username;
    public String pass;
    
    public User(String u, String p){
        this.username = u;
        this.pass = stateManager.getSHA(p);
        this.id = stateManager.getSHA(u+p);
    }
}
