/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import myparser.myparser.Reader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author joona
 */
public class ReaderTest {
    
    public ReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
//    
    @Test
    public void dateFromFile()throws Exception{
        String date=Reader.dateFromPath("/home/joona/ohjelmointi/ot-harjoitustyo-master/documentation/Example-logs/combat_2019-08-21_22_51_05_510269.txt");
            assertEquals("2019-08-21 ",date);
    }
}
