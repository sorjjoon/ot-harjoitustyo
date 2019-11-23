/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.readers.Reader;
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
    
    @Test
    public void correctNumberOfRowsAdded() throws Exception{
        String path="src/test/test.txt";
        ArrayList<Fight> fights=Reader.readFile(path);
        assertEquals(5,fights.size());
        assertEquals(3301,fights.get(0).getRows().size());        
        assertEquals(7,fights.get(1).getRows().size());
    }
    
    @Test
    public void readRowsAreCorrect()throws Exception{
        String path="src/test/test2.txt";
        ArrayList<Fight> fights=Reader.readFile(path);
        ArrayList<Row> correct_rows=new ArrayList();
        correct_rows.add(new Row("[23:01:05.706] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)"));
        correct_rows.add(new Row("[23:01:05.727] [@Firaksîan] [@Firaksîan] [Force Charge {807750204391424}] [Event {836045448945472}: AbilityActivate {836045448945479}] ()"));
        correct_rows.add(new Row("[23:01:05.727] [@Firaksîan] [@Firaksîan] [Unstoppable {3585722166542336}] [ApplyEffect {836045448945477}: Unstoppable {3585722166542336}] ()"));
        correct_rows.add(new Row("[23:01:05.727] [@Firaksîan] [@Firaksîan] [Trauma (PVP) {632919265640448}] [ApplyEffect {836045448945477}: Trauma (PVP) {632919265640448}] ()"));
        correct_rows.add(new Row("[23:01:05.728] [@Firaksîan] [@Firaksîan] [Sprint {810670782152704}] [RemoveEffect {836045448945478}: Sprint {810670782152704}] ()"));
        correct_rows.add(new Row("[23:01:05.728] [@Firaksîan] [] [Recharge and Reload {814287144615936}] [Event {836045448945472}: AbilityInterrupt {836045448945482}] ()"));
        correct_rows.add(new Row("[23:06:39.670] [@Firaksîan] [@Shâløm Kappa] [Ravage {1261367470325760}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (3340* energy {836045448940874}) <8351>"));
        correct_rows.add(new Row("[23:06:39.910] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: ExitCombat {836045448945490}] (Mandalorian Battle Ring)"));
        correct_rows.add(new Row("[23:06:40.493] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)"));
        correct_rows.add(new Row("[23:06:40.497] [@Firaksîan] [@Firaksîan] [Sprint {810670782152704}] [RemoveEffect {836045448945478}: Sprint {810670782152704}] ()"));
        correct_rows.add(new Row("[23:06:40.497] [@Firaksîan] [@Firaksîan] [] [Restore {836045448945476}: rage point {836045448938497}] (1)"));
        correct_rows.add(new Row("[23:06:40.498] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )"));
        correct_rows.add(new Row("[23:06:41.492] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )"));
        correct_rows.add(new Row("[23:06:42.474] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )"));
        correct_rows.add(new Row("[23:06:42.987] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: ExitCombat {836045448945490}] (Mandalorian Battle Ring)"));
        int i=0;
        for(Fight f:fights){
            for(Row r:f.getRows()){
                assertEquals(correct_rows.get(i),r);
                i++;
            }
        }
    }
}
    
