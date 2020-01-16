package database;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

	public static void createNewTableMessages() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS messages ( "
				+ "src TEXT,"				
				+ "dest TEXT,"
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
	public static void createNewTableUsers() {

		// SQL statement for creating a new table
		String sql = "CREATE TABLE IF NOT EXISTS users ( "
				+ "pseudo TEXT NOT NULL,"
				+ "ipAddress TEXT);" ;
		
		String sql2 = "CREATE UNIQUE INDEX IF NOT EXISTS idx_user_pseudo ON users(pseudo);";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
			stmt.execute(sql2);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}


	public static void insertMessage(Message m) {
		String sql = "INSERT INTO messages(src, dest, message) VALUES(?,?,?)";

		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) 
		{
			pstmt.setString(1, m.getSrc().getPseudo());
			pstmt.setString(2, m.getDest().getPseudo());
			pstmt.setString(3, m.getContent());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static void insertUser(User u ) {
		String sql = "REPLACE INTO users(pseudo, ipAddress) VALUES(?,?)";
		
		try (Connection conn = connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)) 
		{

			pstmt.setString(1, u.getPseudo());
			pstmt.setString(2, u.getIp().getHostAddress());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}



	public static void selectAllMessages(){
		String sql = "SELECT src, dest, message, date FROM messages";

		try (Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getString("src") +  "\t" + 
						rs.getString("dest") +  "\t" + 
						rs.getString("message") + "\t" +
						rs.getString("date"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void selectAllUsers(){
		String sql = "SELECT pseudo, ipAddress FROM users";

		try (Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			// loop through the result set
			while (rs.next()) {
				System.out.println(rs.getString("pseudo") +  "\t" + 
						rs.getString("ipAddress")) ;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	public static ArrayList<String> selectHistory(User src, User dest){
		String sql = "SELECT src, dest, message, date "
				+ "FROM messages "
				+ "WHERE (src = '"+src.getPseudo()+"' AND dest = '"+dest.getPseudo()+"')"
						+ " OR (src = '"+dest.getPseudo()+"' AND dest = '"+src.getPseudo()+"')";

		ArrayList<String> res = new ArrayList<String>() ; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			while (rs.next()) {
				res.add(rs.getString("src") +  "\t" + 
						rs.getString("dest") + "\t" +
						rs.getString("message") + "\t" +
						rs.getString("date"));
			}
		} catch (SQLException e) {
			e.printStackTrace();

		} return res; 
	}
	
	public static String selectIp(User user){
		String sql = "SELECT ipAddress "
				+ "FROM users "
				+ "WHERE pseudo = '"+user.getPseudo()+"'";
		String ip = "";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			ip = rs.getString("ipAddress") ;			
		} catch (SQLException e) {
			e.printStackTrace();

		}
		
		return ip;
	}
	
	public static void deleteUser(User user) {
		String sql = "DELETE FROM users "
				+ "WHERE pseudo = '"+user.getPseudo()+"'";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			stmt.executeUpdate(sql); 	
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	/** Selects a user with the given ip address in the database and returns its pseudo
	 * 
	 * @param ipAddress
	 * @return pseudo
	 */
	public static String selectUser(InetAddress ipAddress) {
		String sql = "SELECT pseudo "
				+ "FROM users "
				+ "WHERE ipAddress = '"+ipAddress.toString()+"'";
		String pseudo= "";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			pseudo = rs.getString("pseudo") ;			
		} catch (SQLException e) {
			e.printStackTrace();

		}
		
		return pseudo;
	}

	public static void changePseudo(User newUser, String oldPseudo) {
		String sql = "UPDATE users " + 
				"SET pseudo = '"+newUser.getPseudo()+"' " + 
				"WHERE ipAddress = '"+newUser.getIp().toString()+"';" ; 
		
		String sql2 = "UPDATE messages "
				+ "SET src = REPLACE(src,'"+oldPseudo+"','"+newUser.getPseudo()+"'),"
						+ "dest = REPLACE(dest,'"+oldPseudo+"','"+newUser.getPseudo()+"'); "; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			stmt.executeUpdate(sql); 	
			stmt.executeUpdate(sql2); 
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
}
