package myparser.myparser.ui;

import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import myparser.myparser.domain.Fight;
import myparser.myparser.stats.ActivationStats;
import myparser.myparser.stats.Stats;
import myparser.myparser.stats.Tuple;
import myparser.myparser.types.DamageType;

/**
 *
 * Formats all data created calculated using Stats to Strings (with 2 decimal
 * places)
 * <p>
 * Creates HashMaps for target, dmg/heal pairs used by the gui
 * <p>
 * Ability breakdown map values are not formated, but use Integers (because
 * linechart and piechart can't use Strings)
 * <p>
 * Momentary breakdown map values are not formated, but use Doubles (because
 * linechart can't use Strings)
 * <p>
 * activation map values are not formated here, but in Databasecolumn when
 * inserting to tableview
 * <p>
 * <p>
 * to see all an explanation for a stat, see that stat's getter
 */
public class Analysis {

    //Overview tab
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
    private final HashMap<String, Integer> dmgBreakdownByTarget;

    private final HashMap<String, String> dmgAvgBreakdownByTarget;
    private final HashMap<String, String> dpsBreakdownByTarget;
    private final HashMap<String, String> dmgHitPrecentageAgainstTarget;
    private final HashMap<String, String> dmgCritBreakdownByTarget;
    private final HashMap<String, String> dmgBigBreakdownByTarget;
    private final HashMap<String, String> dmgHitsDoneAgainstTarget;
    private final HashMap<String, String> dmgTotalPrecentageByTarget;

    private final HashMap<String, Integer> dmgAbilityBreakdown;

    private final HashMap<String, HashMap<String, Integer>> dmgAbilityBreakdownByTarget;

    private final HashMap<LocalTime, Integer> dmgCumulative;

    private final HashMap<String, HashMap<LocalTime, Integer>> dmgCumulativeBreakdownByTarget;

    private final ArrayList<Tuple> totalDpsByTime;
    private final HashMap<String, ArrayList<Tuple>> totaldpsByTimeBreakdownByTarget;

    private final DecimalFormat df;
    private final ArrayList<Tuple<LocalTime, Double>> momentaryDpsByTime;
    private final HashMap<String, ArrayList<Tuple<LocalTime, Double>>> momentarydpsByTimeBreakdownByTarget;

    //healing tab
    private final HashMap<String, Integer> healBreakdownByTarget;

    private final HashMap<String, String> healAvgBreakdownByTarget;
    private final HashMap<String, String> hpsBreakdownByTarget;
    private final HashMap<String, String> healCritBreakdownByTarget;
    private final HashMap<String, String> healBigBreakdownByTarget;
    private final HashMap<String, String> healHitsDoneAgainstTarget;
    private final HashMap<String, String> healTotalPrecentageByTarget;

    private final HashMap<String, Integer> healAbilityBreakdown;

    private final HashMap<String, HashMap<String, Integer>> healAbilityBreakdownByTarget;

    private final HashMap<LocalTime, Integer> healCumulative;

    private final HashMap<String, HashMap<LocalTime, Integer>> healCumulativeBreakdownByTarget;

    private final ArrayList<Tuple> totalHpsByTime;
    private final HashMap<String, ArrayList<Tuple>> totalHpsByTimeBreakdownByTarget;

    private final ArrayList<Tuple<LocalTime, Double>> momentaryHpsByTime;
    private final HashMap<String, ArrayList<Tuple<LocalTime, Double>>> momentaryHpsByTimeBreakdownByTarget;

    private final HashMap<String, Double> minActivations;

    private final HashMap<String, Double> avgActivations;
    private final HashMap<String, Double> maxActivations;
    private final HashMap<String, Double> countActivations;

    private final HashMap<String, HashMap<DamageType, Integer>> dmgTakenBreakdownByType;

    private final HashMap<String, String> dmgTakenHitPrecentage;

    private final HashMap<String, Integer> dmgTakenBreakdownBySource;

    private final HashMap<String, String> dmgTakenAvgBreakdownBySource;
    private final HashMap<String, String> dmgTakenCritBreakdownBySource;
    private final HashMap<String, String> dmgTakenBigBreakdownBySource;
    private final HashMap<String, String> dmgTakenHitsBySource;
    private final HashMap<String, String> dmgTakenTotalPrecentageBySource;
    private final HashMap<String, String> dtpsBreakdownBySource;
    private final HashMap<String, HashMap<String, Integer>> dmgTakenBreakdownByAbility;


    /**
     * Formats all data created calculated using Stats to Strings (with 2
     * decimal places)
     * <p>
     * Creates HashMaps for target, dmg/heal pairs used by the gui
     * <p>
     * Ability breakdown map values are not formated, but use Integers (because
     * linechart and piechart can't use Strings)
     * <p>
     * Momentary breakdown map values are not formated, but use Doubles (because
     * linechart can't use Strings)
     * <p>
     * activation map values are not formated here, but in Databasecolumn when
     * inserting to tableview
     * <p>
     * <p>
     * see a list of getters for this class for a list of all stats generated
     *
     * @param f Fight
     */
    public Analysis(Fight f) {

        this.df = new DecimalFormat("#.##");
        //dmg taken tab
//        this.takenAnalysis= new DamageTakenAnalysis(f,df);

        //Overview
        this.owner = f.getOwner().substring(1, f.getOwner().length());    //Removing @ from char name
        this.start = f.getStart().toString();
        this.end = f.getEnd().toString();
        this.APM = df.format(Stats.apm(f));
        this.duration = String.valueOf((double) Stats.durationMs(f) / 1000);
        //Damage
        this.allDmgDone = String.valueOf(Stats.sumOfEffect(f, "Damage", f.getOwner(), null));
        this.dps = df.format(Stats.dps(f));
        this.hitPrecentage = df.format((1 - Stats.missPrecentage(f, null)) * 100);
        this.dmgCritPrecentage = df.format(Stats.critPrecentage(f, "Damage", f.getOwner(), null) * 100);
        this.biggestHit = df.format(Stats.biggestEffect(f, "Damage", f.getOwner(), null));
        this.averageDmg = df.format(Stats.averageSizeOfEffect(f, "Damage", f.getOwner(), null));
        this.hits = String.valueOf(Stats.effectCount(f, "Damage", f.getOwner(), null));

        //dmg taken
        this.dtps = df.format(Stats.dtps(f));
        this.allDmgTaken = String.valueOf(Stats.sumOfEffect(f, "Damage", null, f.getOwner()));
        this.hitsTaken = String.valueOf(Stats.effectCount(f, "Damage", null, f.getOwner()));
        this.avgTakenHit = df.format(Stats.averageSizeOfEffect(f, "Damage", null, f.getOwner()));
        this.bigTakenHit = String.valueOf(Stats.biggestEffect(f, "Damage", null, f.getOwner()));
        this.takenCrits = df.format(Stats.critPrecentage(f, "Damage", null, f.getOwner()) * 100);
        this.takenHitPrecentage = df.format((1 - Stats.takenMissPrecentage(f, null)) * 100);

        //healing done
        this.allHealDone = String.valueOf(Stats.sumOfEffect(f, "Heal", f.getOwner(), null));
        this.hps = df.format(Stats.hps(f));
        this.healCount = String.valueOf(Stats.effectCount(f, "Heal", f.getOwner(), null));
        this.avgHeal = df.format(Stats.averageSizeOfEffect(f, "Heal", f.getOwner(), null));
        this.bigHeal = String.valueOf(Stats.biggestEffect(f, "Heal", f.getOwner(), null));
        this.healCritPrecentage = df.format(Stats.critPrecentage(f, "Heal", f.getOwner(), null) * 100);

        //Healing recieved
        this.allHealTaken = String.valueOf(Stats.sumOfEffect(f, "Heal", null, f.getOwner()));
        this.htps = df.format(Stats.htps(f));
        this.bigTakenHeal = String.valueOf(Stats.biggestEffect(f, "Damage", null, f.getOwner()));
        this.takenHealCritPrecentage = df.format(Stats.critPrecentage(f, "Heal", null, f.getOwner()) * 100);
        this.averageHealTaken = df.format(Stats.averageSizeOfEffect(f, "Heal", null, f.getOwner()));
        this.healTakenCount = String.valueOf(Stats.effectCount(f, "Heal", null, f.getOwner()));

        //Threat
        this.totalThreat = String.valueOf(Stats.totalThreat(f));
        this.tps = df.format(Stats.tps(f));

        //Dmg Tab, creating hashmaps for all targets
        this.dmgBreakdownByTarget = Stats.divideEffectSumByTarget(f, "Damage", f.getOwner(), null);
        this.dpsBreakdownByTarget = createPerSecondHashMap(f, dmgBreakdownByTarget, df);

        this.dmgBigBreakdownByTarget = new HashMap();

        for (String s : this.dmgBreakdownByTarget.keySet()) {
            this.dmgBigBreakdownByTarget.put(s, String.valueOf(Stats.biggestEffect(f, "Damage", f.getOwner(), s)));
        }

        dmgAvgBreakdownByTarget = new HashMap();
        for (String s : this.dmgBreakdownByTarget.keySet()) {
            this.dmgAvgBreakdownByTarget.put(s, df.format(Stats.averageSizeOfEffect(f, "Damage", f.getOwner(), s)));
        }

        dmgCritBreakdownByTarget = new HashMap();
        for (String s : dmgBreakdownByTarget.keySet()) {
            this.dmgCritBreakdownByTarget.put(s, df.format(Stats.critPrecentage(f, "Damage", f.getOwner(), null) * 100));

        }

        dmgHitsDoneAgainstTarget = new HashMap();
        for (String s : dmgBreakdownByTarget.keySet()) {
            this.dmgHitsDoneAgainstTarget.put(s, String.valueOf(Stats.effectCount(f, "Damage", f.getOwner(), s)));

        }

        dmgHitPrecentageAgainstTarget = new HashMap();
        for (String s : dmgBreakdownByTarget.keySet()) {
            this.dmgHitPrecentageAgainstTarget.put(s, df.format((1 - Stats.missPrecentage(f, s)) * 100));

        }

        dmgTotalPrecentageByTarget = new HashMap();
        for (String s : dmgBreakdownByTarget.keySet()) {
            double percentage = (double) dmgBreakdownByTarget.get(s) / Integer.valueOf(allDmgDone);
            percentage *= 100;
            dmgTotalPrecentageByTarget.put(s, df.format(percentage));
        }

        dmgAbilityBreakdown = Stats.divideEffectSumByAbilityF(f, "Damage", f.getOwner(), null);

        dmgAbilityBreakdownByTarget = new HashMap();
        for (String s : dmgBreakdownByTarget.keySet()) {
            dmgAbilityBreakdownByTarget.put(s, Stats.divideEffectSumByAbilityF(f, "Damage", f.getOwner(), s));
        }

        dmgCumulative = Stats.cumulativeEffectDoneAgainstTarget(f, "Damage", f.getOwner(), null);

        dmgCumulativeBreakdownByTarget = new HashMap();

        for (String s : dmgBreakdownByTarget.keySet()) {
            dmgCumulativeBreakdownByTarget.put(s, Stats.cumulativeEffectDoneAgainstTarget(f, "Damage", f.getOwner(), s));
        }

        totalDpsByTime = Stats.totalDpsByTime(f);

        totaldpsByTimeBreakdownByTarget = new HashMap();
        for (String s : dmgBreakdownByTarget.keySet()) {
            totaldpsByTimeBreakdownByTarget.put(s, Stats.totalDpsByTimeAgainstTarget(f, s));
        }

        momentaryDpsByTime = Stats.momentaryEffectAgainstTarget(f, "Damage", f.getOwner(), null);

        momentarydpsByTimeBreakdownByTarget = new HashMap();

        for (String s : dmgBreakdownByTarget.keySet()) {
            momentarydpsByTimeBreakdownByTarget.put(s, Stats.momentaryEffectAgainstTarget(f, "Damage", f.getOwner(), s));
        }

        //Heal tab
        this.healBreakdownByTarget = Stats.divideEffectSumByTarget(f, "Heal", f.getOwner(), null);
        this.hpsBreakdownByTarget = createPerSecondHashMap(f, healBreakdownByTarget, df);

        this.healBigBreakdownByTarget = new HashMap();

        for (String s : this.healBreakdownByTarget.keySet()) {
            this.healBigBreakdownByTarget.put(s, String.valueOf(Stats.biggestEffect(f, "Heal", f.getOwner(), s)));
        }

        healAvgBreakdownByTarget = new HashMap();
        for (String s : this.healBreakdownByTarget.keySet()) {
            this.healAvgBreakdownByTarget.put(s, df.format(Stats.averageSizeOfEffect(f, "Heal", f.getOwner(), s)));
        }

        healCritBreakdownByTarget = new HashMap();
        for (String s : healBreakdownByTarget.keySet()) {
            this.healCritBreakdownByTarget.put(s, df.format(Stats.critPrecentage(f, "Heal", f.getOwner(), s) * 100));

        }

        healHitsDoneAgainstTarget = new HashMap();
        for (String s : healBreakdownByTarget.keySet()) {
            this.healHitsDoneAgainstTarget.put(s, String.valueOf(Stats.effectCount(f, "Heal", f.getOwner(), s)));

        }

        healTotalPrecentageByTarget = new HashMap();
        for (String s : healBreakdownByTarget.keySet()) {
            double percentage = (double) healBreakdownByTarget.get(s) / Integer.valueOf(allHealDone);
            percentage *= 100;
            healTotalPrecentageByTarget.put(s, df.format(percentage));
        }

        healAbilityBreakdown = Stats.divideEffectSumByAbilityF(f, "Heal", f.getOwner(), null);

        healAbilityBreakdownByTarget = new HashMap();
        for (String s : healBreakdownByTarget.keySet()) {
            healAbilityBreakdownByTarget.put(s, Stats.divideEffectSumByAbilityF(f, "Heal", f.getOwner(), s));
        }

        healCumulative = Stats.cumulativeEffectDoneAgainstTarget(f, "Heal", f.getOwner(), null);

        healCumulativeBreakdownByTarget = new HashMap();

        for (String s : healBreakdownByTarget.keySet()) {
            healCumulativeBreakdownByTarget.put(s, Stats.cumulativeEffectDoneAgainstTarget(f, "Heal", f.getOwner(), s));
        }

        totalHpsByTime = Stats.totalHpsByTimeAgainstTarget(f, null);

        totalHpsByTimeBreakdownByTarget = new HashMap();
        for (String s : healBreakdownByTarget.keySet()) {
            totalHpsByTimeBreakdownByTarget.put(s, Stats.totalHpsByTimeAgainstTarget(f, s));
        }

        momentaryHpsByTime = Stats.momentaryEffectAgainstTarget(f, "Heal", f.getOwner(), null);

        momentaryHpsByTimeBreakdownByTarget = new HashMap();

        for (String s : healBreakdownByTarget.keySet()) {
            momentaryHpsByTimeBreakdownByTarget.put(s, Stats.momentaryEffectAgainstTarget(f, "Heal", f.getOwner(), s));
        }

        //Ability activations
        minActivations = ActivationStats.minTimeBetweenActivations(f);

        maxActivations = ActivationStats.maxTimeBetweenActivations(f);
        HashMap<String, Double>[] temp = ActivationStats.avgTimeBetweenActivations(f);
        avgActivations = temp[1];

        countActivations = temp[0];

        //dmg taken tab
        dmgTakenBreakdownBySource = Stats.divideEffectSumBySource(f, "Damage", null, f.getOwner());
        

        dmgTakenBreakdownByType = new HashMap();
        dmgTakenBreakdownByType.put("All", Stats.dmgTakenByType(f, null, f.getOwner()));
        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenBreakdownByType.put(s, Stats.dmgTakenByType(f, s, f.getOwner()));
        }
        dmgTakenAvgBreakdownBySource = new HashMap();
        dmgTakenAvgBreakdownBySource.put("All", df.format(Stats.averageSizeOfEffect(f, "Damage", null, f.getOwner())));
        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenAvgBreakdownBySource.put(s, df.format(Stats.averageSizeOfEffect(f, "Damage", s, f.getOwner())));
        }

        dmgTakenCritBreakdownBySource = new HashMap();
        dmgTakenCritBreakdownBySource.put("All", df.format((double) 100 * Stats.critPrecentage(f, "Damage", null, f.getOwner())));
        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenCritBreakdownBySource.put(s, df.format((double) 100 * Stats.critPrecentage(f, "Damage", s, f.getOwner())));
        }

        dmgTakenBigBreakdownBySource = new HashMap();
        dmgTakenBigBreakdownBySource.put("All", String.valueOf(Stats.biggestEffect(f, "Damage", null, f.getOwner())));
        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenBigBreakdownBySource.put(s, String.valueOf(Stats.biggestEffect(f, "Damage", s, f.getOwner())));
        }

        dmgTakenHitsBySource = new HashMap();
        dmgTakenHitsBySource.put("All", String.valueOf(Stats.effectCount(f, "Damage", null, f.getOwner())));
        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenHitsBySource.put(s, String.valueOf(Stats.effectCount(f, "Damage", s, f.getOwner())));
        }

        dmgTakenTotalPrecentageBySource = new HashMap();
        dmgTakenTotalPrecentageBySource.put("All", "100");

        int allTaken = Integer.valueOf(allDmgTaken);

        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenTotalPrecentageBySource.put(s, df.format((double) 100 * dmgTakenBreakdownBySource.get(s) / allTaken));
        }

        dmgTakenHitPrecentage = new HashMap();
        dmgTakenHitPrecentage.put("All", takenHitPrecentage);

        for (String s : dmgTakenBreakdownBySource.keySet()) {
            dmgTakenHitPrecentage.put(s, df.format((double) 100 * (1 - Stats.takenMissPrecentage(f, s))));
        }

        this.dtpsBreakdownBySource = createPerSecondHashMap(f, dmgTakenBreakdownBySource, df);

        this.dmgTakenBreakdownByAbility = new HashMap();
        this.dmgTakenBreakdownByAbility.put("All", Stats.divideEffectSumByAbilityF(f, "Damage", null, f.getOwner()));
        for (String s : dmgTakenBreakdownBySource.keySet()) {
            this.dmgTakenBreakdownByAbility.put(s, Stats.divideEffectSumByAbilityF(f, "Damage", s, f.getOwner()));

        }

    }

    /**
     * Return contains 2 maps, inner and outer map
     * <p>
     * outer map key is dmg source, value is inner map
     * <p>
     * inner map key is ability name, value dmg taken from that ability (by that
     * source)
     * <p>
     * outer map also has "All" key, which contains a map for a breakdown of all
     * dmg taken
     *
     * @return
     */
    public HashMap<String, HashMap<String, Integer>> getDmgTakenBreakdownByAbility() {
        return dmgTakenBreakdownByAbility;
    }

    /**
     * Map key is source name, value damage taken per second from that source
     *
     * @return
     */
    public HashMap<String, String> getDtpsBreakdownBySource() {
        return dtpsBreakdownBySource;
    }

    /**
     * Map key is source, value is another map.
     * <p>
     * Has additional key "All" which contains map for all dmg taken
     * <p>
     * inner map key is DamageType, value is dmg taken by that type
     *
     * @return
     */
    public HashMap<String, HashMap<DamageType, Integer>> getDmgTakenBreakdownByTypeAndBySource() {
        return dmgTakenBreakdownByType;
    }

    /**
     * Map key is source, value is hit precentage by that source (formated to
     * String)
     * .<p>
     * Has additional key "All" which contains hit precentage all dmg taken
     *
     * @return
     */
    public HashMap<String, String> getDmgTakenHitPrecentageBySource() {
        return dmgTakenHitPrecentage;
    }

    /**
     * Map key is source map value all dmg done by that source.
     *
     * @return
     */
    public HashMap<String, Integer> getDmgTakenBreakdownBySource() {
        return dmgTakenBreakdownBySource;
    }

    /**
     * Map key is source, map value is the average hit done by that source.<p>
     * Has additional key "All" which contains avg for all dmg taken
     *
     * @return
     */
    public HashMap<String, String> getDmgTakenAvgBreakdownBySource() {
        return dmgTakenAvgBreakdownBySource;
    }

    /**
     * map key is source, map value is crit% by that source
     * <p>
     * Has additional key "All" which contains crit% for all dmg taken
     *
     * @return
     */
    public HashMap<String, String> getDmgTakenCritBreakdownBySource() {
        return dmgTakenCritBreakdownBySource;
    }

    /**
     * map key is source, map value is biggest hit by that source
     * <p>
     * Has additional key "All" which contains biggest taken hit from any source
     *
     * @return
     */
    public HashMap<String, String> getDmgTakenBigBreakdownBySource() {
        return dmgTakenBigBreakdownBySource;
    }

    /**
     * map key is source. Map value is he number of hits done by that source
     * <p>
     * Has additional key "All" which for all hits taken
     *
     * @return
     */
    public HashMap<String, String> getDmgTakenHitsBySource() {
        return dmgTakenHitsBySource;
    }

    /**
     * map key is source, map value is how large of a precentage that source's
     * dmg makes up from alll dmg taken. Formated to String, and precentage
     * points (20, not 0.2)
     * <p>
     * Has additional key "All" which contains "100"
     *
     * @return
     */
    public HashMap<String, String> getDmgTakenTotalPrecentageByTarget() {
        return dmgTakenTotalPrecentageBySource;
    }

    /**
     * Map key is target name, value the sum of all healing done to that target
     *
     * @return
     */
    public HashMap<String, Integer> getHealBreakdownByTarget() {
        return healBreakdownByTarget;
    }

    /**
     * map key is ability name, value is smallest time (in seconds) between uses
     * of the ability
     *
     * @return
     */
    public HashMap<String, Double> getMinActivations() {
        return minActivations;
    }

    /**
     * map key is ability name, values is average time (in seconds) between uses
     * of that ability
     *
     * @return
     */
    public HashMap<String, Double> getAvgActivations() {
        return avgActivations;
    }

    /**
     * map key is ability name, values is longest time (in seconds) between uses
     * of that ability
     *
     * @return
     */
    public HashMap<String, Double> getMaxActivations() {
        return maxActivations;
    }

    /**
     * Map key is target name, value is the average size of a heal againsy that
     * target (formated to String)
     *
     * @return
     */
    public HashMap<String, String> getHealAvgBreakdownByTarget() {
        return healAvgBreakdownByTarget;
    }

    /**
     * Map key is target name, value the total hps against that target (over the
     * whole fight), formated to String
     *
     * @return
     */
    public HashMap<String, String> getHpsBreakdownByTarget() {
        return hpsBreakdownByTarget;
    }

    /**
     * Map key is target name, value crit% against target, formated to String,
     * and to percentage points (20 instead of 0.2)
     *
     * @return
     */
    public HashMap<String, String> getHealCritBreakdownByTarget() {
        return healCritBreakdownByTarget;
    }

    /**
     * Map key is target name, value largest heal against that target, formated
     * to String
     *
     * @return
     */
    public HashMap<String, String> getHealBigBreakdownByTarget() {
        return healBigBreakdownByTarget;
    }

    /**
     * Map key is target name, value the number of heals against target,
     * formated to String
     *
     * @return
     */
    public HashMap<String, String> getHealHitsDoneAgainstTarget() {
        return healHitsDoneAgainstTarget;
    }

    /**
     * Map key is target name, value how large of a percentage does healing
     * against this target make up of the total healing done, formated to
     * String, formated to precetnage points (20 instead of 0.2)
     *
     * @return
     */
    public HashMap<String, String> getHealTotalPrecentageByTarget() {
        return healTotalPrecentageByTarget;
    }

    /**
     * Map key is ability name, value the total healing done by that ablity, NOT
     * formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<String, Integer> getHealAbilityBreakdown() {
        return healAbilityBreakdown;
    }

    /**
     * Return contains Map within a map (outer map and inner map)
     * <p>
     * Outer Map key is target name, value is another map.
     * <p>
     * Inner map key ability name, value total healing done by that ability ,NOT
     * formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<String, HashMap<String, Integer>> getHealAbilityBreakdownByTarget() {

        return healAbilityBreakdownByTarget;
    }

    /**
     * Map key is timestamp (LocalTime), value total healing done up to that
     * point, NOT formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<LocalTime, Integer> getHealCumulative() {
        return healCumulative;
    }

    /**
     * Return contains Map within a map (outer map and inner map)
     * <p>
     * Outer Map key is target name, value is another map.
     * <p>
     * Inner map key timestamp, value total healing done up to that point ,NOT
     * formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<String, HashMap<LocalTime, Integer>> getHealCumulativeBreakdownByTarget() {

        return healCumulativeBreakdownByTarget;
    }

    /**
     * returns a list of tuples (not map to maintain order). Tuple first member
     * is timestamp, second member healing per second, at that point in time
     * <p>
     * meaning all healing done up to that point, divided by fight duration at
     * that point
     *
     * @return
     */
    public ArrayList<Tuple> getTotalHpsByTime() {
        return totalHpsByTime;
    }

    /**
     * Map key is target name, value a list of tuples (not map to maintain
     * order). Tuple first member is timestamp, second member healing per
     * second, at that point in time
     * <p>
     * meaning all healing done up to that point, divided by fight duration at
     * that point
     *
     * @return
     */
    public HashMap<String, ArrayList<Tuple>> getTotalHpsByTimeBreakdownByTarget() {
        return totalHpsByTimeBreakdownByTarget;
    }

    /**
     *
     * returns a list of tuples (not map to maintain order). Tuple first member
     * is timestamp, second member momentary healing per second, at that point
     * in time
     * <p>
     * momentary meaning all healing done within the last 10 seconds
     *
     * @return
     */
    public ArrayList<Tuple<LocalTime, Double>> getMomentaryHpsByTime() {
        return momentaryHpsByTime;
    }

    /**
     * map key is target name
     * <p>
     * value a list of tuples (not map to maintain order). Tuple first member is
     * timestamp, second member momentary healing per second, at that point in
     * time
     * <p>
     * momentary meaning all healing done within the last 10 seconds
     *
     * @return
     */
    public HashMap<String, ArrayList<Tuple<LocalTime, Double>>> getMomentaryHpsByTimeBreakdownByTarget() {
        return momentaryHpsByTimeBreakdownByTarget;
    }

    /**
     *
     * returns a list of tuples (not map to maintain order). Tuple first member
     * is timestamp, second member momentary damage per second, at that point in
     * time
     * <p>
     * momentary meaning all damage done within the last 10 seconds
     *
     * @return
     */
    public ArrayList<Tuple<LocalTime, Double>> getMomentaryDpsByTime() {
        return momentaryDpsByTime;
    }

    /**
     * map key is target name
     * <p>
     * value a list of tuples (not map to maintain order). Tuple first member is
     * timestamp, second member momentary damage per second, at that point in
     * time
     * <p>
     * momentary meaning all damage done within the last 10 seconds
     *
     * @return
     */
    public HashMap<String, ArrayList<Tuple<LocalTime, Double>>> getMomentarydpsByTimeBreakdownByTarget() {
        return momentarydpsByTimeBreakdownByTarget;
    }

    /**
     * map key is target name, value a list of tuples
     * <p>
     * tuple first member is timestamp, second member total dps against target
     * up to that point
     *
     * @return
     */
    public HashMap<String, ArrayList<Tuple>> getTotaldpsByTimeBreakdownByTarget() {
        return totaldpsByTimeBreakdownByTarget;
    }

    /**
     * returns a list of tuples (not map to maintain order). Tuple first member
     * is timestamp, second member damage per second, at that point in time
     * <p>
     * meaning all damage done up to that point, divided by fight duration at
     * that point
     *
     * @return
     */
    public ArrayList<Tuple> getTotalDpsByTime() {
        return totalDpsByTime;
    }

    /**
     * return the DecimalFormat used by this class
     *
     * @return
     */
    public DecimalFormat getDf() {
        return df;
    }

    /**
     * Map key is timestamp (LocalTime), value total damage done up to that
     * point, NOT formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<LocalTime, Integer> getDmgCumulative() {
        return dmgCumulative;
    }

    /**
     * Return contains Map within a map (outer map and inner map)
     * <p>
     * Outer Map key is target name, value is another map.
     * <p>
     * Inner map key is timestamp (LocalTime), value total damage done up to
     * that point, NOT formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<String, HashMap<LocalTime, Integer>> getDmgCumulativeBreakdownByTarget() {
        return dmgCumulativeBreakdownByTarget;
    }

    /**
     * Map key is ability name, value the total damage done by that ablity, NOT
     * formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<String, Integer> getDmgAbilityBreakdown() {
        return dmgAbilityBreakdown;
    }

    /**
     * Return contains Map within a map (outer map and inner map)
     * <p>
     * Outer Map key is target name, value is another map.
     * <p>
     * Inner map key ability name, value total damage done by that ability ,NOT
     * formated to String (so can be used by charts)
     *
     * @return
     */
    public HashMap<String, HashMap<String, Integer>> getDmgAbilityBreakdownByTarget() {
        return dmgAbilityBreakdownByTarget;
    }

    /**
     * Return contains Map within a map (outer map and inner map)
     * <p>
     * Outer Map key is target name, value is another map.
     * <p>
     * Inner map key the percentage dmg against that target makes up of all dmg
     * done formated to String and percentage points (20, not 0.2)
     *
     * @return
     */
    public HashMap<String, String> getDmgTotalPrecentageByTarget() {
        return dmgTotalPrecentageByTarget;
    }

    /**
     * Map key is target name, value hit percentage against target formated to
     * String and percentage points (20, not 0.2)
     *
     * @return
     */
    public HashMap<String, String> getDmgHitPrecentageAgainstTarget() {
        return dmgHitPrecentageAgainstTarget;
    }

    /**
     * a helper method for creating a per second hashmap
     * <p>
     *
     * <p>
     * Returned map key is target, value total dps (over whole fight) against
     * that target)
     *
     * @param f
     * @param breakDown
     * @param df
     * @return
     */
    private static HashMap<String, String> createPerSecondHashMap(Fight f, HashMap<String, Integer> breakDown, DecimalFormat df) {

        //Variable names have "dps", but same method can be used for hps or dtps as well, just named this way for clarity
        HashMap<String, String> dpsMap = new HashMap();
        int durationS = (int) Stats.durationMs(f) / 1000;
        for (String s : breakDown.keySet()) {
            double dps = (double) breakDown.get(s) / durationS;
            dpsMap.put(s, df.format(dps));
        }
        return dpsMap;
    }

    /**
     * healing taken per second (for the whole fight) formated to string
     *
     * @return
     */
    public String getHps() {
        return hps;
    }

    /**
     * map key is target name value average size of a hit against that target
     *
     * @return
     */
    public HashMap<String, String> getDmgAvgBreakdownByTarget() {
        return dmgAvgBreakdownByTarget;
    }

    /**
     * map key is target name, value dps done againt target (for the whole
     * fight) formated to String
     *
     * @return
     */
    public HashMap<String, String> getDpsBreakdownByTarget() {
        return dpsBreakdownByTarget;
    }

    /**
     * map key is target name, value crit percentage against that target
     * formated to String and decimal points (29 no 0.29)
     *
     * @return
     */
    public HashMap<String, String> getDmgCritBreakdownByTarget() {
        return dmgCritBreakdownByTarget;
    }

    /**
     * map key is target name, value is largest hit against that target
     *
     * @return
     */
    public HashMap<String, String> getDmgBigBreakdownByTarget() {
        return dmgBigBreakdownByTarget;
    }

    /**
     * map key is target name, value number of hits against target, formated to
     * String
     *
     * @return
     */
    public HashMap<String, String> getDmgHitsDoneAgainstTarget() {
        return dmgHitsDoneAgainstTarget;
    }

    /**
     * largest heal (against any target). Doesn't count usable medpacks as
     * largest heal (as it would always e largest)
     *
     * @return
     */
    public String getBigHeal() {
        return bigHeal;
    }

    /**
     * map key is target name, value is total dmg done against target, not
     * formated to String,( can be used by charts)
     *
     * @return
     */
    public HashMap<String, Integer> getDmgBreakdownByTarget() {
        return dmgBreakdownByTarget;
    }

    /**
     * Total number of heals done
     *
     * @return
     */
    public String getHealCount() {
        return healCount;
    }

    /**
     * Average size of done heal
     *
     * @return
     */
    public String getAvgHeal() {
        return avgHeal;
    }

    /**
     * crit percentage of heals, formated to String and percentage points (20,
     * not 0.2)
     *
     * @return
     */
    public String getHealCritPrecentage() {
        return healCritPrecentage;
    }

    /**
     * total amount of healing taken
     *
     * @return
     */
    public String getAllHealTaken() {
        return allHealTaken;
    }

    /**
     * Average size of a taken heal formated to String
     *
     * @return
     */
    public String getAverageHealTaken() {
        return averageHealTaken;
    }

    /**
     * healing taken per second formated to String
     *
     * @return
     */
    public String getHtps() {
        return htps;
    }

    /**
     * number of taken heals
     *
     * @return
     */
    public String getHealTakenCount() {
        return healTakenCount;
    }

    /**
     * largest taken heal (doesn't count medpac)
     *
     * @return
     */
    public String getBigTakenHeal() {
        return bigTakenHeal;
    }

    /**
     * crit precentage of taken heals, formated to String and percentage points
     * (20, not 0.2)
     *
     * @return
     */
    public String getTakenHealCritPrecentage() {
        return takenHealCritPrecentage;
    }

    /**
     * Total generated threat
     *
     * @return
     */
    public String getTotalThreat() {
        return totalThreat;
    }

    /**
     * threat per second formated to String and percentage points (20, not 0.2)
     *
     * @return
     */
    public String getTps() {
        return tps;
    }

    /**
     * map key is ability name, value amount of times that ability was used
     *
     * @return
     */
    public HashMap<String, Double> getCountActivations() {
        return countActivations;
    }

    /**
     * number of hits done by owner
     *
     * @return
     */
    public String getHits() {
        return hits;
    }

    /**
     * Average size of done attack
     *
     * @return
     */
    public String getAverageDmg() {
        return averageDmg;
    }

    /**
     * damage (done) per second formated to String and percentage points (20,
     * not 0.2)
     *
     * @return
     */
    public String getDps() {
        return dps;
    }

    /**
     * Hit precentage (attacks done by owner) formated to String and percentage
     * points (20, not 0.2)
     *
     * @return
     */
    public String getHitPrecentage() {
        return hitPrecentage;
    }

    /**
     * crit precentage (of attacks by owner) formated to String and percentage
     * points (20, not 0.2)
     *
     * @return
     */
    public String getDmgCritPrecentage() {
        return dmgCritPrecentage;
    }

    /**
     * largets hit (done by owner)
     *
     * @return
     */
    public String getBiggestHit() {
        return biggestHit;
    }

    /**
     * name of the owner of the log (@ at the start of name removed)
     *
     * @return
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Total amount of dmg done
     *
     * @return
     */
    public String getAllDmgDone() {
        return allDmgDone;
    }

    /**
     * total amount of healing done
     *
     * @return
     */
    public String getAllHealDone() {
        return allHealDone;
    }

    /**
     * all dmg taken
     *
     * @return
     */
    public String getAllDmgTaken() {
        return allDmgTaken;
    }

    /**
     * Starting timestamp for the fight formated to String
     *
     * @return
     */
    public String getStart() {
        return start;
    }

    /**
     * ending timestamp for the fight
     *
     * @return
     */
    public String getEnd() {
        return end;
    }

    /**
     * actions per minute, formated to String
     *
     * @return
     */
    public String getAPM() {
        return APM;
    }

    /**
     * duration of the fight in seconds
     *
     * @return
     */
    public String getDuration() {
        return duration;
    }

    /**
     * damage taken per second
     *
     * @return
     */
    public String getDtps() {
        return dtps;
    }

    /**
     * amount of hits taken
     *
     * @return
     */
    public String getHitsTaken() {
        return hitsTaken;
    }

    /**
     * average size of a taken hit
     *
     * @return
     */
    public String getAvgTakenHit() {
        return avgTakenHit;
    }

    /**
     * largest taken hit
     *
     * @return
     */
    public String getBigTakenHit() {
        return bigTakenHit;
    }

    /**
     * amount of taken crits
     *
     * @return
     */
    public String getTakenCrits() {
        return takenCrits;
    }

    /**
     * crit precentage of taken hits formated to String and percentage points
     * (20, not 0.2)
     *
     * @return
     */
    public String getTakenHitPrecentage() {
        return takenHitPrecentage;
    }

}
