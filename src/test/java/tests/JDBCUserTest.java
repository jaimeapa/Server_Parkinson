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
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
    }

    @Test
    void logIn() {
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        User u2 = userManager.logIn(u.getEmail(), new String(u.getPassword()));
        System.out.println(u.toString());
    }

    @Test
    void getIdFromEmail() {
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        System.out.println(userManager.getIdFromEmail(u.getEmail()));
    }

    @Test
    void assignRole() {
        Role role = new Role(1, "patient");
        User u = new User(1,"example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        Role role2 = new Role(2, "doctor");
        userManager.assignRole(u, role2);
    }

    @Test
    void checkPassword() {
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        User u2= userManager.checkPassword(u.getEmail(), new String(u.getPassword()));
        System.out.println(u.toString());
    }

    @Test
    void changePassword() {
        Role role = new Role(1, "patient");
        User u = new User(1,"example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        String newPassword = "newpassword";
        userManager.changePassword(u,newPassword);
    }

    @Test
    void checkUsername() {
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        User user = userManager.checkUsername(u.getEmail());
        System.out.println(u.toString());
    }


}