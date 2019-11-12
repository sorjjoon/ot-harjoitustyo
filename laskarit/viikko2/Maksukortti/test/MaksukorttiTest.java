/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sorjjoon
 */
public class MaksukorttiTest {
    Maksukortti kortti;
    
   
    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }
    


//     TODO add test methods here.
//     The methods must be annotated with annotation @Test. For example:
//    
     @Test
     public void hello() {
         Maksukortti kortti = new Maksukortti(10);
    
        String vastaus = kortti.toString(); 

        assertEquals("Kortilla on rahaa 10.0 euroa", vastaus);
     
     }
     
     @Test
    public void syoEdullisestiVahentaaSaldoaOikein() {
        
        kortti.syoEdullisesti();

        assertEquals("Kortilla on rahaa 7.5 euroa", kortti.toString());
}
     
     
    @Test
    public void syoMaukkaastiVahentaaSaldoaOikein() {
        
        kortti.syoMaukkaasti();

        assertEquals("Kortilla on rahaa 6.0 euroa", kortti.toString());
    }

    @Test
    public void syoEdullisestiEiVieSaldoaNegatiiviseksi() {
       
        kortti.syoMaukkaasti();
        kortti.syoMaukkaasti();
        // nyt kortin saldo on 2
        kortti.syoEdullisesti();

        assertEquals("Kortilla on rahaa 2.0 euroa", kortti.toString());
    }  
    
    @Test
    public void kortilleVoiLadataRahaa() {
        kortti.lataaRahaa(25);
        assertEquals("Kortilla on rahaa 35.0 euroa", kortti.toString());
    }

    @Test
    public void kortinSaldoEiYlitaMaksimiarvoa() {
        kortti.lataaRahaa(200);
        assertEquals("Kortilla on rahaa 150.0 euroa", kortti.toString());
    }
    
    @Test
    public void syoMaukkaastiEiVieSaldoaNegatiiviseksi(){
        kortti.syoMaukkaasti();
        kortti.syoMaukkaasti();
        kortti.syoMaukkaasti();
        
        assertEquals("Kortilla on rahaa 2.0 euroa", kortti.toString());
    }
    
     @Test
    public void negatiiviesellaMaarallaLataaminenEiMuutaArvoa(){
        kortti.lataaRahaa(-2);
        assertEquals("Kortilla on rahaa 10.0 euroa", kortti.toString());
    
        
    }
    @Test
    public void edullinenTasaRahalla(){
        Maksukortti tasa = new Maksukortti(2.5);
        tasa.syoEdullisesti();
        assertEquals("Kortilla on rahaa 0.0 euroa", tasa.toString());
    
    }
    
     @Test
    public void maukasTasaRahalla(){
        Maksukortti tasa = new Maksukortti(4.0);
        tasa.syoMaukkaasti();
        assertEquals("Kortilla on rahaa 0.0 euroa", tasa.toString());
    
    }
    
}
