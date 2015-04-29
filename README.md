# HospitalsToRDF

This repo contains a Eclipse project, that converts a XLS file, containing data about hospitals from Romania, into a RDF file, linked with resources found in opendata.cs.pub.ro repository.

#Implementation
The project has several steps:

 - XLS => JSON
	 - read XLS file 
	 - get more information about the location of the hospitals, from Google Geocode Service 
	 - write data in JSON format
 - JSON => RDF
	 - read JSON file
	 - transform each hospital info into RDF format, using JENA library
	 - write RDF file
 - RDF => N3
	 - read RDF file
	 - link each town and county using SPARQL queries with the data found in opendata.cs.pub.ro repository
	 - In this step, it's possible to link the hospital info with the data found in geonames.org (A webservice is called to find more info about the location of the hospital).

#Configuration

 Before compiling&running the project, you must:

 -  create a Google API key for using Geocode Service. (https://console.developers.google.com/)
	 - put the key  in Config.GoogleApiKey
 -  create a geonames account (http://www.geonames.org/login) and activate the account for using the webservice
	 - put the account in Config.GeonamesUser

#Compiling

 1. Clone
 2. Open in Eclipse
 3. Configurate the accounts
 4. Build

#Running
The MainClass receives three arguments:

 - the name, without the extension, of the files used in application 
 - the input format of the file (Possible values: 0 (means "XLS"),1 ("JSON"), 2 ("RDF") )
 - the output format of the file (Possible values: 1 (means "JSON"),2 ("RDF"), 3 ("N3") )

and it transforms the input file into the desired output file.

#Example 
> java MainClass hospitals_test 0 3

This transforms the file hospitals_test.xls into hospitals_test.n3, creating also hospitals_test.json and hospitals_test.rdf

> Written with http://stackedit.io


