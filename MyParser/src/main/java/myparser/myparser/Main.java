/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser;

import ui.Textui;
import ui.ui;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args) {
            ui textui=new Textui();
            textui.run();
        
        
        
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
//                break;
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
