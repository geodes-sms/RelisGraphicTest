package model.relis_parser;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
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
    }
}
