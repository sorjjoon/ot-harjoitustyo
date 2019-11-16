
package myparser.myparser;

import java.io.File;
import java.io.FileNotFoundException;
//import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;

/**
 * This class is responsible for reading the file passed to it as an argument (meaning path to the file), and creating Row objects based on read rows
 * and returning an ArrayList containing the rows it has read
 */
public class Reader {
    
//    public static ArrayList<Fight> readFile(String path, String date)throws FileNotFoundException{
//        Scanner reader=new Scanner(new File(path));
//        
//        ArrayList<Row> lines = new ArrayList();
//        ArrayList<Fight> fights = new ArrayList();
//        
//        boolean in_fight=false; //variable to determine the end and start of fights, and to read only necessary lines
//        
//        int i=0;
//        ArrayList<Integer> list=new ArrayList();
//        while(reader.hasNext()){
//            
//            String rawline=reader.nextLine();
//            i++;
//            
//                try{
//                    
//            //this doesn't work if logging is turned on while in a fight. (tho this should be super rare...) TODO figure out a way around this
//            
//            
//                    Row row=new Row(rawline,date);
//                    if(row.getEventtype()==Eventtype.EnterCombat){
//                        in_fight=true;
//                    }
//                    else if(row.getEventtype()==Eventtype.ExitCombat){
//                        in_fight=false;
//                        if(!lines.isEmpty()){
//                            Fight fight=new Fight(lines);
//                            fights.add(fight);
//                        }
//                        
//                        //lines.clear();
//                        lines=new ArrayList(); 
//
//                    }
//                    
//                    
//                    if(in_fight){
//                        lines.add(row);
//                        }
//                   
//                }catch(Exception e){
//                    System.out.println("Rivin "+i+" luku ei onnistunut"+e.getMessage());
//                    list.add(i);
//                }
//                
//                
//        }
//        
//            for(Integer z : list){
//                System.out.println(z);
//            }
//            System.out.println(list.size());
//        return fights;
//    }
    
    public static ArrayList<Fight> readFile(String path)throws FileNotFoundException{
        Scanner reader=new Scanner(new File(path));
        
        ArrayList<Row> lines = new ArrayList();
        ArrayList<Fight> fights = new ArrayList();
        
        boolean in_fight=false; //variable to determine the end and start of fights, and to read only necessary lines
        
        int i=0;
        ArrayList<Integer> list=new ArrayList();
        while(reader.hasNext()){
            
            String rawline=reader.nextLine();
            i++;
            
                try{
                    
            //this doesn't work if logging is turned on while in a fight. (tho this should be super rare...) 
            
            
                    Row row=new Row(rawline);
                    if(row.getEventtype()==Eventtype.EnterCombat){
                        in_fight=true;
                    }
                    else if(row.getEventtype()==Eventtype.ExitCombat){
                        in_fight=false;
                        if(!lines.isEmpty()){
                            Fight fight=new Fight(lines);
                            fights.add(fight);
                        }
                        
                        //lines.clear();
                        lines=new ArrayList(); 

                    }
                    
                    
                    if(in_fight){
                        lines.add(row);
                        }
                   
                }catch(Exception e){
                    System.out.println("Rivin "+i+" luku ei onnistunut"+e.getMessage());
                    list.add(i);
                }
                
                
        }
        
            for(Integer z : list){
                System.out.println(z);
            }
            System.out.println(list.size());
        return fights;
    }
    
    
    
    
    public static String dateFromPath(String path) throws IllegalFormatException,IndexOutOfBoundsException{
        String file_name = path.substring(path.lastIndexOf("/")+1);
        int index=file_name.indexOf("_");
        String date=file_name.substring(index+1,index+11)+" ";
        //TODO Check that resulting date is in a valid format
        
        
        
        return date;
    }
    
}
