package jdbcs;

import java.sql.*;
import ifaces.UserManager;

public class JDBCManager  {
	
	private Connection c = null;
	//final static DefaultValues defaultvalues= new DefaultValues();
	//final static Logger TERM = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	public JDBCManager() {
		try {
			// Open database connection
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./db/parkinsonDatabase.db");
			c.createStatement().execute("PRAGMA foreign_keys=ON");
			System.out.println("Database connection opened.");
			this.createTables();
			this.insertValuesIntoRoleTable();
			this.insertValuesIntoSymptomsTable();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection() {
		return c;
	}
	
	public void disconnect() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void createTables() throws SQLException {
		//he cambiado las tablas

		Statement stmt = c.createStatement();

		String sql = "CREATE TABLE IF NOT EXISTS Patient ("
				+ "    patient_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ "    surname TEXT NOT NULL,"
				+ "    dob TEXT NOT NULL,"
				+ "    email TEXT NOT NULL,"
				+ "    doctor_id INTEGER REFERENCES Doctor(doctor_id),"
				+ "    signal TEXT,"
				+ "    symptoms TEXT,"
				+ "    user_id INTEGER REFERENCES User(id)"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS Doctor ("
				+ "    doctor_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ "    surname TEXT NOT NULL,"
				+ "    dob TEXT NOT NULL,"
				+ "    email TEXT NOT NULL,"
				+ "    user_id INTEGER REFERENCES User(id)"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS Role ("
				+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS User ("
				+ "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " email TEXT NOT NULL,"
				+ "	password BLOB NOT NULL,"
				+ " role_id INTEGER REFERENCES Role(id)"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS Symptoms ("
				+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ "    patient_id INTEGER REFERENCES Patient(patient_id)"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS PatientSymptoms (" +
				"    patient_id INTEGER NOT NULL REFERENCES Patient(patient_id) ON DELETE CASCADE," +
				"    symptom_id INTEGER NOT NULL REFERENCES Symptoms(id) ON DELETE CASCADE," +
				"    PRIMARY KEY (patient_id, symptom_id)" +
				");";

		stmt.executeUpdate(sql);


	}

	private boolean existsRole(String nombre) {
		String sql = "SELECT 1 FROM Role WHERE name = ?";
		PreparedStatement s;
		try {
			s = this.c.prepareStatement(sql);
			s.setString(1, nombre);
			ResultSet rs = s.executeQuery();
			boolean answer = rs.next();
			rs.close();
			s.close();
			return answer;
		}catch(SQLException e) {
			System.out.println("Roles not added");
			return false;
		}
	}
	private boolean existsSymptom(String nombre) {
		String sql = "SELECT 1 FROM Symptoms WHERE name = ?";
		PreparedStatement s;
		try {
			s = this.c.prepareStatement(sql);
			s.setString(1, nombre);
			ResultSet rs = s.executeQuery();
			boolean answer = rs.next();
			rs.close();
			s.close();
			return answer;
		}catch(SQLException e) {
			System.out.println("Symptoms not added");
			return false;
		}
	}
	public void insertValuesIntoRoleTable() throws SQLException {
		if(!existsRole("patient")) {
			Statement stmt = c.createStatement();
			String sql = "INSERT OR IGNORE INTO Role (name) VALUES ('patient'), ('doctor'), ('administrator');";

			stmt.executeUpdate(sql);
		}
	}

	public void insertValuesIntoSymptomsTable() throws SQLException {
		if(!existsSymptom("Bradykinesia")) {
			Statement stmt = c.createStatement();
			String sql = "INSERT OR IGNORE INTO Symptoms (name) VALUES ('Tremor'), ('Bradykinesia'), ('Muscle Rigidity'), ('Postural Instability'), ('Gait Changes'), ('Facial Masking'), ('Cognitive Changes'), ('Mood Disorders'), ('Sleep Disturbances'), ('Autonomic Dysfunction'), ('Sensory Symptoms'), ('Fatigue');";

			stmt.executeUpdate(sql);
		}
	}

	public void clearTables() {
		try {
			Statement stmt = c.createStatement();
			// Orden correcto para evitar problemas de integridad referencial
			stmt.executeUpdate("DELETE FROM PatientSymptoms;");
			stmt.executeUpdate("DELETE FROM Symptoms;");
			stmt.executeUpdate("DELETE FROM Patient;");
			stmt.executeUpdate("DELETE FROM User;");

			System.out.println("Todas las tablas han sido vaciadas correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al vaciar las tablas.");
		}
	}

}