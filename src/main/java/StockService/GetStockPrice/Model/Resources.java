package StockService.GetStockPrice.Model;

public class Resources {
	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return resource.toString();
	}
}
