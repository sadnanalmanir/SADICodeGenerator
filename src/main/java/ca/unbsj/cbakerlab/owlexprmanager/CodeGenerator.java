package ca.unbsj.cbakerlab.owlexprmanager;

import com.hp.hpl.jena.rdf.model.Resource;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxClassExpressionParser;
import org.semanticweb.owlapi.model.*;

import java.io.Serializable;
import java.util.*;

/**
 * Generates the code for processing the input RDG graph according to the SADI input class description
 * Created by sadnana on 07/07/15.
 */
public class CodeGenerator {

    public String codeString = "";
    public Set<OWLClass> classes;
    public Set<OWLDataProperty> dataProperties;
    public Set<OWLObjectProperty> objectProperties;



    public String generateIPServiceCode(OWLOntology ontology, OWLDataFactory df, ManchesterOWLSyntaxClassExpressionParser parser, List<Edge> edgesList) {

        classes = ontology.getClassesInSignature();
        dataProperties = ontology.getDataPropertiesInSignature();
        objectProperties = ontology.getObjectPropertiesInSignature();

        for (Edge edge : edgesList) {
            // create code for those edge labels with object/data properties,
            // empty labels are not considered
            if (!edge.getLabel().equals("")) {
                // if the edge is not a data property i.e. is an object property
                if ( !checkPropertyType(objectProperties, dataProperties, edge.getLabel()) ) {
                    //System.out.println("object prop");

                    if(edge.getLabel().equals("subClassOf")){
                        System.out.println("Code generation for subClassOf NOT implemented yet");
                    }

                    else {
                        // for an object property, get the resource node directly
                        codeString += createCodeForResourceValue(edge.getVertex(Direction.IN).getProperty("nodeVariableName").toString(), edge.getLabel(), edge.getVertex(Direction.OUT).getProperty("nodeVariableName").toString());
                    }
                    /*
                    //if(edge.getVertex(Direction.IN).getProperty("name") instanceof OWLClass) {
                    if(isOWLClass(edge.getVertex(Direction.IN).getProperty("name").toString(), classes)){
                        System.out.println(edge.getVertex(Direction.IN).getProperty("name") + " -- is an instance of OWLClass");
                        codeString += createCodeForResourceValue(edge.getVertex(Direction.IN).getProperty("nodeVariableName").toString(), edge.getLabel(), edge.getVertex(Direction.OUT).getProperty("nodeVariableName").toString());
                    }
                    else {
                        codeString += createCodeForResourceStmt(edge.getVertex(Direction.IN).getProperty("nodeVariableName").toString(), edge.getLabel(), edge.getVertex(Direction.OUT).getProperty("nodeVariableName").toString());
                    }
                    */

                } else{
                    codeString += createCodeForDataValue(edge.getVertex(Direction.IN).getProperty("name").toString(), edge.getVertex(Direction.IN).getProperty("nodeVariableName").toString(), edge.getLabel(), edge.getVertex(Direction.OUT).getProperty("nodeVariableName").toString());
                }

            }

        }

        return codeString;

    }

    private String createCodeForDataValue(String dataType, String objVariableName, String predicateName, String subVariableName) {
        String result = "";

        if (dataType.equals("string")) {
            result += "\n" + "\t\t" + "String " + "val" + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")" + "." + "getString" + "(" + ")" + ";";
        }
        else if (dataType.equals("char")) {
            result += "\n" + "\t\t" + "Char " + "val" + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")" + "." + "getChar" + "(" + ")" + ";";
        }
        else if (dataType.equals("int")) {
            result += "\n" + "\t\t" + "int " + "val" + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")" + "." + "getInt" + "(" + ")" + ";";
        }
        else if (dataType.equals("float")) {
            result += "\n" + "\t\t" + "float " + "val" + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")" + "." + "getFloat" + "(" + ")" + ";";
        }
        else if (dataType.equals("double")) {
            result += "\n" + "\t\t" + " " + "val" + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")" + "." + "getDouble" + "(" + ")" + ";";
        }
        else if (dataType.equals("boolean")) {
            result += "\n" + "\t\t" + "boolean " + "val" + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")" + "." + "getBoolean" + "(" + ")" + ";";
        }
        else {
            System.out.println("Unknown data type in input RDF");
        }




        result += "\n" + "\t\t" + "if" + "(" + "val" + objVariableName + " == " + "null" + ")" + "\n"
                + "\t\t" + "{" + "\n"
                + "\t\t" + "\tlog.fatal" + "(" + "\"" + "No " + "val" + objVariableName + " found in the input RDF for " + predicateName + "\"" + ")" + ";" + "\n"
                + "\t\t" + "\tthrow new IllegalArgumentException" + "(" + "\"" + "Cannot extract " + "val" + objVariableName + " from the input RDF attached to " + predicateName + "\"" + ")" + ";" + "\n"
                + "\t\t" + "}";

        return result + "\n";
    }

    private boolean isOWLClass(String className, Set<OWLClass> classes) {
        for(OWLClass cls : classes){
            if(className.equals(cls.getIRI().getFragment()))
                return true;
        }
        return false;
    }

    private String createCodeForResourceStmt(Serializable objVariableName, String predicateName, String subVariableName) {
        String result = "";
        result += "\n" + "\t\t" + "Statement " + objVariableName + " = " + subVariableName + "." + "getProperty" + "(" + "Vocab" + "." + predicateName + ")";

        result += "\n" + "\t\t" + "if" + "(" + objVariableName + " == " + "null" + ")" + "\n"
                    + "\t\t" + "{" + "\n"
                + "\t\t" + "\tlog.fatal" + "(" + "\"" + "No " + objVariableName + " found in the input RDF for " + predicateName + "\"" + ")" + ";" + "\n"
                + "\t\t" + "\tthrow new IllegalArgumentException" + "(" + "\"" + "Cannot extract " + objVariableName + " from the input RDF attached to " + predicateName + "\"" + ")" + ";" + "\n"
                + "\t\t" + "}";

        return result + "\n";
    }

    private String createCodeForResourceValue(String objVariableName, String predicateName, String subVariableName) {
        String result = "";
        if(!predicateName.equals("type")) {
            result += "\n" + "\t\t" + "Resource " + objVariableName + " = " + subVariableName + "." + "getPropertyResourceValue" + "(" + "Vocab" + "." + predicateName + ");";

            result += "\n" + "\t\t" + "if" + "(" + objVariableName + " == " + "null" + ")" + "\n"
                    + "\t\t" + "{" + "\n"
                    + "\t\t" + "\tlog.fatal" + "(" + "\"" + "No " + objVariableName + " found in the input RDF for " + predicateName + "\"" + ")" + ";" + "\n"
                    + "\t\t" + "\tthrow new IllegalArgumentException" + "(" + "\"" + "Cannot extract " + objVariableName + " from the input RDF attached to " + predicateName + "\"" + ")" + ";" + "\n"
                    + "\t\t" + "}";

        }
        return result + "\n";
    }


    /**
     * @param objectProperties set of object properties
     * @param dataProperties   set of data properties
     * @param propertyLabel    edge label i.e. property name
     * @return ture if the edge/property is data property
     */
    private boolean checkPropertyType(Set<OWLObjectProperty> objectProperties, Set<OWLDataProperty> dataProperties, String propertyLabel) {
        boolean result = false;

        for (OWLDataProperty dp : dataProperties) {
            if (dp.getIRI().getFragment().equals(propertyLabel)) {
                result = true;
            }
        }
        for (OWLObjectProperty op : objectProperties) {
            if (op.getIRI().getFragment().equals(propertyLabel)) {
                result = false;
            }
        }
        return result;
    }


    public String generateOPServiceCode(OWLOntology ontology, OWLDataFactory df, ManchesterOWLSyntaxClassExpressionParser parser, List<Edge> edgesListSorted) {

        String outputCodeString = "";
        outputCodeString += "\n\t\t" + "Model outputModel" + " = " + "output.getModel();";
        outputCodeString += "\n\t\t" + "int resourceCounter" + " = " + "0;";
        outputCodeString += "\n\t\t" + "try" + " {";
        outputCodeString += "\n\t\t\t" + "java.sql.Statement stmt = MySqlDatabase.connection.createStatement();";
        outputCodeString += "\n\t\t\t" + "ResultSet rs = stmt.executeQuery(queryText);" ;
        outputCodeString += "\n\n\t\t\t" + "while(rs.next()) {" ;


        classes = ontology.getClassesInSignature();
        dataProperties = ontology.getDataPropertiesInSignature();
        objectProperties = ontology.getObjectPropertiesInSignature();
        List<String> resAttachedDataProp = new ArrayList<String>();
        Map<String, Integer> colId = new HashMap<String, Integer>();
        int colCounter = 0;

        // if the output graph has a data property, store its subject Resource
        for(Edge edge : edgesListSorted){
            if ( checkPropertyType(objectProperties, dataProperties, edge.getLabel()) ){
                resAttachedDataProp.add( edge.getVertex(Direction.OUT).getProperty("nodeVariableName").toString());
                colCounter++;
                colId.put(edge.getLabel(), new Integer(colCounter));
            }
        }

        outputCodeString += "\n\t\t\t\t" + "resourceCounter" + "++" + ";";
        outputCodeString += "\n\t\t\t\t" + "Resource tempResource = null;";

        for(Edge edge : edgesListSorted){
            if ( !edge.getLabel().equals("") && !edge.getLabel().equals("type") ){
                //resAttachedDataProp.add( edge.getVertex(Direction.OUT).getProperty("nodeVariableName").toString());
                // if object property
                if ( !checkPropertyType(objectProperties, dataProperties, edge.getLabel()) ){
                    // if there is more than one output (multiple rows in SQL resultset)
                    if(resAttachedDataProp.contains(edge.getVertex(Direction.IN).getProperty("nodeVariableName")) ){
                        outputCodeString += "\n\t\t\t\t" + "tempResource" + " = " + "outputModel" + "." + "createResource"
                                + "(\"" + getIRIForResource(classes, ontology, edge.getVertex(Direction.IN).getProperty("nodeVariableName").toString())+ "\"" + " + resourceCounter" + ");";
                    }

                    outputCodeString += "\n\t\t\t\t" + edge.getVertex(Direction.OUT).getProperty("nodeVariableName") +
                    "." + "addProperty" + "(" + "Vocab" + "." + edge.getLabel() + ", " + "tempResource" + ");";
                }
                // data property
                else{
                    outputCodeString += createLiteralStmt(colId, "tempResource", edge.getLabel(), edge.getVertex(Direction.IN).getProperty("name").toString());
                }
            }
        }


        outputCodeString += "\n\t\t\t" + "} //end while" ;
        //outputCodeString += "\n\t\t\t" + "connection.close();" ;
        outputCodeString += "\n\t\t" + "} //end try" ;

        //outputCodeString += "\n\n\t\t" + "catch(ClassNotFoundException e){" ;
        //outputCodeString += "\n\t\t" + "e.printStackTrace();" ;
        //outputCodeString += "\n\t\t" + "}" ;

        outputCodeString += "\n\n\t\t" + "catch(Exception e){" ;
        outputCodeString += "\n\t\t\t" + "log.fatal(\"Cannot execute query [\" + queryText + \"] or cannot read results:\" + e);" ;
        outputCodeString += "\n\t\t\t" + "throw new RuntimeException(\"Cannot execute query [\" + queryText + \"] or cannot read results:\" + e,e);";
        outputCodeString += "\n\t\t" + "}" ;

        return outputCodeString;
    }

    /**
     *
     *
     * @param colId
     * @param tempResource subject of the statement
     * @param predicate predicate of the statement
     * @param type literal type of the statement
     * @return
     */
    private String createLiteralStmt(Map<String, Integer> colId, String tempResource, String predicate, String type) {
        String litStmt = "";

        if(type.equals("string")){
            litStmt += "\n\t\t\t\t" + tempResource + "." + "addLiteral" + "(" + "Vocab" + "." + predicate + ", " + "rs" + "." +
                    "getString" + "(" + getColId(colId, predicate) + ")" + ");";
        }

        return litStmt;
    }

    private int getColId(Map<String, Integer> colId, String predicate) {
        int colNum = 0;
        for (Map.Entry<String, Integer> cursor : colId.entrySet()) {
            if(cursor.getKey().equals(predicate))
                colNum = cursor.getValue();
        }
        return colNum;
    }

    /**
     *
     *
     * @param classes
     * @param ontology
     * @param resourceName
     * @return NS for the object or the object property
     */
    private String getIRIForResource(Set<OWLClass> classes, OWLOntology ontology, String resourceName) {

        String resourceIRI = "";
        for (OWLClass res : classes) {
            if (res.getIRI().getFragment().equals(resourceName)) {
                resourceIRI = res.getIRI().toString();
            }
        }

        return resourceIRI;
    }

    /**
     *
     * @param ontology
     * @param property
     * @return NS for the object or the data property
     */
    private String getNSForProperty(OWLOntology ontology, String property) {
        Set<OWLDataProperty> dataProperties = ontology.getDataPropertiesInSignature();
        Set<OWLObjectProperty> objectProperties = ontology.getObjectPropertiesInSignature();
        String propertyNS = "";
        for (OWLDataProperty dp : dataProperties) {
            if (dp.getIRI().getFragment().equals(property)) {
                propertyNS = dp.getIRI().getNamespace();
            }
        }
        if (propertyNS.equals("")) {
            for (OWLObjectProperty op : objectProperties) {
                if (op.getIRI().getFragment().equals(property)) {
                    propertyNS = op.getIRI().getNamespace();
                }
            }
        }
        return propertyNS;
    }
}