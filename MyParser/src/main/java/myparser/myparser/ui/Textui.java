/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myparser.myparser.ui;

import database.Database;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import myparser.myparser.readers.Reader;
import myparser.myparser.domain.Fight;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.stats.Stats;


//This ui is a placeholder for when/if I learn to make a graphical one

public class Textui implements ui{
    ArrayList<Fight> fights;    //List and name of the current logs being analyzed
    private String name;
    private Database data;
    private LocalDate date; 
    public Textui(){
        this.fights=new ArrayList();
        }
    
    @Override
    //TODO make each command into their own method, cause this is currently confusing asf
    public void run(Scanner reader){
        System.out.println("Hi! This ui is still really basic, because I didn't want to spend extra effort in it, as next week I'm moving to a graphical interface");
        System.out.println("So excuse me if it's a little clunky and confusing :)");
        while(true){
            if(this.data!=null){
                try{
                    data.close();
                }catch(SQLException e){ //I don't know why this would happen
                    System.out.println("Can not close database connection");
                    System.out.println(e);
                }
                
            }
            System.out.println("---------------------------");
            System.out.println("Hi, welcome to MyParser!");
            if(this.fights!=null){
                System.out.println("Currently analyzing log "+this.name);
            }
            System.out.println("List of commands ");
            System.out.println("0 - Analyze a new log");
            System.out.println("1 - Basic stats (dps,hps,dtps etc.)");
            System.out.println("2 - More stats (breakdown by ability/target)");
            System.out.println("s - Save the currently analyzed log into storage");
            System.out.println("x - Exit");
            String command=reader.nextLine();
            if(command.equals("0")){
                System.out.println("-----------------------------");
                System.out.println("1 - load a previously analyzed log from storage");
                System.out.println("2 - analyze a new log");
                System.out.println("x - cancel");
                command=reader.nextLine();
                if(command.equals("1")){
                    
                    try{
                        this.data=new Database();
               
                    }catch(SQLException e){
                        System.out.println("Error connecting");
                        System.out.println(e);
                    }
                    while(true){
                        System.out.println("----------------------------------");
                        System.out.println("Give the name of a log from the database.");    //TODO make it so you can't add logs named x and 1 to the database
                        System.out.println("1 - print the names of all logs in the database");
                        System.out.println("x return to start");
                        command=reader.nextLine();
                        if(command.equals(("x"))){
                            break;
                        }else if(command.equals("1")){
                            try{
                                ArrayList<String> names=this.data.getSavedLogs();
                                if(names!=null){
                                    for(String s :names){
                                        System.out.println(s);
                                    }
                                    continue;
                                }else{
                                    System.out.println("No logs found");
                                    break;
                                }
                                
                        
                            }catch(SQLException e){
                                System.out.println("Error with geting saved logs");
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                                break;
                            }
                            }else{
                            System.out.println("Loading log form storage, this may take a few seconds");
                            try{
                                this.name=command;
                                this.fights=data.getFightsFromLog(command);
                                System.out.println("Log loaded succesfully");
                                break;
                             }catch(NullPointerException e){
                                 System.out.println("------------------");
                                System.out.println("Log name not found");
                
                            }catch(SQLException | NoOwnerException e){
                                System.out.println("Error with geting saved logs");
                                System.out.println(e.getMessage());
                                e.printStackTrace();
                                break;
                            }
                        }
                        
                    }
                    
                    
                    
                    
                    
                    
                    
                     }else if(command.equals("2")){
                        
                        System.out.println("Give a path to the file");
                        try{
                            String path=reader.nextLine();
                            ArrayList<Fight> helper=readLog(path);
                            if(helper.isEmpty()){
                                System.out.println("No fights found inside the log.");
                                continue;
                            }
                            this.name=Reader.nameFromPath(path);
                            try{
                                this.date=Reader.dateFromPath(path);
                            }catch(Exception e){
                                System.out.println("Couldn't determine the date from the log file name (you have changed the name?)");
                                    
                                while(true){
                                    System.out.println("Input the date for the log (format yyyy-mm-dd)");
                                    
                                    try{
                                        this.date=LocalDate.parse(reader.nextLine());
                                        break;
                                    }catch(Exception x){
                                        System.out.println("Date not in a valid format");
                                    }
                                }
                            }
                            this.fights=helper;
                        }catch(FileNotFoundException e){
                            System.out.println("File not found");
                            continue;

                        }
                }
                
            }else if(command.equals("1")){
                basicStats();
            }else if(command.equals("2")){
                System.out.println("1 - Damage");
                System.out.println("2 - Healing (note about example logs, most have little to no healing at all, combat_2019-06-27_22_03_07_031196.txt, is one with a little more healing in it)");
                command=reader.nextLine();
                if(command.equals("1")){
                    System.out.println("1 - breakdown by ability");
                    System.out.println("2 - breakdown by target");
                    command=reader.nextLine();
                    if(command.equals("1")){
                        breakDownByAbility();
                    }else if(command.equals("2")){
                        breakDownByTarget();
                    }else{
                        System.out.println("Unknown command");
                    }
                }else if(command.equals("2")){
                    System.out.println("1 - breakdown by ability");
                    System.out.println("2 - breakdown by target");
                    command=reader.nextLine();
                    if(command.equals("1")){
                        breakDownHealingByAbility();
                    }else if(command.equals("2")){
                        breakDownHealingByTarget();
                    }else{
                        System.out.println("Unknown command");
                    }                    
                }
            }else if(command.equals("x")){
                break;
            }else if(command.equals("s")){
                //TODO make it so you can't save the same log twice (or at least give a warning if you are about to)
                try {
                    this.data=new Database();
                    System.out.println("What type of log is this? (dummy, pvp, op etc.)");
                    String type=reader.nextLine();
                    System.out.println("Saving log. This might take a few seconds");
                    this.data.addListOfFights(fights, date, type, name);
                    System.out.println("Log saved succesfully!");
               
                }catch(SQLException e){
                        System.out.println("Can't save file to datbase");
                        System.out.println(e);
                        }
                
            
            
            
            
            
            
            
            
            }else{
                System.out.println("Unknown command");
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
    
    public void breakDownHealingByAbility() {
        int i=1;
        DecimalFormat df = new DecimalFormat("#.##");
        for(Fight f: this.fights){
            long duration=Stats.getDuration(f)/1000;
            System.out.println("----------------------------");
            System.out.println("Fight "+i);
            System.out.println("Duration "+duration+" seconds");
            System.out.println("Total hps " +df.format(Stats.hps(f)));
            System.out.println("");
            HashMap<String,Integer>breakdown=Stats.divideHealingDealtByAbility(f);
            for(String s:breakdown.keySet()){
                double precentage=(double)breakdown.get(s)/Stats.getAllHealingByOwner(f)*100;
                System.out.println(s+", "+df.format(precentage)+" %");
            }
            i++;
        }
        
    }
    public void breakDownHealingByTarget(){
        int i=1;
        DecimalFormat df = new DecimalFormat("#.##");
        for(Fight f: this.fights){
            long duration=Stats.getDuration(f)/1000;
            System.out.println("----------------------------");
            System.out.println("Fight "+i);
            System.out.println("Duration "+duration+" seconds");
            System.out.println("Total hps " +df.format(Stats.hps(f)));
            System.out.println("");
            HashMap<String,Integer>breakdown=Stats.divideHealingDealtByTarget(f);
            for(String s:breakdown.keySet()){
                double precentage=(double)breakdown.get(s)/Stats.getAllHealingByOwner(f)*100;
                System.out.println(s+", "+df.format(precentage)+" %");
            }
            i++;
    }
    }    
    public void breakDownByTarget(){
        int i=1;
        DecimalFormat df = new DecimalFormat("#.##");
        for(Fight f: this.fights){
            long duration=Stats.getDuration(f)/1000;
            System.out.println("----------------------------");
            System.out.println("Fight "+i);
            System.out.println("Duration "+duration+" seconds");
            System.out.println("Total dps " +df.format(Stats.dps(f)));
            System.out.println("");
            HashMap<String,Integer>breakdown=Stats.divideDamageDealtByTarget(f);
            for(String s:breakdown.keySet()){
                double precentage=(double)breakdown.get(s)/Stats.getAllDamageByOwner(f)*100;
                System.out.println(s+", "+df.format(precentage)+" %");
            }
            i++;
    }
    }
    
    public void breakDownByAbility() {
        int i=1;
        DecimalFormat df = new DecimalFormat("#.##");
        for(Fight f: this.fights){
            long duration=Stats.getDuration(f)/1000;
            System.out.println("----------------------------");
            System.out.println("Fight "+i);
            System.out.println("Duration "+duration+" seconds");
            System.out.println("Total dps " +df.format(Stats.dps(f)));
            System.out.println("");
            HashMap<String,Integer>breakdown=Stats.divideDamageDealtByAbility(f);
            for(String s:breakdown.keySet()){
                double precentage=(double)breakdown.get(s)/Stats.getAllDamageByOwner(f)*100;
                System.out.println(s+", "+df.format(precentage)+" %");
            }
            i++;
        }
        
    }
    
    @Override
    public void basicStats(){
        int k=0;
        DecimalFormat df = new DecimalFormat("#.##");
        if(this.fights==null){
            System.out.println("Choose a log to analyze first");
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
