import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class Utility {

    private static ArrayList<RelisUser> relisUsers = new ArrayList<>();


    /**
     *
     *  PASSWORD ENCRIPTION USING MD5
     * @param password
     * @return
     */
    public static String encriptWiht_MD5( String password ) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes( "UTF-8" ));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; i++) {
                sb.append( String.format( "%02x", array[i]));
            }
            return sb.toString();
        } catch ( NoSuchAlgorithmException | UnsupportedEncodingException e) {
            return null;
        }

    }


    public static ArrayList<RelisUser> extractRelisUser(String lines){

        if(lines.equals("")) return null;


        ArrayList<RelisUser> usersData = new ArrayList<>();
        String[] usersLines = lines.split("\n");

        for (int i=0; i < usersLines.length; i++){

            String[] users_arr =  usersLines[i].split(",");

            RelisUser user = new RelisUser();
            user.setFull_name(users_arr[0]);
            user.setUsername(users_arr[1]);
            user.setUser_email(users_arr[2]);
            user.setUser_usergroup(users_arr[3]);
            user.setPassword(users_arr[4]);
            user.setCreation_time(users_arr[5]);
            user.setCreate_by(users_arr[6]);
            user.setUser_state(users_arr[7]);
            user.setUser_active(users_arr[8]);
            usersData.add(user);
        }

        return usersData;
    }

    /**
     * procedure to generate the sql format for
     * all the mock user present in the procedure
     * createMockUsers()
     */
    public static void generateRelisUserSQL(){

        try {
            // output sql format file
            BufferedWriter bf = new BufferedWriter(new FileWriter(
                    "src/main/resources/users/users.sql"));

            ArrayList<RelisUser> users = getMocksRelisUser();
            bf.write("INSERT INTO users (user_name, user_username, user_mail, user_usergroup, " +
                    "user_password,creation_time, create_by, user_state, user_active) VALUES\n");
            for(int i=0; i < users.size(); i++){

                try {

                    if ((i != users.size() - 1)) {
                        bf.write(users.get(i).getRelisUserDBFormatSQL() + ",\n");
                    } else {
                        bf.write(users.get(i).getRelisUserDBFormatSQL());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            bf.flush();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * all the user in RelisUser Object from a file
     * that contains the user information in csv format
     * @return
     */
    private static ArrayList<RelisUser> CreateMockUsers() {

        try{

            // open the .csv file
            BufferedReader reader = (BufferedReader) FileUtils
                    .getFileReaderFor("src/main/resources/users/users.csv");
            // the headear that contains the informations rows
            String header = reader.readLine();
            // now extract all the lines
            String lines = FileUtils.getAllLine(reader);
            // get the RelisUser Object for every line which represent a ReliUser object
            ArrayList<RelisUser> data = extractRelisUser(lines);
            // then return that data
            return data;
        } catch (Exception e){ // exception???

            System.out.println(e.getMessage());
            return null;

        }


    }

    /**
     * get all the mocks user for the test
     * @return
     */
    public static ArrayList<RelisUser> getMocksRelisUser(){

        return getRelisUsers();

    }


    /**
     * display all the relis user
     */
    public static  void showAllRelisUser(){

        ArrayList<RelisUser> users=  getRelisUsers();

        users.stream()
                .sorted()
                .forEach(p -> System.out.println(p));
    }

    public static RelisUser getRandomUser(){

        ArrayList<RelisUser> data = getRelisUsers();
        Random r = new Random();
        int i = r.nextInt(0,data.size());

        return data.get(i);

    }

    /**
     *  get a relis user by it full name
     * @param full_name
     * @return
     */
    public static RelisUser getUserByFullName(String full_name){
        ArrayList<RelisUser> data = getRelisUsers();

        return data.stream()
                .filter(p -> p.getFull_name().equals(full_name))
                .findFirst().orElse(null);

    }

    /**
     *  get a relis user by it user_username
     * @param userName
     * @return
     */
    public static RelisUser getUserByUserName(String userName){
        ArrayList<RelisUser> data = getRelisUsers();
        data.forEach(p-> System.out.println(p.getUsername()));

        return data.stream()
                .filter(p -> p.getUsername().equals(userName))
                .findFirst().orElse(null);

    }

    /**
     * get the admin relis user
     * @return
     */
    public static RelisUser getAdminUser(){
        RelisUser admin = new RelisUser();
        admin.setUsername("admin");
        admin.setPassword("123");
        admin.setFull_name("Admin");
        admin.setUser_state("1");
        admin.setUser_active("1");
        admin.setUser_usergroup("1");
        admin.setUser_email("info@relis.ca");
        admin.setCreate_by("1");;
        admin.setCreation_time("2020-06-22 19:49:48");
        return admin;
    }


    /**
     * get all relis users
     * @return
     */
    public static ArrayList<RelisUser> getRelisUsers() {
        if(relisUsers.size() == 0 ){
          relisUsers.add(getAdminUser());
          ArrayList<RelisUser> users = getMocksRelisUser();
            relisUsers.addAll(users);
        }
        return relisUsers;
    }

    public static boolean hasClass(WebElement element,String className){

        String classes= element.getAttribute("class");
        Optional<String> classFindResult = Arrays.stream(classes.split(" "))
                .filter(el -> el.equals(className))
                .findFirst();
        return classFindResult.isPresent();
    }
}
