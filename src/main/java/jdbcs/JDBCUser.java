package jdbcs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Pojos.User;
import Pojos.Role;
import ifaces.UserManager;
/**
 * The {@code JDBCUser} class manages database operations for users.
 * It includes methods for adding users, authenticating, updating roles, and managing passwords.
 */
public class JDBCUser implements UserManager {
    JDBCManager manager;
    JDBCRole roleManager;

    /**
     * Constructs a {@code JDBCUser} instance with the specified JDBC manager and role manager.
     *
     * @param manager the {@code JDBCManager} instance to handle database connections
     * @param role    the {@code JDBCRole} instance to handle role-related operations
     */
    public JDBCUser(JDBCManager manager, JDBCRole role){
        this.manager = manager;
        this.roleManager = role;
    }

    /**
     * Adds a new user to the database.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @param role_id  the role ID to assign to the user
     */
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
    /**
     * Logs a user into the system by verifying their email and password.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return a {@code User} object if authentication is successful; {@code null} otherwise
     */
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
    /**
     * Retrieves a user's ID from their email.
     *
     * @param email the email of the user
     * @return the user ID, or {@code 0} if not found
     */
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
    /**
     * Assigns a new role to an existing user.
     *
     * @param user the {@code User} object to update
     * @param role the {@code Role} object to assign
     */
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
    /**
     * Verifies the provided email and password combination.
     *
     * @param email    the email of the user
     * @param password the password of the user
     * @return a {@code User} object if the credentials are correct; {@code null} otherwise
     */
    public User checkPassword(String email, String password) {
        String sql = "SELECT id, role_id FROM User WHERE email=? AND password=?";
        PreparedStatement s = null;
        ResultSet rs = null;
        User u = null;

        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            s.setString(2, password);

            rs = s.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");
                int roleId = rs.getInt("role_id");

                Role role = roleManager.getRoleById(roleId);

                byte[] psw = password.getBytes();
                u = new User(userId, email, psw, role);
            }
        } catch (SQLException e) {
            System.out.println("Error during login process.");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (s != null) s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return u;
    }
    /**
     * Changes the password for a specific user.
     *
     * @param user        the {@code User} object to update
     * @param newPassword the new password to set
     */
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
    /**
     * Checks if a username (email) exists and retrieves the associated user information.
     *
     * @param email the email to check
     * @return a {@code User} object if the email exists; {@code null} otherwise
     */
    public User checkUsername(String email) {
        String sql = "SELECT id, role_id, email, password FROM User WHERE email = ?;";
        PreparedStatement s;
        User u = null;
        Role role;
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
