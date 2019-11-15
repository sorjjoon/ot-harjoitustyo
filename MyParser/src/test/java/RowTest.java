/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import myparser.myparser.Row;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author joona
 */
public class RowTest {
    
    public RowTest() {
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
    
    
    @Test
    public void timeStamp()throws Exception{
        Row row = new Row("[23:06:40.498] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )","2019-01-01 ");
        assertEquals(row.getTimestamp().toString(),"2019-01-01 23:06:40.498");
    }
}
