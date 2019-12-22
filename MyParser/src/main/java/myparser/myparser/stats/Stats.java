package myparser.myparser.stats;

import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.util.ArrayList;
import java.util.HashMap;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.types.DamageType;
import myparser.myparser.types.EventType;

/**
 *
 * Contains numerous static methods for calculating statistics about a certain
 * fight
 * <p>
 * NOTE about usage, all given fights must have their rows ordered by row number
 */
public abstract class Stats {

    /**
     * return true if all given effect, source and target match the given's Row
     * parameters or are null
     *
     * @param row
     * @param effect
     * @param source
     * @param target
     * @return
     */
    private static boolean paramsCorretOrNull(Row r, String effect, String source, String target) {
        boolean effectTypeIsCorrect = r.getEffecttype() != null && r.getEffecttype().equals(effect);
        boolean sourceIsCorrect = source == null || r.getSource().equals(source);
        boolean targetIsCorrect = target == null || r.getTarget().equals(target);

        return effectTypeIsCorrect && sourceIsCorrect && targetIsCorrect;
    }

    /**
     * map key is dmg type, value is total dmg dealt by that type. Map doesn't
     * contain miss dmg types (as their dmg is always 0)
     *
     * @param fight
     * @param source
     * @param target
     * @return
     */
    public static HashMap<DamageType, Integer> dmgTakenByType(Fight fight, String source, String target) {
        HashMap<DamageType, Integer> returnMap = new HashMap();
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, "Damage", source, target)) {

                if (r.getDmgType() == null || r.getDmgHeal() == 0) {
                    //r.getDmgHeal()==0 removes misses
                    //Null dmg type here (since we already checked that the row is Damage), should only happen if there is a new kind dmg type (not possible atm, but here just in case, since ui controller breaks if there is a null in the map keyset)
                    continue;
                }

                if (returnMap.get(r.getDmgType()) == null) {
                    returnMap.put(r.getDmgType(), r.getDmgHeal());
                } else {
                    Integer oldValue = returnMap.get(r.getDmgType());
                    returnMap.put(r.getDmgType(), oldValue + r.getDmgHeal());
                }
            }
        }
        return returnMap;
    }

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
    public static Double averageSizeOfEffect(Fight fight, String effect, String source, String target) {
        int sum = 0;
        int i = 0;
        for (Row r : fight.getRows()) {

            if (paramsCorretOrNull(r, effect, source, target)) {
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
    public static int sumOfEffect(Fight fight, String effect, String source, String target) {
        int sum = 0;
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, effect, source, target)) {
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
        long timeMs = durationMs(fight);
        int dmg = sumOfEffect(fight, "Damage", fight.getOwner(), null);
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
        long timeMs = durationMs(fight);
        int dmg = sumOfEffect(fight, "Heal", null, fight.getOwner());
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
            if (r.getEventtype() == EventType.AbilityActivate) {
                i++;
            }
        }
        double lengthMinutes = (double) durationMs(fight) / 60000;
        return (double) i / lengthMinutes;
    }

    /**
     * healing per s
     *
     * @param fight
     * @return
     */
    public static double hps(Fight fight) {
        long timeMs = durationMs(fight);
        int dmg = sumOfEffect(fight, "Heal", fight.getOwner(), null);
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
        long timeMs = durationMs(fight);
        int dmg = sumOfEffect(fight, "Damage", null, fight.getOwner());
        int timeS = (int) timeMs / 1000;
        return (double) dmg / timeS;
    }

    /**
     * Returns diffrence between 2 localtime objects (in MS). Adjusted so that
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
    public static long durationMs(Fight fight) {
        ArrayList<Row> rows = fight.getRows();
        LocalTime start = rows.get(0).getTimestamp();
        LocalTime end = rows.get(rows.size() - 1).getTimestamp();

        return getDiffrence(start, end);
    }

    public static double tps(Fight fight) {
        int sum = totalThreat(fight);
        double duration = (double) durationMs(fight) / 1000;
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
    public static int effectCount(Fight fight, String effect, String source, String target) {

        int i = 0;
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, effect, source, target)) {
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
    public static int biggestEffect(Fight fight, String effect, String source, String target) {
        int big = 0;
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, effect, source, target)) {
                //dmg names don't contain the word "Medpac"
                //A skill named Slow-Release Medpac is taken into account (as it is not a consumable, but an acual skill)
                //no dmg skill contain Medpac
                if (r.getDmgHeal() > big && (!r.getAbilityName().contains("Medpac") || r.getAbilityName().equals("Slow-Release Medpac"))) {
                    big = r.getDmgHeal();
                }
            }
        }

        return big;
    }

    /**
     * miss% of done attacks against target by owner
     *
     * @param fight
     * @param target
     * @return
     */
    public static double missPrecentage(Fight fight, String target) {
        int all = 0;
        int misses = 0;
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, "Damage", fight.getOwner(), target)) {
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
    public static Double critPrecentage(Fight fight, String effect, String source, String target) {
        int crit = 0;
        int i = 0;
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, effect, source, target)) {

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
     * miss% of taken attacks by source
     *
     * @param fight
     * @param source
     * @return
     */
    public static double takenMissPrecentage(Fight fight, String source) {
        int all = 0;
        int misses = 0;
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, "Damage", source, fight.getOwner())) {
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
            if (paramsCorretOrNull(r, effect, source, target)) {
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
     * returns a map where the key is source, and value is dmg/heal dealt by
     * that source.
     * <p>
     * If target/source doesn't matter, use null
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return
     */
    public static HashMap<String, Integer> divideEffectSumBySource(Fight fight, String effect, String source, String target) {
        HashMap<String, Integer> results = new HashMap();
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, effect, source, target)) {
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

    /**
     * returns a map where the key is target, and value is dmg/heal dealt by
     * that target. If target/source doesn't matter, use null
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
            if (paramsCorretOrNull(r, effect, source, target)) {
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
            if (paramsCorretOrNull(r, effect, source, target)) {
                sum += r.getDmgHeal();
                totalDone.put(r.getTimestamp(), sum);
            }

        }
        return totalDone;

    }

    /**
     * returns a new fight which only contains rows with a specfic effect
     * (heal/dmg) if target/source doesn't matter use null
     *
     * @param fight
     * @param effect
     * @param target
     * @return
     */
    protected static Fight rowsWithParams(Fight fight, String effect, String source, String target) {
        ArrayList<Row> specficRows = new ArrayList();
        for (Row r : fight.getRows()) {
            if (paramsCorretOrNull(r, effect, source, target)) {
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
    public static ArrayList<Tuple> totalHpsByTimeAgainstTarget(Fight fight, String target) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = rowsWithParams(fight, "Heal", fight.getOwner(), target).getRows(); //Getting only rows with dmg in them to take this a little simpler
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
    public static ArrayList<Tuple> totalDpsByTime(Fight fight) {
        LocalTime start = fight.getStart();
        ArrayList<Row> rows = rowsWithParams(fight, "Damage", fight.getOwner(), null).getRows(); //Getting only rows with dmg in them to take this a little simpler
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
     * return a list of tuples, tuple first member is timestamp, second member
     * effect sum up to that point
     *
     * @param rows , must contain only effect rows
     * @return
     */
    private static ArrayList<Tuple> effectSumByTime(ArrayList<Row> rows) {
        LocalTime start = rows.get(0).getTimestamp();

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
    public static ArrayList<Tuple> totalDpsByTimeAgainstTarget(Fight fight, String target) {
        ArrayList<Row> rows = rowsWithParams(fight, "Damage", fight.getOwner(), target).getRows(); //Getting only rows with dmg in them to take this a little simpler
        return effectSumByTime(rows);
    }

    /**
     * Returns a list of Tuples containing momentary dps or hps,meaning dps/hps
     * in last 10 seconds (use params "Damage" or "Heal" for effect) from given
     * source at a given time against a target
     * <p>
     * for all targets or sources use null.
     *
     * <p>
     * Tuple format: timestamp, dps
     *
     * @param fight
     * @param effect
     * @param source
     * @param target
     * @return <Tuple<LocalTime, Double>>
     */
    //TODO check what happens with Fight shorter than 10 seconds
    public static ArrayList<Tuple<LocalTime, Double>> momentaryEffectAgainstTarget(Fight fight, String effect, String source, String target) {
        Fight onlyEffectRows = Stats.rowsWithParams(fight, effect, source, target);
        return momentaryEffect(onlyEffectRows);
    }

    /**
     * calcluates momentary dps/hps/dtps (meaning effect in last 10 s) from a
     * list of rows containing only rows of that effect
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
        //This currently a inefficient way of doing this, but atm it's not necessary to make this faster
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

}
