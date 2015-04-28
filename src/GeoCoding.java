import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.hp.hpl.jena.sparql.pfunction.library.container;

public class GeoCoding {

  // URL prefix to the geocoder
  private static final String GEOCODER_REQUEST_PREFIX_FOR_XML = "https://maps.google.com/maps/api/geocode/xml";

  public static final GeoInfo findGeoInfo (String address) throws IOException, XPathExpressionException, ParserConfigurationException, SAXException {
	  
	  try {
		Thread.sleep(1000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  GeoInfo p=new GeoInfo();
	  p.setAddress(address);
	  URL url = new URL(GEOCODER_REQUEST_PREFIX_FOR_XML + "?address=" + URLEncoder.encode(address, "UTF-8") + "&sensor=false&key="+Config.GoogleApiKey);
	  HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	  Document geocoderResultDocument = null;
	
	  try {
		  conn.connect();
		  Reader reader = new InputStreamReader(conn.getInputStream(),StandardCharsets.UTF_8);
		  InputSource geocoderResultInputSource = new InputSource(reader);
		  
		  geocoderResultInputSource.setEncoding("UTF-8");
		 
		  geocoderResultDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(geocoderResultInputSource);
	  }
	  finally {
		  conn.disconnect();
	  }
	  XPath xpath = XPathFactory.newInstance().newXPath();
	  NodeList resultNodeList = null;
	
	// a) obtain the formatted_address field for every result
	  resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result/formatted_address", geocoderResultDocument, XPathConstants.NODESET);
	  
	  for(int i=0; i<resultNodeList.getLength(); ++i) {
		  p.setFormattedAddress(resultNodeList.item(i).getTextContent());
		  break;
	  }
	
	// b) extract the locality for the first result
	  resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/address_component[type/text()='locality']/long_name", geocoderResultDocument, XPathConstants.NODESET);
	  for(int i=0; i<resultNodeList.getLength(); ++i) {
		  p.setTown(resultNodeList.item(i).getTextContent());
		  break;
	  }
	
	// c) extract the coordinates of the first result
	  resultNodeList = (NodeList) xpath.evaluate("/GeocodeResponse/result[1]/geometry/location/*", geocoderResultDocument, XPathConstants.NODESET);
	  double lat = Double.NaN;
	  double lng = Double.NaN;
	  for(int i=0; i<resultNodeList.getLength(); ++i) {
		  Node node = resultNodeList.item(i);
		  if("lat".equals(node.getNodeName())) lat = Double.parseDouble(node.getTextContent());
		  if("lng".equals(node.getNodeName())) lng = Double.parseDouble(node.getTextContent());
	  }
	  p.setLatitude(lat);
	  p.setLongitude(lng);
	  return p;
	}
  }
