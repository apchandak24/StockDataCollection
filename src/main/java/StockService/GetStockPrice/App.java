package StockService.GetStockPrice;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import StockService.GetStockPrice.Database.DatabaseHelper;
import StockService.GetStockPrice.Database.DatabaseService;
import StockService.GetStockPrice.HTTPRequest.HttpRequest;
import StockService.GetStockPrice.Model.Resources;

public class App 
{
    public static void main( String[] args )
    {
       DatabaseService service = new DatabaseService();
       App app = new App();
       Connection dbConnection = DatabaseHelper.getConnection(app.loadPropertiesFile());
       HttpRequest req = new HttpRequest();
       ArrayList<Resources> stocks=new ArrayList<Resources>();
	try {
		stocks = req.getStockPrices(service.getSymbolList(dbConnection));
		for(Resources s :stocks){
	    	   service.insertStockData(s.getResource().getFields(),dbConnection);
	       }
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	}finally {
		if(dbConnection!=null)
			try {
				dbConnection.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
	}
	System.out.println("Data inserted successfully");
       
    }
    
    private Properties loadPropertiesFile(){
    	Properties properties = new Properties();
		try {
			InputStream input = null;
			input = getClass().getClassLoader().getResourceAsStream("config.properties");
			properties.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
    }
}
