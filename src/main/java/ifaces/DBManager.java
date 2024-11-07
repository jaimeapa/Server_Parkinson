package ifaces;


import java.sql.Connection;
import java.util.*;


public interface DBManager {
	Connection getConnection();
	void disconnect();
}

