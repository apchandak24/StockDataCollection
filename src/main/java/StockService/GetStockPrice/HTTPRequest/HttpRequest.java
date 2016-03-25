package StockService.GetStockPrice.HTTPRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import StockService.GetStockPrice.Model.Resources;
import StockService.GetStockPrice.Model.Symbol;

public class HttpRequest {

	public ArrayList<Resources> getStockPrices(ArrayList<Symbol> symbols) {
		StringBuilder sb = new StringBuilder();
		for (Symbol s : symbols) {
			sb.append(s.getSymbol());
			sb.append(",");
		}
		String symbolList = sb.toString().substring(0, sb.toString().length() - 1);

		ArrayList<Resources> stockList = new ArrayList<Resources>();
		if (symbolList.length() > 0) {
			String url = "http://finance.yahoo.com/webservice/v1/symbols/"+symbolList+"/quote?format=json&view=detail";

			URL obj;
			HttpURLConnection con;

			try {
				obj = new URL(url);
				con = (HttpURLConnection) obj.openConnection();

				con.setRequestMethod("GET");

				// add request header
				// con.setRequestProperty("User-Agent", USER_AGENT);

				int responseCode = con.getResponseCode();
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					stockList = parseJson(response.toString());
					in.close();

				} else {
					System.out.println("Some error has occured");
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stockList;
	}

	private ArrayList<Resources> parseJson(String response) {
		ArrayList<Resources> stockList = new ArrayList<Resources>();
		JsonParser parser = new JsonParser();
		JsonObject obj = parser.parse(response).getAsJsonObject();

		JsonObject meta = obj.getAsJsonObject("list").getAsJsonObject("meta");
		int count = meta.get("count").getAsInt();

		JsonArray resources = obj.getAsJsonObject("list").getAsJsonArray("resources");

		Gson gson = new Gson();
		stockList = gson.fromJson(resources, new TypeToken<List<Resources>>() {
		}.getType());


		return stockList;
	}

}
