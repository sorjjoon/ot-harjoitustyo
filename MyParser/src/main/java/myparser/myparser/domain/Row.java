/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
    private  final LocalTime timestamp;
    private final String source;
    private final String target;
    private final Type type;
    private String effecttype;
    private Eventtype eventtype;
    private final String ability_name;
    private int dmg_heal;
    private int threat;
    private boolean crit;
    private Damagetype dmg_type;
    private boolean shielded;
    private boolean miss;
    private final int row_number;

    public boolean isMiss() {
        return miss;
    }

    public Row(LocalTime timestamp, String source, String target, Type type, String effecttype, Eventtype eventtype, String ability_name, int dmg_heal, boolean crit, boolean shielded, boolean miss, int row_number) {
        this.timestamp = timestamp;
        this.source = source;
        this.target = target;
        this.type = type;
        this.effecttype = effecttype;
        this.eventtype = eventtype;
        this.ability_name = ability_name;
        this.dmg_heal = dmg_heal;
        this.crit = crit;
        this.shielded = shielded;
        this.miss = miss;
        this.row_number=row_number;
    }
    
    
 
    
    public Row(String rawline,int row_number) throws IndexOutOfBoundsException, NumberFormatException, IllegalArgumentException, EnumConstantNotPresentException{
        this.row_number=row_number;
        //TODO threat, dmg_type
        //these are values changed later if needed
        this.miss =false;
        this.crit=false;
        this.shielded=false;
        String[] parts=rawline.split("\\] \\[");

        //removing useless chars from input String and storing the values from input String into the row object
        
        //removing first [
        this.timestamp=LocalTime.parse(parts[0].substring(1));
        this.source=parts[1];
        this.target=parts[2];
        
        //removing id and empty space
        if(parts[3].isEmpty()){
            this.ability_name="";
        }else{
            this.ability_name=parts[3].substring(0,parts[3].indexOf("{")-1);
        }
        this.type=Type.valueOf(parts[4].substring(0,parts[4].indexOf("{")-1));
        int index=parts[4].indexOf(":");
       
        String after_type=parts[4].substring(index,parts[4].length()-1);
        
        if(this.type==Type.Event){
            this.eventtype=Eventtype.valueOf(after_type.substring(2,after_type.indexOf("{")-1));
            this.effecttype=null;
        }else if(this.type==Type.ApplyEffect||Type.RemoveEffect==this.type){
            this.eventtype=null;
            this.effecttype=after_type.substring(2,after_type.indexOf("{")-1);
            
            //Check if effecttype is dmg or heal
            //For some reason heal and dmg are in diffrent formats, reflected, aborbed dmg is in a diffrent format and a few heals ( such as resurrection) are in the same format as dmg, so below testing 2 methods of reading dmg/heal and seeing which works
            if(this.effecttype.equals("Heal")||this.effecttype.equals("Damage")){
                if(after_type.contains("shield")){
                    this.shielded=true;
            }else{
                    this.shielded=false;
                }
                //TODO threat, dmg_type
                try{

                    String rivi=after_type.substring(after_type.indexOf("(")+1, after_type.substring(after_type.indexOf("("),after_type.length()-1).indexOf(" ")+after_type.indexOf("(")+1);
                  
                        rivi=rivi.replace(")", "");
//                    }
                    if(rivi.contains("*")){

                        rivi=rivi.trim();
                        this.crit=true;
                        rivi=rivi.substring(0,rivi.length()-1);
                    }else{
                        this.crit=false;
                    }
                    this.dmg_heal=Integer.valueOf(rivi.trim());
                  
                }catch(IndexOutOfBoundsException |  NumberFormatException e){  
                  
                    String rivi=rawline.substring(rawline.lastIndexOf("(")+1, rawline.lastIndexOf(")"));
                    index=rivi.indexOf(")");
                    if(index>1){
                        rivi=rivi.substring(0, index);
                        
                    }
                    if(rivi.contains("*")){
                        this.crit=true;
                        rivi=rivi.substring(0,rivi.length()-1);
                    }else{
                        this.crit=false;
                    }
                    this.dmg_heal=Integer.valueOf(rivi.trim());

                    }
                
                
        }
        if(this.dmg_heal==0){
            this.miss=true;
        }
        }else{
            //this should happen only with resource lines (energy/rage/heat etc usage). We could do something with these, but it would be very complicated (we'd have to take in account owner's ala, spec etc.)
            this.eventtype=null;
            this.effecttype=null; 
            }
        
        
//        }
        
        
        
    
    }
    
    
    @Override
    public String toString(){
        String output="";
        output+=this.timestamp+", "+this.source+", "+this.target+", "+this.ability_name+", "+this.type;
        if(this.eventtype!=null){
            output+=", "+this.eventtype;
        }else if(this.effecttype!=null){
            output+=", "+this.effecttype;
            if(this.effecttype.equals("Heal")||this.effecttype.equals("Damage")){
                output+=", "+this.dmg_heal;
                }
        }
        if(this.crit){
            output+=", *";
        }
        return output;
    }
    

    public LocalTime getTimestamp() {
        return timestamp;
    }

    public String getTarget() {
        return target;
    }


    public String getAbility_name() {
        return ability_name;
    }

    public int getDmg_heal() {
        return dmg_heal;
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

    public Damagetype getDmg_type() {
        return dmg_type;
    }            

    public boolean isShielded() {
        return shielded;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Row other = (Row) obj;
        if (this.dmg_heal != other.dmg_heal) {
            return false;
        }
        if (this.threat != other.threat) {
            return false;
        }
        if (this.crit != other.crit) {
            return false;
        }
        if (this.shielded != other.shielded) {
            return false;
        }
        if (this.miss != other.miss) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.target, other.target)) {
            return false;
        }
        if (!Objects.equals(this.effecttype, other.effecttype)) {
            return false;
        }
        if (!Objects.equals(this.ability_name, other.ability_name)) {
            return false;
        }
        if (!Objects.equals(this.timestamp, other.timestamp)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.eventtype != other.eventtype) {
            return false;
        }
        if (this.dmg_type != other.dmg_type) {
            return false;
        }
        return true;
    }

    public int getRow_number() {
        return row_number;
    }
    
    
}
