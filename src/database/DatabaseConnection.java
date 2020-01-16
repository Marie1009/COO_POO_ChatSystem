package database;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
			e.printStackTrace();
			
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

	public static void createNewTableSelf() {
		String sql = "CREATE TABLE IF NOT EXISTS self ( "
				+ "pseudo TEXT) ;";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
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
		String sql = "INSERT OR REPLACE INTO users(pseudo, ipAddress) VALUES(?,?)";
		System.out.println("j'insere dans la db");
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
	
	public static String selectIp(String pseudo){
		String sql = "SELECT ipAddress "
				+ "FROM users "
				+ "WHERE (pseudo = '"+pseudo+"')";
		String ip = "";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			if (rs.next())
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
		System.out.println("select user"+ipAddress.getHostAddress());
		String sql = "SELECT pseudo "
				+ "FROM users "
				+ "WHERE ipAddress = '"+ipAddress.getHostAddress()+"'";
		String pseudo= "";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			if (rs.next())
				pseudo = rs.getString("pseudo") ;			
		} catch (SQLException e) {
			e.printStackTrace();

		}
		
		return pseudo;
	}

	public static void changePseudoInUsers(User newUser, String oldPseudo) {
		String sql = "UPDATE users " + 
				"SET pseudo = '"+newUser.getPseudo()+"' " + 
				"WHERE ipAddress = '"+newUser.getIp().getHostAddress()+"';" ; 
		
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			stmt.executeUpdate(sql); 
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
	
	public static void changePseudoInMessages(String newPseudo, String oldPseudo) {
		String sql2 = "UPDATE messages "
				+ "SET src = REPLACE(src,'"+oldPseudo+"','"+newPseudo+"'),"
						+ "dest = REPLACE(dest,'"+oldPseudo+"','"+newPseudo+"'); "; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{		
			stmt.executeUpdate(sql2); 
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}
	
	public static void changeIP(User newUser, String oldIP) {
		String sql = "UPDATE users " + 
				"SET ipAddress = '"+newUser.getIp().getHostAddress()+"' " + 
				"WHERE pseudo = '"+newUser.getPseudo()+"';" ; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			stmt.executeUpdate(sql); 	
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	public static String selectSelf() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM self";
		String self = "";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
			// loop through the result set
			if (rs.next())
				self = rs.getString("pseudo") ;
		} catch (SQLException e) {
			e.printStackTrace();

		}
		
		return self ;
	}

	public static void insertSelf(String pseudo) {
		
	}
	
	public static void updateSelf(String pseudo) {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM self ;";
		String sql2 = "INSERT INTO self (pseudo) "
				+ "VALUES ('"+pseudo+"') ; " ; 
		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) 
		{
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql2) ; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
