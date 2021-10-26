import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import utils.Utility;

public class MainTest {

    public static void main(String[] args) {
//        TestListenerAdapter tla = new TestListenerAdapter();
//        TestNG testng = new TestNG();
//        testng.setTestClasses(new Class[] {ProjectTest.class});
//        testng.addListener(tla);
//        testng.run();
        Utility.showAllRelisUser();
    }
}
