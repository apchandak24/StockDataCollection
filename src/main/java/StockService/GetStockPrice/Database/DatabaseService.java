package StockService.GetStockPrice.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import StockService.GetStockPrice.Model.StockModel;
import StockService.GetStockPrice.Model.Symbol;

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
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "SELECT * from " + SYMBOL_TABLE_NAME;
			ResultSet result = stmt.executeQuery(query);
			while (result.next()) {
				Symbol sym = new Symbol(result.getString("symbol"));
				symbols.add(sym);
			}
		}catch (SQLException exception){
			System.out.println(exception.getMessage());
		}
		return symbols;
	}

	public boolean insertStockData(StockModel stock,Connection dbConnection) throws SQLException{
		createStocksTable(dbConnection);
		int id = getSymbolID(stock.getSymbol(),dbConnection);
		if (id != -1) {
			try {
				String query = "INSERT INTO " + STOCKS_TABLE_NAME + " (symbolid, name, price, volume, timestamp) VALUES"
						+ "(?,?,?,?,?)";
				PreparedStatement stmt = dbConnection.prepareStatement(query);
				stmt.setInt(1, id);
				stmt.setString(2, stock.getName());
				stmt.setDouble(3, stock.getPrice());
				stmt.setLong(4, stock.getVolume());
				stmt.setLong(5, stock.getTs());
				stmt.executeUpdate();
				return true;
			} catch (SQLException exception){
				System.out.println(exception.getMessage());
			}
		}
		return false;
	}

	private int getSymbolID(String symbol,Connection dbConnection) throws SQLException {
		int id = -1;
		try {
			String query = "SELECT id from " + SYMBOL_TABLE_NAME + " where symbol LIKE ?";
			PreparedStatement stmt = dbConnection.prepareStatement(query);
			stmt.setString(1, symbol);
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				id = result.getInt("id");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return id;
	}

	private void createStocksTable(Connection dbConnection) throws SQLException {
		try {
			Statement stmt = dbConnection.createStatement();
			String query = "CREATE TABLE IF NOT EXISTS " + STOCKS_TABLE_NAME + " ("
					+ " symbolid INTEGER NOT NULL, " + " name VARCHAR(255), "
					+ " price DOUBLE, volume BIGINT, timestamp BIGINT, " + " PRIMARY KEY (symbolid,timestamp), "
					+ " FOREIGN KEY(symbolid) REFERENCES symbol(id)  ON DELETE CASCADE )";
			stmt.executeUpdate(query);
		}catch (SQLException exception){
			System.out.println(exception.getMessage());
		}

	}

}
