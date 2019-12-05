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

    /**
     * get all dmg by owner
     *
     * @param fight
     * @return
     */
    public static int getAllDamageByOwner(Fight fight) {
        return getSumOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);
    }

    public static Double getAverageOfEffectFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {
        int sum = 0;
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {
                sum += r.getDmgHeal();
                i++;
            }
        }
        if (i == 0) {
            return 0.0;
        }
        double avg = (double) sum / i;
        return avg;
    }

    /**
     * return the crit dmg/heal of given effect (meaning heal or damage) from a
     * given source If source/target doesn't matter, use null
     *
     * @param fight
     * @param effect, damage or heal
     * @param source, for example owner
     * @return
     */
    public static int getSumOfEffectFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (r.getSource().equals(source) || source == null) && (r.getTarget().equals(target) || target == null)) {
                sum += r.getDmgHeal();
            }
        }

        return sum;
    }

    public static int getAllHealingToOwner(Fight fight) {
        return getSumOfEffectFromSourceAgainstTarget(fight, "Heal", null, fight.getOwner());
    }

    public static int getAllHealingByOwner(Fight fight) {
        return getSumOfEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), null);
    }

    public static int getAllDamageToOwner(Fight fight) {
        return getSumOfEffectFromSourceAgainstTarget(fight, "Damage", null, fight.getOwner());
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
    public static double apm(Fight fight) {
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

    /**
     * Returns diffrence between 2 datetime objects (in MS). Adjusted so that
     * durations over midnight are correct
     *
     * @param start
     * @param end
     * @return
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
        return getAverageOfEffectFromSourceAgainstTarget(fight, "Damage", null, fight.getOwner());

    }

    public static double averageHeal(Fight fight) {
        return getSumOfEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), null);

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
            if (r.getSource().equals(fight.getOwner())) {
                sum += r.getThreat();
            }
        }
        return sum;
    }

    public static double averageTakenHeal(Fight fight) {
        return getAverageOfEffectFromSourceAgainstTarget(fight, "Heal", null, fight.getOwner());

    }

    public static double averageHit(Fight fight) {
        return getAverageOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);

    }

    public static int getNumberOfEffectsFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {

        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {
                i++;
            }
        }
        return i;
    }

    public static int numberOfTakenHits(Fight fight) {
        return getNumberOfEffectsFromSourceAgainstTarget(fight, "Damage", null, fight.getOwner());
    }

    public static int numberOfHits(Fight fight) {
        return getNumberOfEffectsFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);

    }

    public static int numberOfHitsAgainstTarget(Fight fight, String target) {

        return getNumberOfEffectsFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target);

    }

    public static int numberOfHealsTaken(Fight fight) {
        return getNumberOfEffectsFromSourceAgainstTarget(fight, "Heal", null, fight.getOwner());

    }

    public static int numberOfHeals(Fight fight) {
        return getNumberOfEffectsFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), null);

    }

    /**
     * Returns biggest dmg/heal Doesn't count medpacks (because they would
     * always be the biggest heal)
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
    public static int getBigOfEffectFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (r.getSource().equals(source) || source == null) && (r.getTarget().equals(target) || target == null)) {
                //dmg names don't contain the word "Medpack"
                //Skill named Slow-Release Medpac is taken into account
                if (r.getDmgHeal() > big && (!r.getAbilityName().contains("Medpac") || r.getAbilityName().equals("Slow-Release Medpac"))) {
                    big = r.getDmgHeal();
                }
            }
        }

        return big;
    }

    public static int biggestTakenHit(Fight fight) {
        return getBigOfEffectFromSourceAgainstTarget(fight, "Damage", null, fight.getOwner());
    }

    //Doesnt count medpac as the biggest heal (or it would always be medpac)
    public static int biggestTakenHeal(Fight fight) {
        return getBigOfEffectFromSourceAgainstTarget(fight, "Heal", null, fight.getOwner());

    }

    //Doesnt count medpac as the biggest heal (or it would always be medpac)
    public static int biggestHeal(Fight fight) {
        return getBigOfEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), null);

    }

    public static int biggestHit(Fight fight) {
        return getBigOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);

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

    public static Double getCritOfEffectFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {
        int crit = 0;
        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {

                i++;
                if (r.isCrit()) {
                    crit++;
                }
            }
        }
        if (i == 0) {
            return 0.0;
        }
        double critPre = (double) crit / i;
        return critPre;
    }

    public static double takenHealCritPrecentage(Fight fight) {
        return getCritOfEffectFromSourceAgainstTarget(fight, "Heal", null, fight.getOwner());
    }

    public static double healCritPrecentage(Fight fight) {
        return getCritOfEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), null);

    }

    public static double dmgCritPrecentage(Fight fight) {
        return getCritOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);

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
        return getCritOfEffectFromSourceAgainstTarget(fight, "Damage", null, fight.getOwner());

    }

    /**
     * returns a map where the key is ability name, and value is dmg/heal by the
     * ability if target/source doesn't matter, use null
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
    public static HashMap<String, Integer> divideEffectSumByAbilityF(Fight fight, String effect, String source, String target) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {
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

    public static HashMap<String, Integer> divideHealingDealtByAbility(Fight fight) {
        return divideEffectSumByAbilityF(fight, "Heal", fight.getOwner(), null);
    }

    public static HashMap<String, Integer> divideDamageDealtAgainstTargetByAbility(Fight fight, String target) {
        return divideEffectSumByAbilityF(fight, "Damage", fight.getOwner(), target);

    }

    public static HashMap<String, Integer> divideDamageDealtByAbility(Fight fight) {
        return divideEffectSumByAbilityF(fight, "Damage", fight.getOwner(), null);

    }

    /**
     * returns a map where the key is target, and value dmg by that ability
     * (source is owner)
     *
     * @param fight
     * @return
     */
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
        return getBigOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target);
    }

    /**
     * return a map with key timestamp, and value dmg/heal done at that point
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
    public static HashMap<LocalTime, Integer> cumulativeEffectDoneAgainstTarget(Fight fight, String effect, String source, String target) {
        int sum = 0;
        HashMap<LocalTime, Integer> totalDone = new HashMap();

        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(fight.getOwner())) && (target == null || r.getTarget().equals(target))) {
                sum += r.getDmgHeal();
                totalDone.put(r.getTimestamp(), sum);
            }

        }
        return totalDone;

    }

    /**
     * return HashMap with key timestamp, and value dmg done up to that point
     *
     * @param fight
     * @param target
     * @return
     */
    public static HashMap<LocalTime, Integer> cumulativeDmgDoneAgainstTarget(Fight fight, String target) {
        return cumulativeEffectDoneAgainstTarget(fight, "Damage", fight.getOwner(), target);

    }

    /**
     * returns a new fight wich only contains rows with a specfic effect
     * (heal/dmg) if target/source doesn't matter use null
     *
     * @param fight
     * @param effect
     * @param target
     * @return
     */
    public static Fight getRowsWithSpecficEffectFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {
        ArrayList<Row> specficRows = new ArrayList();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {
                specficRows.add(r);
            }

        }

        return new Fight(specficRows, fight.getOwner());
    }
    
    
    /**
     * Returns an arraylist with tuples of LocalTime, Double For hps by moment
     * for all targets use null
     * @param fight
     * @param target
     * @return
     */
    public static ArrayList<Tuple> getTotalHpsByTimeAgainstTarget(Fight fight,String target) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), target).getRows(); //Getting only rows with dmg in them to take this a little simpler
        int sum = 0;
        ArrayList<Tuple> list = new ArrayList();
        for (Row r : rows) {
            sum += r.getDmgHeal();
            int durationS = (int) getDiffrence(start, r.getTimestamp()) / 1000;
            double dps = (double) sum / durationS;
            Tuple<LocalTime, Double> tuple = new Tuple(r.getTimestamp(), dps);

            list.add(tuple);
        }

        return list;
    }
    /**
     * Returns an arraylist with tuples of LocalTime, Double For dps by moment
     *
     * @param fight
     * @return
     */
    public static ArrayList<Tuple> getTotalDpsByTime(Fight fight) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null).getRows(); //Getting only rows with dmg in them to take this a little simpler
        int sum = 0;
        ArrayList<Tuple> list = new ArrayList();
        for (Row r : rows) {
            sum += r.getDmgHeal();
            int durationS = (int) getDiffrence(start, r.getTimestamp()) / 1000;
            double dps = (double) sum / durationS;
            Tuple<LocalTime, Double> tuple = new Tuple(r.getTimestamp(), dps);

            list.add(tuple);
        }

        return list;
    }

    public static ArrayList<Tuple> getTotalDpsByTimeAgainstTarget(Fight fight, String target) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target).getRows(); //Getting only rows with dmg in them to take this a little simpler
        int sum = 0;
        ArrayList<Tuple> list = new ArrayList();
        for (Row r : rows) {
            sum += r.getDmgHeal();
            int durationS = (int) getDiffrence(start, r.getTimestamp()) / 1000;
            double dps = (double) sum / durationS;
            Tuple<LocalTime, Double> tuple = new Tuple(r.getTimestamp(), dps);

            list.add(tuple);
        }

        return list;
    }

    public static HashMap<LocalTime, Integer> cumulativeDmgDone(Fight fight) {
        return cumulativeEffectDoneAgainstTarget(fight, "Damage", fight.getOwner(), null);
    }

    public static double averageHitAgainstTarget(Fight fight, String target) {
        return getAverageOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target);

    }

    public static double dmgCritPrecentageAgainstTarget(Fight fight, String target) {
        return getCritOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target);

    }
    /**
     * Returns a list of Tuples containing momentary Hps at a given time
     * (meaning dps in last 10 seconds)
     * for all targets use null
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryHpsAgainstTarget(Fight fight, String target) {
        Fight onlyDmgRows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), target); //To make this a little more simple, get only rows regarding dmg

        return momentaryEffect(onlyDmgRows);

    }
    /**
     * Returns a list of Tuples containing momentary Dps at a given time
     * (meaning dps in last 10 seconds)
     *
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryDps(Fight fight) {
        Fight onlyDmgRows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null); //To make this a little more simple, get only rows regarding dmg

        return momentaryEffect(onlyDmgRows);

    }

    /**
     * calcluates momentary dps/hps/dtps from a list of rows containing only
     * rows of that effect (meaning effect in last 10 s)
     *
     * @param rows
     * @return
     */
    public static ArrayList<Tuple<LocalTime, Double>> momentaryEffect(Fight onlyEffectRows) {
        ArrayList<Tuple<LocalTime, Double>> list = new ArrayList();
        ArrayList<Row> rows = onlyEffectRows.getRows();
        //This currently a very inefficient way of doing this, but atm it's not necessary to make this faster
        for (Row r : rows) {
            int sum = 0;
            LocalTime currentTime = r.getTimestamp();
            //get all the rows in a 10 second window
            ArrayList<Row> rowsInTimeFrame = onlyEffectRows.rowsInTimeFrame(currentTime.minusSeconds(10), currentTime).getRows();
            //sum the dmg for rows in the 10 s window
            for (Row x : rowsInTimeFrame) {
                sum += x.getDmgHeal();
            }
            list.add(new Tuple(currentTime, (double) sum / 10));

        }
        return list;
    }

    /**
     * Returns a list of Tuples containing momentary dps at a given time against
     * a target (meaning dps in last 10 seconds)
     *
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryDpsAgainstTarget(Fight fight, String target) {
        ArrayList<Tuple<LocalTime, Double>> list = new ArrayList();
        Fight onlyDmgRows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target); //To make this a little more simple, get only rows regarding dmg

        ArrayList<Row> rows = onlyDmgRows.getRows();

        //This currently a very inefficient way of doing this, but atm it's not necessary to make this faster
        return momentaryEffect(onlyDmgRows);
    }

}
