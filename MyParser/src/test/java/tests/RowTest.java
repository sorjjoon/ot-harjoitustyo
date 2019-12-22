package tests;

import myparser.myparser.domain.Row;
import myparser.myparser.types.DamageType;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

    @Test
    public void rowEquals1() {
        Row row1 = new Row("[23:03:45.393] [@Blonde'kate] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (629*)", 1);
        Row row2 = new Row("[23:03:45.393] [@Blonde'kate] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (629*)", 1);
        assertEquals(row1, row2);
    }

    @Test
    public void rowNumbers() {
        Row row1 = new Row("[23:03:45.393] [@Blonde'kate] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (629*)", 1);
        Row row2 = new Row("[23:03:45.393] [@Blonde'kate] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (629*)", 1);
        assertEquals(row1.getRowNumber(), row2.getRowNumber());

    }

    @Test
    public void rowNotEquals1() {
        Row row1 = new Row("[23:06:40.498] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )", 1);
        Row row2 = new Row("[23:03:45.393] [@Blonde'kate] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (629*)", 1);
        assertFalse(row1.equals(row2));
    }

    @Test
    public void rowEquals2() {
        Row row1 = new Row("[22:52:09.959] [@Firaksîan] [@Firaksîan] [Chilling Scream {3436051146211328}] [Event {836045448945472}: AbilityActivate {836045448945479}] ()", 1);
        Row row2 = new Row("[22:52:09.959] [@Firaksîan] [@Firaksîan] [Chilling Scream {3436051146211328}] [Event {836045448945472}: AbilityActivate {836045448945479}] ()", 1);
        assertEquals(row1, row2);
    }

    @Test
    public void rowEqualsdmgCritShield() throws Exception {
        Row row1 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertEquals(row1, row2);

    }

    @Test
    public void rowNotEqualsdmgCritShieldTimeStampChange() throws Exception {
        //changed timestamp on row1
        Row row1 = new Row("[23:22:32.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertFalse(row1.equals(row2));

    }

    @Test
    public void rowNotEqualsdmgCritShieldNameChange() throws Exception {
        //changed name on row1
        Row row1 = new Row("[23:22:31.876] [@Mamalookiamamagiian] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertFalse(row1.equals(row2));

    }

    @Test
    public void rowNotEqualsdmgCritShieldAbilityNameChange() throws Exception {
        //changed ability name on row1
        Row row1 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Foce Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertFalse(row1.equals(row2));

    }

    @Test
    public void rowNotEqualsdmgCritShieldDmgChange() throws Exception {
        //changed damage on row1
        Row row1 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (42* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertFalse(row1.equals(row2));

    }

    @Test
    public void rowNotEqualsdmgCritShieldTargetChange() throws Exception {
        //changed target from row 1 (note changed the î to i) 
        Row row1 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksian] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertFalse(row1.equals(row2));

    }

    @Test
    public void rowNotEqualsdmgName() throws Exception {
        //changed target from row 1 (note changed the î to i) 
        Row row1 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        Row row2 = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);

        assertFalse(row1.equals(row2));

    }

    @Test
    public void dmgType() {
        Row row = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        assertEquals(DamageType.energy, row.getDmgType());
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
    }

    @Test
    public void dmgType2() {
        Row row = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* elemental {836045448940874} {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        assertEquals(DamageType.elemental, row.getDmgType());
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
    }

    @Test
    public void timeStamp() throws Exception {
        Row row = new Row("[23:06:40.498] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )", 1);
        assertEquals(row.getTimestamp().toString(), "23:06:40.498");
        assertEquals(row.getDmgHeal(), 1382);

        assertFalse(row.isShielded());
    }

    @Test
    public void healWithCrit() throws Exception {
        Row row = new Row("[23:03:45.393] [@Blonde'kate] [@Firaksîan] [Kolto Infusion {1014376786034688}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (629*)", 1);
        assertEquals(row.getDmgHeal(), 629);
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
    }

    @Test
    public void resist() throws Exception {
        Row row = new Row("[23:16:08.901] [@Mamalookiamamagician] [@Firaksîan] [Crushed (Demolish) {3400617666019619}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (0 -resist {836045448945507}) <1>", 1);
        assertEquals(row.getDmgHeal(), 0);
        assertFalse(row.isCrit());
        assertTrue(row.isMiss());
        assertFalse(row.isShielded());
    }

    @Test
    public void miss() throws Exception {
        Row row = new Row("[23:01:49.828] [@Zyy'r] [@Firaksîan] [Zealous Leap {1263330270380032}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (0 -miss {836045448945502}) <1>", 1);
        assertEquals(row.getDmgHeal(), 0);
        assertFalse(row.isCrit());
        assertTrue(row.isMiss());
        assertFalse(row.isShielded());
    }

    @Test
    public void dodge() throws Exception {
        Row row = new Row("[23:01:32.228] [@Firaksîan] [@Shâløm Kappa] [Aegis Assault {3438288824172544}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (0 -dodge {836045448945505}) <1>", 1);
        assertEquals(row.getDmgHeal(), 0);
        assertFalse(row.isCrit());
        assertTrue(row.isMiss());
        assertFalse(row.isShielded());
    }

    @Test
    public void healNoCrit() throws Exception {
        Row row = new Row("[23:03:45.394] [@Blonde'kate] [@Firaksîan] [Kolto Probe {814832605462528}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (2042)", 1);
        assertEquals(row.getDmgHeal(), 2042);
        assertFalse(row.isCrit());
        assertFalse(row.isMiss());
        assertFalse(row.isShielded());
    }

    @Test
    public void healCrit2() throws Exception {
        Row row = new Row("[22:59:39.071] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1821*) <819>", 1);
        assertEquals(row.getDmgHeal(), 1821);
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
        assertFalse(row.isShielded());
    }

    @Test
    public void dmgCritShield() throws Exception {
        Row row = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        assertEquals(742, row.getDmgHeal());
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
        assertTrue(row.isShielded());
    }

    @Test
    public void threat() {
        Row row = new Row("[23:22:31.876] [@Mamalookiamamagician] [@Firaksîan] [Force Lightning {808252715565056}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (742* energy {836045448940874} -shield {836045448945509} (515 absorbed {836045448945511})) <742>", 1);
        assertEquals(742, row.getThreat());

    }

    @Test
    public void dmgNoCrit() throws Exception {
        Row row = new Row("[22:56:49.762] [@Firaksîan] [@Psdjdbbuhulfwmv] [Crushing Blow {2211062048882688}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (5087 energy {836045448940874})", 1);
        assertEquals(row.getDmgHeal(), 5087);
        assertFalse(row.isMiss());
        assertFalse(row.isShielded());
        assertFalse(row.isCrit());
    }

    @Test
    public void dmgCrit2Shielded() throws Exception {
        Row row = new Row("[23:06:35.017] [@Firaksîan] [@Shâløm Kappa] [Force Charge {807750204391424}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1147* energy {836045448940874} -shield {836045448945509} (3148 absorbed {836045448945511})) <2867>", 1);
        assertEquals(row.getDmgHeal(), 1147);
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
        assertTrue(row.isShielded());
    }

    @Test
    public void dmgWithCrit() throws Exception {
        Row row = new Row("[22:57:22.913] [@Firaksîan] [@Yuugì] [Piercing Chill {3909511161053184}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1001* elemental {836045448940875}) <2502>", 1);
        assertEquals(row.getDmgHeal(), 1001);
        assertTrue(row.isCrit());
        assertFalse(row.isMiss());
        assertFalse(row.isShielded());
    }
    
    @Test
    public void threat2(){
       Row row = new Row("[23:06:24.365] [@Blonde'kate] [@Firaksîan] [Kolto Probe {814832605462528}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (2947*) <2502>", 1);
       assertEquals(2502, row.getThreat());  
    }

    @Test
    public void healNoCrit2() throws Exception {
        Row row = new Row("[23:22:30.868] [@Predori] [@Firaksîan] [Revivification {808703687131136}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (822))", 1);
        assertEquals("@Predori", row.getSource());
        assertEquals("@Firaksîan",row.getTarget());
        assertEquals(row.getDmgHeal(), 822);
        assertFalse(row.isCrit());
        assertFalse(row.isMiss());
        assertFalse(row.isShielded());
    }

    @Test
    public void sourceAndTarget() {

        Row row = new Row("[23:22:30.868] [@Predori] [@Firaksîan] [Revivification {808703687131136}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (822))", 1);
        assertEquals("@Predori", row.getSource());
        assertEquals("@Firaksîan", row.getTarget());
    }

    @Test
    public void sourceAndTarget2() {

        Row row = new Row("[23:01:47.019] [@Firaksîan] [@Zyy'r] [Crushing Blow {2211062048882688}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (4548* energy {836045448940874}) <14781>", 1);
        assertEquals("@Firaksîan", row.getSource());
        assertEquals("@Zyy'r", row.getTarget());
    }

    @Test
    public void sourceAndTargetWeirdName() {

        Row row = new Row("[23:15:31.711] [@Firaksîan] [@Who ?¶wintrader] [Force Charge {807750204391424}] [ApplyEffect {836045448945477}: Immobilized (Physical) {807750204391700}] ()", 1);
        assertEquals("@Firaksîan", row.getSource());
        assertEquals("@Who ?¶wintrader", row.getTarget());
    }
    

}
