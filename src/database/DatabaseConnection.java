package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Message;
import model.User;

/** Handles connections, queries and updates in the local database. 
 * Using SQLite. 
 * 
 * @author Jeanne Bertrand and Marie Laur
 *
 */
public class DatabaseConnection {
	private static String dbName = "history.db" ;

	/** Gets the connection to the database (in the ./db directory).
	 * 
	 * @return the connection
	 */
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

	/** Creates the messages table (if not exists) with four columns : 
	 * source IP address
	 * destination IP address
	 * message text
	 * timestamp
	 *  
	 */
	public static void createNewTableMessages() {

		String sql = "CREATE TABLE IF NOT EXISTS messages ( "
				+ "src TEXT,"				
				+ "dest TEXT,"
				+ "message TEXT," 
				+ "date DATE DEFAULT CURRENT_TIMESTAMP) ;";

		try (Connection conn = connect();
				Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/** Creates the users table (if not exists) with two columns :
	 * pseudo (with a unique index)
	 * IP address
	 * 
	 */
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

	/** Inserts a Message in the messages table of the DB. 
	 * 
	 * @param m the Message
	 */
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

	/** Inserts a User in the users table of the DB. 
	 * 
	 * @param u the User
	 */
	public static void insertUser(User u ) {
		String sql = "INSERT OR REPLACE INTO users(pseudo, ipAddress) VALUES(?,?)";
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

	/** Selects all the messages from the messages table of the DB. 
	 * 
	 * @return the list of messages in the format "src\tdest\tmessage\tdate\t"
	 */
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

	/** Selects all the users from the users table of the DB.
	 * 
	 * @return the list of users in the format "pseudo\tipAddress\t"
	 */
	public static ArrayList<String> selectAllUsers(){
		String sql = "SELECT pseudo, ipAddress FROM users";

		ArrayList<String> res = new ArrayList<String>() ; 
		try (Connection conn = connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){

			while (rs.next()) {
				res.add(rs.getString("pseudo") +  "\t" + 
						rs.getString("ipAddress")) ;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return res ; 
	}

	/** Selects all the messages with the couple of given Users in source or destination, in either way. 
	 *  
	 * @param first the first User to be searched for
	 * @param second the second User to be searched for
	 * @return the list of messages (same format than selectAllMessages())
	 */
	public static ArrayList<String> selectHistory(User first, User second){
		String sql = "SELECT src, dest, message, date "
				+ "FROM messages "
				+ "WHERE (src = '"+first.getIp().getHostAddress()+"' AND dest = '"+second.getIp().getHostAddress()+"')"
				+ " OR (src = '"+second.getIp().getHostAddress()+"' AND dest = '"+first.getIp().getHostAddress()+"')";

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

	/** Returns the IP address of the user with the given pseudo. 
	 * 
	 * @param pseudo to be searched for
	 * @return the IP address 
	 */
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

	/** Deletes a user from the users table of the DB. 
	 * 
	 * @param user to removed
	 */
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
