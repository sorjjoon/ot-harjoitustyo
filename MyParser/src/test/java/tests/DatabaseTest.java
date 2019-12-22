package tests;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import database.Database;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import myparser.myparser.domain.Fight;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Tuple;
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
public class DatabaseTest {
    private Database database;
    public DatabaseTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
        
    }
    
    @Before
    public void setUp() throws SQLException{
        
        if(this.database==null){
            this.database=new Database("./src/test/test_database.mv.db");
            this.database.reset();
        }
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void insertAndReadFromDatabase()throws Exception{

        String path="src/test/test.txt";
        ArrayList<Fight> fights=Reader.readFile(new File(path));
        this.database.addListOfFights(fights, LocalDate.now(), "test", "test.txt");
        ArrayList<Fight> fights2=database.getFightsFromLog("test.txt");
        for(int i=0;i<fights.get(0).getRows().size();i++){
            assertEquals(fights.get(0).getRows().get(i), fights2.get(0).getRows().get(i));
        }
        
        
    }
    
    @Test
    public void listOfEntries()throws Exception{
        
        String path="src/test/test.txt";
        ArrayList<Fight> fights=Reader.readFile(new File(path));
        this.database.addListOfFights(fights, LocalDate.now(), "test", "test.txt");
        assertEquals(true,this.database.getSavedLogsAndMessages()[0].contains("test.txt"));
        assertEquals(true,this.database.getSavedLogsAndMessages()[1].contains("test"));
        
    }
}
