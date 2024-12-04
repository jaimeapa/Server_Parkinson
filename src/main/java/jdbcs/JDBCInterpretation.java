package jdbcs;

import Pojos.*;
import ifaces.InterpretationManager;
//import sun.jvm.hotspot.debugger.cdbg.Sym;

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
    JDBCSymptoms symptomsManager;

    public JDBCInterpretation(JDBCManager manager) {

        this.manager = manager;
        this.symptomsManager = new JDBCSymptoms(manager);
    }

    // Método para insertar una nueva interpretación
    public void addInterpretation(Interpretation interpretation) {
        String sql = "INSERT INTO Interpretation (date, interpretation, signalEMG, signalEDA, patient_id, doctor_id, observation) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try {
            // Preparamos la declaración SQL
            PreparedStatement statement = manager.getConnection().prepareStatement(sql);

            // Establecemos los parámetros a partir del objeto Interpretation
            statement.setString(1, interpretation.getDate().toString());  // Convertir LocalDate a String
            statement.setString(2, interpretation.getInterpretation());
            statement.setString(3, interpretation.getSignalEMG().valuesToString());  // Convertir signalEMG a String
            statement.setString(4, interpretation.getSignalEDA().valuesToString());  // Convertir signalEDA a String
            statement.setInt(5, interpretation.getPatient_id());
            statement.setInt(6, interpretation.getDoctor_id());
            statement.setString(7, interpretation.getObservation());

            // Ejecutar la actualización
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();  // Mostrar error en consola
        }
    }


    public LinkedList<Interpretation> getInterpretationsFromPatient_Id(Integer id) {
        String sql = "SELECT * FROM Interpretation WHERE patient_id=?";
        PreparedStatement s = null;
        LinkedList<Interpretation> interpretations = new LinkedList<>();
        Interpretation interpretation = null;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);  // Establecer el ID del paciente
            rs = s.executeQuery();
            while (rs.next()) {
                int interpretation_id = rs.getInt("id");
                String date = rs.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date2 = LocalDate.parse(date, formatter);
                String feedback = rs.getString("interpretation");
                String signalEMGString = rs.getString("signalEMG");
                String signalEDAString = rs.getString("signalEDA");
                String observation = rs.getString("observation");

                Signal signalEMG = new Signal(Signal.SignalType.EMG);
                Signal signalEDA = new Signal(Signal.SignalType.EDA);

                // List<Integer> emgValues = signalEMG.stringToValues(signalEMGString);
                //List<Integer> edaValues = signalEDA.stringToValues(signalEDAString);

                signalEMG.setValuesEMG(signalEMGString);
                signalEDA.setValuesEDA(signalEDAString);

                // Crear la interpretación y agregarla a la lista
                interpretation = new Interpretation(interpretation_id, date2, feedback, signalEMG, signalEDA,
                        rs.getInt("patient_id"), rs.getInt("doctor_id"), observation);
                interpretations.add(interpretation);
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
            s.setInt(1, id);  // Establecer el ID del paciente
            rs = s.executeQuery();
            while (rs.next()) {
                int interpretation_id = rs.getInt("id");
                String date = rs.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date2 = LocalDate.parse(date, formatter);
                String feedback = rs.getString("interpretation");
                String signalEMGString = rs.getString("signalEMG");
                String signalEDAString = rs.getString("signalEDA");
                String observation = rs.getString("observation");

                Signal signalEMG = new Signal(Signal.SignalType.EMG);
                Signal signalEDA = new Signal(Signal.SignalType.EDA);

                // List<Integer> emgValues = signalEMG.stringToValues(signalEMGString);
                //List<Integer> edaValues = signalEDA.stringToValues(signalEDAString);

                signalEMG.setValuesEMG(signalEMGString);
                signalEDA.setValuesEDA(signalEDAString);

                // Crear la interpretación y agregarla a la lista
                interpretation = new Interpretation(interpretation_id, date2, feedback, signalEMG, signalEDA,
                        rs.getInt("patient_id"), rs.getInt("doctor_id"), observation);
                interpretations.add(interpretation);
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
        return interpretations;
    }


    public void assignSymtomsToInterpretation(int interpretation_id, int symptomId) {
        String sql = "INSERT INTO  InterpretationSymptoms (interpretation_id, symptom_id) VALUES (?, ?)";
        try  {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, interpretation_id);
            pstmt.setInt(2, symptomId);
            pstmt.executeUpdate();
            pstmt.close();
            /*int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Symptom assigned successfully");
            } else {
                System.out.println("Assignment failed");
            }*/

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getId(LocalDate date, int patient_id) {
        String sql = "SELECT id FROM Interpretation WHERE date = ? AND patient_id = ?;";
        int id = 0;

        try (PreparedStatement s = manager.getConnection().prepareStatement(sql)) {
            s.setString(1, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            s.setInt(2, patient_id);

            try (ResultSet rs = s.executeQuery()) {

                while (rs.next()) {
                    id = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
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
                symptom = symptomsManager.getSymptomById(symptom_id);
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

    public Interpretation getInterpretationFromId(Integer id)
    {
        String sql = "SELECT * FROM Interpretation WHERE id=?";
        PreparedStatement s = null;
        Patient patient = null;
        ResultSet rs = null;
        Interpretation interpretation = null;
        try{
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
            rs = s.executeQuery();
            if (rs.next()) { // Ensure the ResultSet has data
                int interpretation_id = rs.getInt("id");
                String date = rs.getString("date");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date2 = LocalDate.parse(date, formatter);
                String feedback = rs.getString("interpretation");
                String signalEMGString = rs.getString("signalEMG");
                String signalEDAString = rs.getString("signalEDA");
                String observation = rs.getString("observation");

                Signal signalEMG = new Signal(Signal.SignalType.EMG);
                Signal signalEDA = new Signal(Signal.SignalType.EDA);

                // List<Integer> emgValues = signalEMG.stringToValues(signalEMGString);
                //List<Integer> edaValues = signalEDA.stringToValues(signalEDAString);

                signalEMG.setValuesEMG(signalEMGString);
                signalEDA.setValuesEDA(signalEDAString);

                // Crear la interpretación y agregarla a la lista
                interpretation = new Interpretation(interpretation_id, date2, feedback, signalEMG, signalEDA,
                        rs.getInt("patient_id"), rs.getInt("doctor_id"), observation);
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
        return interpretation;
    }
}


