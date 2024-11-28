package jdbcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Pojos.User;
import Pojos.Role;
import ifaces.UserManager;

public class JDBCUser implements UserManager {
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
            p.setInt(3, role_id);
            p.executeUpdate();
            p.close();
        }catch(SQLException e ) {
            e.printStackTrace();
        }
    }

    public User logIn(String email, String password)
    {
        String sql = "SELECT id, role_id FROM User WHERE email=? AND password=?; ";
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
            roleId = rs.getInt("role_id");
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
    public int getIdFromEmail(String email){
        String sql = "SELECT id FROM User WHERE email=?; ";
        PreparedStatement s;
        Integer userId = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            userId = rs.getInt("id");
            s.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return userId;
    }
    public void assignRole(User user, Role role){
        String sql = "UPDATE User SET role_id = ? WHERE id = ?;";
        try{
            PreparedStatement s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, role.getId());
            s.setInt(2, user.getId());
            s.executeUpdate();
            s.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public User checkPassword(String email, String password){
        String sql = "SELECT id, role_id FROM User WHERE email=? AND password=?;";
        PreparedStatement s;
        User u = null;
        Role role = null;
        int roleId;
        int userId;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            s.setBytes(2, password.getBytes());
            ResultSet rs = s.executeQuery();
            userId = rs.getInt("id");
            roleId = rs.getInt("role_id");
            byte[] psw = password.getBytes();
            role = roleManager.getRoleById(roleId);
            u = new User(userId, email, psw, role);
            s.close();
            rs.close();
        }catch(SQLException e){
            System.out.println("Username or password incorrect");
            e.printStackTrace();
        }
        if (role != null)
            return u;
        else
            return null;
    }
    public void changePassword(User user, String newPassword){
        String sql = "UPDATE User SET password = ? WHERE id = ?;";
        try{
            PreparedStatement s = manager.getConnection().prepareStatement(sql);
            s.setString(1, newPassword);
            s.setInt(2, user.getId());
            s.executeUpdate();
            s.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public User checkUsername(String email) {
        String sql = "SELECT id, role_id, email, password FROM User WHERE email = ?;";
        PreparedStatement s;
        User u = null;
        Role role = null;
        int roleId;
        int userId;

        try {

            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
                roleId = rs.getInt("role_id");
                byte[] psw = rs.getBytes("password");

                role = roleManager.getRoleById(roleId);

                u = new User(userId, email, psw, role);
            }

            s.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;
    }


}
