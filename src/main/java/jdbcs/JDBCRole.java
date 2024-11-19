package jdbcs;

import Pojos.Role;
import ifaces.RoleManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCRole implements RoleManager {
    JDBCManager manager;
    public JDBCRole(JDBCManager manager){this.manager = manager;}

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
