/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import javafx.event.Event;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.stats.Stats;
import static myparser.myparser.types.Damagetype.elemental;
import static myparser.myparser.types.Damagetype.energy;
import static myparser.myparser.types.Eventtype.AbilityActivate;
import static myparser.myparser.types.Eventtype.EnterCombat;
import static myparser.myparser.types.Type.ApplyEffect;
import org.h2.tools.Restore;

import static org.junit.Assert.assertEquals;
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
public class StatsTest {
    private Fight fight;
    
    public StatsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp()throws Exception {
        ArrayList<Row> rows=new ArrayList();
        rows.add(new Row("[22:36:00.00] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Rishi Cove Arena)",1));
        rows.add(new Row("[22:36:57.931] [@Firaksîan] [@Firaksîan] [Force Charge {807750204391424}] [Event {836045448945472}: AbilityActivate {836045448945479}] ()",1));
        rows.add(new Row("[22:36:57.932] [@Firaksîan] [@Firakn] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (2*)",1));
        rows.add(new Row("[22:36:57.934] [@Firaksîan] [@Tecktime] [Force Charge {807750204391424}] [ApplyEffect {836045448945477}: Immobilized (Physical) {807750204391700}] ()",1));
        rows.add(new Row("[22:36:58.733] [@Firaksîan] [@Tecktime] [Force Charge {807750204391424}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1497 energy {836045448940874} -shield {836045448945509} (1213 absorbed {836045448945511})) <3742>",1));
        rows.add(new Row("[22:36:58.981] [@Firaksîan] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (92*)",1));
        rows.add(new Row("[22:36:58.981] [@Tecktime] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (9021*)",1));
        rows.add(new Row("[22:36:58.981] [@Tecktime] [@Firaksîan] [Combust {991390121066496}] [ApplyEffect {836045448945477}: Trauma (Physical) {991390121066787}] ()",1));
        rows.add(new Row("[22:36:59.557] [@something] [@Fisîan] [Lethal Strike {3403731517308928}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (8433* internal {836045448940876}) <8433>",2));

        rows.add(new Row("[22:36:59.134] [@Firaksîan] [@Tecktime] [Flame Burst {814222720106496}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (3452 elemental {836045448940875}) <8630>",1));
        rows.add(new Row("[22:36:59.422] [R8-X8 {4208664223154176}:17970551675746] [@Firaksîan] [Corrosive Dart {3465720780292096}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1969 internal {836045448940876}) <1969>",2));
        rows.add(new Row("[22:36:59.557] [@something] [@Firaksîan] [Lethal Strike {3403731517308928}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (8433* internal {836045448940876}) <8433>",2));
        rows.add(new Row("[22:37:00.000] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: ExitCombat {836045448945490}] (Rishi Cove Arena)",1));
        this.fight=new Fight(rows);
    }
    
    @After
    public void tearDown() {
    }
    @Test
    public void allDamageTaken(){
        assertEquals(10402,Stats.getAllDamageToOwner(fight));
    }
    @Test
    public void dtps(){
        assertEquals(173.3666666666,Stats.dtps(fight),0.1);
    }
     @Test
     public void smallDuration()throws Exception {
         Row row1=new Row("[23:06:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>",1);
         Row row2=new Row("[23:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()",2);
         
        ArrayList<Row> test=new ArrayList(); 
        Row rowtest=new Row("[23:06:43.047] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",1);
        test.add(rowtest);
         test.add(row1);
         test.add(row2);
         Fight fight=new Fight(test);
         assertEquals("@Firaksîan",fight.getOwner());
         assertEquals(Stats.getDurationMs(fight),(long)1);
         
     }
     
     @Test
     public void bigDuration()throws Exception {
         Row row1=new Row("[22:05:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>",1);
         Row row2=new Row("[23:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()",2);
         
        ArrayList<Row> test=new ArrayList(); 
        Row rowtest=new Row("[22:05:43.047] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",1);
        test.add(rowtest);
         test.add(row1);
         test.add(row2);
         Fight fight=new Fight(test);
         assertEquals("@Firaksîan",fight.getOwner());
         assertTrue(Stats.getDurationMs(fight)==(long)1+3.66e+6);
         
     }
     
     @Test
     public void bigDurationOverMidnight()throws Exception {
         Row row1=new Row("[22:05:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>",1);
         Row row2=new Row("[00:06:43.048] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Invigorated {815232037421555}] ()",2);
         
        ArrayList<Row> test=new ArrayList(); 
        Row rowtest=new Row("[22:05:43.047] [@Firaksîan] [@Firaksîan] [] [Event {836045448945472}: EnterCombat {836045448945489}] (Mandalorian Battle Ring)",1);
        test.add(rowtest);
         test.add(row1);
         test.add(row2);
         Fight fight=new Fight(test);
         
         
         
         assertEquals((long)1+7.26e+6,Stats.getDurationMs(fight),0);
         assertEquals("@Firaksîan",fight.getOwner());
     }
    @Test
    public void allDamageFromOwner()throws Exception{
        ArrayList<Row> rows=new ArrayList();
        assertEquals(4949,Stats.getAllDamageByOwner(fight));
    } 
    
    @Test
    public void allHealingToOwner()throws Exception{
     
        assertEquals(9113,Stats.getAllHealingToOwner(fight));
    }
    @Test
    public void htps()throws Exception{
        assertEquals((double)9115/60,Stats.htps(fight),0.1);
    }
    @Test
    public void allHealingByOwner()throws Exception{
        assertEquals(94,Stats.getAllHealingByOwner(fight));
    }
 
    @Test
    public void hps()throws Exception{
        double dps=94/60.0;
        assertEquals(dps,Stats.hps(fight),0.1);
    }
 
    @Test
    public void dps()throws Exception{
        double dps=4949/60.0;
        
        assertEquals(dps,Stats.dps(fight),0.1);
    }
}

