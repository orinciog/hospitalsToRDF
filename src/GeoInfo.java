import org.json.simple.JSONObject;


public class GeoInfo {
	private double latitude;
	private double longitude;
	private String address;
	private String formattedAddress;
	private String town;
	
	public GeoInfo()
	{
		
	}
	
	public boolean isCorrect()
	{
		return latitude!=Double.NaN && longitude!=Double.NaN && formattedAddress!=null && town!=null;
	}
	
	public GeoInfo(double latitude,double longitude)
	{
		this.latitude=latitude;
		this.longitude=longitude;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString()
	{
		return "Place:"+address+
				" Address="+formattedAddress+
				" Town="+town+
				" ("+latitude+
				","+longitude+")";
	}
	
	public JSONObject toJSON() {
		JSONObject obj=new JSONObject();
		obj.put("latitude",this.latitude);
		obj.put("longitude",this.longitude);
		obj.put("address",this.address);
		obj.put("town",this.town);
		obj.put("formattedAddress",this.formattedAddress);
		return obj;
	}
	
	public void fromJSON(JSONObject obj) {
		if (obj==null)
			return;
		this.address= (String)obj.get("address");
		this.town= obj.get("town") != null?(String)obj.get("town"):"";
		this.formattedAddress= obj.get("formattedAddress") != null?(String)obj.get("formattedAddress"):"";
		this.latitude= obj.get("latitude") != null?(Double)obj.get("latitude"):0.0;
		this.longitude= obj.get("longitude")!= null?(Double)obj.get("longitude"):0.0;
	}
}
