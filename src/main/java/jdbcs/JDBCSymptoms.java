package jdbcs;

import Pojos.Symptoms;
import ifaces.SymptomsManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
/**
 * The {@code JDBCSymptoms} class provides database access and management methods
 * for the "Symptoms" table and its related operations. It implements the {@code SymptomsManager} interface.
 */
public class JDBCSymptoms implements SymptomsManager {
    private JDBCManager manager;
    /**
     * Constructs a {@code JDBCSymptoms} instance with the specified database manager.
     *
     * @param manager the {@code JDBCManager} instance to handle database connections
     */
    public JDBCSymptoms(JDBCManager manager) {
        this.manager = manager;
    }
    /**
     * Retrieves all symptoms from the "Symptoms" table.
     *
     * @return a list of {@code Symptoms} objects representing all symptoms in the database
     */
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
    /**
     * Retrieves the ID of a given symptom by its name.
     *
     * @param symptoms the {@code Symptoms} object to query
     * @return the ID of the symptom, or 0 if not found
     */
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
    /**
     * Adds a new symptom to the "Symptoms" table.
     *
     * @param symptom the {@code Symptoms} object to be added
     */
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
    /**
     * Retrieves a {@code Symptoms} object by its ID.
     *
     * @param id the ID of the symptom to retrieve
     * @return the {@code Symptoms} object, or {@code null} if not found
     */
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
    /**
     * Retrieves the total number of symptoms in the "Symptoms" table.
     *
     * @return the total count of symptoms
     */
    public int getSymptomsLength() {
        String sql = "SELECT COUNT(*) AS total FROM Symptoms";
        int length = 0;

        try (PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                length = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return length;
    }

    /**
     * Retrieves a list of symptoms associated with a specific interpretation.
     *
     * @param interpretation_id the ID of the interpretation
     * @return a linked list of {@code Symptoms} objects associated with the interpretation
     */
    public LinkedList<Symptoms> getSymptomsFromInterpretation(int interpretation_id){
        String sql = "SELECT symptom_id FROM InterpretationSymptoms WHERE interpretation_id=?";
        PreparedStatement s = null;
        LinkedList<Symptoms> symptoms = new LinkedList<>();
        Symptoms symptom = null;
        ResultSet rs = null;
        try {
            s = manager.getConnection().prepareStatement(sql);
            s.setInt(1, interpretation_id);
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
