package jdbcs;

import Pojos.Symptoms;
import ifaces.SymptomsManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class JDBCSymptoms implements SymptomsManager {
    private JDBCManager manager;

    public JDBCSymptoms(JDBCManager manager) {
        this.manager = manager;
    }

    public ArrayList<Symptoms> readSymptoms() {// read table Patients from db
        ArrayList<Symptoms> symptoms = new ArrayList<>();
        String sql = "SELECT * FROM Symptoms;";

        try {
            Statement stmt = manager.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                Symptoms symptom = new Symptoms(id, name);

                symptoms.add(symptom);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return symptoms;
    }

    public void addSymptom(Symptoms symptom) {
        String sql = "INSERT INTO Symptoms (id, name) VALUES (?, ?)";

        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, symptom.getId());
            pstmt.setString(2, symptom.getName());

            pstmt.executeUpdate();

            System.out.println("Symptom added successfully: " + symptom.getName());

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Symptoms getSymptomById(int id)  {
        String sql = "SELECT * FROM Symptoms WHERE id = ?";
        Symptoms symptom = null;

        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                symptom = new Symptoms(id, name);
            } else {
                System.out.println("No symptom found with ID: " + id);
            }

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return symptom;
    }
    public int getSymptomsLength() {
        String sql = "SELECT COUNT(*) AS total FROM Symptoms";
        int length = 0;

        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                length = rs.getInt("total"); // Obtener el valor de la columna "total"
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return length; // Devuelve la cantidad total de síntomas
    }
    public ArrayList<String> getSymptomsForPatient(int patientId) {
        ArrayList<String> symptoms = new ArrayList<>();
        String sql = "SELECT s.name " +
                "FROM Symptoms s " +
                "INNER JOIN PatientSymptoms ps ON s.id = ps.symptom_id " +
                "WHERE ps.patient_id = ?";
        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, patientId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                symptoms.add(rs.getString("name"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return symptoms;
    }




}
