/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import myparser.myparser.Fight;
import myparser.myparser.Row;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author joona
 */
public class FightTest {
    
    public FightTest() {
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
     @Test
     public void smallDuration()throws Exception {
         Row row1=new Row("[23:06:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>");
         Row row2=new Row("[23:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()");
         ArrayList<Row> test=new ArrayList();
         test.add(row1);
         test.add(row2);
         Fight fight=new Fight(test);
         assertEquals(fight.getDuration(),(long)1);
         
     }
     
     @Test
     public void bigDuration()throws Exception {
         Row row1=new Row("[22:05:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>");
         Row row2=new Row("[23:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()");
         ArrayList<Row> test=new ArrayList();
         test.add(row1);
         test.add(row2);
         Fight fight=new Fight(test);
         assertTrue(fight.getDuration()==(long)1+3.66e+6);
         
     }
     
     @Test
     public void bigDurationOverMidnight()throws Exception {
         Row row1=new Row("[22:05:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>");
         Row row2=new Row("[00:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()");
         ArrayList<Row> test=new ArrayList();
         test.add(row1);
         test.add(row2);
         Fight fight=new Fight(test);
         
         
         
         assertEquals((long)1+7.26e+6,fight.getDuration(),0);
         
     }
     
     
     
}
