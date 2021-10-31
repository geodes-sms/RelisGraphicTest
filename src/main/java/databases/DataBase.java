package databases;

import com.google.common.collect.ArrayListMultimap;
import model.RelisUser;
import utils.FileUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataBase {

    private static final String MOCK_FULL_NAMES_FILE ="src/main/resources/users/mock_full_name.txt" ;
    private static DataBase db = null;

    private static final ArrayList<RelisUser> reviewers = new ArrayList<>();
    private static final ArrayList<RelisUser> project_managers =new ArrayList<>();
    private static final ArrayList<RelisUser> validators = new ArrayList<>();
    private static final ArrayList<RelisUser> guests = new ArrayList<>();

    Random random = new Random();
    private static  ArrayListMultimap<String,String> dataClassification;
    private static ArrayList<String> mockFullNames = new ArrayList<>();


    public void extractMockFullNames(){
        if(mockFullNames.size() == 0){
            String lines = FileUtils.getLinesFrom(MOCK_FULL_NAMES_FILE);
            String [] data= lines.split("\n");
            mockFullNames.addAll(Arrays.asList(data));

        }
    }

    public String getMockFullName(){

        extractMockFullNames();
        int x = random.nextInt(0, mockFullNames.size());
        return mockFullNames.get(x);
    }


    public void showMockFullNames(){
        extractMockFullNames();
        mockFullNames.forEach(System.out::println);
    }
    public static void setClassificationData(){

        dataClassification = ArrayListMultimap.create();
        extractClassificationData();


    }

    public static void extractClassificationData(){

        try {
            String lines = FileUtils.getLinesFrom("src/main/resources/classification.txt");
            for (String line : lines.split("\n")){

                String fieldName = line.substring(0,line.indexOf("{"));
                int pos = line.indexOf("{")+1,nextPos=0;
                while (pos  < line.length() ){
                     nextPos = line.indexOf(",",pos);
                    if(nextPos == -1) nextPos = line.indexOf("}",pos);
                    String optins = line.substring(pos,nextPos);
                    dataClassification.put(fieldName,optins);
                    pos = nextPos+1;

                }

            }

        } catch (Exception e) {e.printStackTrace();};
    }

    public String getNextClassificationCategory(String category){
        List<String> fields = dataClassification.get(category);
        int index = random.nextInt(0,fields.size());
        return fields.get(index);
    }

    /**
     *  we use a single instance for this classe
     * @return the single instance
     */
    public static DataBase getInstance(){

        if(db == null)
        {
            db = new DataBase();
            setClassificationData();
        }

        return db;
    }



    public RelisUser GetAReviewer(){

        return reviewers.get(new Random().nextInt(0, reviewers.size()));
    }
    public RelisUser getAValidator(){

        return validators.get(new Random().nextInt(0, validators.size()));
    }
    public RelisUser getAProjectManager(){

        return project_managers.get(new Random().nextInt(0, project_managers.size()));
    }
    public RelisUser getAGuestUser(){

        return guests.get(new Random().nextInt(0, guests.size()));
    }

    public void addReviewer( RelisUser user){
        reviewers.add(user);
    }
    public void deleteReviewer(RelisUser reviewer){
        reviewers.remove(reviewer);
    }
    public void addProjectManager(RelisUser user){
        project_managers.add(user);
    }
    public void deleteProjectManager(RelisUser pm){
        project_managers.remove(pm);
    }
    public void addValidator(RelisUser user){
        validators.add(user);
    }
    public void deleteValidator(RelisUser validator){
        validators.remove(validator);
    }
    public void addGuestUser(RelisUser user){
        guests.add(user);
    }
    public void deleteGuest(RelisUser guest){
        guests.remove(guest);
    }


}
