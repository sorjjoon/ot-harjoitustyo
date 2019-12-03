/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;


//These are useless atm, but here for testing
import database.Database;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Stats;
import myparser.myparser.ui.Analysis;
import myparser.myparser.ui.Gui;
import myparser.myparser.ui.Textui;
import myparser.myparser.ui.ui;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args)throws Exception{
        
        try {
            Gui.main(args);
            
        }catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.exit(0);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); 
            System.out.println(sStackTrace);

        }   
    }
}
