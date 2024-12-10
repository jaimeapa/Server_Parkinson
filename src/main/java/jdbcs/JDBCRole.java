package jdbcs;

import Pojos.Role;
import ifaces.RoleManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * The {@code JDBCRole} class provides database access and management methods
 * for the "Role" table. It implements the {@code RoleManager} interface.
 */
public class JDBCRole implements RoleManager {
    JDBCManager manager;
    /**
     * Constructs a {@code JDBCRole} instance with the specified database manager.
     *
     * @param manager the {@code JDBCManager} instance to handle database connections
     */
    public JDBCRole(JDBCManager manager){this.manager = manager;}
    /**
     * Retrieves a {@code Role} object by its ID.
     *
     * @param role_id the ID of the role to retrieve
     * @return the {@code Role} object corresponding to the given ID, or {@code null} if not found
     */
    public Role getRoleById( int role_id){
        String sql = "SELECT name FROM Role WHERE id=?; ";
        PreparedStatement s;
        String name;
        Role role = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, role_id);
            ResultSet rs = s.executeQuery();
            name = rs.getString("name");
            role = new Role(role_id, name);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return role;
    }

}
