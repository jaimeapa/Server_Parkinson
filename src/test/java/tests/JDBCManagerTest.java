package tests;

import jdbcs.JDBCManager;
import jdbcs.JDBCRole;
import jdbcs.JDBCUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JDBCManagerTest {

    private static JDBCManager manager;
    private static JDBCUser userManager;
    private static JDBCRole roleManager;

    @BeforeEach
    void setUp() {
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
    void getConnection() {
    }

    @Test
    void disconnect() {
    }
}