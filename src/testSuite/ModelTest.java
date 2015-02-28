package testSuite;

import othello.OthelloModel;

import org.junit.After;
import org.junit.Before;

public class ModelTest {

    OthelloModel model;

    @Before
    public void setUp() throws Exception {
        model = new OthelloModel();
    }



    @After
    public void tearDown() throws Exception {

    }
}