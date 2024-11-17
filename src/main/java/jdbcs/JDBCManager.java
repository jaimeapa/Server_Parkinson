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
				+ "    signal TEXT NOT NULL,"
				+ "    symptoms TEXT NOT NULL,"
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
		sql = "CREATE TABLE IF NOT EXISTS Symptons ("
				+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ "    patient_id INTEGER REFERENCES Patient(patient_id)"
				+ ");";
		stmt.executeUpdate(sql);
	}

	private void insertValuesIntoRoleTable() throws SQLException {

		Statement stmt = c.createStatement();
		String sql = "INSERT INTO Role (name) VALUES ('patient')/*, ('doctor')*/;";

		stmt.executeUpdate(sql);
	}

}