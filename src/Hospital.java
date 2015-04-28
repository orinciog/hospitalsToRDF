import java.util.HashMap;

import org.json.simple.JSONObject;


public class Hospital {
	private HospitalCategory category;
	private GeoInfo geoInfo;
	
	private String county;
	private String name;
	private String region;
	private String clasification;
	private String town;
	private String title;
	private String country;
	
	public Hospital()
	{
		geoInfo = new GeoInfo();
		category = new HospitalCategory();
		country="Romania";
	}
	
	public GeoInfo getGeoInfo() {
		return geoInfo;
	}

	public void setGeoInfo(GeoInfo coordinates) {
		this.geoInfo = coordinates;
	}

	public HospitalCategory getCategory() {
		return category;
	}

	public void setCategory(HospitalCategory category) {
		this.category = category;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.title = name.replaceAll(" ", "_");
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getClasification() {
		return clasification;
	}

	public void setClasification(String clasification) {
		this.clasification = clasification;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	@Override
	public String toString()
	{
		return name+" "+category.toString()+geoInfo.toString();
		
	}
	
	public JSONObject toJSON()
	{		
		JSONObject obj=new JSONObject();
		obj.put("name", this.name);
		obj.put("county", this.county);
		obj.put("region", this.region);
		obj.put("clasification", this.clasification);
		obj.put("town", this.town);
		obj.put("category",this.category.toJSON());
		obj.put("geoInfo",this.geoInfo.toJSON());
		return obj;
	}
	
	public void fromJSON(JSONObject obj)
	{		
		this.name = (String) obj.get("name");
		this.county = (String) obj.get("county");
		this.region = (String) obj.get("region");
		this.clasification = (String) obj.get("clasification");
		this.town = (String) obj.get("town");
		this.title = this.name.replaceAll(" ", "_");
		this.category.fromJSON((JSONObject)obj.get("category"));
		this.geoInfo.fromJSON((JSONObject)obj.get("geoInfo"));
	}

}
