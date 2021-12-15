package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class FileUtils {

    /**
     * Open  a file using the path
     * @param src the path of the file
     * @return
     */
    public static File openFile(String src){

        try {
            // create a file object
            File file = new File(src);
            // check if the file is existing
            // if so then return the file
            if (file.isFile())  return file;
            return null;
        }
        // exception?
        catch (Exception e){
            return null;
        }
    }

    /**
     * get a FileReader for the passed fileName
     * @param fileName
     * @return the reader
     */
    public static Reader getFileReaderFor(String fileName ){

        try{
            FileReader file = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(file);

            return reader;

        } catch (Exception e ){

            return null;
        }

    }

    /**
     * extract all the line of a file
     * @param reader
     * @return all the file in a string form
     */
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

    /**
     *
     * @param fileName
     * @return
     */
    public static String getLinesFrom(String fileName){

            BufferedReader reader = (BufferedReader) getFileReaderFor(fileName);
            return getAllLine(reader);
    }

    public static void copyFiles(File source, File dest) {

        try {
            Files.move(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e){


            e.printStackTrace();
        }

    }

    public static String getPath(String drl, String whereIAm) {
        File dir = new File(whereIAm); //StaticMethods.currentPath() + "\\src\\main\\resources\\" +
        for(File e : dir.listFiles()) {
            if(e.isFile() && e.getName().equals(drl)) {return dir.getPath();}
            if(e.isDirectory() && !(e.getPath().contains("."))) {
                String idiot = getPath(drl, e.getPath());
                if(idiot != null) {return idiot;}
            }
        }
        return null;
    }
}
