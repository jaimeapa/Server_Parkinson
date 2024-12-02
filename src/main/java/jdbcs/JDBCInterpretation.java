package jdbcs;

import Pojos.Doctor;
import Pojos.Interpretation;
import Pojos.Patient;
import ifaces.InterpretationManager;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class JDBCInterpretation implements InterpretationManager {
    JDBCManager manager;

    public JDBCInterpretation(JDBCManager manager) {
        this.manager = manager;
    }

    // Método para insertar una nueva interpretación
    public boolean addInterpretation(Interpretation interpretation) {
        String query = "INSERT INTO Interpretation (date, interpretation, patient_id, doctor_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = manager.getConnection().prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(interpretation.getDate()));
            statement.setString(2, interpretation.getInterpretation());
            statement.setInt(3, interpretation.getPatient_id());
            statement.setInt(4, interpretation.getDoctor_id());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener interpretaciones de un paciente
    public LinkedList<Interpretation> getInterpretationsFromPatient_Id(Integer id) {
        String sql = "SELECT * FROM Interpretation WHERE patient_id=?";
        PreparedStatement s = null;
        LinkedList<Interpretation> interpretations = new LinkedList<>();
        Interpretation interpretation = null;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
            rs = s.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date2 = LocalDate.parse(date, formatter);
                String feedback = rs.getString("interpretation");
                interpretation = new Interpretation(date2, feedback);
                interpretations.add(interpretation);

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
        return interpretations;
    }

    // Método para obtener interpretaciones de un paciente
    public LinkedList<Interpretation> getInterpretationsFromDoctor_Id(Integer id) {
        String sql = "SELECT * FROM Interpretation WHERE doctor_id=?";
        PreparedStatement s = null;
        LinkedList<Interpretation> interpretations = new LinkedList<>();
        Interpretation interpretation = null;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
            rs = s.executeQuery();
            while (rs.next()) {
                String date = rs.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date2 = LocalDate.parse(date, formatter);
                String feedback = rs.getString("interpretation");
                interpretation = new Interpretation(date2, feedback);
                interpretations.add(interpretation);

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
        return interpretations;
    }

    public void assignSymtomsToInterpretation(int interpretation_id, int symptomId) {
        String sql = "INSERT INTO  InterpretationSymptoms (interpretation_id, symptom_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, interpretation_id);
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
}


