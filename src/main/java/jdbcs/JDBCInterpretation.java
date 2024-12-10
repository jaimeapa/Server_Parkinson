package jdbcs;

import Pojos.*;
import ifaces.InterpretationManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
/**
 * The {@code JDBCInterpretation} class provides methods to manage interpretation data in a database.
 * It implements the {@code InterpretationManager} interface and uses JDBC for database operations.
 */
public class JDBCInterpretation implements InterpretationManager {
    /** The database manager */
    JDBCManager manager;

    /**
     * Constructs a {@code JDBCInterpretation} object with the specified {@code JDBCManager}.
     *
     * @param manager the {@code JDBCManager} used to establish database connections
     */
    public JDBCInterpretation(JDBCManager manager) {

        this.manager = manager;
    }

    /**
     * Adds a new interpretation to the database.
     *
     * @param interpretation the {@code Interpretation} object containing the interpretation details
     */
    public void addInterpretation(Interpretation interpretation) {
        String sql = "INSERT INTO Interpretation (date, interpretation, signalEMG, signalEDA, patient_id, doctor_id, observation) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try {

            PreparedStatement statement = manager.getConnection().prepareStatement(sql);

            statement.setString(1, interpretation.getDate().toString());
            statement.setString(2, interpretation.getInterpretation());
            statement.setString(3, interpretation.getSignalEMG().valuesToString());
            statement.setString(4, interpretation.getSignalEDA().valuesToString());
            statement.setInt(5, interpretation.getPatient_id());
            statement.setInt(6, interpretation.getDoctor_id());
            statement.setString(7, interpretation.getObservation());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all interpretations for a specific patient based on their ID.
     *
     * @param id the patient ID
     * @return a list of {@code Interpretation} objects for the patient
     */
    public LinkedList<Interpretation> getInterpretationsFromPatient_Id(Integer id) {
        String sql = "SELECT * FROM Interpretation WHERE patient_id=?";
        PreparedStatement s = null;
        LinkedList<Interpretation> interpretations = new LinkedList<>();
        Interpretation interpretation;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
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


    /**
     * Retrieves all interpretations for a specific doctor based on their ID.
     *
     * @param id the doctor ID
     * @return a list of {@code Interpretation} objects for the doctor
     */
    public LinkedList<Interpretation> getInterpretationsFromDoctor_Id(Integer id) {
        String sql = "SELECT * FROM Interpretation WHERE doctor_id=?";
        PreparedStatement s = null;
        LinkedList<Interpretation> interpretations = new LinkedList<>();
        Interpretation interpretation;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, id);
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


                signalEMG.setValuesEMG(signalEMGString);
                signalEDA.setValuesEDA(signalEDAString);
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

    /**
     * Assigns a symptom to an interpretation by their respective IDs.
     *
     * @param interpretation_id the interpretation ID
     * @param symptomId         the symptom ID
     */
    public void assignSymtomsToInterpretation(int interpretation_id, int symptomId) {
        String sql = "INSERT INTO  InterpretationSymptoms (interpretation_id, symptom_id) VALUES (?, ?)";
        try  {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, interpretation_id);
            pstmt.setInt(2, symptomId);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Retrieves the ID of an interpretation based on its date and associated patient ID.
     *
     * @param date       the date of the interpretation
     * @param patient_id the patient ID
     * @return the interpretation ID, or 0 if not found
     */
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

    /**
     * Retrieves an {@code Interpretation} object based on its ID.
     *
     * @param id the interpretation ID
     * @return the {@code Interpretation} object, or {@code null} if not found
     */
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
            if (rs.next()) {
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

                signalEMG.setValuesEMG(signalEMGString);
                signalEDA.setValuesEDA(signalEDAString);

                interpretation = new Interpretation(interpretation_id, date2, feedback, signalEMG, signalEDA,
                        rs.getInt("patient_id"), rs.getInt("doctor_id"), observation);
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
        return interpretation;
    }
    /**
     * Updates the interpretation feedback for a specific interpretation.
     *
     * @param interpretation     the updated interpretation feedback
     * @param interpretation_id the interpretation ID to be updated
     */
    public void setInterpretation(String interpretation, int interpretation_id){
        String sql = "UPDATE Interpretation SET interpretation = ? WHERE id = ?;";
        try{
            PreparedStatement s = manager.getConnection().prepareStatement(sql);
            s.setString(1, interpretation);
            s.setInt(2, interpretation_id);
            s.executeUpdate();
            s.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}


