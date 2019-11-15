/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser;

import java.sql.Timestamp;

/**
 *
 * @author joona
 */
public class Row {
    private  Timestamp timestamp;
    private  String owner;
    private  String target;
    private  Type type;
    private String effecttype;
    private Eventtype eventtype;
    private  String ability_name;
    private  String  size;
    
    
    public Row(String rawline,String date) throws Exception{
        
        String[] parts=rawline.split("\\] \\[");
//        for(String s :parts){
//            System.out.println(s);
//        }
        //removing useless chars from input String and storing the values from input String into the row object
        
        //removing first [
        this.timestamp=Timestamp.valueOf(date+parts[0].substring(1));
        this.owner=parts[1];
        this.target=parts[2];
        
        //removing id and empty space
        this.ability_name=parts[3].substring(0,parts[3].indexOf("{")-1);
//        System.out.println(parts[4].substring(0,parts[4].indexOf("{")-1));
        this.type=Type.valueOf(parts[4].substring(0,parts[4].indexOf("{")-1));
        int index=parts[4].indexOf(":");
        //System.out.println("moiii");
        //System.out.println(this.type);
        String after_type=parts[4].substring(index,parts[4].length()-1);
        //System.out.println(after_type);
        if(this.type==Type.Event){
            this.eventtype=Eventtype.valueOf(after_type.substring(2,after_type.indexOf("{")-1));
            this.effecttype=null;
        }else if(this.type==Type.ApplyEffect||Type.RemoveEffect==this.type){
            this.eventtype=null;
            this.effecttype=after_type.substring(2,after_type.indexOf("{")-1);
        }else{
            //this shouldn't happen tho...
            this.eventtype=null;
            this.effecttype=null; 
            }
        
        if(this.effecttype.equals("Damage") || this.effecttype.equals("Heal")){
            //TODO
            this.size=after_type.substring(index);
        }
        
        
//        }
        
        
        
    
    }
    @Override
    public String toString(){
        return this.timestamp+", "+this.owner+", "+this.target+", "+this.ability_name+", "+this.type;
    }
    

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getOwner() {
        return owner;
    }

    public String getTarget() {
        return target;
    }


    public String getAbility_name() {
        return ability_name;
    }

    public String getSize() {
        return size;
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
    
    
    
}
