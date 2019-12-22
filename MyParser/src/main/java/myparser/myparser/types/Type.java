/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.types;

/**
 *
 * Different possible Types of rows
 * <p>
 * A Row's type is meant to represent the fifth element in a rawline, 
 * [23:14:53.711] [@Predori] [@Firaksîan] [Mark of Power {952795544944640}] [ApplyEffect {836045448945477}: Hunter's Boon {952795544945169}] ()
 * <p> example Type is applyEffect
 * 
 * <p>
 * applyEffect represents either an status effect being applied on the target, or healing or damage done to target. 
 * <p>
 * removeEffect represents a status effect being removed from the log owner (as log doesn't track remove effects on other players)
 * <p>
 * Different types of effects are too numerous to list an enums, so they are used by the application as Strings
 * <p>
 * Event represent numerous different types of in-game events, such a enterCombat, Death or fallingDamage, see EventType for full list
 * <p>
 * Restore and Spend represent log owner spending a class resource to use an ability (energy, rage etc.) Not used in the current application NOTE Restore and spend are the only Types who have both their Eventtype and Effecttype null (as they are neither effects or events)
 * <p>
 * Event, apply and remove effect are only ones used by the application
 * 
 */
public enum Type {
    ApplyEffect,
    RemoveEffect,
    Event,
    Restore,
    Spend
}
