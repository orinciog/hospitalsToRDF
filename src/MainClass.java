import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.VCARD;

public class MainClass extends Object {
    
	
     public static void main (String args[]) {
	  		if (args.length!=3)
	  		{
	  			System.out.println("Required arguments: input_prefix_file input_format output_format");
	  			System.exit(0);
	  		}
	  		
	  		String inputPrefixFile = args[0];
	  		int inputformat = Integer.parseInt(args[1]);
	  		int outputformat = Integer.parseInt(args[2]);
	  		
	  		if (inputformat<FILE_FORMAT.XLS.ordinal() || inputformat>=(int)FILE_FORMAT.N3.ordinal() ||
	  			outputformat<=FILE_FORMAT.XLS.ordinal() || outputformat>(int)FILE_FORMAT.N3.ordinal() ||
	  			inputformat >=outputformat)
	  		{
	  			System.out.println("Required arguments: input_prefix_file input_format output_format");
	  			System.exit(0);
	  		}
	  		
	  		try {
		  		for (int i=inputformat;i<outputformat;i++)
		  		{
		  			System.out.println(String.format("Transform from %s to %s",Config.FileFormatException[i],Config.FileFormatException[i+1]));
		  			String inputFile = inputPrefixFile+Config.FileFormatException[i];
		  			String outputFile = inputPrefixFile+Config.FileFormatException[i+1];
		  			if (i==FILE_FORMAT.XLS.ordinal())
	    	  		{
	    	  			HospitalXLSFile file=new HospitalXLSFile(inputFile);
	        	  		file.doParseFile();
	        	  		file.writeJSONFile(outputFile);
	    	  		}
	    	  		else if (i==FILE_FORMAT.JSON.ordinal())
	    	  		{
	    	  			HospitalJSONFile file=new HospitalJSONFile(inputFile);
	        	  		file.doParseFile();
	        	  		file.writeRDFFile(outputFile,Config.RESOURCE_ROOT);
	    	  		}
	    	  		
	    	  		else if (i==FILE_FORMAT.RDF.ordinal())
	    	  		{
	    	  			HospitalRDFFile file=new HospitalRDFFile(inputFile);
	        	  		file.doParseFile();
	        	  		file.writeRDFFile(outputFile);
	    	  		}
		  		}
			} catch (XPathExpressionException | ParserConfigurationException | SAXException | IOException | ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  	
    }
}