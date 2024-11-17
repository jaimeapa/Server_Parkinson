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
}
