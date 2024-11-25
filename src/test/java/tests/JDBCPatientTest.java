package tests;

import Pojos.Role;
import Pojos.User;
import Pojos.Symptoms;
import jdbcs.*;
import org.junit.jupiter.api.*;
import Pojos.Patient;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;



import static org.junit.jupiter.api.Assertions.*;

class JDBCPatientTest {

    private static JDBCManager manager;
    private static JDBCUser userManager;
    private static JDBCPatient patientManager;
    private static JDBCSymptoms symptomManager;
    private static JDBCRole roleManager;
    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        roleManager = new JDBCRole(manager);
        userManager = new JDBCUser(manager, roleManager);
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
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        User u2 = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u.getPassword()), u2.getRole().getId());
        LocalDate dob1= LocalDate.of(2003, 6, 12);
        LocalDate dob2= LocalDate.of(2004, 2, 22);
        patientManager.addPatient("Antonio", "Griezmann", dob1,"anto.grizi@example.com", u.getId());
        patientManager.addPatient("Jorge", "Resurreccion", dob2,"jorge.res@example.com",u2.getId());
        ArrayList<Patient> patients = patientManager.readPatients();
        assertEquals(2,patients.size());
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Alice") && p.getSurname().equals("Smith")));
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Bob") && p.getSurname().equals("Brown")));

    }

    @Test
    void addPatient() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getId());
        LocalDate dob = LocalDate.of(2000, 1, 1);
        patientManager.addPatient("David", "Broncano", dob, "david.bro@example.com", u.getId());
        ArrayList<Patient> patients = patientManager.readPatients();
        assertEquals(1, patients.size());
        assertEquals("David", patients.get(0).getName());
        assertEquals("Broncano", patients.get(0).getSurname());
    }

    @Test
    void getId() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getId());
        LocalDate dob = LocalDate.of(1990, 1, 1);
        patientManager.addPatient("Pablo", "Motos", dob, "pablo.mo@example.com", u.getId());
        int id = patientManager.getId("Pablo");
        assertTrue(id > 0);
    }

    @Test
    void emailToId() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getId());
        LocalDate dob = LocalDate.of(1985, 3, 15);
        patientManager.addPatient("Donald", "Trump", dob, "donald.tru@example.com", u.getId());
        int id = patientManager.emailToId("donald.tru@example.com");
        assertTrue(id > 0);
    }

    @Test
    void getPatientFromUser() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getId());
        LocalDate dob = LocalDate.of(1992, 7, 20);
        patientManager.addPatient("Even", "Adams", dob, "eve.adams@example.com", u.getId());

        Patient patient = patientManager.getPatientFromUser(5);
        assertNotNull(patient);
        assertEquals("Even", patient.getName());
    }

    @Test
    void getPatientFromId() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getId());
        LocalDate dob = LocalDate.of(1993, 8, 25);
        patientManager.addPatient("Frank", "Cuesta", dob, "frank.cuesta@example.com", u.getId());

        Patient patient = patientManager.getPatientFromId(6);
        assertNotNull(patient);
        assertEquals("Frank", patient.getName());
        assertEquals("Cuesta", patient.getSurname());

    }

    @Test
    void getPatientFromEmail() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        LocalDate dob = LocalDate.of(1994, 8, 25);
        patientManager.addPatient("Lamine","Yamal", dob, "lamine.yamal@example.com", u.getId());

        Patient patient = patientManager.getPatientFromEmail("lamine.yamal@example.com");
        assertNotNull(patient);
        assertEquals("Lamine", patient.getName());
        assertEquals("Yamal", patient.getSurname());
    }

    @Test
    void removePatientById() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        LocalDate dob = LocalDate.of(1994, 10, 10);
        patientManager.addPatient("Belen", "Esteban", dob, "belen.esteban@example.com", u.getId());
        ArrayList<Patient> patientsBefore = patientManager.readPatients();
        assertEquals(1, patientsBefore.size());
        patientManager.removePatientById(7);
        ArrayList<Patient> patientsAfter = patientManager.readPatients();
        assertTrue(patientsAfter.isEmpty());

    }

    @Test
    void assignSymtomsToPatient() {
        patientManager= new JDBCPatient(manager);
        symptomManager = new JDBCSymptoms(manager);
        Role role = new Role(1, "patient");
        User u = new User("example@gmail.com", "password".getBytes(), role);
        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getId());
        Symptoms symptom1 = new Symptoms(1, "Fever");
        Symptoms symptom2 = new Symptoms(2, "Headache");
        symptomManager.addSymptom(symptom1);
        symptomManager.addSymptom(symptom2);


        LocalDate dob = LocalDate.of(2000, 1, 1);
        patientManager.addPatient("Leo", "Messi", dob, "leo.messi@example.com", u.getId());


        patientManager.assignSymtomsToPatient(u.getRole().getId(), 1);
        patientManager.assignSymtomsToPatient(u.getRole().getId(), 2);

        ArrayList<String> symptoms = symptomManager.getSymptomsForPatient(u.getId());
        assertEquals(2, symptoms.size());
        assertTrue(symptoms.contains("Fever"));
        assertTrue(symptoms.contains("Headache"));
    }

}