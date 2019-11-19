/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser;

import myparser.myparser.readers.Reader;
import myparser.myparser.domain.Fight;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import static myparser.myparser.stats.Stats.*;
import java.util.Scanner;
import myparser.myparser.stats.Stats;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args) {
        try{
            System.out.println("Hi, this currently a really bad ui, but haven't had time to work on it, and I'm not sure if this week we needed this so you can test the project");
            System.out.println("Give me the path to a log file and I'll print out some sample stats for it (you can find example logs in documentation)");
            Scanner reader = new Scanner(System.in);
            String path=reader.nextLine();
            ArrayList<Fight> fights_in_the_log = Reader.readFile(path);
            System.out.println("There were "+fights_in_the_log.size()+" different fights in this log, I'll print some stats for you");
            int k=1;
            for(Fight f:fights_in_the_log){
                System.out.println("----------------------------------");
                System.out.println("Fight "+k);
                System.out.println("Total dmg done "+Stats.getAllDamageByOwner(f));
                System.out.println("dps " +Stats.dps(f));
                System.out.println("Total dmg taken "+Stats.getAllDamageToOwner(f));
                System.out.println("dtps "+Stats.dtps(f));
                System.out.println("Total healing done "+Stats.getAllHealingByOwner(f));
                System.out.println("hps "+Stats.hps(f));
                System.out.println("");
                k++;
            }
            
            ;
            
}
        catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); 
            System.out.println(sStackTrace);

        }
    
}
}
