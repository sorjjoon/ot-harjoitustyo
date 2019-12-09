/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

//These are useless atm, but here for testing
import database.Database;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args) throws Exception {

        try {
//            System.out.println("moi");
            Gui.main(args);

        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            System.exit(0);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String sStackTrace = sw.toString();
            System.out.println(sStackTrace);

        }
    }
}
