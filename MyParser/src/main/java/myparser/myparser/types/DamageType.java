/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.types;

/**
 * Different possible dmg types in the game 
 * miss, dodge and resist are not dmg types,  but types of misses, (as missed attacks dmg type is not shown in the log)
 * 
 * 
 * <b>NOTE enums are not capitalized on purpose (as they are not capitalized on raw logs either)</b>
 * 
 */
public enum DamageType {
    
    /**
     * a normal dmg type
     */
    internal,
    
    /**
     * a normal dmg type
     */
    energy,
    
    /**
     * a normal dmg type
     */
    kinetic,
    /**
     * a normal dmg type
     */
    elemental,
    /**
     * a type of miss
     */
    miss,
    /**
     * a type of miss
     */
    dodge,
    /**
     * a type of miss
     */
    resist,
    /**
     * a type of miss
     */
    parry,
    /**
     * this is a special case as it is not techinically a dmg type, but a form of dmg used by the game server to deal dmg to players. It's used by the last line in the Row constructor    
     */
    contamination 
    
    
    
}
