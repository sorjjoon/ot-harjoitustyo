/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args) {
        try{
            //Type moi=Type.valueOf("name");
            //ArrayList<Integer> moi = new ArrayList();
//            Row row=new Row("[23:06:43.047] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (1014) <456>");
            String date=Reader.dateFromPath("/home/joona/ohjelmointi/ot-harjoitustyo-master/documentation/Example-logs/combat_2019-08-21_22_51_05_510269.txt");
//      //  String date=Reader.dateFromPath("/home/joona/ohjelmointi/ot-harjoitustyo-master/documentation/Example-logs/combat_201oadiljk.txt");
//            Row row=new Row("[23:06:35.017] [@Firaksîan] [@Shâløm Kappa] [Force Charge {807750204391424}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1147* energy {836045448940874} -shield {836045448945509} (3148 absorbed {836045448945511})) <2867>");
//            System.out.println(row.getDmg_heal());
//            System.out.println(row.isCrit());
            ArrayList<Fight> fights=Reader.readFile("/home/joona/ohjelmointi/ot-harjoitustyo-master/documentation/Example-logs/combat_2019-08-21_22_51_05_510269.txt");
            int sum=0;
            for(Fight f : fights){
                HashMap<String,Integer> moi=f.divideDamageTakenByOwner();
                for(String s:moi.keySet()){
//                    System.out.println(moi.get(s));
                    sum+=moi.get(s);
                }
                System.out.println(sum-f.getAllDamageToOwner());
                sum=0;
            }
//            for(int i=0;i<50;i++){
//                System.out.println("---------------------------");
//                
//            }
//                
//            }
}
        catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString(); // stack trace as a string
            System.out.println(sStackTrace);

        }
    
}
}
