/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.model;

/**
 *
 * @author e19
 */
public class UserExistsException extends Exception{
    public UserExistsException(){
        super("user already exists");
    }
}
