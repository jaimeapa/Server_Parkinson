package jdbcs;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import Encryption.EncryptPassword;

/**
 * The {@code JDBCManager} class handles database connection and management tasks
 * for a SQLite database used in the Parkinson system. It creates tables, inserts default
 * values, and provides methods to manage the database.
 */
public class JDBCManager  {
	/**
	 * The {@code c} field is a private member variable that holds the database connection.
	 * It is used to interact with the underlying database for executing SQL queries.
	 */
	private Connection c = null;
	/**
	 * Constructs a new {@code JDBCManager} instance, initializing a SQLite database connection
	 * and setting up required tables and initial values.
	 */
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
			this.insertAdministrator();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Retrieves the active database connection.
	 *
	 * @return the current {@code Connection} instance
	 */
	public Connection getConnection() {
		return c;
	}

	/**
	 * Closes the database connection.
	 */
	public void disconnect() {
		try {
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Creates the necessary tables in the database if they do not exist.
	 *
	 * @throws SQLException if an SQL error occurs during table creation
	 */
	private void createTables() throws SQLException {

		Statement stmt = c.createStatement();

		String sql = "CREATE TABLE IF NOT EXISTS Patient ("
				+ "    patient_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ "    surname TEXT NOT NULL,"
				+ "    dob TEXT NOT NULL,"
				+ "    email TEXT NOT NULL,"
				+ "    doctor_id INTEGER REFERENCES Doctor(doctor_id),"
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
				+ "    name TEXT NOT NULL"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS InterpretationSymptoms (" +
				"    interpretation_id INTEGER NOT NULL REFERENCES Interpretation(id) ON DELETE CASCADE," +
				"    symptom_id INTEGER NOT NULL REFERENCES Symptoms(id) ON DELETE CASCADE," +
				"    PRIMARY KEY (interpretation_id, symptom_id)" +
				");";

		stmt.executeUpdate(sql);

		sql = "CREATE TABLE IF NOT EXISTS Interpretation ("
				+ "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " date TEXT NOT NULL,"
				+ " interpretation TEXT ,"
				+ " signalEMG TEXT ,"
				+ " signalEDA TEXT ,"
				+ "observation TEXT,"
				+ " patient_id INTEGER NOT NULL REFERENCES Patient(patient_id), "
				+ "doctor_id INTEGER NOT NULL REFERENCES Doctor(doctor_id)"
				+ ");";
		stmt.executeUpdate(sql);

	}

	/**
	 * Checks if a role with the specified name exists in the "Role" table.
	 *
	 * @param name the name of the role
	 * @return {@code true} if the role exists, {@code false} otherwise
	 */
	private boolean existsRole(String name) {
		String sql = "SELECT 1 FROM Role WHERE name = ?";
		PreparedStatement s;
		try {
			s = this.c.prepareStatement(sql);
			s.setString(1, name);
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
	/**
	 * Checks if a symptom with the specified name exists in the "Symptoms" table.
	 *
	 * @param name the name of the symptom
	 * @return {@code true} if the symptom exists, {@code false} otherwise
	 */
	private boolean existsSymptom(String name) {
		String sql = "SELECT 1 FROM Symptoms WHERE name = ?";
		PreparedStatement s;
		try {
			s = this.c.prepareStatement(sql);
			s.setString(1, name);
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
	/**
	 * Inserts default roles into the "Role" table if they do not already exist.
	 *
	 * @throws SQLException if an SQL error occurs during insertion
	 */
	public void insertValuesIntoRoleTable() throws SQLException {
		if(!existsRole("patient")) {
			Statement stmt = c.createStatement();
			String sql = "INSERT OR IGNORE INTO Role (name) VALUES ('patient'), ('doctor'), ('administrator');";

			stmt.executeUpdate(sql);
		}
	}
	/**
	 * Inserts default symptoms into the "Symptoms" table if they do not already exist.
	 *
	 * @throws SQLException if an SQL error occurs during insertion
	 */
	public void insertValuesIntoSymptomsTable() throws SQLException {
		if(!existsSymptom("Bradykinesia")) {
			Statement stmt = c.createStatement();
			String sql = "INSERT OR IGNORE INTO Symptoms (name) VALUES ('Tremor'), ('Bradykinesia'), ('Muscle Rigidity'), ('Postural Instability'), ('Gait Changes'), ('Facial Masking'), ('Cognitive Changes'), ('Mood Disorders'), ('Sleep Disturbances'), ('Autonomic Dysfunction'), ('Sensory Symptoms'), ('Fatigue');";

			stmt.executeUpdate(sql);
		}
	}
	/**
	 * Inserts a default administrator into the "User" table if none exists.
	 *
	 * @throws SQLException if an SQL error occurs during insertion
	 */
	public void insertAdministrator() throws SQLException {
		String email = "florentino@gmail.com";
		byte[] password;
		String psw = "mbappe";
		try {
			password = EncryptPassword.encryptPassword(psw);
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			password = null;
		}
		if (!existsAdministrator()) {
			String sql = "INSERT INTO User (email, password, role_id) VALUES (?, ?, 3)";
			PreparedStatement stmt = null;
			try {
				stmt = c.prepareStatement(sql);
				stmt.setString(1, email);
				stmt.setString(2, new String(password));
				stmt.executeUpdate();
				System.out.println("Administrator inserted correctly.");
			} catch (SQLException e) {
				System.out.println("Error inserting the administrator: " + e.getMessage());
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
		} else {
			System.out.println("The administrator already exists.");
		}
	}

	/**
	 * Checks if an administrator exists in the "User" table.
	 *
	 * @return {@code true} if an administrator exists, {@code false} otherwise
	 */
	private boolean existsAdministrator() {
		String sql = "SELECT 1 FROM User WHERE role_id = 3";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = c.prepareStatement(sql);
			rs = stmt.executeQuery();
			boolean exists = rs.next();
			return exists;
		} catch (SQLException e) {
			System.out.println("Error proving the administrator existency: " + e.getMessage());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error closing resources: " + e.getMessage());
			}
		}
	}
	/**
	 * Clears all data from the database tables while maintaining the table structure.
	 * Foreign key constraints are temporarily disabled during the operation.
	 */
	public void clearTables() {
		try (Statement stmt = c.createStatement()) {

			stmt.execute("PRAGMA foreign_keys = OFF;");

			stmt.executeUpdate("DELETE FROM InterpretationSymptoms;");
			stmt.executeUpdate("DELETE FROM Symptoms;");
			stmt.executeUpdate("DELETE FROM Interpretation;");
			stmt.executeUpdate("DELETE FROM Patient;");
			stmt.executeUpdate("DELETE FROM Doctor;");
			stmt.executeUpdate("DELETE FROM User;");

			stmt.execute("PRAGMA foreign_keys = ON;");

			System.out.println("All tables have been cleared correctly.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error clearing the tables.");
		}
	}

}