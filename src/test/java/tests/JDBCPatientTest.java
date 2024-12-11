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
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class JDBCPatientTest {

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
    void readPatients() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("anto.grizi@example.com", "password".getBytes(), role);
        User u2 = new User("jorge.res@example.com", "password".getBytes(), role);
        User u3 = new User("rodri.hernandez@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());
        userManager.addUser(u3.getEmail(), new String(u3.getPassword()), u3.getRole().getId());

        LocalDate dob1= LocalDate.of(2003, 6, 12);
        LocalDate dob2= LocalDate.of(2004, 2, 22);
        LocalDate dob3= LocalDate.of(1996, 6, 3);

        int id1 = userManager.getIdFromEmail("anto.grizi@example.com");
        int id2 = userManager.getIdFromEmail("jorge.res@example.com");
        int id3 = userManager.getIdFromEmail("rodri.hernandez@example.com");

        doctorManager.addDoctor("Rodrigo", "Hernandez", dob3, "rodri.hernandez@example.com", id3);
        int doctor_id = doctorManager.getId("Rodrigo");

        patientManager.addPatient("Antonio", "Griezmann", dob1,"anto.grizi@example.com", doctor_id, id1);
        patientManager.addPatient("Jorge", "Resurreccion", dob2,"jorge.res@example.com", doctor_id, id2);

        ArrayList<Patient> patients = patientManager.readPatients();
        assertEquals(2,patients.size());
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Antonio") && p.getSurname().equals("Griezmann")));
        assertTrue(patients.stream().anyMatch(p -> p.getName().equals("Jorge") && p.getSurname().equals("Resurreccion")));

    }

    @Test
    void addPatient() {

        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("david.bro@example.com", "password".getBytes(), role);
        User u2 = new User("pablo.motos@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(2000, 1, 1);
        LocalDate dob2 = LocalDate.of(1976, 6, 2);

        int id = userManager.getIdFromEmail("david.bro@example.com");
        int id2 = userManager.getIdFromEmail("pablo.motos@example.com");

        doctorManager.addDoctor("Pablo","Motos", dob, "pablo.motos@example.com", id2);
        int doctor_id = doctorManager.getId("Pablo");

        patientManager.addPatient("David", "Broncano", dob2, "david.bro@example.com", doctor_id, id);

        ArrayList<Patient> patients = patientManager.readPatients();
        assertEquals(1, patients.size());
        assertEquals("David", patients.get(0).getName());
        assertEquals("Broncano", patients.get(0).getSurname());
    }

    @Test
    void getId() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("Ibai.llanos@example.com", "password".getBytes(), role);
        User u2 = new User("Ale.galan@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1990, 1, 1);
        LocalDate dob2 = LocalDate.of(1996, 6, 2);

        int id = userManager.getIdFromEmail("Ibai.llanos@example.com");
        int id2 = userManager.getIdFromEmail("Ale.galan@example.com");

        doctorManager.addDoctor("Ale","Galan", dob, "Ale.galan@example.com", id2);
        int doctor_id = doctorManager.getId("Ale");

        patientManager.addPatient("Ibai", "Llanos", dob2, "Ibai.llanos@example.com", doctor_id,  id);
        int patient_id = patientManager.getId("Ibai");
        assertTrue(patient_id > 0);
    }

    @Test
    void emailToId() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("donald.tru@example.com", "password".getBytes(), role);
        User u2 = new User("javier.milei@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1985, 3, 15);
        LocalDate dob2 = LocalDate.of(2003, 6, 2);

        int id = userManager.getIdFromEmail("donald.tru@example.com");
        int id2 = userManager.getIdFromEmail("javier.milei@example.com");

        doctorManager.addDoctor("Javier","Milei", dob2, "Javier.milei@example.com", id2);
        int doctor_id = doctorManager.getId("Javier");

        patientManager.addPatient("Donald", "Trump", dob, "donald.tru@example.com", doctor_id,  id);
        int patient_id = patientManager.emailToId("donald.tru@example.com");
        assertTrue(patient_id > 0);
    }

    @Test
    void getPatientFromUser() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");
        User u = new User("ayuso.diaz@example.com", "password".getBytes(), role);
        User u2 = new User("mariano.rajoy@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1992, 7, 20);
        LocalDate dob2 = LocalDate.of(1956, 6, 2);

        int id = userManager.getIdFromEmail("ayuso.diaz@example.com");
        int id2 = userManager.getIdFromEmail("mariano.rajoy@example.com");

        doctorManager.addDoctor("Mariano","Rajoy", dob2, "mariano.rajoy@example.com", id2);
        int doctor_id =doctorManager.getId("Mariano");

        patientManager.addPatient("Ayuso", "Diaz", dob, "ayuso.diaz@example.com", doctor_id, id);

        Patient patient = patientManager.getPatientFromUser(id);
        assertNotNull(patient);
        assertEquals("Ayuso", patient.getName());
    }

    @Test
    void getPatientFromId() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("frank.cuesta@example.com", "password".getBytes(), role);
        User u2 = new User("sanfe.guti@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1993, 8, 25);
        LocalDate dob2 = LocalDate.of(1990, 6, 2);

        int id = userManager.getIdFromEmail("frank.cuesta@example.com");
        int id2= userManager.getIdFromEmail("sanfe.guti@example.com");

        doctorManager.addDoctor("Sanfe","Guti", dob2, "sanfe.guti@example.com", id2);
        int doctor_id =doctorManager.getId("Sanfe");

        patientManager.addPatient("Frank", "Cuesta", dob, "frank.cuesta@example.com", doctor_id, id);
        int patient_id= patientManager.getId("Frank");

        Patient patient = patientManager.getPatientFromId(patient_id);
        assertNotNull(patient);
        assertEquals("Frank", patient.getName());
        assertEquals("Cuesta", patient.getSurname());

    }

    @Test
    void getPatientFromEmail() {
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("lamine.yamal@example.com", "password".getBytes(), role);
        User u2= new User("ansu.fati@example.com", "password".getBytes(), role2);

        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());

        LocalDate dob = LocalDate.of(1993, 2, 1);
        LocalDate dob2 = LocalDate.of(1990, 6, 2);

        int id = userManager.getIdFromEmail("lamine.yamal@example.com");
        int id2 = userManager.getIdFromEmail("ansu.fati@example.com");

        doctorManager.addDoctor("Ansu", "Fati", dob2, "ansu.fati@example.com", id2);
        int doctor_id = doctorManager.getId("Ansu");

        patientManager.addPatient("Lamine","Yamal", dob, "lamine.yamal@example.com", doctor_id, id);

        Patient patient = patientManager.getPatientFromEmail("lamine.yamal@example.com");
        assertNotNull(patient);
        assertEquals("Lamine", patient.getName());
        assertEquals("Yamal", patient.getSurname());
    }

    @Test
    void removePatientById() {

        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");


        User u = new User("belen.esteban@example.com", "password".getBytes(), role);
        User u2 = new User("maria.patino@example.com", "password".getBytes(), role2);
        User u3 = new User("kiko.rivera@example.com", "password".getBytes(), role);


        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u3.getEmail(), new String(u3.getPassword()), u3.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u2.getPassword()), u2.getRole().getId());


        LocalDate dob = LocalDate.of(1994, 10, 10);
        LocalDate dob2 = LocalDate.of(1991, 6, 2);
        LocalDate dob3 = LocalDate.of(1992, 6, 3);


        int id = userManager.getIdFromEmail("belen.esteban@example.com");
        int id2 = userManager.getIdFromEmail("maria.patino@example.com");
        int id3 = userManager.getIdFromEmail("kiko.rivera@example.com");


        doctorManager.addDoctor("Maria", "Patino", dob2, "maria.patino@example.com", id2);
        int doctor_id = doctorManager.getId("Maria");

        patientManager.addPatient("Belen", "Esteban", dob, "belen.esteban@example.com", doctor_id, id);
        patientManager.addPatient("Kiko", "Rivera", dob3, "kiko.rivera@example.com", doctor_id, id3);
        int patient_id1=patientManager.getId("Belen");
        int patient_id2=patientManager.getId("Kiko");

        ArrayList<Patient> patientsBefore = patientManager.readPatients();
        assertEquals(2, patientsBefore.size(), "Antes de la eliminación, debe haber 2 pacientes.");


        patientManager.removePatientById(patient_id1);


        ArrayList<Patient> patientsAfter = patientManager.readPatients();
        assertEquals(1, patientsAfter.size(), "Después de la eliminación, debe haber 1 paciente.");


        Patient remainingPatient = patientsAfter.get(0);
        assertEquals("Kiko", remainingPatient.getName(), "El paciente restante debe ser Kiko.");
        assertEquals("Rivera", remainingPatient.getSurname(), "El apellido del paciente restante debe ser Rivera.");
    }


    @Test
    void getPatientsByDoctorId(){
        Role role = new Role(1, "patient");
        Role role2 = new Role(2, "doctor");

        User u = new User("juan.perez@example.com", "password".getBytes(), role);
        User u2 = new User("maria.gomez@example.com", "password".getBytes(), role);
        User u3 = new User("dr.lopez@example.com", "password".getBytes(), role2);


        userManager.addUser(u.getEmail(), new String(u.getPassword()), u.getRole().getId());
        userManager.addUser(u2.getEmail(), new String(u.getPassword()), u2.getRole().getId());
        userManager.addUser(u3.getEmail(), new String(u.getPassword()), u3.getRole().getId());


        int doctorId = userManager.getIdFromEmail(u3.getEmail());
        int patientId1 = userManager.getIdFromEmail(u.getEmail());
        int patientId2 = userManager.getIdFromEmail(u2.getEmail());


        LocalDate doctorDob = LocalDate.of(1980, 5, 15);
        doctorManager.addDoctor("Dr", "Lopez", doctorDob, u3.getEmail(), doctorId);
        int doctor_Id = doctorManager.getId("Dr");


        LocalDate patientDob1 = LocalDate.of(1990, 7, 20);
        LocalDate patientDob2 = LocalDate.of(1992, 3, 10);
        patientManager.addPatient("Juan", "Perez", patientDob1, u.getEmail(), doctor_Id, patientId1);
        patientManager.addPatient("Maria", "Gomez", patientDob2, u2.getEmail(), doctor_Id, patientId2);


        List<Patient> patients = patientManager.getPatientsByDoctorId(doctor_Id);

        assertNotNull(patients, "La lista de pacientes no debería ser nula");
        assertEquals(2, patients.size(), "Debería haber exactamente 2 pacientes asociados al doctor");


        Patient patient1 = patients.get(0);
        Patient patient2 = patients.get(1);

        assertEquals("Juan", patient1.getName());
        assertEquals("Perez", patient1.getSurname());
        assertEquals(patientDob1, patient1.getDob());

        assertEquals("Maria", patient2.getName());
        assertEquals("Gomez", patient2.getSurname());
        assertEquals(patientDob2, patient2.getDob());
    }

}