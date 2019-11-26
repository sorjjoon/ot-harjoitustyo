/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.stats;

import java.time.LocalTime;
import myparser.myparser.domain.Row;

/**
 *
 * This class provides a way to deal with ability activations, and helps track ends and starts of effects
 */
public class AbilityActivation {
    private Row activate;
    private Row applyEffect;
    private Row removeEffect;
    private int dmgHeal;    //current version doesn't use this (because this class is older than Stats and dmg calculations, but this could be used later
                            //with stuff that deals dmg, and applies an effect (like reflect)

    public AbilityActivation(Row applyEffect, Row removeEffect) {
        this.applyEffect = applyEffect;
        this.removeEffect = removeEffect;
    }
    
    
    
    public AbilityActivation(Row activate) {
        this.activate = activate;
        this.dmgHeal = 0;
    }

    public void setApplyEffect(Row applyEffect) {
        this.applyEffect = applyEffect;
    }

    public void addDmgHeal(int dmgHeal) {
        this.dmgHeal +=  dmgHeal;
    }

    public void setRemoveEffect(Row removeEffect) {
        this.removeEffect = removeEffect;
    }
    //this is kinda pointless since the timestamp on this and effect start are the same (or really, really close)
    //but since we could use this class for stuff other than effects, it's here
    public LocalTime getActivation() {  
        return this.activate.getTimestamp();
    }
    public LocalTime getEffectStart() {  
        return this.applyEffect.getTimestamp();
    }
    public LocalTime getEffectEnd() {
        return this.removeEffect.getTimestamp();
    }

    public int getDmgHeal() {
        return dmgHeal;
    }

    @Override
    public String toString() {
        return "AbilityActivation{" + "applyEffect = " + applyEffect.getTimestamp() + ", removeEffect = " + removeEffect.getTimestamp() + '}';
    }
    
    
    
    
}
