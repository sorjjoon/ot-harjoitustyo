/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import java.text.DecimalFormat;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import static javafx.scene.input.KeyCode.D;

/**
 *
 * Wrapper for interacting with the property value factory of table view class
 */
public class ColumnData {

    private SimpleStringProperty abilityName;
    private double avg;
    private double max;
    private double min;
    private int count;

    public ColumnData(String abilityName, Double avg, Double max, Double min, Double count) {
        this.abilityName = new SimpleStringProperty(abilityName);
        if (avg != null) {
            this.avg = (double) Math.round(avg * 100) / 100;

        }

        if (max != null) {
            this.max = (double) Math.round(max * 100) / 100;

        }

        if (count != null) {    //Count should never be null, but just in case
            this.count = count.intValue() + 1;  //+1 because count tracks times between uses
        }
        if (min != null) {
            this.min = (double) Math.round(min * 100) / 100;;

        }

    }

    public String getAbilityName() {
        return abilityName.get();
    }

    public double getAvg() {
        return avg;
    }

    public double getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }

    public double getMin() {
        return min;
    }

}
