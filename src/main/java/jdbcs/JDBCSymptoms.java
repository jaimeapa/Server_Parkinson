package jdbcs;

import Pojos.Symptoms;
import ifaces.SymptomsManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;

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

    public int getId(Symptoms symptoms){
        String sql = "SELECT id FROM Symptoms WHERE name = ?;";
        int id = 0;

        try (PreparedStatement s = manager.getConnection().prepareStatement(sql)) {
            s.setString(1, symptoms.getName());

            try (ResultSet rs = s.executeQuery()) {

                if (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
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


    public LinkedList<Symptoms> getSymptomsFromInterpretation(int interpretation_id){
        String sql = "SELECT symptom_id FROM InterpretationSymptoms WHERE interpretation_id=?";
        PreparedStatement s = null;
        LinkedList<Symptoms> symptoms = new LinkedList<>();
        Symptoms symptom = null;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, interpretation_id);  // Establecer el ID del paciente
            rs = s.executeQuery();
            while (rs.next()) {
                int symptom_id = rs.getInt("symptom_id");
                symptom = getSymptomById(symptom_id);
                symptoms.add(symptom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (s != null) s.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return symptoms;
    }


}
