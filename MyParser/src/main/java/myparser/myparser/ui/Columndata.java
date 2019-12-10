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
 * @author joona
 */
public class Columndata {
    private final SimpleStringProperty abilityName;
    private final SimpleStringProperty avg;
    private final SimpleStringProperty max;
    private final SimpleStringProperty min;

    public Columndata(String abilityName, double avg, double max, double min) {
        this.abilityName = new SimpleStringProperty(abilityName);
        DecimalFormat df = new DecimalFormat("#.##");

        this.avg = new SimpleStringProperty(df.format(avg));
        this.max = new SimpleStringProperty(df.format(max));
        this.min = new SimpleStringProperty(df.format(min));
    }    

    public String getAbilityName() {
        return abilityName.get();
    }

    public String getAvg() {
        return avg.get();
    }

    public String getMax() {
        return max.get();
    }

    public String getMin() {
        return min.get();
    }
    
    
    
}
