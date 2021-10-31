package utils;

import controller.ProjectController;
import model.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.BiFunction;

public class Utility {

    private static final ArrayList<RelisUser> relisUsers = new ArrayList<>();


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
     * all the user in model.RelisUser Object from a file
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
            // get the model.RelisUser Object for every line which represent a ReliUser object
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
        return data.get(r.nextInt(0,data.size()));

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

    public static RelisUser GetMyInfo(){

        RelisUser user = new RelisUser();
        user.setPassword("azerty10");
        user.setUsername("youssouf1");
        user.setFull_name("Issa");
        user.setUser_usergroup("1");
        return user;
    }
    /**
     * get all relis users
     * @return
     */
    public static ArrayList<RelisUser> getRelisUsers() {
        if(relisUsers.size() == 0 ){
          relisUsers.add(getAdminUser());
          relisUsers.add(GetMyInfo());

          ArrayList<RelisUser> users = CreateMockUsers();
            relisUsers.addAll(users);
        }
        return relisUsers;
    }

    /**
     *
     * @param element the web element
     * @param className the class we're looking for
     * @return true if the web element has the className as a class
     */
    public static boolean hasClass(WebElement element,String className){

        String classes= element.getAttribute("class");
        Optional<String> classFindResult = Arrays.stream(classes.split(" "))
                .filter(el -> el.equals(className))
                .findFirst();
        return classFindResult.isPresent();
    }

    /**
     * choose and click a web element if the list item text is equal the the param cond
     * @param data
     * @param cond
     * @return
     */
    public  static WebElement chooseWebElement(List<WebElement> data, String cond){


        Optional<WebElement> element = data.stream()
                .filter(elem -> elem.getText().equals(cond))
                .findFirst();
        return element.orElse(null);
    }

    public static void chooseWebElementAndClick(List<WebElement> data,String conditon){
        WebElement element = chooseWebElement(data,conditon);

        if (element != null) element.click();
    }
    public static void checkWindownSize(WebDriver driver){

        if(driver == null) return;
        Dimension dimension= driver.manage().window().getSize();
        if(dimension.getWidth() < 977)
            driver.findElement(By.className(ScreeningUtils.CLASS_MAIN_MENU)).click();

    }


    public static void sleep(long sec){

        try {
            Thread.sleep(sec*1000);
        } catch (Exception e ){

            System.err.println("Erreur survenue dans la methode Sleep : message => " + e.getMessage());
        }
    }


    /**
     *
     * @param driver the current web driver
     * @return the connected user
     */
    public static RelisUser getCurrentConnectedUser(WebDriver driver){
        String user_name = driver.findElement(By.className(
                ConnexionUtils.CLASS_CONNECTED_USER_PROFILE_NAME)).getText();
        return Utility.getUserByFullName(user_name);
    }


    public static   ArrayList<String> work_through_table(WebDriver driver){


        try{

            // select the table that contains the screening phaseas
            WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));

            WebElement element;
            ArrayList<String> assigments= new ArrayList<>();
            while (true){
                try{
                    // get web element for the next click link
                    element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                    // get all the papers present from the current table
                    List<WebElement> papers = table.findElements(By.tagName("tr"));
                    // we remove the first web element which is the table header
                    papers.remove(0);
                    papers.forEach(paper ->{
                        String paper_key = takePaperKey(paper.getText());
                        assigments.add(paper_key);

                    });
                    // there is no next table  ?
                    if(Utility.hasClass(element,"disabled")) break;
                    element.findElement(By.linkText("Next")).click();
                } catch (Exception e){
                    System.out.println("ERROR " + e.getMessage());

                }
            }
            return assigments;
        } catch ( Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }


    }


    public static   ArrayList<String> work_through_table_function(WebDriver driver, BiFunction
                                                                  <List<WebElement>,Object,Integer> function
                                                        ,Object sujet, By elem){


        try{

            // select the table that contains the screening phaseas
            WebElement table = driver.findElement(elem);

            WebElement element;
            ArrayList<String> assigments= new ArrayList<>();
            while (true){
                try{
                    // get web element for the next click link
                    element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                    // get all the papers present from the current table
                    List<WebElement> papers = table.findElements(By.tagName("tr"));
                    // we remove the first web element which is the table header
                    papers.remove(0);

                    function.apply(papers,sujet);
                    // there is no next table  ?
                    if(Utility.hasClass(element,"disabled")) break;
                    element.findElement(By.linkText("Next")).click();
                } catch (Exception e){
                    System.out.println("ERROR " + e.getMessage());

                }
            }
            return assigments;
        } catch ( Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }


    }

    public static ArrayList<String> work_through_table_id(WebDriver driver,BiFunction<List<WebElement>
            ,Object,Integer> functions,Object obj){

        By by = By.id(ProjectUtils.ID_PROJECT_TABLE_USERS);
        return work_through_table_function(driver,functions,obj,by);

    }

    public static ArrayList<String> work_through_table_class(WebDriver driver,BiFunction<List<WebElement>
            ,Object,Integer> functions,Object obj){

        By by = By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE);
        return work_through_table_function(driver,functions,obj,by);

    }

    public static ArrayList<Paper> getAllPapersFrom(WebDriver driver, WebElement table){


        WebElement element;
        ArrayList<Paper> assigments= new ArrayList<>();
        while (true){
            try{
                // get all the papers present from the current table
                List<WebElement> papers = table.findElements(By.tagName("tr"));
                // we remove the first web element which is the table header
                papers.remove(0);
                papers.forEach(paper ->{
                    Paper paper_key = getPaper(paper);
                    assigments.add(paper_key);

                });
                // get web element for the next click link
                element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
                // there is no next table  ?
                if(Utility.hasClass(element,"disabled")) break;
                element.findElement(By.linkText("Next")).click();
            } catch (Exception e){
                break;

            }
        }
        return assigments;
    }

    public static ArrayList<Paper> getPapersForClassi(WebDriver driver, WebElement tables){


        WebElement element;
        ArrayList<Paper> assigments= new ArrayList<>();
        while (true){
            try{
                WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));
                // get all the papers present from the current table
                List<WebElement> papers = table.findElements(By.tagName("tr"));
                // we remove the first web element which is the table header
                papers.remove(0);
                papers.forEach(paper ->{
                    Paper paper_key = getPaper(paper);
                    assigments.add(paper_key);

                });
                // get web element for the next click link
                try {
                    element = driver.findElement(By.linkText(">"));
                    element.click();

                } catch (Exception e) {
                    break;
                };

                // there is no next table  ?

            } catch (Exception e){
                System.out.println("error " + e.getMessage());
                e.printStackTrace();
                break;

            }
        }
        return assigments;
    }
    /**
     *
     * @param driver the web driver
     * @return return all the papers for the project
     */
    public static   ArrayList<Paper> getAllPapers(WebDriver driver){
        ProjectController.openAllPapersPage(driver);
        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.id(ProjectUtils.ID_PROJECT_TABLE_USERS));
        return getAllPapersFrom(driver,table);

    }

    public static ArrayList<Paper> getAllPapersToCLassify(WebDriver driver){
        ProjectController.openAllPapersPage(driver);
        // select the table that contains the screening phaseas
        WebElement table = driver.findElement(By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE));
        return getPapersForClassi(driver,table);

    }



    /**
     *
     * @param paper the web element that contains paper info
     * @return a paper
     */
    private static Paper getPaper(WebElement paper) {

        List<WebElement> tds = paper.findElements(By.tagName("td"));

        Paper paper1 = new Paper();
        paper1.setKey(tds.get(1).getText());
        paper1.setTitle(tds.get(2).getText());
        return paper1;
    }


    private static String takePaperKey(String text) {

        return text.substring(0,text.indexOf(" "));
    }




    public static Paper getPaperByKey(ArrayList<Paper> paperArrayList, String key){


        return paperArrayList.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }

    public static String extractYearFrom(String title){
        String year= "";
        int i=0,j=0;

        while (i < title.length()){

            if(Character.isDigit(title.charAt(i))){
                while (j++ < 4)
                year += title.charAt(i++);
                break;
            }
            i++;
        }
        return (year.equals(""))? "2021": year;
    }

    /**
     * find a paper by his title
     * @param papers
     * @param title
     * @return a paper
     */
    public static Paper getPaperByTitle(ArrayList<Paper> papers, String title) {

        return papers.stream()
                .filter(paper -> paper.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    /**
     * find a paper by his title
     * @param papers
     * @param title
     * @return a paper
     */
    public static QA_Paper getQA_PaperByTitle(ArrayList<QA_Paper> papers, String title) {

        return papers.stream()
                .filter(paper -> paper.getPaper_title().equals(title))
                .findFirst()
                .orElse(null);
    }


    public static TypeOf getTypeOfObject(String type, int maxChar){

        if(type.equals("string") || type.equals("text")){
            StringType type1=  new StringType();
            type1.setMaxCharacter(maxChar);
            return type1;
        }

        else if(type.equals("real"))
            return new RealType();
        else if(type.equals("bool"))
            return new BooleanType();
        else if(type.equals("date"))
            return new DateType();
        else if(type.equals("int")){
            IntegerType intType = new IntegerType();
            intType.setMaxCharacter(maxChar);
            return intType;
        }
        return null;

    }
}
