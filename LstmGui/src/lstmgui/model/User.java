/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lstmgui.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author jakob
 */
public class User implements Serializable {
    public String id;
    public String username;
    public String pass;
    
    public UserManager.permission userType;
    
    public User(String u, String p, UserManager.permission pe){
        setUsername(u);
        setPass(p);
        this.id = stateManager.getSHA(u+p+(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
        setUserType(pe);
    }

    void setUsername(String u) {
        this.username = u;
    }

    void setPass(String p) {
        this.pass = stateManager.getSHA(p);
    }

    void setUserType(UserManager.permission per) {
        this.userType = per;
    }
    
    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User obj = (User) o;
        return (this.id.equals(obj.id) && this.pass.equals(obj.pass) && this.username.equals(obj.username) && this.userType.equals(obj.userType));
    }
}
