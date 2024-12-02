package jdbcs;

import Pojos.Doctor;
import Utilities.Utilities;
import ifaces.DoctorManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class JDBCDoctor implements DoctorManager {

    private JDBCManager manager;

    public JDBCDoctor(JDBCManager manager) {
        this.manager = manager;
    }

    public ArrayList<Doctor> readDoctors() {// read table Patients from db
        ArrayList<Doctor> doctors = new ArrayList<>();
        String sql = "SELECT * FROM Doctor;";

        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("doctor_id");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String d = rs.getString("dob");
                LocalDate dob = Utilities.stringToDate(d);
                String email = rs.getString("email");
                Doctor doctor = new Doctor(id, name, surname, dob, email);

                doctors.add(doctor);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public void addDoctor(String name, String surname, LocalDate dob, String email, int user_id) {
        String sql= "INSERT INTO Doctor (name, surname, dob, email, user_id) VALUES (?,?,?,?,?);";
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

    public int getId(String name) {
        String sql = "SELECT doctor_id FROM Doctor WHERE name = ?;";
        PreparedStatement s;
        int id = 0;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, name);
            ResultSet rs = s.executeQuery();
            id = rs.getInt("doctor_id");
            rs.close();
            s.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Integer emailToId(String email) {
        String sql = "SELECT doctor_id FROM Doctor WHERE email=?;";
        PreparedStatement s;
        int id = 0;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            id = rs.getInt("doctor_id");
            rs.close();
            s.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public Doctor getDoctorFromUser(int user_id)
    {
        String sql = "SELECT doctor_id FROM Doctor WHERE user_id=?;";
        PreparedStatement s = null;
        Doctor doc = null;
        ResultSet rs = null;
        Integer id = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, user_id);
            rs = s.executeQuery();
            if (rs.next()) { // Move the cursor to the first row
                id = rs.getInt("doctor_id");
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
        doc = getDoctorFromId(id);
        return doc;
    }

    public Doctor getDoctorFromId(Integer id)
    {
        String sql = "SELECT * FROM Doctor WHERE doctor_id=?";
        PreparedStatement s = null;
        Doctor doctor = null;
        ResultSet rs = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
            rs = s.executeQuery();
            if (rs.next()) { // Ensure the ResultSet has data
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String date = rs.getString("dob");
                String email = rs.getString("email");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate dob = LocalDate.parse(date, formatter);
                doctor = new Doctor(id, name, surname, dob, email);
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
        return doctor;
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
    public Doctor getDoctorFromEmail(String email)
    {
        String sql = "SELECT * FROM Doctor WHERE email=?";
        PreparedStatement s;
        Doctor doctor = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setString(1, email);
            ResultSet rs = s.executeQuery();
            int id = rs.getInt("patient_id");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            LocalDate dob = rs.getDate("dob").toLocalDate();
            String patientEmail = rs.getString("email");
            doctor = new Doctor(id, name, surname, dob, patientEmail);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return doctor;
    }
    public void removeDoctorById (Integer id) {

        try {
            String sql ="DELETE FROM Doctor WHERE id=?;";
            PreparedStatement prep = manager.getConnection().prepareStatement(sql);

            prep.setInt(1, id);

            prep.executeUpdate();

        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}
