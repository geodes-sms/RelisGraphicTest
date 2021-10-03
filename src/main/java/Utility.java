import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Utility {


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

        return CreateMockUsers();

    }


    /**
     * display all the relis user
     */
    public static  void showAllRelisUser(){

        ArrayList<RelisUser> users=  getMocksRelisUser();

        users.forEach(System.out::println);
    }
}
