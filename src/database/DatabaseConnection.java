package database;

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
	private static String dbName = "history.db" ;

	
	public static Connection connect() {
		Connection conn = null;
		String url = "jdbc:sqlite:db/"+dbName;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	public static void createNewTableMessages() {

		String sql = "CREATE TABLE IF NOT EXISTS messages ( "
				+ "src TEXT,"				
				+ "dest TEXT,"
				+ "message TEXT," 
				+ "date DATE DEFAULT CURRENT_TIMESTAMP) ;";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			// create a new table
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void createNewTableUsers() {

		String sql = "CREATE TABLE IF NOT EXISTS users ( "
				+ "pseudo TEXT NOT NULL,"
				+ "ipAddress TEXT);" ;
		
		String sql2 = "CREATE UNIQUE INDEX IF NOT EXISTS idx_user_pseudo ON users(pseudo);";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
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
			pstmt.setString(1, m.getSrc().getIp().getHostAddress());
			pstmt.setString(2, m.getDest().getIp().getHostAddress());
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

	public static ArrayList<String> selectAllMessages(){
		String sql = "SELECT src, dest, message, date FROM messages";
		ArrayList<String> res = new ArrayList<String>() ; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			while (rs.next()) {
				res.add(rs.getString("src") +  "\t" + 
						rs.getString("dest") + "\t" +
						rs.getString("message") + "\t" +
						rs.getString("date"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return res; 
	}

	public static void selectAllUsers(){
		String sql = "SELECT pseudo, ipAddress FROM users";

		try (Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

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
				+ "WHERE (src = '"+src.getIp().getHostAddress()+"' AND dest = '"+dest.getIp().getHostAddress()+"')"
						+ " OR (src = '"+dest.getIp().getHostAddress()+"' AND dest = '"+src.getIp().getHostAddress()+"')";

		ArrayList<String> res = new ArrayList<String>() ; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			ResultSet rs    = stmt.executeQuery(sql) ; 
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
			if (rs.next())
				ip = rs.getString("ipAddress") ;
		} catch (SQLException e) {
			e.printStackTrace();

		}
		
		return ip;
	}
	
	public static void deleteUser(User user) {
		String sql = "DELETE FROM users "
				+ "WHERE (pseudo = '"+user.getPseudo()+"') OR (ipAddress = '"+user.getIp().getHostAddress()+"')";
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement())
		{	
			stmt.executeUpdate(sql); 	
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

}
