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
     * average dmg/heal from source against target for all targets/sources use
     * null
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
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

    /**
     * damage per s
     *
     * @param fight
     * @return
     */
    public static double dps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getSumOfEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    /**
     * healing taken per s
     *
     * @param fight
     * @return
     */
    public static double htps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getSumOfEffectFromSourceAgainstTarget(fight, "Heal", null, fight.getOwner());
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    /**
     * ability usage per min
     *
     * @param fight
     * @return
     */
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

    /**
     * healing per s
     *
     * @param fight
     * @return
     */
    public static double hps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getSumOfEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), null);
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    /**
     * damage taken per s
     *
     * @param fight
     * @return
     */
    public static double dtps(Fight fight) {
        long timeMs = getDurationMs(fight);
        int dmg = getSumOfEffectFromSourceAgainstTarget(fight, "Damage", null, fight.getOwner());
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
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

    /**
     * fight duration in ms
     *
     * @param fight
     * @return
     */
    public static long getDurationMs(Fight fight) {
        ArrayList<Row> rows = fight.getRows();
        LocalTime start = rows.get(0).getTimestamp();
        LocalTime end = rows.get(rows.size() - 1).getTimestamp();

        return getDiffrence(start, end);
    }

    public static double tps(Fight fight) {
        int sum = totalThreat(fight);
        double duration = (double) getDurationMs(fight) / 1000;
        double avg = (double) sum / duration;
        return avg;
    }

    /**
     * total generated threat
     *
     * @param fight
     * @return
     */
    public static int totalThreat(Fight fight) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (r.getSource().equals(fight.getOwner())) {
                sum += r.getThreat();
            }
        }
        return sum;
    }

    /**
     * count the number of hits/heal against target for all targets/sources use
     * null
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
    public static int getNumberOfEffectsFromSourceAgainstTarget(Fight fight, String effect, String source, String target) {

        int i = 0;
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {
                i++;
            }
        }
        return i;
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

    /**
     * miss% of done attacks against target
     *
     * @param fight
     * @param target
     * @return
     */
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

    /**
     * miss% of done attacks
     *
     * @param fight
     * @return
     */
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

    /**
     * crit% of Damage/Heal effect from a source against a target If
     * source/target doesn't matter use null
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
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

    /**
     * miss% of taken attacks
     *
     * @param fight
     * @return
     */
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

    /**
     * returns a map where the key is ability name, and value is dmg/heal by the
     * ability. If target/source doesn't matter, use null
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

    /**
     * returns a list of all ability activations
     *
     * @param fight
     *
     * @return
     */
    private static ArrayList<AbilityActivation> getActivations(Fight fight) {
        ArrayList<AbilityActivation> activations = new ArrayList();

        for (Row r : fight.getRows()) {
            if (r.getEventtype() == Eventtype.AbilityActivate) {
                activations.add(new AbilityActivation(r));
            }
        }
        return activations;
    }

    /**
     * map key is ability name, value the avg time between activations for that
     * ability
     *
     * @param fight
     * @return
     */
    public static HashMap<String, Double> avgTimeBetweenActivations(Fight fight) {
        HashMap<String, LocalTime> lastTime = new HashMap(); //Helper map, to store timestamp of last activation
        HashMap<String, Double> sums = new HashMap();
        HashMap<String, Integer> count = new HashMap();
        ArrayList<AbilityActivation> activations = getActivations(fight);
        for (AbilityActivation a : activations) {
            String name = a.getAbilityName();
            if (lastTime.get(name) == null) {
                lastTime.put(name, a.getActivation());
                sums.put(name, 0.0);
                count.put(name, 1);
            } else {
                sums.put(name, sums.get(name) + (double) getDiffrence(lastTime.get(name), a.getActivation()) / 1000);
                count.put(name, count.get(name) + 1);
                lastTime.put(name, a.getActivation());
            }
        }
        return createAvgMap(count, sums);
    }
    
    /**
     * This method is here to fit checkstyle for avgTimeBetweenactivations
     * @return 
     */
    private static HashMap<String, Double> createAvgMap(HashMap<String, Integer> count, HashMap<String, Double> sums) {
        HashMap<String, Double> ret = new HashMap(); 
        for (String s : sums.keySet()) {
            ret.put(s, (double) sums.get(s) / count.get(s));
        }
        return ret;
    }

    /**
     * map key is ability name, value the max time between activations for that
     * ability
     *
     * @param fight
     * @return
     */
    public static HashMap<String, Double> maxTimeBetweenActivations(Fight fight) {
        HashMap<String, LocalTime> lastTime = new HashMap(); //Helper map, to store timestamp of last activation
        HashMap<String, Double> ret = new HashMap();        //returned map

        ArrayList<AbilityActivation> activations = getActivations(fight);

        for (AbilityActivation a : activations) {
            String name = a.getAbilityName();
            if (lastTime.get(name) == null) {
                lastTime.put(name, a.getActivation());

            } else if (ret.get(name) == null || ret.get(name) < Double.valueOf(getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0) {

                ret.put(name, Double.valueOf(getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0);
                lastTime.put(name, a.getActivation());

            } else {
                lastTime.put(name, a.getActivation());

            }

        }
        return ret;
    }

    /**
     * map key is ability name, value the min time between activations for that
     * ability
     *
     * @param fight
     * @return
     */
    public static HashMap<String, Double> minTimeBetweenActivations(Fight fight) {
        ArrayList<AbilityActivation> activations = getActivations(fight);
        HashMap<String, LocalTime> lastTime = new HashMap(); //Helper map, to store timestamp of last activation
        HashMap<String, Double> ret = new HashMap();        //returned map

        for (AbilityActivation a : activations) {
            String name = a.getAbilityName();
            if (lastTime.get(name) == null) {
                lastTime.put(name, a.getActivation());

            } else if (ret.get(name) == null || ret.get(name) > Double.valueOf(getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0) {

                ret.put(name, Double.valueOf(getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0);
                lastTime.put(name, a.getActivation());

            } else {
                lastTime.put(name, a.getActivation());
            }

        }
        return ret;
    }

    /**
     * returns a map where the key is target, and value is dmg/heal by the
     * ability. If target/source doesn't matter, use null
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
    public static HashMap<String, Integer> divideEffectSumByTarget(Fight fight, String effect, String source, String target) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (r.getEffecttype() != null && r.getEffecttype().equals(effect) && (source == null || r.getSource().equals(source)) && (target == null || r.getTarget().equals(target))) {
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

    //Unused method for tracking effects
//    public static ArrayList<AbilityActivation> getEffects(Fight fight) {
//        ArrayList<AbilityActivation> activations = new ArrayList();
//        ArrayList<Row> rows = fight.getRows();
//        for (int i = 0; i < rows.size(); i++) {
//            //limits the rows we are dealing with to include only rows where we are the target and the source
//            //Type is never null
//            if (rows.get(i).getType() == Type.ApplyEffect && rows.get(i).getSource().equals(fight.getOwner()) && rows.get(i).getTarget().equals(fight.getOwner())) {
//                //TODO find the ability activation behind this row
//
//                for (int j = i + 1; j < rows.size(); j++) {
//                    //This if is big and confusing asf. TODO, find out if u can remove getSource and getTarget comparisons
//                    //abilityname is never null
//                    if (rows.get(j).getType() == Type.RemoveEffect && rows.get(j).getSource().equals(fight.getOwner()) && rows.get(j).getTarget().equals(fight.getOwner()) && rows.get(i).getAbilityName().equals(rows.get(j).getAbilityName())) {
//                        activations.add(new AbilityActivation(rows.get(i), rows.get(j)));
//                        break;
//                    }
//
//                }
//
//            }
//        }
//
//        return activations;
//    }
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
     *
     * @param fight
     * @param target
     * @return
     */
    public static ArrayList<Tuple> getTotalHpsByTimeAgainstTarget(Fight fight, String target) {
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

    /**
     * return a list of tuples containg the total dmg done up to that point
     *
     * @param fight
     * @param target
     * @return
     */
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

    /**
     * Returns a list of Tuples containing momentary Hps at a given time
     * (meaning dps in last 10 seconds) for all targets use null Tuple format:
     * timestamp, dps
     *
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryHpsAgainstTarget(Fight fight, String target) {
        Fight onlyDmgRows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Heal", fight.getOwner(), target);

        return momentaryEffect(onlyDmgRows);

    }

    /**
     * Returns a list of Tuples containing momentary Dps at a given time
     * (meaning dps in last 10 seconds) Tuple format: timestamp, value
     *
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryDps(Fight fight) {
        Fight onlyDmgRows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), null);

        return momentaryEffect(onlyDmgRows);

    }

    /**
     * calcluates momentary dps/hps/dtps from a list of rows containing only
     * rows of that effect (meaning effect in last 10 s)
     *
     * NOTE given parameter must contain only rows with the wanted effect (dmg
     * or heal)
     *
     * @param onlyEffectRows
     * @return
     */
    private static ArrayList<Tuple<LocalTime, Double>> momentaryEffect(Fight onlyEffectRows) {
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
     * a target (meaning dps in last 10 seconds) Tuple format: timestamp, dps
     *
     * @param fight
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryDpsAgainstTarget(Fight fight, String target) {
        ArrayList<Tuple<LocalTime, Double>> list = new ArrayList();
        Fight onlyDmgRows = getRowsWithSpecficEffectFromSourceAgainstTarget(fight, "Damage", fight.getOwner(), target); //To make this a little more simple, get only rows regarding dmg

        ArrayList<Row> rows = onlyDmgRows.getRows();

        return momentaryEffect(onlyDmgRows);
    }

}
