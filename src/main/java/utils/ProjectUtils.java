package utils;

import org.openqa.selenium.WebElement;

public class ProjectUtils {


    // VARIABLES OF THE CLASS
    public static final String CLASS_OPEN_PROJECT = ".fa-paper-plane";
    public static final String LK_ADD_NEW_PROJECT_BUTTON = "Add new project";

    public static final String SRC_GEN_DIR_NAME  = "src-gen";

    public static final String LINK_TEST_UPLOAD_MODE = "Upload configuration file";
    public static final String NAME_CHOOSE_FILE_ELEMENT = "install_config";

    public static final String CLASS_PROJECT_USERS = "fa-user";
    public static final String CLASS_ADD_USER_BUTTON = "fa-plus";
    public static final String CLASS_HOME_PROJECT = "fa-home";
    // web element for show or hide all relis users
    public static final String CLASS_SHOW_ALL_USERS = "select2-selection__arrow";
    // web element for the list of relis users
    public static final String ID_RELIS_ALL_USERS = "select2-user_id-results";

    public static final String CLASS_SUCCESS_BUTTON ="btn-success";

    public static final String ID_PROJECT_USERS_TABLE = "datatable-responsive";
    public static final String CLASS_USERS_TABLES = "dataTable";


    public static final String ID_PROJECT_TABLE_USERS = "datatable-responsive";

    public static final String CLASS_EDIT_PROJECT_USER = "fa-edit";
    public static final String CLASS_REMOVE_PROJECT_USER = "fa-trash";

    public static final String CLASS_SIDEBAR_FOOTER_MENU = "sidebar-footer";


    /**
     * PAPERS MANAGEMENT
     */

    public static final String ID_ALL_PAPERS_TABLE = "datatable-responsive";
    public static final String LK_DELETE_ALL_PAPERS_BUTTON = "Delete all";
    public static final String LK_CONFIRM_DELETE_ALL_PAPERS = "Continue to delete";
    public static final String ID_PAPERS_INFOS = "datatable-responsive_info";
    public static final String  ID_NEXT_PAPERS_PAGE ="datatable-responsive_next";

    /***
     * VENUES MANAGEMENT
     */
    public static final String LK_VENUES = "Venues";
    public static final String CSS_ADD_VENUE = "a[title='Add a new venue']";
    public static final String ID_VENUE_FULL_NAME = "venue_fullName";
    public static final String ID_VENUE_YEAR_INPUT = "venue_year";

    /*****************************************************************************************
     *
     *
     *
     * USER ROLE AND ROLE WEB ELEMENT FOR A PROJECT
     *
     *
     * */


    // Web element
    public static final String ID_USER_ROLES_OPTIONS ="select2-user_role-container";
    public static final String ID_ALL_USER_ROLE_FOR_A_PROJECT = "select2-user_role-results";
    // different user role in a project
    public static final String REVIEWER_ROLE = "Reviewer";
    public static final String VALIDATOR_ROLE = "Validator";
    public static final String PROJECT_MANAGER_ROLE = "Project manager";
    public static final String GUEST_ROLE = "Guest";
    public static final String LK_CURRENT_PROJECT = "Project";
    public static final String ID_PROJECT_CONFIG_SELECT = "selected_config";
    public static final String CLOSED_PHASE = "Closed" ;
    public static final String LK_USERS_MENU = "Users";
    public static final String CSS_ADD_NEW_USER ="a[title='Add a new user']";
    public static final String NAME_USER_NAME_INPUT ="user_username" ;
    public static final String NAME_USER_EMAIL_INPUT = "user_mail";
    public static final String NAME_USER_PASSWORD_INPUT = "user_password";
    public static final String NAME_USER_PASSWORD_CONFIRMATION = "user_password_val";
    public static final String ID_USER_GROUP_UL = "select2-user_usergroup-results";
    public static final String CLASS_USER_GROUP_ELEMENT = "select2-selection__rendered";

    public static final String LK_AUTHORS_MENU = "Authors";
    public static final String LK_ALL  = "All";
    public static final String CSS_ADD_NEW_AUTHOR_BTN = "a[title='Add a new author']";
    public static final String ID_INPUT_AUTHOR_FULL_NAME = "author_name";

    /********************************************************************************
     *
     * model.Project tested
     */

    public static String model_transformation_project = "Model transformation";
    public static final String CSS_OPENED_PROJECT_NAME = ".x_title h3" ;
    public static final String BIBTEX_FILE1 = "src/main/resources/bibtex/my_pub.bib";
    public static String NAME_USER_FULL_NAME_INPUT = "user_name";

    private static final String RELIS_DIRECTORY = "relis_dev";
    private static String _WORKSPACE_FOLDER_PATH = RELIS_DIRECTORY +"/workspace/dslforge_workspace/";

    public static void set_relis_workspace_path(String path){

        _WORKSPACE_FOLDER_PATH = path;
    }

    public static  String get_workspace_path(){
        return _WORKSPACE_FOLDER_PATH;
    }


    public static String getProjectConfigFileName(String projet_id){

        return "classification_install_" + projet_id +".php";
    }
    public static String extract_project_id(String filenName){

        String res= "";

        int pos = Utility.indexOfNth(filenName,"_",2)+1;
        while ((pos < filenName.length()) && filenName.charAt(pos) != '.') res += filenName.charAt(pos++);

        return res;
    }
}
