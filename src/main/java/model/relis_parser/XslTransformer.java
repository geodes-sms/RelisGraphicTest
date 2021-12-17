package model.relis_parser;

import model.Project;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.FileUtils;
import utils.ProjectUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class XslTransformer {


    public static final  String TEST_XML_FILE = "testng1.xml";
    private static final String RELIS_CONFIG_XML = "relis_test.xml";



    public static  void transformToXml(){



        try {
            StreamSource source_xsl = new StreamSource(new File("test.xsl"));
            StreamSource source_xml = new StreamSource(new File(RELIS_CONFIG_XML));
            StreamResult output_xml = new StreamResult(new File( TEST_XML_FILE));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer(source_xsl);
            transformer.transform(source_xml, output_xml);
        } catch (Exception e){

            e.printStackTrace();
        }

      }

    public static void main(String[] args) {


        Project p = project();

        System.out.println(p);

    }


    public static Project project(){

        String dir = ProjectUtils.get_workspace_path();
        String projectName  = getAttributeOf("project", "name") +".relis";

        String res = FileUtils.getPath(projectName, dir);

        String fileName = res +"/"+ projectName;

        return RelisParser.getProjectFrom(fileName);


    }


    private static String getAttributeOf( String tag, String attr){
        String res= "";

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(new File(RELIS_CONFIG_XML));
            NodeList nodeList = document.getElementsByTagName(tag);
            for(int x=0,size= nodeList.getLength(); x<size; x++) {
              res = nodeList.item(x).getAttributes().getNamedItem(attr).getNodeValue();
            }
        }  catch (Exception e){

            e.printStackTrace();
        }
        System.out.println("Project selected is " + res);
        return res;
    }


}
