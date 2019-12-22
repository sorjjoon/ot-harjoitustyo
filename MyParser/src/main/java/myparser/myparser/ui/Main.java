/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import javafx.scene.control.Alert;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args) {

        try {
            Gui.main(args);
        
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error occured during runtime" + e.getMessage()).show();

        }
    }
}
