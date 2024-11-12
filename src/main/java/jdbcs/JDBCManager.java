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

		String sql = "CREATE TABLE Patient ("
				+ "    patient_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ "    surname TEXT NOT NULL,"
				+ "    dob TEXT NOT NULL,"
				+ "    email TEXT NOT NULL,"
				+ "    signal TEXT NOT NULL,"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE Role ("
				+ "    id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "    name TEXT NOT NULL,"
				+ ");";
		stmt.executeUpdate(sql);

		sql = "CREATE TABLE User ("
				+ "	id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " email TEXT NOT NULL,"
				+ "	password BLOB NOT NULL"
				+ " role_id INTEGER REFERENCES Role(id)"
				+ ");";
		stmt.executeUpdate(sql);
	}

}