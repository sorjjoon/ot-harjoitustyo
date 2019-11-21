/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import myparser.myparser.domain.Fight;

/**
 *
 * @author sorjjoon
 */
public interface ui {
    
    public void run();
    
    public ArrayList<Fight> readLog(String path)throws FileNotFoundException;
    
    public void basicStats();
}
