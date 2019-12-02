/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.time.LocalDate;
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
        LocalDate date=Reader.dateFromName("combat_2019-08-21_22_51_05_510269.txt");
            assertEquals("2019-08-21",date.toString());
    }
    
    @Test
    public void correctNumberOfRowsAdded() throws Exception{
        String path="src/test/test.txt";
        ArrayList<Fight> fights=Reader.readFile(new File(path));
        assertEquals(4,fights.size());
        assertEquals(3425,fights.get(0).getRows().size());        
        }
    
    @Test
    public void rowNumberingWithEmptyRows()throws Exception{
        String path="src/test/test.txt";
        ArrayList<Fight> fights=Reader.readFile(new File(path));
        int i=149;
        for(Row r:fights.get(0).getRows()){
//            System.out.println(r);
//            System.out.println(i +" , "+r.getRowNumber());
            if(i==3567){
                i=3576;
            }
            assertEquals(i,r.getRowNumber());

                
            i++;
            
        }
 
        
        i=3648;
        for(Row r:fights.get(1).getRows()){
            
            assertEquals(i,r.getRowNumber());
            i++;
        }
        
    }
    
    @Test
    public void readRowsAreCorrect()throws Exception{
        String path="src/test/test2.txt";
        ArrayList<Fight> fights=Reader.readFile(new File(path));
        ArrayList<Row> correct_rows=new ArrayList();
        correct_rows.add(new Row("[23:01:05.706] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",1));
        correct_rows.add(new Row("[23:01:05.727] [@Firaksîan] [@Firaksîan] [Force Charge {807750204391424}] [Event {836045448945472}: AbilityActivate {836045448945479}] ()",2));
        correct_rows.add(new Row("[23:01:05.727] [@Firaksîan] [@Firaksîan] [Unstoppable {3585722166542336}] [ApplyEffect {836045448945477}: Unstoppable {3585722166542336}] ()",3));
        correct_rows.add(new Row("[23:01:05.727] [@Firaksîan] [@Firaksîan] [Trauma (PVP) {632919265640448}] [ApplyEffect {836045448945477}: Trauma (PVP) {632919265640448}] ()",4));
        correct_rows.add(new Row("[23:01:05.728] [@Firaksîan] [@Firaksîan] [Sprint {810670782152704}] [RemoveEffect {836045448945478}: Sprint {810670782152704}] ()",5));
        correct_rows.add(new Row("[23:01:05.728] [@Firaksîan] [] [Recharge and Reload {814287144615936}] [Event {836045448945472}: AbilityInterrupt {836045448945482}] ()",6));
        correct_rows.add(new Row("[23:06:39.670] [@Firaksîan] [@Shâløm Kappa] [Ravage {1261367470325760}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (3340* energy {836045448940874}) <8351>",7));
        correct_rows.add(new Row("[23:06:39.910] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: ExitCombat {836045448945490}] (Mandalorian Battle Ring)",8));
        correct_rows.add(new Row("[23:07:40.493] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",9));
        correct_rows.add(new Row("[23:06:40.497] [@Firaksîan] [@Firaksîan] [Sprint {810670782152704}] [RemoveEffect {836045448945478}: Sprint {810670782152704}] ()",10));
        correct_rows.add(new Row("[23:06:40.497] [@Firaksîan] [@Firaksîan] [] [Restore {836045448945476}: rage point {836045448938497}] (1)",11));
        correct_rows.add(new Row("[23:06:40.498] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )",12));
        correct_rows.add(new Row("[23:06:41.492] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )",13));
        correct_rows.add(new Row("[23:06:42.474] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )",14));
        correct_rows.add(new Row("[23:06:42.987] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: ExitCombat {836045448945490}] (Mandalorian Battle Ring)",15));
        int i=0;
        for(Fight f:fights){
            for(Row r:f.getRows()){
                assertEquals(correct_rows.get(i),r);
                i++;
            }
        }
    }
}
    
