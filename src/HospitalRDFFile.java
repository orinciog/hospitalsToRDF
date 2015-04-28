import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Vector;

import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.VCARD;


public class HospitalRDFFile {
	private String filePath;
	Model model;
	public HospitalRDFFile(String filePath)
	{
		this.filePath = filePath;
		model = ModelFactory.createDefaultModel();
	}
	
	public void doParseFile() throws FileNotFoundException, IOException, ParseException
	{ 
		 InputStream in = FileManager.get().open( filePath );
		 model.read(in, null, "N3");
		 
		 Resource hospitalDbpedia = model.createResource("http://dbpedia.org/ontology/Hospital");
		 ResIterator iter = model.listResourcesWithProperty(RDF.type,hospitalDbpedia);
		 Property geonameCounty = model.createProperty(Config.PROPERTY_ROOT +"institutie_in_judet");
		 Property geonameTown   = model.createProperty(Config.PROPERTY_ROOT +"institutie_in_localitate");
		 
	     while(iter.hasNext()) {
	    	 Resource res=iter.nextResource();
	    	 
	    	 String town=res.getProperty(VCARD.Locality).getString();
	    	 String county=res.getProperty(VCARD.Region).getString();
	    	 Resource countyResource=getGeonamesCounty(county);
	    	 Resource townResource=getGeonamesTown(town, countyResource);
	    	 
	    	 if (countyResource!=null)
	    		 res.addProperty(geonameCounty,countyResource);
	    	 if (townResource!=null)
	    		 res.addProperty(geonameTown, townResource);
	     }        
	}
	
	public String getGeonamesInfo(String town)
	{
		WebService.setUserName(Config.GeonamesUser);

		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
		searchCriteria.setQ(town);
		searchCriteria.setStyle(Style.FULL);
		ToponymSearchResult searchResult;
		int id=0;

		try {
		    searchResult = WebService.search(searchCriteria);
		    for (Toponym toponym : searchResult.getToponyms()) {
		    	//System.out.println(toponym.getFeatureCode());
		    	if (toponym.getFeatureCode().substring(0, 3).compareTo("PPL")==0){
		    		id = toponym.getGeoNameId();
		    		break;
		    	}
		    }

		} catch (Exception e) {     
		    e.printStackTrace();
		}

		return "http://sws.geonames.org/"+id+"/";
	}

	public Resource getGeonamesCounty(String county)
	{
		return SparqlQuery.searchCounty(county);
	}
	public Resource getGeonamesTown(String town,Resource county)
	{
		return SparqlQuery.searchTown(town, county);
	}
	
	public void writeRDFFile(String fileName) throws IOException
	{
		model.setNsPrefix( "op",Config.PROPERTY_ROOT );
	 
	  	FileOutputStream fs=new FileOutputStream(new File(fileName));
	  	model.write(fs,"N3");
	  	fs.close();
	}
}
