package jdbcs;

import Pojos.Symptoms;
import ifaces.SymptomsManager;

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
            pstmt.setString(2, symptom.getNombre());

            pstmt.executeUpdate();

            System.out.println("Symptom added successfully: " + symptom.getNombre());

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Symptoms getSymptomById(int id) {
        String sql = "SELECT * FROM Symptoms WHERE id = ?";
        Symptoms symptom = null;

        try {
            PreparedStatement pstmt = manager.getConnection().prepareStatement(sql);
            pstmt.setInt(1, id);

            ResultSet rs = pstmt.executeQuery();
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


}
