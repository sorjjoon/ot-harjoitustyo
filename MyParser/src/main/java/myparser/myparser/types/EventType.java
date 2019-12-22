/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.types;


/**
 * Constains the (english) String representation of all possible Events (see Type) found in logs <p>
 * Only ones used by the application are Enter and ExitCombat, death and abilityActivate 
 */
public enum EventType {
    
    Death,
    Revived,
    ModifyThreat,
    EnterCombat,
    AbilityInterrupt,
    ExitCombat,
    AbilityActivate,
    AbilityDeactivate,
    FallingDamage,
    AbilityCancel,
    FailedEffect,
    Crouch,
    LeaveCover
    
    
}
