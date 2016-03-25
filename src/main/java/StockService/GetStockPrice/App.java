package StockService.GetStockPrice;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import StockService.GetStockPrice.Database.DatabaseHelper;
import StockService.GetStockPrice.Database.DatabaseService;
import StockService.GetStockPrice.Database.PropertiesInstance;
import StockService.GetStockPrice.HTTPRequest.HttpRequest;
import StockService.GetStockPrice.Model.Resources;

public class App {
	public static void main(String[] args) {
		DatabaseService service = new DatabaseService();
		Connection dbConnection = DatabaseHelper.getConnection(PropertiesInstance.getInstance().getProperties());
		HttpRequest req = new HttpRequest();
		ArrayList<Resources> stocks = new ArrayList<Resources>();
		try {
			stocks = req.getStockPrices(service.getSymbolList(dbConnection));
			for (Resources s : stocks) {
				service.insertStockData(s.getResource().getFields(), dbConnection);
				System.out.println("Data inserted successfully");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (dbConnection != null)
				try {
					dbConnection.close();
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
		}
		

	}
}
