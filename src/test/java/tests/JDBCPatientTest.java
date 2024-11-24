package tests;


import Pojos.Symptoms;
import jdbcs.JDBCSymptoms;
import org.junit.jupiter.api.*;
import jdbcs.JDBCManager;
import jdbcs.JDBCPatient;
import Pojos.Patient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;



import static org.junit.jupiter.api.Assertions.*;

class JDBCPatientTest {

    private static JDBCManager manager;
    private static JDBCPatient patientManager;
    private static JDBCSymptoms symptomManager;
    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        patientManager = new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
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
    void setUp() throws SQLException {
        manager.clearTables(); // Assuming this clears test-related data
        assertNotNull(patientManager);
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
    void readPatients() {
        LocalDate dob1= LocalDate.of(2003, 6, 12);
        LocalDate dob2= LocalDate.of(2004, 2, 22);
        patientManager.addPatient("Antonio", "Griezmann", dob1,"anto.grizi@example.com", 1);
        patientManager.addPatient("Jorge", "Resurreccion", dob2,"jorge.res@example.com",2);
        ArrayList<Patient> patients = patientManager.readPatients();
        assertEquals(2,patients.size());
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Alice") && p.getSurname().equals("Smith")));
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Bob") && p.getSurname().equals("Brown")));

    }

    @Test
    void addPatient() {
        LocalDate dob = LocalDate.of(2000, 1, 1);
        patientManager.addPatient("David", "Broncano", dob, "david.bro@example.com", 1);
        ArrayList<Patient> patients = patientManager.readPatients();
        assertEquals(1, patients.size());
        assertEquals("David", patients.get(0).getName());
        assertEquals("Broncano", patients.get(0).getSurname());
    }

    @Test
    void getId() {
        LocalDate dob = LocalDate.of(1990, 1, 1);
        patientManager.addPatient("Pablo", "Motos", dob, "pablo.mo@example.com", 3);
        int id = patientManager.getId("Pablo");
        assertTrue(id > 0);
    }

    @Test
    void emailToId() {
        LocalDate dob = LocalDate.of(1985, 3, 15);
        patientManager.addPatient("Donald", "Trump", dob, "donald.tru@example.com", 4);
        int id = patientManager.emailToId("donald.tru@example.com");
        assertTrue(id > 0);
    }

    @Test
    void getPatientFromUser() {
        LocalDate dob = LocalDate.of(1992, 7, 20);
        patientManager.addPatient("Even", "Adams", dob, "eve.adams@example.com", 5);

        Patient patient = patientManager.getPatientFromUser(5);
        assertNotNull(patient);
        assertEquals("Even", patient.getName());
    }

    @Test
    void getPatientFromId() {
        LocalDate dob = LocalDate.of(1993, 8, 25);
        patientManager.addPatient("Frank", "Cuesta", dob, "frank.cuesta@example.com", 6);

        Patient patient = patientManager.getPatientFromId(6);
        assertNotNull(patient);
        assertEquals("Frank", patient.getName());
        assertEquals("Cuesta", patient.getSurname());

    }

    @Test
    void getPatientFromEmail() {
        LocalDate dob = LocalDate.of(1994, 8, 25);
        patientManager.addPatient("Lamine","Yamal", dob, "lamine.yamal@example.com", 7);

        Patient patient = patientManager.getPatientFromEmail("lamine.yamal@example.com");
        assertNotNull(patient);
        assertEquals("Lamine", patient.getName());
        assertEquals("Yamal", patient.getSurname());
    }

    @Test
    void removePatientById() {
        LocalDate dob = LocalDate.of(1994, 10, 10);
        patientManager.addPatient("Belen", "Esteban", dob, "belen.esteban@example.com", 7);
        ArrayList<Patient> patientsBefore = patientManager.readPatients();
        assertEquals(1, patientsBefore.size());
        patientManager.removePatientById(7);
        ArrayList<Patient> patientsAfter = patientManager.readPatients();
        assertTrue(patientsAfter.isEmpty());

    }

    @Test
    void assignSymtomsToPatient() {
        Symptoms symptom1 = new Symptoms(1, "Fiebre");
        Symptoms symptom2 = new Symptoms(1, "Dolor de cabeza");
        symptomManager.addSymptom(symptom1);
        symptomManager.addSymptom(symptom2);


        LocalDate dob = LocalDate.of(2000, 1, 1);
        patientManager.addPatient("Leo", "Messi", dob, "leo.messi@example.com", 8);


        patientManager.assignSymtomsToPatient(8, 1);
        patientManager.assignSymtomsToPatient(8, 2);

        ArrayList<String> symptoms = symptomManager.getSymptomsForPatient(10);
        assertEquals(2, symptoms.size());
        assertTrue(symptoms.contains("Fiebre"));
        assertTrue(symptoms.contains("Dolor de cabeza"));
    }

}