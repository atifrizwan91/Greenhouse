package SQLDatabase;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseHelper {
    public static Connection getConnection(){
       Connection conn;
        try {  
        	String url       = "jdbc:mysql://184.154.33.162/jsoftpk_atif?autoReconnect=true&useSSL=false";
            String user      = "jsoftpk_atif";
            String password  = "[Wm5lo2h^T_o";
            conn = (Connection) DriverManager.getConnection(url, user, password);
           
            return conn;
        }catch (SQLException ex) {
        	 System.out.print("conn-----------------------"+ ex.getMessage());
            return null;
        }
    }
  
    public static void insertData(String q) {
    	Connection con = getConnection();
    	try {
			PreparedStatement preparedStmt = con.prepareStatement(q);
			preparedStmt.execute();
			con.close();
		} catch (SQLException e1) {
			System.out.print("Error");
			e1.printStackTrace();
		}
    }
}
