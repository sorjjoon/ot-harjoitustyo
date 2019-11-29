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
import myparser.myparser.types.Type;

/**
 *
 * @author joona
 */
public class Stats {

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
        int timeS = (int)  timeMs / 1000;
        return (double)  dmg / timeS;
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

    public static long getDurationMs(Fight fight) {
        ArrayList<Row> rows = fight.getRows();
        LocalTime start = rows.get(0).getTimestamp();
        LocalTime end = rows.get(rows.size() - 1).getTimestamp();
        long time = start.until(end, MILLIS);
        if (time < 0) {
            time += 8.64e+7;    //amount of ms in a day
        }
        return time;
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

    //TODO make this class more efficient and more readable
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

}
