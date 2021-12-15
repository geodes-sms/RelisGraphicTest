package model.relis_parser;

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

    public static  void transformToXml(String fileName){



        try {
            StreamSource source_xsl = new StreamSource(new File("test.xsl"));
            StreamSource source_xml = new StreamSource(new File(fileName));
            StreamResult output_xml = new StreamResult(new File( TEST_XML_FILE));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            Transformer transformer = transformerFactory.newTransformer(source_xsl);
            transformer.transform(source_xml, output_xml);
        } catch (Exception e){

            e.printStackTrace();
        }

      }

    public static void main(String[] args) {

        String name = "relis_test.xml";

      transformToXml(name);
        project_files();

        String dir = ProjectUtils.get_workspace_path();

        String look_for = ProjectUtils.getProjectConfigFileName("test");
        String res = FileUtils.getPath(look_for, dir);

        System.out.println("RESULT => {"+ res +"}");



    }

    public static void project_files(){

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document document = db.parse(new File("config.xml"));
            NodeList nodeList = document.getElementsByTagName("workspace");
            System.out.println("We have project Length =>" + nodeList.getLength());
            for(int x=0,size= nodeList.getLength(); x<size; x++) {
                System.out.println(nodeList.item(x).getAttributes().getNamedItem("src").getNodeValue());
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}
