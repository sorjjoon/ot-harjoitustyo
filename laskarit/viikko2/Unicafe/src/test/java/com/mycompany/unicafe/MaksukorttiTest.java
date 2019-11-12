package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinArvoAlussa() {
        assertTrue(kortti.saldo()==10);      
    }
    
    @Test
    public void kortinLataus() {
        kortti.lataaRahaa(20);
        assertTrue(kortti.saldo()==30);      
    }
 
    
    @Test
    public void rahanOttoVahentaaSaldoaOikein() {
        
        kortti.otaRahaa(5);

        assertTrue(kortti.saldo()==5);
}
    
     
    @Test
    public void rahanOttoLiianIsolla() {
        
        kortti.otaRahaa(50);

        assertTrue(kortti.saldo()==10);
}
    
    
     
    @Test
    public void rahanOttoPalauttaaFalseLiianIsolla() {
        
        

        assertFalse(kortti.otaRahaa(50));
}
    
    @Test
    public void rahanOttoPalauttaaTrue() {
        
        

        assertTrue(kortti.otaRahaa(5));
}
    
    
    @Test
    public void toStringMetodiToimiiOikein(){
        assertEquals("saldo: "+0+"."+10,kortti.toString());
    }
    
    
    
    
    
}
