import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class FileUtils {

    public static File openFile(String src){

        try {
            File file = new File(src);
            if (file.isFile())  return file;
            return null;
        }
        catch (Exception e){
            return null;
        }
    }

    public static Reader getFileReaderFor(String fileName ){

        try{
            FileReader file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);

            return reader;

        } catch (Exception e ){

            return null;
        }

    }

    public static String getAllLine(BufferedReader reader){

        if(reader == null) return "";
        String lines = "", line="";

        try{

            while ( (line = reader.readLine() ) != null){
                lines += line +"\n";

            }

        } catch ( Exception e){
        }

        return lines;

    }

    public static String getLinesFrom(String fileName){

            BufferedReader reader = (BufferedReader) getFileReaderFor(fileName);
            return getAllLine(reader);
    }
}
