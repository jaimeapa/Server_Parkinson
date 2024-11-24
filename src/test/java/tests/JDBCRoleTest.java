package tests;

import Pojos.Role;
import jdbcs.JDBCManager;
import jdbcs.JDBCRole;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCRoleTest {
    private static JDBCManager manager;
    private static JDBCRole roleManager;

    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        roleManager = new JDBCRole(manager);
        try{
            manager.getConnection().setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    @AfterAll
    static void afterAll() {
        if(manager != null){
            try{
                manager.getConnection().setAutoCommit(true);
                manager.disconnect();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    @BeforeEach
    public void setUp()  throws SQLException{
        manager.clearTables();
        assertNotNull(roleManager);
    }

    @AfterEach
    void tearDown() {
        if (manager.getConnection() != null) {
            try {
                manager.getConnection().rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void getRoleById() {
        Role role = roleManager.getRoleById(1);
        System.out.println(role.getName());
    }


}