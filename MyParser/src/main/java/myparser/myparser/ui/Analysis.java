/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import myparser.myparser.domain.Fight;
import myparser.myparser.readers.Reader;
import myparser.myparser.stats.Stats;
import myparser.myparser.stats.Tuple;

/**
 *
 * this class generates all the data shown on the gui
 */
public class Analysis {
    private final String owner;
    private final String allDmgDone;
    private final String allHealDone;
    private final String allDmgTaken;
    private final String start;
    private final String end;
    private final String APM;
    private final String duration;
    private final String dps;
    private final String hitPrecentage;
    private final String dmgCritPrecentage;
    private final String biggestHit;
    private final String averageDmg;
    private final String hits;
    private final String dtps;
    private final String hitsTaken;
    private final String avgTakenHit;
    private final String bigTakenHit;
    private final String takenCrits;
    private final String takenHitPrecentage;
    private final String hps;
    private final String bigHeal;
    private final String healCount;
    private final String avgHeal;
    private final String healCritPrecentage;
    private final String allHealTaken;
    private final String averageHealTaken;
    private final String htps;
    private final String healTakenCount;
    private final String bigTakenHeal;
    private final String takenHealCritPrecentage;
    private final String totalThreat;
    private final String tps;
    private final HashMap<String,Integer> dmgBreakdownByTarget;
    
    
    private  HashMap<String,String> dmgAvgBreakdownByTarget;
    private  HashMap<String,String> dpsBreakdownByTarget;
    private HashMap<String,String> dmgHitPrecentageAgainstTarget;
    private  HashMap<String,String> dmgCritBreakdownByTarget;
    private  HashMap<String,String> dmgBigBreakdownByTarget;
    private  HashMap<String,String> dmgHitsDoneAgainstTarget;
    private HashMap<String,String> dmgTotalPrecentageByTarget;
    
    
    private HashMap<String,Integer> dmgAbilityBreakdown;
    
    private HashMap<String,HashMap<String,Integer>> dmgAbilityBreakdownByTarget;
    
    
    private HashMap<LocalTime,Integer> dmgCumulative;
    
    private HashMap<String,HashMap<LocalTime,Integer>> dmgCumulativeBreakdownByTarget;
    
    private final ArrayList<Tuple> totalDpsByTime;
    private HashMap<String,ArrayList<Tuple>> totaldpsByTimeBreakdownByTarget;
    
    private final DecimalFormat df;
    private final ArrayList<Tuple<LocalTime, Double>> momentaryDpsByTime;
    private HashMap<String,ArrayList<Tuple<LocalTime,Double>>> momentarydpsByTimeBreakdownByTarget;
    
  
    public Analysis(Fight f){
        //Overview
        this.df = new DecimalFormat("#.##");
        this.owner=f.getOwner().substring(1, f.getOwner().length());    //Removing @ from char name
        this.start=f.getStart().toString();
        this.end=f.getEnd().toString();
        this.APM=df.format(Stats.APM(f));
        this.duration=String.valueOf((double)Stats.getDurationMs(f)/1000);
        //Damage
        this.allDmgDone=String.valueOf(Stats.getAllDamageByOwner(f));
        this.dps=df.format(Stats.dps(f));
        this.hitPrecentage=df.format((1-Stats.missPrecentage(f))*100);
        this.dmgCritPrecentage=df.format(Stats.dmgCritPrecentage(f)*100);
        this.biggestHit=df.format(Stats.biggestHit(f));
        this.averageDmg=df.format(Stats.averageHit(f));
        this.hits=String.valueOf(Stats.numberOfHits(f));
        
        //dmg taken
        this.dtps=df.format(Stats.dtps(f));
        this.allDmgTaken=String.valueOf(Stats.getAllDamageToOwner(f));
        this.hitsTaken=String.valueOf(Stats.numberOfTakenHits(f));
        this.avgTakenHit=df.format(Stats.averageTakenHit(f));
        this.bigTakenHit=String.valueOf(Stats.biggestTakenHit(f));
        this.takenCrits=df.format(Stats.takenCritPrecentage(f)*100);
        this.takenHitPrecentage=df.format((1-Stats.takenMissPrecentage(f))*100);
        
        //healing done
        
        this.allHealDone=String.valueOf(Stats.getAllHealingByOwner(f));
        this.hps=df.format(Stats.hps(f));
        this.healCount=String.valueOf(Stats.numberOfHeals(f));
        this.avgHeal=df.format(Stats.averageHeal(f));
        this.bigHeal=String.valueOf(Stats.biggestHeal(f));
        this.healCritPrecentage=df.format(Stats.healCritPrecentage(f)*100);

        //Healing recieved
         this.allHealTaken=String.valueOf(Stats.getAllHealingToOwner(f));
         this.htps=df.format(Stats.htps(f));
         this.bigTakenHeal=String.valueOf(Stats.biggestTakenHeal(f));
         this.takenHealCritPrecentage=df.format(Stats.takenHealCritPrecentage(f)*100);
         this.averageHealTaken=df.format(Stats.averageTakenHeal(f));
         this.healTakenCount=String.valueOf(Stats.numberOfHealsTaken(f));
         
         //Threat
         this.totalThreat=String.valueOf(Stats.totalThreat(f));
         this.tps=df.format(Stats.tps(f));
         
         
         //Dmg Tab, creating hashmaps for all targets
         this.dmgBreakdownByTarget=Stats.divideDamageDealtByTarget(f);
         this.dpsBreakdownByTarget=dpsHashMap(f, dmgBreakdownByTarget, df);
         
         this.dmgBigBreakdownByTarget=new HashMap();         
         
         for(String s :this.dmgBreakdownByTarget.keySet()){
             this.dmgBigBreakdownByTarget.put(s, Stats.biggestHitAgainstTarget(f, s).toString());
         }
         
         dmgAvgBreakdownByTarget=new HashMap();
         for(String s :this.dmgBreakdownByTarget.keySet()){
             this.dmgAvgBreakdownByTarget.put(s, df.format(Stats.averageHitAgainstTarget(f,s)));
         }
         
         dmgCritBreakdownByTarget=new HashMap();
         for(String s : dmgBreakdownByTarget.keySet()){
            this.dmgCritBreakdownByTarget.put(s, df.format(Stats.dmgCritPrecentageAgainstTarget(f,s)*100));
         
         }
         
         dmgHitsDoneAgainstTarget=new HashMap();
         for(String s : dmgBreakdownByTarget.keySet()){
            this.dmgHitsDoneAgainstTarget.put(s, String.valueOf(Stats.numberOfHitsAgainstTarget(f, s)));
         
         }
         
        dmgHitPrecentageAgainstTarget=new HashMap();
         for(String s : dmgBreakdownByTarget.keySet()){
            this.dmgHitPrecentageAgainstTarget.put(s, df.format((1-Stats.missPrecentageAgainstTarget(f, s))*100));
         
         }
         
         dmgTotalPrecentageByTarget=new HashMap();
         for(String s : dmgBreakdownByTarget.keySet()){
             double precentage = (double) dmgBreakdownByTarget.get(s)/Integer.valueOf(allDmgDone);
             precentage*=100;
             dmgTotalPrecentageByTarget.put(s,df.format(precentage));
         }
         
         
         dmgAbilityBreakdown=Stats.divideDamageDealtByAbility(f);
         
         dmgAbilityBreakdownByTarget=new HashMap();
         for(String s :dmgBreakdownByTarget.keySet()){
             dmgAbilityBreakdownByTarget.put(s, Stats.divideDamageDealtAgainstTargetByAbility(f, s));
         }
         
         dmgCumulative=Stats.cumulativeDmgDone(f);
         
         dmgCumulativeBreakdownByTarget=new HashMap();
         
         for(String s :dmgBreakdownByTarget.keySet()){
             dmgCumulativeBreakdownByTarget.put(s, Stats.cumulativeDmgDoneAgainstTarget(f, s));
         }
         
         totalDpsByTime=Stats.getTotalDpsByTime(f);
         
         
         
         totaldpsByTimeBreakdownByTarget=new HashMap();
                  for(String s :dmgBreakdownByTarget.keySet()){
             totaldpsByTimeBreakdownByTarget.put(s, Stats.getTotalDpsByTimeAgainstTarget(f, s));
         }
                  
        momentaryDpsByTime=Stats.momentaryDps(f);
         
         
         momentarydpsByTimeBreakdownByTarget=new HashMap();
         
         for(String s :dmgBreakdownByTarget.keySet()){
             momentarydpsByTimeBreakdownByTarget.put(s, Stats.momentaryDpsAgainstTarget(f, s));
         }
         
        
    }

    public ArrayList<Tuple<LocalTime, Double>> getMomentaryDpsByTime() {
        return momentaryDpsByTime;
    }

    public HashMap<String, ArrayList<Tuple<LocalTime, Double>>> getMomentarydpsByTimeBreakdownByTarget() {
        return momentarydpsByTimeBreakdownByTarget;
    }

    public HashMap<String, ArrayList<Tuple>> getTotaldpsByTimeBreakdownByTarget() {
        return totaldpsByTimeBreakdownByTarget;
    }

    public ArrayList<Tuple> getTotalDpsByTime() {
        return totalDpsByTime;
    }

    public DecimalFormat getDf() {
        return df;
    }
    
    

    public HashMap<LocalTime, Integer> getDmgCumulative() {
        return dmgCumulative;
    }

    public HashMap<String, HashMap<LocalTime, Integer>> getDmgCumulativeBreakdownByTarget() {
        return dmgCumulativeBreakdownByTarget;
    }
    
    
    public HashMap<String, Integer> getDmgAbilityBreakdown() {
        return dmgAbilityBreakdown;
    }

    public HashMap<String, HashMap<String, Integer>> getDmgAbilityBreakdownByTarget() {
        return dmgAbilityBreakdownByTarget;
    }

    public HashMap<String, String> getDmgTotalPrecentageByTarget() {
        return dmgTotalPrecentageByTarget;
    }
    

    public HashMap<String, String> getDmgHitPrecentageAgainstTarget() {
        return dmgHitPrecentageAgainstTarget;
    }
    
    private static HashMap<String,String> dpsHashMap(Fight f,HashMap<String,Integer> dmgBreakDown,DecimalFormat df){
        HashMap<String,String> dpsMap=new HashMap();
        int durationS= (int) Stats.getDurationMs(f)/1000;
        for(String s:dmgBreakDown.keySet()){
//            System.out.println(durationS);
            double dps=(double) dmgBreakDown.get(s) / durationS;
//            System.out.println(dps);
            dpsMap.put(s, df.format(dps));
        }
        return dpsMap;
    }
    
    public String getHps() {
        return hps;
    }

    public HashMap<String, String> getDmgAvgBreakdownByTarget() {
        return dmgAvgBreakdownByTarget;
    }

    public HashMap<String, String> getDpsBreakdownByTarget() {
        return dpsBreakdownByTarget;
    }

    public HashMap<String, String> getDmgCritBreakdownByTarget() {
        return dmgCritBreakdownByTarget;
    }

    public HashMap<String, String> getDmgBigBreakdownByTarget() {
        return dmgBigBreakdownByTarget;
    }

    public HashMap<String, String> getDmgHitsDoneAgainstTarget() {
        return dmgHitsDoneAgainstTarget;
    }

    public String getBigHeal() {
        return bigHeal;
    }

    public HashMap<String,Integer> getDmgBreakdownByTarget() {
        return dmgBreakdownByTarget;
    }

    public String getHealCount() {
        return healCount;
    }

    public String getAvgHeal() {
        return avgHeal;
    }

    public String getHealCritPrecentage() {
        return healCritPrecentage;
    }

    public String getAllHealTaken() {
        return allHealTaken;
    }

    public String getAverageHealTaken() {
        return averageHealTaken;
    }

    public String getHtps() {
        return htps;
    }

    public String getHealTakenCount() {
        return healTakenCount;
    }

    public String getBigTakenHeal() {
        return bigTakenHeal;
    }

    public String getTakenHealCritPrecentage() {
        return takenHealCritPrecentage;
    }

    public String getTotalThreat() {
        return totalThreat;
    }

    public String getTps() {
        return tps;
    }
    

    public String getHits() {
        return hits;
    }

    public String getAverageDmg() {
        return averageDmg;
    }

    public String getDps() {
        return dps;
    }

    public String getHitPrecentage() {
        return hitPrecentage;
    }

    public String getDmgCritPrecentage() {
        return dmgCritPrecentage;
    }

    public String getBiggestHit() {
        return biggestHit;
    }
    
    
    public String getOwner() {
        return owner;
    }

    public String getAllDmgDone() {
        return allDmgDone;
    }

    public String getAllHealDone() {
        return allHealDone;
    }

    public String getAllDmgTaken() {
        return allDmgTaken;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getAPM() {
        return APM;
    }

    public String getDuration() {
        return duration;
    }
    
      public String getDtps() {
        return dtps;
    }

    public String getHitsTaken() {
        return hitsTaken;
    }

    public String getAvgTakenHit() {
        return avgTakenHit;
    }

    public String getBigTakenHit() {
        return bigTakenHit;
    }

    public String getTakenCrits() {
        return takenCrits;
    }

    public String getTakenHitPrecentage() {
        return takenHitPrecentage;
    }
    
    
    
}
