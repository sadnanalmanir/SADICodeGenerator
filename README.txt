*TO change RDB mapping and ontology
-Go to TPTPQueryGenerator.java and ExecutionEngine.java
--change ONT_RDB_MAPPING_FILENAME and ONT_TO_TPTP_FILENAME
-copy new ONT_RDB_MAPPING_FILENAME file into resources
-copy new ONT_TO_TPTP_FILENAME into resources
-copy new extensional_predicates.xml into resources
-change new predicate_views.xml into resources

*After generating pom.xml, replace the
-Change the <name>, <finalName> and <contextPath> values to mysadi_demo
-mysadi_demo is taken from serviceClass = "ca.unbsj.cbakerlab.mysadi_demo.getStudentNameByCourse";

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

*SQL Template Query
-1)make sure that the resource directory contains
--vkernel
--answer_predicates.xml
--extensional_predicates.xml
--predicate_views.xml
--PredicateViews.xsd
--translated ontology either in cnf or fof in tptp syntax HAI_no_Illegal_Symbolsl.ontology.cnf.tptp
--mapping of ontology and relaitonal data tables tohdw_haio_semantic_map.fof.tptp
-2)make sure to have external libraries
--CusionJE.jar
--derby.jar
--JSAP-2.0a.jar
--LogicWarehouseJE.jar
--mysql-connection-java-5.0.8-bin.jar
