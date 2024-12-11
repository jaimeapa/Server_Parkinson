package tests;

import Pojos.Doctor;
import Pojos.Patient;
import Pojos.Role;
import Pojos.User;
import jdbcs.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JDBCDoctorTest {
    private static JDBCManager manager;
    private static JDBCUser userManager;
    private static JDBCPatient patientManager;
    private static JDBCSymptoms symptomManager;
    private static JDBCRole roleManager;
    private static JDBCDoctor doctorManager;

    @BeforeAll
    static void beforeAll() {
        manager = new JDBCManager();
        roleManager = new JDBCRole(manager);
        userManager = new JDBCUser(manager, roleManager);
        patientManager= new JDBCPatient(manager);
        doctorManager = new JDBCDoctor(manager);
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
    void readDoctors() {

        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("anto.grizi@example.com", "password".getBytes(), role2);
        User u2 = new User("jorge.res@example.com", "password".getBytes(), role2);
        User u3 = new User("rodri.hernandez@example.com", "password".getBytes(), role);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());
        userManager.addUser(u3.getEmail(), new String(u3.getPassword()), u3.getRole().getId());

        LocalDate dob1= LocalDate.of(2003, 6, 12);
        LocalDate dob2= LocalDate.of(2004, 2, 22);
        LocalDate dob3= LocalDate.of(1996, 6, 3);

        int id1 = userManager.getIdFromEmail("anto.grizi@example.com");
        int id2 = userManager.getIdFromEmail("jorge.res@example.com");
        int id3 = userManager.getIdFromEmail("rodri.hernandez@example.com");


        doctorManager.addDoctor("Antonio", "Griezmann", dob1,"anto.grizi@example.com", id1);
        doctorManager.addDoctor("Jorge", "Resurreccion", dob2,"jorge.res@example.com", id2);
        int doctor_id= doctorManager.getId("Antonio");

        patientManager.addPatient("Rodrigo", "Hernandez", dob3, "rodri.hernandez@example.com",doctor_id, id3);
        int patient_id = patientManager.getId("Rodrigo");


        ArrayList<Doctor> doctors = doctorManager.readDoctors();
        assertEquals(2,doctors.size());
        assertTrue(doctors.stream().anyMatch(p -> p.getName().equals("Antonio") && p.getSurname().equals("Griezmann")));
        assertTrue(doctors.stream().anyMatch(p -> p.getName().equals("Jorge") && p.getSurname().equals("Resurreccion")));

    }

    @Test
    void addDoctor() {

        Role role2 = new Role(2, "doctor");
        User u = new User("david.bro@example.com", "password".getBytes(), role2);
        User u2 = new User("pablo.motos@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(2000, 1, 1);
        LocalDate dob2 = LocalDate.of(1976, 6, 2);

        int id = userManager.getIdFromEmail("david.bro@example.com");
        int id2 = userManager.getIdFromEmail("pablo.motos@example.com");

        doctorManager.addDoctor("Pablo","Motos", dob, "pablo.motos@example.com", id2);
        doctorManager.addDoctor("David","Broncano", dob2, "david.bro@example.com", id);

        ArrayList<Doctor> doctors = doctorManager.readDoctors();
        assertEquals(2, doctors.size());
        assertEquals("Pablo", doctors.get(0).getName());
        assertEquals("Motos", doctors.get(0).getSurname());
        assertEquals("David", doctors.get(1).getName());
        assertEquals("Broncano", doctors.get(1).getSurname());
    }

    @Test
    void getId() {
        Role role2 = new Role(2, "doctor");
        User u2 = new User("Ale.galan@example.com", "password".getBytes(), role2);

        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1990, 1, 1);
        int id2 = userManager.getIdFromEmail("Ale.galan@example.com");

        doctorManager.addDoctor("Ale","Galan", dob, "Ale.galan@example.com", id2);
        int doctor_id = doctorManager.getId("Ale");


        assertTrue(doctor_id > 0);
    }

    @Test
    void getDoctorFromUser() {
        Role role2 = new Role(2, "doctor");
        User u2 = new User("Ale.galan@example.com", "password".getBytes(), role2);

        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1990, 1, 1);

        int id2 = userManager.getIdFromEmail("Ale.galan@example.com");
        doctorManager.addDoctor("Ale","Galan", dob, "Ale.galan@example.com", id2);

        Doctor doctor = doctorManager.getDoctorFromUser(id2);
        assertNotNull(doctor);
        assertEquals("Ale", doctor.getName());

    }

    @Test
    void getDoctorFromId() {

        Role role2 = new Role(2, "doctor");
        User u = new User("frank.cuesta@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());

        LocalDate dob = LocalDate.of(1993, 8, 25);


        int id = userManager.getIdFromEmail("frank.cuesta@example.com");




        doctorManager.addDoctor("Frank", "Cuesta", dob, "frank.cuesta@example.com", id);
        int doctor_id= doctorManager.getId("Frank");

        Doctor doctor = doctorManager.getDoctorFromId(doctor_id);
        assertNotNull(doctor);
        assertEquals("Frank", doctor.getName());
        assertEquals("Cuesta", doctor.getSurname());
    }
}