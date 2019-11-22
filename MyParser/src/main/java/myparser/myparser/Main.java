/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser;

import database.Database;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Stats;
import myparser.myparser.ui.Textui;
import myparser.myparser.ui.ui;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args)throws Exception {
        Database moi=new Database();
        
//        moi.reset();
        
        String path="/home/joona/ohjelmointi/ot-harjoitustyo-master/documentation/Example-logs/combat_2019-08-18_22_33_23_136322.txt";
        ArrayList<Fight> fights = Reader.readFile(path);
        Fight fight=fights.get(0);
        moi.addFight(fight, Reader.dateFromPath(path), "test","combat_2019-08-18_22_33_23_136322.txt");
////    
        ArrayList<String> hei=moi.getSavedLogs();
        for(String s:hei){
            System.out.println(s);
        }
        moi.close();
//        Test.main(args);
//            ui textui=new Textui();
//            textui.run();
//        
        
        
//        while(true){
//        try{
//            System.out.println("Hi, this currently a really bad ui (and it's the main method cause I'm lazy, because this ui is basicly a placeholder), but haven't had time to work on a proper ui yet, and I'm not sure if this week we needed this so you can test the project");
//            System.out.println("Give me the path to a log file and I'll print out some sample stats for it (you can find example logs in documentation)");
//            Scanner reader = new Scanner(System.in);
//            String path=reader.nextLine();
//            ArrayList<Fight> fights_in_the_log = Reader.readFile(path);
//            System.out.println("There were "+fights_in_the_log.size()+" different fights in this log, I'll print some stats for you");
//            int k=1;
//            for(Fight f:fights_in_the_log){
//                ArrayList<Row> rows =f.getRows();
//                for(Row r:rows){
//                    if(r.getEventtype()==null&&r.getEffecttype()==null){
//                        System.out.println(r);
//                    }
//                }
//            }
//                System.out.println("----------------------------------");
//                System.out.println("Fight "+k);
//                long duration=Stats.getDuration(f)/1000;
//                System.out.println("Duration "+duration+" seconds");
//                System.out.println("Total dmg done "+Stats.getAllDamageByOwner(f));
//                System.out.println("dps " +Stats.dps(f));
//                System.out.println("Total dmg taken "+Stats.getAllDamageToOwner(f));
//                System.out.println("dtps "+Stats.dtps(f));
//                System.out.println("Total healing done "+Stats.getAllHealingByOwner(f));
//                System.out.println("hps "+Stats.hps(f));
//                
//                System.out.println("Total healing recieved "+Stats.getAllHealingToOwner(f));
//                System.out.println("htps "+Stats.htps(f));
//                k++;
//            }
//            System.out.println("-------------------------------");
//            System.out.println("Note, these are not all the stats I can generate atm, but just the ones I can show you in a nice way in my terrible ui :)");
//                
//            
//        }catch(FileNotFoundException e){
//            System.out.println("File not found, give a proper path");
//            System.out.println("");
//            System.out.println("");
//        }
//
//        catch(Exception e){
//            StringWriter sw = new StringWriter();
//            PrintWriter pw = new PrintWriter(sw);
//            e.printStackTrace(pw);
//            String sStackTrace = sw.toString(); 
//            System.out.println(sStackTrace);
//
//        }
//        }   
}
}
