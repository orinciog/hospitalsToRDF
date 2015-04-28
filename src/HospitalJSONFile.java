import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;


public class HospitalJSONFile {
	private String filePath;
	private Vector<Hospital> hospitals;
	public HospitalJSONFile(String filePath)
	{
		this.filePath = filePath;
		hospitals = new Vector<Hospital>();
	}
	
	public void doParseFile() throws FileNotFoundException, IOException, ParseException
	{
		JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(filePath));
        JSONArray arrayJSON = (JSONArray)obj;
        Iterator<JSONObject> iterator = arrayJSON.iterator();
        while (iterator.hasNext()) {
            JSONObject hospitalJSON=iterator.next();
            Hospital h=new Hospital();
            h.fromJSON(hospitalJSON);
            hospitals.add(h);
        }
	}
	
	public Vector<Hospital> getHospitals()
	{
		return hospitals;
	}

	
	public void writeRDFFile(String fileName,String prefixName) throws IOException
	{
	    	Model model = ModelFactory.createDefaultModel();
    		model.setNsPrefix( "opendata",prefixName );
    		
    	  	for(Hospital h:hospitals)
    	  	{
    	  		HospitalRDF rdf = new HospitalRDF(h);
    	  		rdf.setPrefix(prefixName);
    	  		model.add(rdf.transform());
    	  	}
    	  	
    	  	FileOutputStream fs=new FileOutputStream(new File(fileName));
    	  	model.write(fs,"N3");
    	  	fs.close();
	}
		
		

}
