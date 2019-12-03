/*
 * To change this license header , choose License Headers in Project Properties.
 * To change this template file , choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.domain;

import myparser.myparser.types.Type;
import myparser.myparser.types.Eventtype;
import myparser.myparser.types.Damagetype;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.util.IllegalFormatException;
import java.util.Objects;

/**
 *
 * @author joona
 */
public class Row {

    private final LocalTime timestamp;
    private String source;
    private String target;
    private final Type type;
    private String effecttype;
    private Eventtype eventtype;
    private final String abilityName;
    private int dmgHeal;
    private int threat;   //TODO test this
    private boolean crit;
    private Damagetype dmgType; //this is not used in the current version
    private boolean shielded;
    private boolean miss;
    private final int rowNumber;

    public boolean isMiss() {
        return miss;
    }

    public Row(LocalTime timestamp, String source, String target, Type type, String effecttype, Eventtype eventtype, String abilityName, int dmgHeal, boolean crit, boolean shielded, boolean miss, int rowNumber) {
        this.timestamp = timestamp;
        this.source = source;
        this.target = target;
        this.type = type;
        this.effecttype = effecttype;
        this.eventtype = eventtype;
        this.abilityName = abilityName;
        this.dmgHeal = dmgHeal;
        this.crit = crit;
        this.shielded = shielded;
        this.miss = miss;
        this.rowNumber = rowNumber;
        this.threat = 0;
    }

    public Row(String rawline, int rowNumber) throws IndexOutOfBoundsException, NumberFormatException, IllegalArgumentException, EnumConstantNotPresentException {
        this.rowNumber = rowNumber;
        //TODO threat , dmgType
        //TODO pve names for source/target, + tests for pve source/target
        //these are values changed later if  needed
        this.miss = false;
        this.threat = 0;
        this.crit = false;
        this.shielded = false;
        String[] parts = rawline.split("\\] \\[");

        //removing useless chars from input String and storing the values from input String into the row object
        //Threat
        if (rawline.contains("<")) {
            try {
                this.threat = Integer.valueOf(rawline.substring(rawline.lastIndexOf("<") + 1, rawline.lastIndexOf(">")));
            } catch (Exception e) {
                System.out.println(e);
                //This should only happen if for some reason player name contains < and > (and the line being looked at doesn't contin threat

            }
        }

        //removing first [
        this.timestamp = LocalTime.parse(parts[0].substring(1));
        this.source = parts[1];
        this.target = parts[2];

        if (this.target.contains("{")) {
            this.target = this.target.substring(0, this.target.indexOf("{") - 1);
        }
        if (this.source.contains("{")) {
            this.source = this.source.substring(0, this.source.indexOf("{") - 1);
        }

        //removing id and empty space
        if (parts[3].isEmpty()) {
            this.abilityName = "";
        } else {
            this.abilityName = parts[3].substring(0, parts[3].indexOf("{") - 1);
        }
        this.type = Type.valueOf(parts[4].substring(0, parts[4].indexOf("{") - 1));
        int index = parts[4].indexOf(":");

        String afterType = parts[4].substring(index, parts[4].length() - 1);

        if (this.type == Type.Event) {
            this.eventtype = Eventtype.valueOf(afterType.substring(2, afterType.indexOf("{") - 1));
            this.effecttype = null;
        } else if (this.type == Type.ApplyEffect || Type.RemoveEffect == this.type) {
            this.eventtype = null;
            this.effecttype = afterType.substring(2, afterType.indexOf("{") - 1);

            //Check if  effecttype is dmg or heal
            //For some reason heal and dmg are in diffrent formats , reflected , aborbed dmg is in a diffrent format and a few heals ( such as resurrection) are in the same format as dmg , so below testing 2 methods of reading dmg/heal and seeing which works
            if (this.effecttype.equals("Heal") || this.effecttype.equals("Damage")) {

                if (afterType.contains("shield")) {
                    this.shielded = true;
                } else {
                    this.shielded = false;
                }
                //TODO dmgType
                try {

                    String rivi = afterType.substring(afterType.indexOf("(") + 1, afterType.substring(afterType.indexOf("("), afterType.length() - 1).indexOf(" ") + afterType.indexOf("(") + 1);

                    rivi = rivi.replace(")", "");

                    if (rivi.contains("*")) {

                        rivi = rivi.trim();
                        this.crit = true;
                        rivi = rivi.substring(0, rivi.length() - 1);
                    } else {
                        this.crit = false;
                    }
                    this.dmgHeal = Integer.valueOf(rivi.trim());

                } catch (IndexOutOfBoundsException | NumberFormatException e) {

                    String rivi = rawline.substring(rawline.lastIndexOf("(") + 1, rawline.lastIndexOf(")"));
                    index = rivi.indexOf(")");
                    if (index > 1) {
                        rivi = rivi.substring(0, index);

                    }
                    if (rivi.contains("*")) {
                        this.crit = true;
                        rivi = rivi.substring(0, rivi.length() - 1);
                    } else {
                        this.crit = false;
                    }
                    this.dmgHeal = Integer.valueOf(rivi.trim());

                }

            }
            if (this.dmgHeal == 0) {
                this.miss = true;
            }
        } else {
            //this should happen only with resource lines (energy/rage/heat etc usage). We could do something with these , but it would be very complicated (we'd have to take in account owner's ala , spec etc.)
            this.eventtype = null;
            this.effecttype = null;
        }

//        } 
    }

    @Override
    public String toString() {
        String output = "";
        output += this.timestamp + " , " + this.source + " , " + this.target + " , " + this.abilityName + " , " + this.type;
        if (this.eventtype != null) {
            output += " , " + this.eventtype;
        } else if (this.effecttype != null) {
            output += " , " + this.effecttype;
            if (this.effecttype.equals("Heal") || this.effecttype.equals("Damage")) {
                output += " , " + this.dmgHeal;
            }
        }
        if (this.crit) {
            output += " , *";
        }
        return output;
    }

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public String getTarget() {
        return target;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public int getDmgHeal() {
        return dmgHeal;
    }

    public Type getType() {
        return type;
    }

    public String getEffecttype() {
        return effecttype;
    }

    public Eventtype getEventtype() {
        return eventtype;
    }

    public String getSource() {
        return source;
    }

    public int getThreat() {
        return threat;
    }

    public boolean isCrit() {
        return crit;
    }

    public Damagetype getDmgType() {
        return dmgType;
    }

    public boolean isShielded() {
        return shielded;
    }

    //Hashcode is used by the gui for making a HashMap with fight as key (not yet implemented)
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + Objects.hashCode(this.timestamp);
        hash = 17 * hash + Objects.hashCode(this.source);
        hash = 17 * hash + Objects.hashCode(this.target);
        hash = 17 * hash + Objects.hashCode(this.type);
        hash = 17 * hash + Objects.hashCode(this.effecttype);
        hash = 17 * hash + Objects.hashCode(this.eventtype);
        hash = 17 * hash + Objects.hashCode(this.abilityName);
        hash = 17 * hash + this.dmgHeal;
        hash = 17 * hash + this.threat;
        hash = 17 * hash + (this.crit ? 1 : 0);
        hash = 17 * hash + Objects.hashCode(this.dmgType);
        hash = 17 * hash + (this.shielded ? 1 : 0);
        hash = 17 * hash + (this.miss ? 1 : 0);
        return hash;
    }

    //Equals method mainly for unit tests
    //this method is split is split in 5 because Checkstyle (I really need this method for unit tests, so I had to find a way to keep it here)
    @Override
    public boolean equals(Object obj) {
        if (!this.checkClass(obj)) {
            return false;
        }
        final Row other = (Row) obj;
        if (!this.checkDmg(other)) {
            return false;
        }

        if (!this.checkTypes(other)) {
            return false;
        }
        if (!this.checkSourceTargetAbility(other)) {
            return false;
        }

        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }

        return true;
    }

    private boolean checkSourceTargetAbility(Row other) {
        if (!Objects.equals(this.source, other.getSource())) {
            return false;
        }
        if (!Objects.equals(this.target, other.getTarget())) {
            return false;
        }

        if (!Objects.equals(this.abilityName, other.getAbilityName())) {
            return false;
        }
        return true;
    }

    private boolean checkClass(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return true;
    }

    private boolean checkDmg(Row other) {
        if (this.dmgHeal != other.getDmgHeal()) {
            return false;
        }
        if (this.threat != other.getThreat()) {
            return false;
        }
        if (this.crit != other.isCrit()) {
            return false;
        }
        if (this.shielded != other.isShielded()) {
            return false;
        }
        return true;
    }

    private boolean checkTypes(Row other) {
        if (!Objects.equals(this.effecttype, other.getEffecttype())) {
            return false;
        }
        if (this.type != other.getType()) {
            return false;
        }
        if (this.eventtype != other.getEventtype()) {
            return false;
        }
        if (this.dmgType != other.getDmgType()) {
            return false;
        }
        return true;
    }

    public int getRowNumber() {
        return rowNumber;
    }

}
