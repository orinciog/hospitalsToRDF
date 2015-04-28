import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.sparql.vocabulary.FOAF;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.VCARD;


public class HospitalRDF {
	private Hospital hospital;
	private String prefix;
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public HospitalRDF(Hospital hospital)
	{
		this.hospital=hospital;
	}

	public Model transform()
	{
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("opendata", prefix );
		model.setNsPrefix("geo","http://www.w3.org/2003/01/geo/wgs84_pos#");
		model.setNsPrefix("vcard", "http://www.w3.org/2001/vcard-rdf/3.0#");
		
		Property latProperty = model.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lat");
		Property lonProperty = model.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#long");
		Property tipProperty = model.createProperty(Config.PROPERTY_ROOT+"tip_spital");
		Property adrProperty = model.createProperty(Config.PROPERTY_ROOT+"adresa_institutie");
		Resource hospitalDbpedia = model.createResource("http://dbpedia.org/ontology/Hospital");
		
		Resource resHospital = model.createResource(prefix+hospital.getTitle())
				.addLiteral(latProperty, hospital.getGeoInfo().getLatitude())
				.addLiteral(lonProperty, hospital.getGeoInfo().getLongitude()) 
				.addProperty(RDF.type, hospitalDbpedia)
				.addProperty(RDFS.label, hospital.getName())
				.addProperty(tipProperty, hospital.getCategory().getCategory())
				.addProperty(adrProperty, hospital.getGeoInfo().getFormattedAddress())
                .addProperty(VCARD.Locality, hospital.getTown())
                .addProperty(VCARD.Country, hospital.getCountry())
                .addProperty(VCARD.Region, hospital.getCounty());
		return model;
	}
}
