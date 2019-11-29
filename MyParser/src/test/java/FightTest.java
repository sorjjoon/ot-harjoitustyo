/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Stats;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author joona
 */
public class FightTest {
    
    public FightTest() {
    }
    
    
    ArrayList<Row> test;
    
    //TODO figure why tf this isn't working
    @Before
    public void setUp() {
        ArrayList<Row> test=new ArrayList(); 
        Row rowtest=new Row("[23:07:25.988] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",1);
        test.add(rowtest);
        //System.out.println("moi");

    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     
     
     @Test
     public void FightOwner()throws Exception{
         Row row1= new Row("[23:07:25.988] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",1);
         Row row2=new Row("[22:05:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>",2);
         Row row3=new Row("[00:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()",3);
         
        ArrayList<Row> test=new ArrayList(); 
        Row rowtest=new Row("[23:07:25.988] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",4);
        test.add(rowtest);
         test.add(row1);
         test.add(row2);
         test.add(row3);
         Fight fight=new Fight(test);
         assertEquals("@Firaksîan",fight.getOwner());
     }
     
     @Test
     public void notEqualsFight()throws Exception{
         Fight fight1=Reader.readFile("src/test/test2.txt").get(0);
         Fight fight2=Reader.readFile("src/test/test2.txt").get(1);
                 assertFalse(fight1.equals(fight2));
     }
     @Test
     public void equalsFight()throws Exception{
         Fight fight1=Reader.readFile("src/test/test2.txt").get(0);
         Fight fight2=Reader.readFile("src/test/test2.txt").get(0);
                 assertEquals(fight1,fight2);
     }
     
     
     
}
