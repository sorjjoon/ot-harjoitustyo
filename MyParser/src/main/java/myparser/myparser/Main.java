/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser;

/**
 *
 * @author joona
 */
public class Main {

    public static void main(String[] args) {
        // TODO code application logic here
        //System.out.println("moi");
        Row row=new Row("[23:06:40.382] [@Blonde'kate] [@Firaksîan] [Recuperative Nanotech {815232037421056}] [ApplyEffect {836045448945477}: Heal {836045448945500}] (583*) <262>","2019-01-01 ");
        
        System.out.println(row);
//        System.out.println("");
//        row = new Row("[23:06:40.498] [] [@Firaksîan] [Sudden Death Toxic Contamination {3291448187289600}] [ApplyEffect {836045448945477}: Damage {836045448945501}] (1382 )","moi");
    }
    
}
