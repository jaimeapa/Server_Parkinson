package jdbcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Pojos.User;
import Pojos.Role;

public class JDBCUser {
    JDBCManager manager;
    JDBCRole roleManager;
    public JDBCUser(JDBCManager manager, JDBCRole role){
        this.manager = manager;
        this.roleManager = role;
    }

    public void addUser(String email, String password, Integer role_id) {
        String sql= "INSERT INTO User (email, password, role_id) VALUES (?,?,?);";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, email);
            p.setString(2, password);
            p.setInt(4, role_id);
            p.executeUpdate();
            p.close();
        }catch(SQLException e ) {
            e.printStackTrace();
        }
    }

    public User logIn(String email, String password)
    {
        String sql = "SELECT role_id AND id FROM User WHERE email=? AND password=?; ";
        PreparedStatement s;
        User u = null;
        Role role = null;
        int roleId;
        int userId;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            s.setString(2, password);
            ResultSet rs = s.executeQuery();
            userId = rs.getInt("id");
            roleId = rs.getInt("role_Id");
            byte[] psw = password.getBytes();
            role = roleManager.getRoleById(roleId);
            u = new User(userId, email, psw, role);
            s.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        if (role != null)
            return u;
        else
            return null;
    }

}
