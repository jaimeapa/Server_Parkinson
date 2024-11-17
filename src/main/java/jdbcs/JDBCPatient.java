package jdbcs;

import java.sql.*;
import java.time.LocalDate;
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

    public void addPatient(String name, String surname, LocalDate dob, String email) {
        String sql= "INSERT INTO Patient (name, surname, dob, email) VALUES (?,?,?,?);";
        try {
            PreparedStatement p = manager.getConnection().prepareStatement(sql);
            p.setString(1, name);
            p.setString(2, surname);
            String date = dob.toString();
            p.setString(3, date);
            p.setString(4, email);
            p.executeUpdate();
            p.close();
        }catch(SQLException e ) {
            e.printStackTrace();
        }
    }

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
        PreparedStatement s;
        Patient p = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, user_id);
            ResultSet rs = s.executeQuery();
            int id = rs.getInt("patient_id");
            p = getPatientFromId(id);
            rs.close();
            s.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return p;
    }

    public Patient getPatientFromId(Integer id)
    {
        String sql = "SELECT * FROM Patient WHERE email=?";
        PreparedStatement s;
        Patient patient = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
            ResultSet rs = s.executeQuery();
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
}
