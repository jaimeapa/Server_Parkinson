package tests;

import jdbcs.JDBCPatient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class JDBCPatientTest {
    private JDBCPatient jdbcPatient;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        //para configurar una conexion a la  en memoria
        connection = DriverManager.getConnection("jdbc:sqlite:./db/parkinsonDatabase.db", "sa", "");
        Statement stmt = connection.createStatement();

        //TABLA DE PRUEBAS
        String createTableSql =
                "CREATE TABLE Patient (" +
                        "patient_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(255), " +
                        "surname VARCHAR(255), " +
                        "dob DATE, " +
                        "email VARCHAR(255), " +
                        "user_id INT" +
                        ");";
        stmt.execute(createTableSql);

        //DATOS DE EJEMPLO PARA PROBAR EL TEST
        String insertDataSql =
                "INSERT INTO Patient (name, surname, dob, email, user_id) " +
                        "VALUES ('Pablo', 'Hita', '200-01-01', 'pab.hi@example.com', 1), " +
                        "('Antonio', 'Griezmann', '1992-02-02', 'anto.gri@example.com', 2);";
        stmt.execute(insertDataSql);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void readPatients() {
    }

    @Test
    void addPatient() {
    }

    @Test
    void getId() {
    }

    @Test
    void emailToId() {
    }

    @Test
    void getPatientFromUser() {
    }

    @Test
    void getPatientFromId() {
    }

    @Test
    void getPatientFromEmail() {
    }

    @Test
    void removePatientById() {
    }

    @Test
    void assignSymtomsToPatient() {
    }
}