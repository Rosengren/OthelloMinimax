package testSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)

@Suite.SuiteClasses({
        ControllerTest.class,
        ModelTest.class,
        ViewTest.class
})
public class othelloTestSuite {

}
