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
    private  String ability_name;
    private  int  size;
    
    
    public Row(String rawline,String date) throws IndexOutOfBoundsException{
        
        String[] parts=rawline.split("\\] \\[");
        for(String s :parts){
            System.out.println(s);
        }
        //removing useless chars from input String and storing the values from input String into the row object
        
        //removing first [
        this.timestamp=Timestamp.valueOf(date+parts[0].substring(1));
        this.owner=parts[1];
        this.target=parts[2];
        
        //removing id and empty space
        this.ability_name=parts[3].substring(0,parts[3].indexOf("{")-1);
//        System.out.println(parts[4].substring(0,parts[4].indexOf("{")-1));
        this.type=Type.valueOf(parts[4].substring(0,parts[4].indexOf("{")-1));
        
        
        
    }
    
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

    public Eventtype getEffect() {
        return effect;
    }

    public String getAbility_name() {
        return ability_name;
    }

    public int getSize() {
        return size;
    }
    
    
    
}
