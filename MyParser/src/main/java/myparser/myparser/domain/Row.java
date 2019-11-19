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

    public boolean isMiss() {
        return miss;
    }
    
    
    
 
    
    public Row(String rawline) throws IndexOutOfBoundsException, NumberFormatException, IllegalArgumentException, EnumConstantNotPresentException{
        
        //TODO threat, dmg_type
        //these values changed later if needed
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
                    //String rivi=rawline.substring(rawline.lastIndexOf("(")+1, rawline.lastIndexOf(")"));
//                    String[] rivi_split=after_type.split(" ");
//                    for(String s:rivi_split){
//                        System.out.println(s);
//                    }
////
//                      System.out.println(rivi);
//                    if(rivi.contains(")")){
                        rivi=rivi.replace(")", "");
//                    }
                    if(rivi.contains("*")){
//                        this.crit=true;
                        rivi=rivi.trim();
                        this.crit=true;
                        rivi=rivi.substring(0,rivi.length()-1);
                    }else{
                        this.crit=false;
                    }
                    
////                    rivi_split[3].replaceFirst("(", "");
////                        System.out.println(rivi_split[3]);
                    this.dmg_heal=Integer.valueOf(rivi.trim());
//                    
////                    System.out.println(rivi_split[1]);
//                    this.dmg_type=Damagetype.valueOf(rivi_split[1]);
                    
                    
                }catch(IndexOutOfBoundsException |  NumberFormatException e){  
                   
//                        String rivi=after_type.substring(after_type.indexOf("(")+1, after_type.substring(after_type.indexOf("("),after_type.length()-1).indexOf(" ")+after_type.indexOf("(")+1);
//                        System.out.println();
////                    System.out.println(after_type);
//                    String rivi=after_type.substring(after_type.indexOf("(")+1, after_type.length());

                    String rivi=rawline.substring(rawline.lastIndexOf("(")+1, rawline.lastIndexOf(")"));
                    
//                    System.out.println(after_type.length());
//                    System.out.println(after_type.charAt(after_type.length()-1));
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
            //this shouldn't happen tho...
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
    
}
