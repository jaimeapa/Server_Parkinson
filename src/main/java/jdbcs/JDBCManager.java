package jdbcs;

import java.sql.*;


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
			this.insertAdministrator();
			
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

	public void insertAdministrator() throws SQLException {
		// Comprobar si ya existe un administrador en la tabla User
		String email = "florentino@gmail.com";
		String password = "mbappe";
		if (!existsAdministrator()) {
			String sql = "INSERT INTO User (email, password, role_id) VALUES (?, ?, 3)";
			PreparedStatement stmt = null;
			try {
				stmt = c.prepareStatement(sql);
				stmt.setString(1, email);
				stmt.setString(2, password); // Convierte la contraseña a bytes
				stmt.executeUpdate();
				System.out.println("Administrador insertado correctamente.");
			} catch (SQLException e) {
				System.out.println("Error al insertar el administrador: " + e.getMessage());
			} finally {
				if (stmt != null) {
					stmt.close();
				}
			}
		} else {
			System.out.println("Ya existe un administrador en el sistema.");
		}
	}

	// Metodo para comprobar si ya existe un administrador en la tabla User
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
			System.out.println("Error al comprobar la existencia del administrador: " + e.getMessage());
			return false;
		} finally {
			try {
				if (rs != null) rs.close();
				if (stmt != null) stmt.close();
			} catch (SQLException e) {
				System.out.println("Error al cerrar recursos: " + e.getMessage());
			}
		}
	}

	public void clearTables() {
		try (Statement stmt = c.createStatement()) {
			// Desactivar claves foráneas temporalmente
			stmt.execute("PRAGMA foreign_keys = OFF;");

			// Vaciar todas las tablas
			stmt.executeUpdate("DELETE FROM InterpretationSymptoms;");
			stmt.executeUpdate("DELETE FROM Symptoms;");
			stmt.executeUpdate("DELETE FROM Interpretation;");
			stmt.executeUpdate("DELETE FROM Patient;");
			stmt.executeUpdate("DELETE FROM Doctor;");
			stmt.executeUpdate("DELETE FROM User;");

			// Reactivar claves foráneas
			stmt.execute("PRAGMA foreign_keys = ON;");

			System.out.println("Todas las tablas han sido vaciadas correctamente.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Error al vaciar las tablas.");
		}
	}

}