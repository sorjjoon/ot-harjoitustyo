/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.stats;

import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.util.ArrayList;
import java.util.HashMap;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;

/**
 *
 * @author joona
 */
public  class Stats {
    
    public static int getAllDamageByOwner(Fight fight){
        int sum=0;
        for(Row r : fight.getRows()){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getSource().equals(fight.getOwner())){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    
    public static int getAllHealingToOwner(Fight fight){
        int sum=0;
        for(Row r : fight.getRows()){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Heal")&&r.getTarget().equals(fight.getOwner())){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    public static int getAllHealingByOwner(Fight fight){
        int sum=0;
        for(Row r : fight.getRows()){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Heal")&&r.getSource().equals(fight.getOwner())){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    
    public static int getAllDamageToOwner(Fight fight){
        int sum=0;
        for(Row r : fight.getRows()){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getTarget().equals(fight.getOwner())){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    
    public static double dps(Fight fight){
        long time_ms=getDuration(fight);
        int dmg=getAllDamageByOwner(fight);
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    
    public static double htps(Fight fight){
        long time_ms=getDuration(fight);
        int dmg=getAllHealingToOwner(fight);
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    public static double hps(Fight fight){
        long time_ms=getDuration(fight);
        int dmg=getAllHealingByOwner(fight);
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    public static double dtps(Fight fight){
        long time_ms=getDuration(fight);
        int dmg=getAllDamageToOwner(fight);
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    
    
    public static long getDuration(Fight fight){
        ArrayList<Row> rows=fight.getRows();
        LocalTime start=rows.get(0).getTimestamp();
        LocalTime end=rows.get(rows.size()-1).getTimestamp();
        long time=start.until(end, MILLIS);
        if(time<0){
            time+=8.64e+7;
        }
        return time;
    }   
    
    
    
    
    public static HashMap<String,Integer> divideDamageTakenByOwner(Fight fight){
        HashMap<String,Integer> results=new HashMap();
        for(Row r : fight.getRows()){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getTarget().equals(fight.getOwner())){
                if(results.get(r.getSource())==null){
                    results.put(r.getSource(), r.getDmg_heal());
                }else{
                    Integer helper =results.get(r.getSource());
                    results.put(r.getSource(), helper+r.getDmg_heal());
                }
            }
                
            }
        return results;
    }
    
    public static HashMap<String,Integer> divideDamageDealtByTarget(Fight fight){
        HashMap<String,Integer> results=new HashMap();
        for(Row r : fight.getRows()){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getSource().equals(fight.getOwner())){
                if(results.get(r.getTarget())==null){
                    results.put(r.getTarget(), r.getDmg_heal());
                }else{
                    Integer helper =results.get(r.getTarget());
                    results.put(r.getTarget(), helper+r.getDmg_heal());
                }
            }
                
            }
        return results;
    }
}
