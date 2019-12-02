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
import java.util.HashSet;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.domain.Row;
import myparser.myparser.types.Eventtype;
import myparser.myparser.types.Type;

/**
 *
 * Contains numerous static methods for calculating statistics about a certain
 * fight
 */
//TODO could make effective healing precentage (by comparing threat generated to healing done), but as it's only possible for pve, probably not worth the effort
public class Stats {

    public static HashSet<String> getDmgTargets(Fight fight) {
        HashSet<String> targets = new HashSet();
        for (Row r : fight.getRows()) {
            //we shouldn't be able to add Owner (since owner can't dmg himself), so not checking for it here for performance
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                targets.add(r.getTarget());
            }
        }
        return targets;
    }

    public static int getAllDamageByOwner(Fight fight) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                sum += r.getDmgHeal();
            }
        }

        return sum;
    }

    public static int getAllHealingToOwner(Fight fight) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getTarget().equals(fight.getOwner())) {
                sum += r.getDmgHeal();
            }
        }

        return sum;
    }

    public static int getAllHealingByOwner(Fight fight) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner())) {
                sum += r.getDmgHeal();
            }
        }

        return sum;
    }

    public static int getAllDamageToOwner(Fight fight) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner())) {
                sum += r.getDmgHeal();
            }
        }

        return sum;
    }

    public static double dps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getAllDamageByOwner(fight);
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    public static double htps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getAllHealingToOwner(fight);
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    //TODO when ability activation class is done, redo this
    public static double APM(Fight fight) {
        int i = 0; //counter for ability activations
        for (Row r : fight.getRows()) {
            if (r.getEventtype() == Eventtype.AbilityActivate) {
                i++;
            }
        }
        double lengthMinutes = (double) getDurationMs(fight) / 60000;
        return (double) i / lengthMinutes;
    }

    public static double hps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getAllHealingByOwner(fight);
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    public static double dtps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getAllDamageToOwner(fight);
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    public static LocalTime getStart(Fight fight) {
        return fight.getRows().get(0).getTimestamp();
    }

    public static LocalTime getEnd(Fight fight) {
        ArrayList<Row> rows = fight.getRows();
        return rows.get(rows.size() - 1).getTimestamp();
    }

    /*
    Return diffrence between 2 datetime objects
    Adjusted so that durations over midnight are correct
     */
    public static long getDiffrence(LocalTime start, LocalTime end) {
        long time = start.until(end, MILLIS);
        if (time < 0) {
            time += 8.64e+7;    //amount of ms in a day
        }
        return time;
    }

    public static long getDurationMs(Fight fight) {
        ArrayList<Row> rows = fight.getRows();
        LocalTime start = rows.get(0).getTimestamp();
        LocalTime end = rows.get(rows.size() - 1).getTimestamp();

        return getDiffrence(start, end);
    }

    public static double averageTakenHit(Fight fight) {
        int number = 0;
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner())) {
                number++;
                sum += r.getDmgHeal();
            }

        }
        if (number == 0) {
            return 0;
        }
        return (double) sum / number;

    }

    public static double averageHeal(Fight fight) {
        int number = 0;
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner())) {
                number++;
                sum += r.getDmgHeal();
            }

        }
        if (number == 0) {
            return 0;
        }
        return (double) sum / number;

    }

    public static double tps(Fight fight) {
        int sum = totalThreat(fight);
        double duration = (double) getDurationMs(fight) / 1000;
        double avg = (double) sum / duration;
        return avg;
    }

    public static int totalThreat(Fight fight) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            sum += r.getThreat();
        }
        return sum;
    }

    public static double averageTakenHeal(Fight fight) {
        int number = 0;
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getTarget().equals(fight.getOwner())) {
                number++;
                sum += r.getDmgHeal();
            }

        }
        if (number == 0) {
            return 0;
        }
        return (double) sum / number;

    }

    public static double averageHit(Fight fight) {
        int number = 0;
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                number++;
                sum += r.getDmgHeal();
            }

        }
        if (number == 0) {
            return 0;
        }
        return (double) sum / number;

    }

    public static int numberOfTakenHits(Fight fight) {
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner())) {
                i++;
            }

        }
        return i;
    }

    public static int numberOfHits(Fight fight) {
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                i++;
            }

        }
        return i;
    }

    public static int numberOfHitsAgainstTarget(Fight fight, String target) {
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(target) && r.getSource().equals(fight.getOwner())) {
                i++;
            }

        }
        return i;
    }

    public static int numberOfHealsTaken(Fight fight) {
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getTarget().equals(fight.getOwner())) {
                i++;
            }

        }
        return i;
    }

    public static int numberOfHeals(Fight fight) {
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner())) {
                i++;
            }

        }
        return i;
    }

    public static int biggestTakenHit(Fight fight) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner()) && r.getDmgHeal() > big) {
                big = r.getDmgHeal();
            }

        }
        return big;
    }

    //Doesnt count medpac as the biggest heal (or it would always be medpac)
    public static int biggestTakenHeal(Fight fight) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getTarget().equals(fight.getOwner()) && r.getDmgHeal() > big && !r.getAbilityName().contains("medpac")) {
                big = r.getDmgHeal();
            }

        }
        return big;
    }

    //Doesnt count medpac as the biggest heal (or it would always be medpac)
    public static int biggestHeal(Fight fight) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner()) && r.getDmgHeal() > big && !r.getAbilityName().contains("medpac")) {
                big = r.getDmgHeal();
            }

        }
        return big;
    }

    public static int biggestHit(Fight fight) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner()) && r.getDmgHeal() > big) {
                big = r.getDmgHeal();
            }

        }
        return big;
    }

    public static double missPrecentageAgainstTarget(Fight fight, String target) {
        int all = 0;
        int misses = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(target) && r.getSource().equals(fight.getOwner())) {
                all++;
                if (r.isMiss()) {
                    misses++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) misses / all;
        return precentage;
    }

    public static double missPrecentage(Fight fight) {
        int all = 0;
        int misses = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                all++;
                if (r.isMiss()) {
                    misses++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) misses / all;
        return precentage;
    }

    public static double takenHealCritPrecentage(Fight fight) {
        int all = 0;
        int crits = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getTarget().equals(fight.getOwner())) {
                all++;
                if (r.isCrit()) {
                    crits++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) crits / all;
        return precentage;
    }

    public static double healCritPrecentage(Fight fight) {
        int all = 0;
        int crits = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner())) {
                all++;
                if (r.isCrit()) {
                    crits++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) crits / all;
        return precentage;
    }

    public static double dmgCritPrecentage(Fight fight) {
        int all = 0;
        int crits = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                all++;
                if (r.isCrit()) {
                    crits++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) crits / all;
        return precentage;
    }

    public static double takenMissPrecentage(Fight fight) {
        int all = 0;
        int misses = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner())) {
                all++;
                if (r.isMiss()) {
                    misses++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) misses / all;
        return precentage;
    }

    public static double takenCritPrecentage(Fight fight) {
        int all = 0;
        int crits = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner())) {
                all++;
                if (r.isCrit()) {
                    crits++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) crits / all;
        return precentage;
    }

    public static HashMap<String, Integer> divideHealingDealtByAbility(Fight fight) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner())) {
                if (results.get(r.getAbilityName()) == null) {
                    results.put(r.getAbilityName(), r.getDmgHeal());
                } else {
                    Integer helper = results.get(r.getAbilityName());
                    results.put(r.getAbilityName(), helper + r.getDmgHeal());
                }
            }

        }
        return results;
    }

    public static HashMap<String, Integer> divideDamageDealtAgainstTargetByAbility(Fight fight, String target) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(target) && r.getSource().equals(fight.getOwner())) {
                if (results.get(r.getAbilityName()) == null) {
                    results.put(r.getAbilityName(), r.getDmgHeal());
                } else {
                    Integer helper = results.get(r.getAbilityName());
                    results.put(r.getAbilityName(), helper + r.getDmgHeal());
                }
            }

        }
        return results;
    }

    public static HashMap<String, Integer> divideDamageDealtByAbility(Fight fight) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                if (results.get(r.getAbilityName()) == null) {
                    results.put(r.getAbilityName(), r.getDmgHeal());
                } else {
                    Integer helper = results.get(r.getAbilityName());
                    results.put(r.getAbilityName(), helper + r.getDmgHeal());
                }
            }

        }
        return results;
    }

    public static HashMap<String, Integer> divideDamageTakenByOwner(Fight fight) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(fight.getOwner())) {
                if (results.get(r.getSource()) == null) {
                    results.put(r.getSource(), r.getDmgHeal());
                } else {
                    Integer helper = results.get(r.getSource());
                    results.put(r.getSource(), helper + r.getDmgHeal());
                }
            }

        }
        return results;
    }

    public static HashMap<String, Integer> divideDamageDealtByTarget(Fight fight) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                if (results.get(r.getTarget()) == null) {
                    results.put(r.getTarget(), r.getDmgHeal());
                } else {
                    Integer helper = results.get(r.getTarget());
                    results.put(r.getTarget(), helper + r.getDmgHeal());
                }
            }

        }
        return results;
    }

    //TODO make this function more efficient and more readable
    //TODO test if this method works...
    public static ArrayList<AbilityActivation> getEffects(Fight fight) {
        ArrayList<AbilityActivation> activations = new ArrayList();
        ArrayList<Row> rows = fight.getRows();
        for (int i = 0; i < rows.size(); i++) {
            //limits the rows we are dealing with to include only rows where we are the target and the source
            //Type is never null
            if (rows.get(i).getType() == Type.ApplyEffect && rows.get(i).getSource().equals(fight.getOwner()) && rows.get(i).getTarget().equals(fight.getOwner())) {
                //TODO find the ability activation behind this row

                for (int j = i + 1; j < rows.size(); j++) {
                    //This if is big and confusing asf. TODO, find out if u can remove getSource and getTarget comparisons
                    //abilityname is never null
                    if (rows.get(j).getType() == Type.RemoveEffect && rows.get(j).getSource().equals(fight.getOwner()) && rows.get(j).getTarget().equals(fight.getOwner()) && rows.get(i).getAbilityName().equals(rows.get(j).getAbilityName())) {
                        activations.add(new AbilityActivation(rows.get(i), rows.get(j)));
                        break;
                    }

                }

            }
        }

        return activations;
    }

    public static HashMap<String, Integer> divideHealingDealtByTarget(Fight fight) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Heal") && r.getSource().equals(fight.getOwner())) {
                if (results.get(r.getTarget()) == null) {
                    results.put(r.getTarget(), r.getDmgHeal());
                } else {
                    Integer helper = results.get(r.getTarget());
                    results.put(r.getTarget(), helper + r.getDmgHeal());
                }
            }

        }
        return results;
    }

    public static Integer biggestHitAgainstTarget(Fight fight, String target) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(target) && r.getDmgHeal() > big) {
                big = r.getDmgHeal();
            }

        }
        return big;
    }

    public static HashMap<LocalTime, Integer> cumulativeDmgDoneAgainstTarget(Fight fight, String target) {
        int sum = 0;
        HashMap<LocalTime, Integer> dmgDone = new HashMap();

        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner()) && r.getTarget().equals(target)) {
                sum += r.getDmgHeal();
                dmgDone.put(r.getTimestamp(), sum);
            }

        }
        return dmgDone;

    }
    public static Fight getRowsWithSpecficEffectFromOwnerAgainstTarget(Fight fight, String effect, String target) {
        ArrayList<Row> specficRows = new ArrayList();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && r.getSource().equals(fight.getOwner()) && r.getTarget().equals(target)) {
                specficRows.add(r);
            }

        }

        return new Fight(specficRows, fight.getOwner());
    }
    /*
    Used to make dps and hps Stats a bit more efficient by cutting down the amount of rows needed
     */
    public static Fight getRowsWithSpecficEffectFromOwner(Fight fight, String effect) {
        ArrayList<Row> specficRows = new ArrayList();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && r.getSource().equals(fight.getOwner())) {
                specficRows.add(r);
            }

        }

        return new Fight(specficRows, fight.getOwner());
    }

    /*
    Returns an arraylist with tuples of LocalTime, Double
    For dps by moment
     */
    public static ArrayList<Tuple> getTotalDpsByTime(Fight fight) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = getRowsWithSpecficEffectFromOwner(fight, "Damage").getRows(); //Getting only rows with dmg in them to take this a little simpler
        int sum = 0;
        ArrayList<Tuple> list = new ArrayList();
        for (Row r : rows) {
            sum += r.getDmgHeal();
            int durationS = (int) getDiffrence(start, r.getTimestamp())/1000;
            double dps = (double) sum/durationS;
            Tuple<LocalTime, Double> tuple=new Tuple(r.getTimestamp(), dps);
            
            list.add(tuple);
        }
        
        return list;
    }
    public static ArrayList<Tuple> getTotalDpsByTimeAgainstTarget(Fight fight , String target) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = getRowsWithSpecficEffectFromOwnerAgainstTarget(fight, "Damage", target).getRows(); //Getting only rows with dmg in them to take this a little simpler
        int sum = 0;
        ArrayList<Tuple> list = new ArrayList();
        for (Row r : rows) {
            sum += r.getDmgHeal();
            int durationS = (int) getDiffrence(start, r.getTimestamp())/1000;
            double dps = (double) sum/durationS;
            Tuple<LocalTime, Double> tuple=new Tuple(r.getTimestamp(), dps);
            
            list.add(tuple);
        }
        
        return list;
    }
    /*
    splits the given fight to timeframes. (Used by the gui for dps map)
    return start times for timeframes
     */
//    public static ArrayList<LocalTime> splitIntoIntervals(Fight fight) {
//        
//        //This method is taking some shortcuts by allowing itself to be used the already made method rowsInTimeFrame
//        //However in order to do this, we need to avoid timeframes that go over midnight
//        
//        
//    }
    public static HashMap<LocalTime, Integer> cumulativeDmgDone(Fight fight) {
        int sum = 0;
        HashMap<LocalTime, Integer> dmgDone = new HashMap();

        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner())) {
                sum += r.getDmgHeal();
                dmgDone.put(r.getTimestamp(), sum);
            }

        }
        return dmgDone;
    }

    public static double averageHitAgainstTarget(Fight fight, String target) {
        int number = 0;
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getSource().equals(fight.getOwner()) && r.getTarget().equals(target)) {
                number++;
                sum += r.getDmgHeal();
            }

        }
        if (number == 0) {
            return 0;
        }
        return (double) sum / number;

    }

    public static double dmgCritPrecentageAgainstTarget(Fight fight, String target) {
        int all = 0;
        int crits = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals("Damage") && r.getTarget().equals(target) && r.getSource().equals(fight.getOwner())) {
                all++;
                if (r.isCrit()) {
                    crits++;
                }
            }

        }
        if (all == 0) {
            return 0.0;
        }
        double precentage = (double) crits / all;
        return precentage;
    }
    /**
     * Returns a list of Tuples containing momentary dps at a given time (meaning dps in last 10 seconds)
     * 
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryDps(Fight fight) {
        ArrayList<Tuple<LocalTime, Double>> list = new ArrayList();
        Fight onlyDmgRows=getRowsWithSpecficEffectFromOwner(fight, "Damage"); //To make this a little more simple, get only rows regarding dmg
        
        ArrayList<Row> rows=onlyDmgRows.getRows(); 
        
        //This currently a very inefficient way of doing this, but atm it's not necessary to make this faster
        for(Row r : rows){
            int sum=0;
            LocalTime currentTime = r.getTimestamp();
            //get all the rows in a 10 second window
            ArrayList<Row> rowsInTimeFrame =onlyDmgRows.rowsInTimeFrame(currentTime.minusSeconds(10), currentTime).getRows();
            //sum the dmg for rows in the 10 s window
            for(Row x :rowsInTimeFrame){
                sum+=x.getDmgHeal();
            }
            list.add(new Tuple(currentTime,(double)sum/10));
            
        }
        return list;
    }
    
    /**
     * Returns a list of Tuples containing momentary dps at a given time against a target (meaning dps in last 10 seconds)
     * 
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryDpsAgainstTarget(Fight fight,String target) {
        ArrayList<Tuple<LocalTime, Double>> list = new ArrayList();
        Fight onlyDmgRows=getRowsWithSpecficEffectFromOwnerAgainstTarget(fight, "Damage", target); //To make this a little more simple, get only rows regarding dmg
        
        ArrayList<Row> rows=onlyDmgRows.getRows(); 
        
        //This currently a very inefficient way of doing this, but atm it's not necessary to make this faster
        for(Row r : rows){
            int sum=0;
            LocalTime currentTime = r.getTimestamp();
            //get all the rows in a 10 second window
            ArrayList<Row> rowsInTimeFrame =onlyDmgRows.rowsInTimeFrame(currentTime.minusSeconds(10), currentTime).getRows();
            //sum the dmg for rows in the 10 s window
            for(Row x :rowsInTimeFrame){
                sum+=x.getDmgHeal();
            }
            list.add(new Tuple(currentTime,(double)sum/10));
            
        }
        return list;
    }

}
