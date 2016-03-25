package StockService.GetStockPrice.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import StockService.GetStockPrice.Model.StockModel;
import StockService.GetStockPrice.Model.Symbol;
/**
 * A service class to carry out CRUD operations on stocks table
 * @author ankita
 */
public class DatabaseService {
	private static final String SYMBOL_TABLE_NAME = "symbol";
	private static final String DATABASE_NAME = "stock";
	private static final String STOCKS_TABLE_NAME = "stocks";
	

	public DatabaseService() {
	}
	/**
	 * Get the list of symbols entered by user from database
	 * @param dbConnection
	 * @return ArrayList<Symbol>
	 */
	public ArrayList<Symbol> getSymbolList(Connection dbConnection)  {
		ArrayList<Symbol> symbols = new ArrayList<Symbol>();
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			String query = "SELECT * from " + SYMBOL_TABLE_NAME;
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				Symbol sym = new Symbol(result.getString("symbol"));
				symbols.add(sym);
			}
		}catch (SQLException exception){
			System.out.println(exception.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return symbols;
	}
	/**
	 * Insert record into stocks table
	 * @param stock
	 * @param dbConnection
	 * @return
	 * @throws SQLException
	 */
	public boolean insertStockData(StockModel stock,Connection dbConnection) throws SQLException{
		createStocksTable(dbConnection);
		PreparedStatement stmt = null;
		int id = getSymbolID(stock.getSymbol(),dbConnection);
		if (id != -1) {
			try {
				String query = "INSERT INTO " + STOCKS_TABLE_NAME + " (symbolid, name, price, volume, timestamp) VALUES"
						+ "(?,?,?,?,?)";
			    stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, id);
				stmt.setString(2, stock.getName());
				stmt.setDouble(3, stock.getPrice());
				stmt.setLong(4, stock.getVolume());
				stmt.setLong(5, stock.getTs());
				stmt.executeUpdate();
				return true;
			} catch (SQLException exception){
				System.out.println(exception.getMessage());
			}finally{
				if(stmt!=null)
					try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
			}
		}
		return false;
	}
	/**
	 * Get the ID of a symbol from database. It is used for internal clustering
	 * 
	 * @param symbol
	 * @param dbConnection
	 * @return
	 */
	private int getSymbolID(String symbol,Connection dbConnection) throws SQLException {
		int id = -1;
		PreparedStatement stmt=null;
		ResultSet result=null;
		try {
			String query = "SELECT id from " + SYMBOL_TABLE_NAME + " where symbol LIKE ?";
			stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol);
		    result = stmt.executeQuery();
			while (result.next()) {
				id = result.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
			if(result!=null)
				try {result.close();} catch (SQLException e) {e.printStackTrace();}
		}
		return id;
	}
	/**
	 * Create the Stocks table if it is not present in database. This can be
	 * avoided if table is already created in data base before the application
	 * starts.
	 * In the table symbol ID from symbol table and time stamp are primary key.
	 * Symbol ID is foreign key referencing ID in symbol table 
	 * @param dbConnection
	 */
	private void createStocksTable(Connection dbConnection) throws SQLException {
		Statement stmt = null;
		try {
			stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + STOCKS_TABLE_NAME + " ("
					+ " symbolid INTEGER NOT NULL, " + " name VARCHAR(255), "
					+ " price DOUBLE, volume BIGINT, timestamp BIGINT, " + " PRIMARY KEY (symbolid,timestamp), "
					+ " FOREIGN KEY(symbolid) REFERENCES symbol(id)  ON DELETE CASCADE )";
			stmt.executeUpdate(query);
		}catch (SQLException exception){
			System.out.println(exception.getMessage());
		}finally{
			if(stmt!=null)
				try {stmt.close();} catch (SQLException e) {e.printStackTrace();}
		}

	}

}
