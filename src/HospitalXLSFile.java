import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.json.simple.JSONArray;
import org.xml.sax.SAXException;


public class HospitalXLSFile {
	private String filePath;
	private Vector<Hospital> hospitals;
	public HospitalXLSFile(String filePath)
	{
		this.filePath = filePath;
		hospitals = new Vector<Hospital>();
	}
	
	public void doParseFile() throws IOException, XPathExpressionException, ParserConfigurationException, SAXException
	{
		FileInputStream file = new FileInputStream(new File(filePath));
        
		//Get the workbook instance for XLS file 
		HSSFWorkbook workbook = new HSSFWorkbook(file);
		 
		//Get first sheet from the workbook
		HSSFSheet sheet = workbook.getSheetAt(0);
		 
		//Get iterator to all the rows in current sheet
		Iterator<Row> rowIterator = sheet.iterator();
		rowIterator.next();
		while(rowIterator.hasNext())
		{
			Row row=rowIterator.next();
			Hospital hospital=new Hospital();
			hospitals.add(hospital);
			Iterator<Cell> cellIterator = row.cellIterator();
			int i=0;
			while(cellIterator.hasNext())
			{
				Cell cell=cellIterator.next();
				String value = "";
				switch(i)
				{
				case 1:
					value=cell.getStringCellValue();
					hospital.setRegion(value);
					break;
				case 2:
					value=cell.getStringCellValue();
					HospitalCategory category=new HospitalCategory(value);
					hospital.setCategory(category);
					break;
				case 3:
					value=cell.getStringCellValue();
					hospital.setClasification(value);
					break;
				case 4:
					value=cell.getStringCellValue();
					hospital.setName(value);
					break;
				case 5:
					value=cell.getStringCellValue();
					hospital.setTown(value);
					break;
				case 6:
					value=cell.getStringCellValue();
					hospital.setCounty(value);
					break;
				}
				i++;
			}
			hospital.setGeoInfo(findGeoInfo(hospital));
			System.out.println(hospital.getGeoInfo());
		}	
	}
	
	private GeoInfo findGeoInfo(Hospital hospital) throws XPathExpressionException, IOException, ParserConfigurationException, SAXException
	{
		GeoInfo info=GeoCoding.findGeoInfo(hospital.getName());
		if (!info.isCorrect())
		{
			info=GeoCoding.findGeoInfo(hospital.getTown()+" "+hospital.getCounty());
		}
		return info;
	}
	
	public Vector<Hospital> getHospitals()
	{
		return hospitals;
	}
	
	public void writeJSONFile(String fileName) throws IOException
	{
		JSONArray arrayJSON=new JSONArray();
		for (Hospital h:hospitals)
		{
			arrayJSON.add(h.toJSON());
		}
		FileWriter fw=new FileWriter(fileName);
		arrayJSON.writeJSONString(fw);
		fw.close();
	}
	
	
}
