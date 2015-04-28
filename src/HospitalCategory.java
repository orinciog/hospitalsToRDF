import java.util.HashMap;

import org.json.simple.JSONObject;


public class HospitalCategory {
	
	private String category;
	public HospitalCategory(String category)
	{
		this.category = category;
	}
	public HospitalCategory() {
		
	}
	public String getCategory() {
		return category;
	}
	public void getCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString()
	{
		return category;
	}
	public JSONObject toJSON() {
		JSONObject obj=new JSONObject();
		obj.put("name",this.category);
		return obj;
	}
	
	public void fromJSON(JSONObject obj) {
		this.category= (String)obj.get("name");
	}

	
}
