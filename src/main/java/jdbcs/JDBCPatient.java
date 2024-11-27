package jdbcs;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import Pojos.Patient;
import Utilities.Utilities;
import ifaces.PatientManager;

public class JDBCPatient implements PatientManager {

    private JDBCManager manager;

    public JDBCPatient(JDBCManager manager) {
        this.manager = manager;
    }

    public ArrayList<Patient> readPatients() {// read table Patients from db
        ArrayList<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient;";

        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String d = rs.getString("dob");
                LocalDate dob = Utilities.stringToDate(d);
                String email = rs.getString("email");
                Patient p = new Patient(id, name, surname, dob, email);

                patients.add(p);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public void addPatient(String name, String surname, LocalDate dob, String email, int user_id) {
        String sql= "INSERT INTO Patient (name, surname, dob, email, user_id) VALUES (?,?,?,?,?);";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, name);
            p.setString(2, surname);
            String date = dob.toString();
            p.setString(3, date);
            p.setString(4, email);
            p.setInt(5, user_id);
            p.executeUpdate();
            p.close();
        }catch(SQLException e ) {
            e.printStackTrace();
        }
    }

    /*public void addSymptomsToPatient(int patient, int symptom) {
        String sql= "INSERT INTO PatientSymptoms(patient_id, symptom_id) VALUES (?,?);";
        PreparedStatement s;
        try {
            s=manager.getConnection().prepareStatement(sql);
            s.setInt(1, patient);
            s.setInt(2, symptom);
            s.executeUpdate();
            s.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }*/

    public int getId(String name) {
        String sql = "SELECT Id FROM Patient WHERE name = ?;";
        PreparedStatement s;
        int id = 0;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, name);
            ResultSet rs = s.executeQuery();
            id = rs.getInt("patient_id");
            rs.close();
            s.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Integer emailToId(String email) {
        String sql = "SELECT patient_id FROM Patient WHERE email=?;";
        PreparedStatement s;
        int id = 0;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            id = rs.getInt("patient_id");
            rs.close();
            s.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Patient getPatientFromUser(int user_id)
    {
        String sql = "SELECT patient_id FROM Patient WHERE user_id=?;";
        PreparedStatement s = null;
        Patient p = null;
        ResultSet rs = null;
        Integer id = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, user_id);
            rs = s.executeQuery();
            if (rs.next()) { // Move the cursor to the first row
                id = rs.getInt("patient_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet after retrieving data
                if (s != null) s.close();   // Close PreparedStatement
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ID from user: "+ id);
        p = getPatientFromId(id);
        return p;
    }

    public Patient getPatientFromId(Integer id)
    {
        String sql = "SELECT * FROM Patient WHERE patient_id=?";
        PreparedStatement s = null;
        Patient patient = null;
        ResultSet rs = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
            rs = s.executeQuery();
            if (rs.next()) { // Ensure the ResultSet has data
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String date = rs.getString("dob");
                String patientEmail = rs.getString("email");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dob = LocalDate.parse(date, formatter);
                patient = new Patient(id, name, surname, dob, patientEmail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close(); // Close ResultSet
                if (s != null) s.close();   // Close PreparedStatement
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return patient;
            /*String name = rs.getString("name");
            String surname = rs.getString("surname");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            String patientEmail = rs.getString("email");
            patient = new Patient(id, name, surname, dob, patientEmail);
            s.close();
            rs.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return patient;*/
    }
    public Patient getPatientFromEmail(String email)
    {
        String sql = "SELECT * FROM Patient WHERE email=?";
        PreparedStatement s;
        Patient patient = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            int id = rs.getInt("patient_id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            String patientEmail = rs.getString("email");
            patient = new Patient(id, name, surname, dob, patientEmail);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return patient;
    }
    public void removePatientById (Integer id) {

        try {
            String sql ="DELETE FROM Patient WHERE id=?;";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);

            prep.setInt(1, id);

            prep.executeUpdate();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
    public void assignSymtomsToPatient(int patientId, int symptomId) {
        String sql = "INSERT INTO  PatientSymptoms (patient_id, symptom_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            pstmt.setInt(2, symptomId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Symptom assigned successfully");
            } else {
                System.out.println("Assignment failed");
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Patient> getPatientsByDoctorId(int doctor_id) {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM Patient WHERE doctor_id = ?";
        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, doctor_id);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("patient_id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                LocalDate dob = rs.getDate("dob").toLocalDate();
                String patientEmail = rs.getString("email");
                Patient patient= new Patient(id, name, surname, dob, patientEmail);
                patients.add(patient);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

}
