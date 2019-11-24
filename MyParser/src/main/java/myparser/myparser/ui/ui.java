/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import myparser.myparser.domain.Fight;

/**
 *
 * @author sorjjoon
 */
public interface ui {
    
    public void run(Scanner reader);
    
    public ArrayList<Fight> readLog(String path)throws FileNotFoundException;
    
    public void basicStats();
}
