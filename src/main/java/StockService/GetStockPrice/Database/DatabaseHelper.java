package StockService.GetStockPrice.Database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseHelper {


	public static Connection getConnection(Properties properties) {
		try {
			
			String dbName = properties.getProperty("database");
			String dbUser = properties.getProperty("dbuser");
			String dbPassword = properties.getProperty("dbpassword");

			String connectionURL = "jdbc:mysql://localhost:3306/"+dbName;
			Connection connection = null;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(connectionURL, dbUser,dbPassword);
			return connection;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}