package utils;

import controller.ProjectController;
import model.Paper;
import model.QA_Paper;
import model.RelisUser;
import model.relis_parser.StringParser;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

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
            MessageDigest md = MessageDigest.getInstance("MD5");
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
        return data.get(nextInt(0,data.size()));

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




    //Previous [1] [2] Next
    private static boolean hasNextPage(WebDriver driver){
      WebElement  element = driver.findElement(By.id(ProjectUtils.ID_NEXT_PAPERS_PAGE));
        // there is no next table  ?
        if(Utility.hasClass(element,"disabled")) return false;
        element.findElement(By.linkText("Next")).click();
        return true;
    }

    public static boolean hasNextPageForClassification(WebDriver driver){
        try {
          WebElement  element = driver.findElement(By.linkText(">"));
            element.click();
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    public static void work_through_table_function(WebDriver driver, BiFunction
                                                                  <List<WebElement>,Object,Integer> function
                                                        , Object sujet, By elem, Predicate<WebDriver> predicate){


        try{

            while (true){
                try{
                    // select the table that contains the screening phaseas
                    WebElement table = driver.findElement(elem);
                    // get all the papers present from the current table
                    List<WebElement> papers = table.findElements(By.tagName("tr"));
//                    // we remove the first web element which is the table header
                   // papers.remove(0);
                    function.apply(papers,sujet);
                    // there is no next table  ?
                    if(!predicate.test(driver))
                         break;
                } catch (Exception e){
                    System.out.println("ERROR " + e.getMessage());
                    e.printStackTrace();
                    break;

                }
            }

        } catch ( Exception e){
            e.printStackTrace();

        }


    }




    public static int work_through_table_find_element(WebDriver driver, Functions function
            , Object sujet, Object object, By elem, Predicate<WebDriver> predicate){

        int val = 0;
        try{

            while (true){
                try{
                    // select the table that contains the screening phaseas
                    WebElement table = driver.findElement(elem);
                    // get all the papers present from the current table
                    List<WebElement> papers = table.findElements(By.tagName("tr"));
//                    // we remove the first web element which is the table header
                    // papers.remove(0);
                    int result = ( (FourthParamsFunctions) function).apply(driver, papers, sujet, object);
                    // there is no next table  ?
                    boolean nextTable = predicate.test(driver);
                    if(result == 1 || !nextTable ){
                        if(!nextTable)
                            val = 1;
                        break;
                    }

                } catch (Exception e){
                    System.out.println("ERROR " + e.getMessage());
                    e.printStackTrace();
                    break;

                }
            }

        } catch ( Exception e){
            e.printStackTrace();

        }
        return val;

    }

    /**
     * this function performs some function above the 'table' of papers for the dom
     * @param driver the webdriver
     * @param functions an action function
     * @param obj the subject like an array to retrive papers ...
     */
    public static void work_through_table_id(WebDriver driver, BiFunction<List<WebElement>
            ,Object,Integer> functions, Object obj){

        By by = By.id(ProjectUtils.ID_PROJECT_TABLE_USERS);
        work_through_table_function(driver, functions, obj, by,Utility::hasNextPage);

    }

    public static int find_element_table_id(WebDriver driver, Functions functions, Object obj, Object subject){

        By by = By.id(ProjectUtils.ID_PROJECT_TABLE_USERS);
        return work_through_table_find_element(driver,functions,subject,obj,by,Utility::hasNextPage);

    }

    /**
     * function to performs some action in the table represented by the class of papers of every project phases
     * from the DOM
     * @param driver the the current web driver
     * @param functions a function that represents some action to perform like (adding paper to a list)
     * @param obj the subject it can be an list of objects
     */
    public static void work_through_table_class(WebDriver driver,BiFunction<List<WebElement>
            ,Object,Integer> functions,Object obj){

        By by = By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE);
         work_through_table_function(driver,functions,obj,by,Utility::hasNextPage);

    }


    /**
     * this a function that retrieves all the papers from the dom page
     */
    private  static final BiFunction<List<WebElement>,Object,Integer> getPaperFromDOM = (papers, array) ->{
        ArrayList<Paper> assigments =(ArrayList<Paper>) array;
        papers.remove(0);
        papers.forEach(paper ->{
            Paper paper_key = getPaper(paper);
            assigments.add(paper_key);
            //System.out.println("paper p->" + paper_key);

        });
        return -1;
    };

    /**
     * this function retrieves all the key also the 'id' of every paper
     */
    private static final BiFunction<List<WebElement>, Object,Integer> getPapersKeyFromDOM = (papers, array) ->{

        ArrayList<String> assigments =(ArrayList<String>) array;
        int index =0;
        System.out.println("dans le extract papers @@@@@@@@@#######################");
        Utility.sleep(10);
        List<WebElement> header = papers.get(0).findElements(By.tagName("th"));
        System.out.println("We have " + header.size() +" TD");
        for (WebElement t : header){
            String name = t.getAttribute("aria-label");

            name  = name.substring(0, name.indexOf(":"));
            if(name.equals("Paper")) {
                System.out.println("name=> " + name);
                break;
            }
            index++;
        }

        System.out.println("INDEX = " + index);
        papers.remove(0);
        int finalIndex = index;
        papers.forEach(paper ->{
            List<WebElement> tds = paper.findElements(By.tagName("td"));

            String paper_key = takePaperKey( tds.get(finalIndex).getText());
            assigments.add(paper_key);
        });
        return -1;
    };

    public static final void getPapersKeyFromDOMFromId(WebDriver driver, Object object){
        work_through_table_id(driver,getPapersKeyFromDOM,object);
    }


    public static void getAllPaperFromTable_id(WebDriver driver, Object object){
        ProjectController.openAllPapersPage(driver);
        work_through_table_id(driver,getPaperFromDOM,object);
    }

    public static void getAllPaperFromTable_class(WebDriver driver, Object object){
        ProjectController.openAllPapersPage(driver);
        work_through_table_class(driver,getPaperFromDOM,object);
    }

    public static void getPapersForClassification(WebDriver driver, Object object){

        ProjectController.openAllPapersPage(driver);
        // select the table that contains the screening phaseas
        By table = By.className(ScreeningUtils.CLASS_SCREENING_PHASES_TABLE);
        work_through_table_function(driver,getPaperFromDOM,object,table,Utility::hasNextPageForClassification);

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




    public static int indexOfNth(String data,String str, int rank){

        assert  rank >=1;
        int i=0,from=0;

        while (rank-- >= 1 ){

            i = data.indexOf(str,from);
            from = i+1;
        }
        return i;
    }


    public static boolean isRelisPunctuation(char ch){

        return ch == '{' || ch == '[' || ch == ']' || ch == '}' || ch == '*' || ch == '\"'
                || ch == '\'' || ch == ':' || ch == '(' || ch == ')'|| ch == '=';
    }

    public static char getNextPunctuation(StringParser data){
        String str = data.getData();
        int i=0;
        while (i < str.length())
        if(str.charAt(i) == '!' || str.charAt(i) == ',' || str.charAt(i) == ';' || str.charAt(i) == '.' ||
                str.charAt(i) == '?' || str.charAt(i) == '-' ||
                str.charAt(i) == '\'' || str.charAt(i) == '\"' || str.charAt(i) == ':')
        {
            data.updateData(i);
            return str.charAt(i);
        }
        else if(str.charAt(i) == ' ' || str.charAt(i) == '\n') i++;
        else return ' ';
        return ' ';
    }


    public static int nextInt(int min,int max) {
        return min + (int)(Math.random() * ((max - min) ));
    }
    public static double nextDouble(double min, double max) { return min + (Math.random() * ((max - min) + 1));}
}
