package tests;

import Pojos.Role;
import Pojos.User;
import jdbcs.JDBCManager;
import jdbcs.JDBCRole;
import jdbcs.JDBCUser;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCUserTest {
    private static JDBCManager manager;
    private static JDBCUser userManager;
    private static JDBCRole roleManager;

    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        roleManager = new JDBCRole(manager);
        userManager = new JDBCUser(manager, roleManager);
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
    void addUser() {
        Role role = new Role(1,"patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), 1);
    }

    @Test
    void logIn() {
    }

    @Test
    void getIdFromEmail() {
    }

    @Test
    void assignRole() {
    }

    @Test
    void checkPassword() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void checkUsername() {
    }


}