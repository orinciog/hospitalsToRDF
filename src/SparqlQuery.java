import com.hp.hpl.jena.query.ParameterizedSparqlString;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;


public class SparqlQuery {
	private static final String sparqlEndpoint = "http://opendata.cs.pub.ro/sparql";
	
	public static Resource searchTown(String town,Resource county)
	{
		String townLowerCase=town.toLowerCase().replaceAll(" ", "_").replaceAll("-", "_");
		String countyFilter = "";
		if (county!=null)
			countyFilter=String.format("?s <http://opendata.cs.pub.ro/property/localitate_in_judet>  <%s> . \n",county.toString());
		String sparqlQuery = String.format(""+
			      "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
			      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
			      "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
				  "SELECT ?s { \n"+
				  "?s rdfs:label  $label . \n" +
				  "?s rdf:type <http://dbpedia.org/ontology/Settlement> . \n" +
				   countyFilter +
				  "BIND(replace($label,' ', '_') AS ?result) . \n" + 
				  "BIND(replace($result, '-', '_') AS ?result1) . \n" +
				  "FILTER (lcase(str(?result1)) = \"%s\") \n"+
				"}",townLowerCase);
		ResultSet results=doSearch(sparqlQuery);
		while (results.hasNext()) 
		{
		      QuerySolution solution = results.next();
		      Resource expression = solution.get("s").asResource();
		      return expression;
		}
		return null;
	}
	
	public static Resource searchCounty(String county)
	{
		String countyLowerCase=county.toLowerCase().replaceAll(" ", "_").replaceAll("-", "_");
		String sparqlQuery = String.format(""+
			      "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
			      "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
			      "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
				  "SELECT ?s { \n"+
				  "?s rdfs:label  $label . \n" +
				  "?s rdf:type <http://dbpedia.org/class/yago/CountiesOfRomania> . \n" +
				  "BIND(replace($label,' ', '_') AS ?result) . \n" + 
				  "BIND(replace($result, '-', '_') AS ?result1) . \n" +
				  "FILTER (lcase(str(?result1)) = \"%s\")"+
				"}",countyLowerCase);
		ResultSet results=doSearch(sparqlQuery);
		while (results.hasNext()) 
		{
		      QuerySolution solution = results.next();
		      Resource expression = solution.get("s").asResource();
		      return expression;
		}
		return null;
	}
	
	private static ResultSet doSearch(String sparqlQuery)
	{		
		Query query = QueryFactory.create(sparqlQuery, Syntax.syntaxARQ) ;
	    QuerySolutionMap querySolutionMap = new QuerySolutionMap();

	    ParameterizedSparqlString parameterizedSparqlString = new ParameterizedSparqlString(query.toString(), querySolutionMap);

	    QueryEngineHTTP httpQuery = new QueryEngineHTTP(sparqlEndpoint,parameterizedSparqlString.asQuery());
	    // execute a Select query
	    ResultSet results = httpQuery.execSelect();
	    return results;
	}
}
