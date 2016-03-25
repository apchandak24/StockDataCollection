package StockService.GetStockPrice.Model;

public class Resource {
	
	private StockModel fields;
	
	public StockModel getFields() {
		return fields;
	}

	public void setFields(StockModel fields) {
		this.fields = fields;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fields.toString();
	}

}
