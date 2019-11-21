/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.FileNotFoundException;
import myparser.myparser.readers.Reader;
import myparser.myparser.domain.Fight;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import myparser.myparser.stats.Stats;


//This ui is a placeholder for when/if I learn to make a graphical one

public class Textui implements ui{
    ArrayList<Fight> fights;
    private String name;
    
    public Textui(){
        }
    
    @Override
    public void run(){
        while(true){
            
            Scanner reader=new Scanner(System.in);
            System.out.println("-----------------------------------");
            System.out.println("Hi, welcome to MyParser!");
            if(this.fights!=null){
                System.out.println("Currently analyzing a log from "+this.name);
            }
            System.out.println("List of commands ");
            System.out.println("0 - analyze a new log");
            System.out.println("1 - Basic stats (dps,hps,dtps etc.)");    
            System.out.println("x - exit");
            String command=reader.nextLine();
            if(command.equals("0")){
                System.out.println("1 - load a previously analyzed log");
                System.out.println("2 - analyze a new log");
                System.out.println("x - cancel");
                command=reader.nextLine();
                if(command.equals("1")){
                    System.out.println("todo");
                    //TODO
                }else if(command.equals("2")){
                    
                    System.out.println("Give a path to the file");
                    try{
                        String path=reader.nextLine();
                        ArrayList<Fight> helper=readLog(path);
                        if(helper.isEmpty()){
                            System.out.println("No fights found inside the log.");
                            continue;
                        }
                        this.name=Reader.dateFromPath(path);
                        this.fights=helper;
                    }catch(FileNotFoundException e){
                        System.out.println("File not found");
                        continue;
                        
                    }
                }
                
            }else if(command.equals("1")){
                basicStats();
            }else if(command.equals("x")){
                break;
            }
             }
    }

    public void setFights(ArrayList<Fight> fights) {
        this.fights = fights;
    }
    
    
    
    @Override
    public ArrayList<Fight> readLog(String path)throws FileNotFoundException{
          
                    ArrayList<Fight> fights=Reader.readFile(path);
                    return fights;
      
             
    }
    
    
    
    @Override
    public void basicStats(){
        int k=0;
        DecimalFormat df = new DecimalFormat("#.##");
        if(this.fights==null){
            return;
        }
        for(Fight f:this.fights){
            System.out.println("----------------------------------");
            System.out.println("Fight "+k);
            long duration=Stats.getDuration(f)/1000;
            System.out.println("Duration "+duration+" seconds");
            System.out.println("Total dmg done "+Stats.getAllDamageByOwner(f));
            System.out.println("dps " +df.format(Stats.dps(f)));
            System.out.println("Total dmg taken "+Stats.getAllDamageToOwner(f));
            System.out.println("dtps "+df.format(Stats.dtps(f)));
            System.out.println("Total healing done "+Stats.getAllHealingByOwner(f));
            System.out.println("hps "+df.format(Stats.hps(f)));

            System.out.println("Total healing recieved "+Stats.getAllHealingToOwner(f));
            System.out.println("htps "+df.format(Stats.htps(f)));
            k++;
        }
    }
    
}
