
package myparser.myparser.readers;
//import myparser.Graphicui.*;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.types.Eventtype;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
//import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;

/**
 * This class is responsible for reading the file passed to it as an argument (meaning path to the file),  and creating Row objects based on read rows
 * and returning an ArrayList containing the rows it has read
 */
public class Reader   {

    //TODO find out if  really weird names (like in arabic letters) break this reader
    
    public static ArrayList<Fight> readFile(String path)throws FileNotFoundException  {
        Scanner reader = new Scanner(new File(path), "ISO-8859-1");
        ArrayList<Row> lines  =  new ArrayList();
        ArrayList<Fight> fights  =  new ArrayList();
        
        boolean inFight = false; //variable to determine the end and start of fights,  and to read only necessary lines
        
        //variable i is used for numbering rows
        int i = 0;
        while (reader.hasNext())  {
            String rawline = reader.nextLine();
            i++;
            
            try {

                //Remove empty rows
                if (rawline.isEmpty())  {
                    i--;
                    continue; 
                }
                Row row = new Row(rawline, i);

                if (row.getEventtype() == Eventtype.EnterCombat)  {
                    inFight = true;

                }
                //for some reason,  death events seem to not always register the exitCombat effect correctly (they sometimes do),  so had to add some work arounds for that
                // && in_fight is there to check,  if  the exitCombat trigger has already been handled (and the in_fight variable has been set to false)
                else if ((row.getEventtype() == Eventtype.ExitCombat || row.getEventtype() == Eventtype.Death) && inFight)  {

                    inFight = false;
                    lines.add(row); //adding the exitcombat line (to get exit time)

                    Fight fight = new Fight(lines);
                    fights.add(fight);


                    //lines.clear();    idk why,  but clear doesn't work here
                    lines = new ArrayList(); 

                }


                if (inFight)  {
                    lines.add(row);
                }

            } catch (NoOwnerException e)  {
                    //TODO no owner exception
                    
                    
                    
            } catch (Exception e)  {
                    //for debuging
                    //in case we fail to read a row,  it's because the log owner has changed it for some reason, 
                    
                    
                System.out.println("Rivin " + i + " luku ei onnistunut" + e.getMessage());

            }
                
                
        }
        return fights;
    }
    
    
    //this is an old method you could use to get a date from the logfile name, ( log files have default names which contain the date)
    public static LocalDate dateFromName(String fileName) throws IllegalFormatException, IndexOutOfBoundsException  {
        int index = fileName.indexOf("_");
        String date = fileName.substring(index + 1, index + 11);
        //If this fails,  date not in a valid format
        
        
        return LocalDate.parse(date);
    }
    
    //this is an old method you could use to get a date from the logfile name, ( log files have default names which contain the date)
    //It is not used in the current version,  but I'm not yet sure if  it is completelly unnecessary
    public static LocalDate dateFromPath(String path) throws IllegalFormatException, IndexOutOfBoundsException  {
        String fileName  =  path.substring(path.lastIndexOf("/") + 1);
        int index = fileName.indexOf("_");
        String date = fileName.substring(index + 1, index + 11);
        LocalDate.parse(date);
        
        
        
        return LocalDate.parse(date);
    }
    
    
    public static String nameFromPath(String path) throws IllegalFormatException, IndexOutOfBoundsException  {        
        return path.substring(path.lastIndexOf("/") + 1);
       
    }
    
}
