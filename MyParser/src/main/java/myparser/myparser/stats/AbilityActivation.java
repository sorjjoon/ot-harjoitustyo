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
 * This class provides a way to deal with ability activations, and helps track
 * ends and starts of effects Current version doesn't use this yet
 */
public class AbilityActivation {

    private final String abilityName;
    private Row activate;
    private Row applyEffect;
    private Row removeEffect;
    private int dmgHeal;

    /**
     * use to track effect ends and starts
     *
     * @param applyEffect
     * @param removeEffect
     */
    public AbilityActivation(Row applyEffect, Row removeEffect) {
        this.abilityName = applyEffect.getAbilityName();
        this.applyEffect = applyEffect;
        this.removeEffect = removeEffect;
    }

    /**
     * used for only tracking ability activations
     *
     * @param activate
     */
    public AbilityActivation(Row activate) {
        this.abilityName = activate.getAbilityName();
        this.activate = activate;
        this.dmgHeal = 0;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public void setApplyEffect(Row applyEffect) {
        this.applyEffect = applyEffect;
    }

    /**
     * add dmg heal to the sum of this ability
     *
     * @param dmgHeal
     */

    public void addDmgHeal(int dmgHeal) {
        this.dmgHeal += dmgHeal;
    }

    public void setRemoveEffect(Row removeEffect) {
        this.removeEffect = removeEffect;
    }

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

    //this is for testing mostly
    @Override
    public String toString() {
        return "AbilityActivation{" + "applyEffect = " + applyEffect.getTimestamp() + ", removeEffect = " + removeEffect.getTimestamp() + '}';
    }

}
