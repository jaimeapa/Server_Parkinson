package tests;

import jdbcs.JDBCInterpretation;
import org.junit.jupiter.api.*;
import jdbcs.*;
import Pojos.*;

import java.sql.SQLException;
import java.util.ArrayList;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;


import static org.junit.jupiter.api.Assertions.*;

class JDBCSymptomsTest {

    private static JDBCManager manager;
    private static JDBCSymptoms symptomsManager;
    private static JDBCInterpretation interpretationManager;
    private static JDBCUser userManager;
    private static JDBCPatient patientManager;
    private static JDBCDoctor doctorManager;

    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        symptomsManager = new JDBCSymptoms(manager);
        userManager = new JDBCUser(manager, new JDBCRole(manager));
        patientManager = new JDBCPatient(manager);
        doctorManager = new JDBCDoctor(manager);
        symptomsManager = new JDBCSymptoms(manager);
        interpretationManager = new JDBCInterpretation(manager);
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
    void getId() {

        Symptoms symptom1 = new Symptoms(1, "Headache");
        Symptoms symptom2 = new Symptoms(2, "Fever");

        symptomsManager.addSymptom(symptom1);
        symptomsManager.addSymptom(symptom2);

        int id1 = symptomsManager.getId(symptom1);
        int id2 = symptomsManager.getId(symptom2);

        assert id1 == symptom1.getId() : "Failed to retrieve correct ID for symptom1";
        assert id2 == symptom2.getId() : "Failed to retrieve correct ID for symptom2";

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

    @Test
    void getSymptomsFromInterpretation() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("leo.messi@example.com", "password".getBytes(), role);
        User u2 = new User("vini.junior@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());
        Symptoms symptom1 = new Symptoms(1, "Fever");
        Symptoms symptom2 = new Symptoms(2, "Headache");
        symptomsManager.addSymptom(symptom1);
        symptomsManager.addSymptom(symptom2);


        LocalDate dob = LocalDate.of(2000, 1, 1);
        LocalDate dob2 = LocalDate.of(2004, 1, 2);
        int id = userManager.getIdFromEmail("leo.messi@example.com");
        int id2 = userManager.getIdFromEmail("vini.junior@example.com");

        doctorManager.addDoctor("Vini", "Junior", dob2, "vini.junior@example.com", id2);
        int doctor_id = doctorManager.getId("Vini");
        patientManager.addPatient("Leo", "Messi", dob, "leo.messi@example.com", doctor_id, id);
        int patient_id = patientManager.getId("Leo");

        Signal signalEMG = new Signal(Arrays.asList(1, 2, 3, 4, 5), Signal.SignalType.EMG);
        Signal signalEDA = new Signal(Arrays.asList(6, 7, 8, 9, 10), Signal.SignalType.EDA);

        LocalDate date = LocalDate.now();

        Interpretation interpretation =new Interpretation(date,"patient is improving.",signalEMG,signalEDA,patient_id,doctor_id,"Nice job");
        interpretationManager.addInterpretation(interpretation);
        System.out.println(interpretation);
        int interpretation_id = interpretationManager.getId(date,patient_id);
        interpretationManager.assignSymtomsToInterpretation(interpretation_id, 1);
        interpretationManager.assignSymtomsToInterpretation(interpretation_id, 2);
        LinkedList<Symptoms> symptoms = symptomsManager.getSymptomsFromInterpretation(interpretation_id);
        System.out.println(symptoms);
    }
}