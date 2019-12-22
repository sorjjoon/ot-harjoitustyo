/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.stats;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.types.EventType;

/**
 *
 * Contains all static methods for Stats regarding ability activations
 *
 */
public abstract class ActivationStats {

    /**
     * map key is ability name, value the max time between activations for that
     * ability
     *
     * abilites with 1 usage have null as value
     *
     * @param fight
     * @return
     */
    public static HashMap<String, Double> maxTimeBetweenActivations(Fight fight) {
        HashMap<String, LocalTime> lastTime = new HashMap(); //Helper map, to store timestamp of last activation
        HashMap<String, Double> ret = new HashMap(); //returned map
        ArrayList<AbilityActivation> activations = getActivations(fight);
        for (AbilityActivation a : activations) {
            String name = a.getAbilityName();
            if (lastTime.get(name) == null) {
                lastTime.put(name, a.getActivation());
                ret.put(name, null); //to make sure we get abilites with only 1 activation into the keyset
            } else if (ret.get(name) == null || ret.get(name) < Double.valueOf(Stats.getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0) {
                ret.put(name, Double.valueOf(Stats.getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0);
                lastTime.put(name, a.getActivation());
            } else {
                lastTime.put(name, a.getActivation());
            }
        }
        return ret;
    }

    /**
     * map key is ability name, value the min time between activations for that
     * ability.
     * <p>
     * Abilites which have only 1 use have null as value
     *
     * @param fight
     * @return
     */
    public static HashMap<String, Double> minTimeBetweenActivations(Fight fight) {
        ArrayList<AbilityActivation> activations = getActivations(fight);
        HashMap<String, LocalTime> lastTime = new HashMap(); //Helper map, to store timestamp of last activation
        HashMap<String, Double> ret = new HashMap(); //returned map
        for (AbilityActivation a : activations) {
            String name = a.getAbilityName();

            if (lastTime.get(name) == null) { //This is triggered ONLY by the first usage of ability
                lastTime.put(name, a.getActivation());

                ret.put(name, null); //to make sure we get abilites with only 1 activation into the keyset

                //ret.get(name) == null is here to put the first time between activations in (so 2. usage of ability)
            } else if (ret.get(name) == null || ret.get(name) > Double.valueOf(Stats.getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0) {
                ret.put(name, Double.valueOf(Stats.getDiffrence(lastTime.get(name), a.getActivation())) / 1000.0);
                lastTime.put(name, a.getActivation());
            } else {
                lastTime.put(name, a.getActivation());
            }
        }
        return ret;
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
            if (r.getEventtype() == EventType.AbilityActivate) {
                activations.add(new AbilityActivation(r));
            }
        }
        return activations;
    }

    /**
     * returns an array with 2 maps, map at index 0 is with ability name as key,
     * value number of times between uses (so number of uses -1)
     * <p>
     * map at index 1 avgMap, key ability name, value avg time between uses for
     * that ability ability
     *
     * @param fight
     * @return array[count, avg]
     */
    public static HashMap<String, Double>[] avgTimeBetweenActivations(Fight fight) {
        HashMap<String, LocalTime> lastTime = new HashMap(); //Helper map, to store timestamp of last activation
        HashMap<String, Double> sums = new HashMap();
        HashMap<String, Double> count = new HashMap(); //double because makes using the value possible for Columndata
        ArrayList<AbilityActivation> activations = getActivations(fight);
        for (AbilityActivation a : activations) {
            String name = a.getAbilityName();
            if (lastTime.get(name) == null) {
                lastTime.put(name, a.getActivation());
                sums.put(name, 0.0);
                count.put(name, 0.0); //0, because we are counting times between activations, not activations,
            } else {
                sums.put(name, sums.get(name) + (double) Stats.getDiffrence(lastTime.get(name), a.getActivation()) / 1000);
                count.put(name, count.get(name) + 1);
                lastTime.put(name, a.getActivation());
            }
        }
        return createArray(count, sums);
    }

    /**
     * This method is here to fit checkstyle for avgTimeBetweenactivations
     * creates and array with 0 as count map, 1 as avg time map
     *
     * @param count
     * @param sums
     * @return HashMap<String, Double> []
     */
    private static HashMap<String, Double>[] createArray(HashMap<String, Double> count, HashMap<String, Double> sums) {
        HashMap<String, Double>[] ret = new HashMap[2];
        HashMap<String, Double> avgMap = new HashMap();
        ret[0] = count;
        for (String s : sums.keySet()) {
            if (count.get(s) == 0) {
                avgMap.put(s, null);
            } else {
                avgMap.put(s, sums.get(s) / count.get(s));
            }
        }
        ret[1] = avgMap;
        return ret;
    }

}
