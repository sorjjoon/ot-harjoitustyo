/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

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
public class KassapaateTest {
    
    Kassapaate kassa;
    Maksukortti kortti;

    
    @Before
    public void setUp() {
        kortti = new Maksukortti(1000);
        kassa = new Kassapaate();
    }
    
    @Test
    public void alkutilanteenRahaJaLounas(){
        assertTrue(kassa.kassassaRahaa()==100000);        
        assertTrue(kassa.maukkaitaLounaitaMyyty()==0);
        assertTrue(kassa.edullisiaLounaitaMyyty()==0);
    }
    
    @Test
    public void  edullisenOstoLiianIsollaKateis(){
         assertTrue(kassa.syoEdullisesti(1000)==760);
        assertTrue(kassa.kassassaRahaa()==100240);
    }
    @Test
    public void  edullisenOstoLiianPinella(){
         assertTrue(kassa.syoEdullisesti(10)==10);
        assertTrue(kassa.kassassaRahaa()==100000);
    }
    @Test
    public void  edullisenOstoTasallaKateis(){
         assertTrue(kassa.syoEdullisesti(240)==0);
        assertTrue(kassa.kassassaRahaa()==100240);
    }
    
    @Test
    public void  edullisenOstoLiianIsollaKortti(){
         assertTrue(kassa.syoEdullisesti(kortti));
        assertTrue(kassa.kassassaRahaa()==100000);
        assertTrue(kortti.saldo()==760);
    }
    @Test
    public void  edullisenOstoLiianPienellaKortti(){
        Maksukortti pieni =new Maksukortti(100);
         assertFalse(kassa.syoEdullisesti(pieni));
        assertTrue(kassa.kassassaRahaa()==100000);
        assertTrue(pieni.saldo()==100);
    }
    
     @Test
    public void  edullisenOstoTasallaKortti(){
        Maksukortti pieni =new Maksukortti(240);
         assertTrue(kassa.syoEdullisesti(pieni));
        assertTrue(kassa.kassassaRahaa()==100000);
        assertTrue(pieni.saldo()==0);
    }
    
        
    @Test
    public void  maukkaanOstoLiianIsollaKateis(){
         assertTrue(kassa.syoMaukkaasti(1000)==600);
        assertTrue(kassa.kassassaRahaa()==100400);
    }
    @Test
    public void  maukkaanOstoLiianPinella(){
         assertTrue(kassa.syoMaukkaasti(10)==10);
        assertTrue(kassa.kassassaRahaa()==100000);
    }
    @Test
    public void  maukkaanOstoTasallaKateis(){
         assertTrue(kassa.syoMaukkaasti(400)==0);
        assertTrue(kassa.kassassaRahaa()==100400);
    }
    
    @Test
    public void  maukkaanOstoLiianIsollaKortti(){
         assertTrue(kassa.syoMaukkaasti(kortti));
        assertTrue(kassa.kassassaRahaa()==100000);
        assertTrue(kortti.saldo()==600);
    }
    @Test
    public void  maukkaanOstoLiianPienellaKortti(){
        Maksukortti pieni =new Maksukortti(100);
         assertFalse(kassa.syoMaukkaasti(pieni));
        assertTrue(kassa.kassassaRahaa()==100000);
        assertTrue(pieni.saldo()==100);
    }
    
     @Test
    public void  maukkaanOstoTasallaKortti(){
        Maksukortti pieni =new Maksukortti(400);
         assertTrue(kassa.syoMaukkaasti(pieni));
        assertTrue(kassa.kassassaRahaa()==100000);
        assertTrue(pieni.saldo()==0);
    }
    
    @Test
    public void luonaidenMaaraOikein(){
        //toimii
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);
        //ei toimi
        kassa.syoMaukkaasti(kortti);
        kassa.syoEdullisesti(kortti);
        kassa.syoEdullisesti(10);
        kassa.syoMaukkaasti(100);
        //toimiii
        kassa.syoMaukkaasti(1000);
        kassa.syoEdullisesti(1000);
        assertTrue(kassa.edullisiaLounaitaMyyty()==3 && kassa.maukkaitaLounaitaMyyty()==2);
    }
    
    
    @Test
    public void kortinLatausPositiivisella(){
        kassa.lataaRahaaKortille(kortti, 500);
        assertTrue(kortti.saldo()==1500);
        assertTrue(kassa.kassassaRahaa()==100500);
    }
    
    
    @Test
    public void kortinLatausNegatiivisella(){
        kassa.lataaRahaaKortille(kortti, -500);
        assertTrue(kortti.saldo()==1000);
        assertTrue(kassa.kassassaRahaa()==100000);
    }
    
    
    
    
    
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
