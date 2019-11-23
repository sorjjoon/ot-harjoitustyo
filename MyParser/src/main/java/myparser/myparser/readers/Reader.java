
package myparser.myparser.readers;

import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.types.Eventtype;
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
    //This is an old version of reader, which also used a date when reading. I'll probably remove it later when I'm certain I don't need it
    
    
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
    
    
                    //TODO find out if really weird names (like in arabic letters) break this reader
    
    public static ArrayList<Fight> readFile(String path)throws FileNotFoundException{
        Scanner reader=new Scanner(new File(path),"ISO-8859-1");
        //System.out.println("moi");
        ArrayList<Row> lines = new ArrayList();
        ArrayList<Fight> fights = new ArrayList();
        
        boolean in_fight=false; //variable to determine the end and start of fights, and to read only necessary lines
        
        //variable i is used for debugging, to find out which rows are not being read
        int i=0;
        while(reader.hasNext()){
            String rawline=reader.nextLine();
            i++;
            
                try{
                    
                    //Remove empty rows
                    if(rawline.isEmpty()){
                       continue; 
                    }
                    Row row=new Row(rawline);
                    
                    if(row.getEventtype()==Eventtype.EnterCombat){
                        in_fight=true;
                        
                    }
                    //for some reason, death events seem to not always register the exitCombat effect correctly (they sometimes do), so we had to add some work arounds for that
                    //&&in_fight is there to check, if the exitCombat trigger has already been handled (and the in_fight variable has been set to false)
                    else if((row.getEventtype()==Eventtype.ExitCombat||row.getEventtype()==Eventtype.Death)&&in_fight){
                        
                        in_fight=false;
                        lines.add(row); //adding the exitcombat line (to get exit time)
                        
                        Fight fight=new Fight(lines);
                        fights.add(fight);
                        
                        
                        //lines.clear();    idk why, but clear doesn't work here
                        lines=new ArrayList(); 

                    }
                    
                    
                    if(in_fight){
                        lines.add(row);
                        }
                   
                }catch(NoOwnerException e){
                    //TODO no owner exception
                    
                    
                    
                }catch(Exception e){
                    //for debuging
                    //in case we fail to read a row, it's because the log owner has changed it for some reason, or it's a very rare kind
                    //entry, which is in a diffrent kind of format (shouldn't happen)
                    //in either case we can freely discard the single line, and it shouldn't affect the result that much
                    
                    
                    System.out.println("Rivin "+i+" luku ei onnistunut"+e.getMessage());
                    
                }
                
                
        }
        return fights;
    }
    
    
    
    //this is an old method you could use to get a date from the logfile name,( log files have default names which contain the date)
    //It is not used in the current version, but I'm not yet sure if it is completelly unnecessary
    public static String dateFromPath(String path) throws IllegalFormatException,IndexOutOfBoundsException{
        String file_name = path.substring(path.lastIndexOf("/")+1);
        int index=file_name.indexOf("_");
        String date=file_name.substring(index+1,index+11)+" ";
        //TODO Check that resulting date is in a valid format
        
        
        
        return date;
    }
    
}
