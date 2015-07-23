*Currently, the code is generated in
-File basePath = new File("/home/sadnana/Dropbox/Experiments/AutoSADIServiceCode/").getAbsoluteFile();
*Editing ontology
-Go to the tab Ontology Prefix
--change it to the proper prefix e.g. http://cbakerlab.unbsj.ca:8080/lubm/lubm-sadi-service-ontology.owl#
-Go to Refactor->Change Ontology IRI
--Check and uncheck Enable Version Iri
-Go to Refactor->Rename Multiple Entities
--change http://swat.cse.lehigh.edu/onto/univ-bench.owl# into http://cbakerlab.unbsj.ca:8080/lubm/lubm-sadi-service-ontology.owl#

*database
-MySql database class is supported at this moment with the generated MySqlDatabase.java class
-It requires database.properties in /src/main/java/resources directory
-resources directory is not created at this moment
- the format of database.properties file should look like:

haiku.database.url=jdbc:oracle:thin:@132.216.183.68:1521:orclhaik
haiku.database.user=toh
haiku.database.password=toh245

# for preliminary testing
#haiku.database.url=jdbc:mysql://sadi2:3306/my_university
#haiku.database.user=mu_user
#haiku.database.password=mu_password

