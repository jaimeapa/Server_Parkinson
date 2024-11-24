package tests;

import org.junit.jupiter.api.*;
import jdbcs.JDBCSymptoms;
import jdbcs.JDBCManager;
import Pojos.Symptoms;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class JDBCSymptomsTest {

    private static JDBCManager manager;
    private static JDBCSymptoms symptomsManager;

    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        symptomsManager = new JDBCSymptoms(manager);
        try {
            manager.getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @AfterAll
    static void afterAll() {
        if (manager != null) {
            try {
                manager.getConnection().setAutoCommit(true);
                manager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @BeforeEach
    void setUp() throws SQLException{
        manager.clearTables();
        assertNotNull(symptomsManager);
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
    void readSymptoms() {

        symptomsManager.addSymptom(new Symptoms(1, "Cough"));
        symptomsManager.addSymptom(new Symptoms(2, "Headache"));

        ArrayList<Symptoms> symptomsList = symptomsManager.readSymptoms();
        assertEquals(2, symptomsList.size());
        assertTrue(symptomsList.stream().anyMatch(s -> s.getName().equals("Cough")));
        assertTrue(symptomsList.stream().anyMatch(s -> s.getName().equals("Headache")));


        /*ArrayList<Symptoms> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM Symptoms";

        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Symptoms symptom = new Symptoms(id, name);
                symptoms.add(symptom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return symptoms;^*/

    }

    @Test
    void addSymptom() {
        Symptoms symptom = new Symptoms(1, "Fever");
        symptomsManager.addSymptom(symptom);

        ArrayList<Symptoms> symptomsList = symptomsManager.readSymptoms();
        assertEquals(1, symptomsList.size());
        assertEquals("Fever", symptomsList.get(0).getName());
    }

    @Test
    void getSymptomById() throws SQLException{
        symptomsManager.addSymptom(new Symptoms(1, "Fatigue"));
        Symptoms symptom = symptomsManager.getSymptomById(1);

        assertNotNull(symptom);
        assertEquals("Fatigue", symptom.getName());

    }

    @Test
    void getSymptomsLength() {
        symptomsManager.addSymptom(new Symptoms(1, "Nausea"));
        symptomsManager.addSymptom(new Symptoms(2, "Dizziness"));

        int counter = symptomsManager.getSymptomsLength();
        assertEquals(2, counter);
    }
}