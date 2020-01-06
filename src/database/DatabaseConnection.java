package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;

import model.Message;
import model.User;

public class DatabaseConnection {

	public DatabaseConnection() {

	}

	public static void createNewDatabase(String fileName) {

		String url = "jdbc:sqlite:db/" + fileName;

		try (Connection conn = DriverManager.getConnection(url)) {
			if (conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println("The driver name is " + meta.getDriverName());
				System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static Connection connect() {
		Connection conn = null;
		// db parameters
		String url = "jdbc:sqlite:db/history.db";
		// create a connection to the database
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static void createNewTable() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS messages ( "
				+ "user TEXT,"
				+ "message TEXT," 
				+ "date TEXT DEFAULT CURRENT_TIMESTAMP) ;";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	public static void insert(User u, Message m) {
		String sql = "INSERT INTO messages(user, message) VALUES(?,?)";

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) 
		{

			pstmt.setString(1, u.getPseudo());
			pstmt.setString(2, m.getContent());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}



	public static void selectAll(){
		String sql = "SELECT user, message, date FROM messages";

		try (Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getString("user") +  "\t" + 
						rs.getString("message") + "\t" +
						rs.getString("date"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void select(User user){
		String sql = "SELECT user, message, date "
				+ "FROM messages "
				+ "WHERE user = '"+user.getPseudo()+"'";

		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getString("user") +  "\t" + 
						rs.getString("message") + "\t" +
						rs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
}
