package myparser.myparser.readers;
//import myparser.Graphicui.*;

import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.types.Eventtype;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
//import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;
import myparser.myparser.stats.Stats;

/**
 * This class is responsible for reading the file passed to it as an argument
 * (meaning path to the file), and creating Row objects based on read rows and
 * returning an ArrayList containing the rows it has read
 */
public class Reader {

    //TODO find out if  really weird names (like in arabic letters) break this reader
   
    public static ArrayList<Fight> readFile(File file) throws FileNotFoundException {
        Scanner reader = new Scanner(file, "ISO-8859-1");
        ArrayList<Row> lines = new ArrayList();
        ArrayList<Fight> fights = new ArrayList();

        boolean inFight = false; //variable to determine the end and start of fights,  and to read only necessary lines
        //variable i is used for numbering rows
        String owner="";
        int i = 0;
        while (reader.hasNext()) {
            String rawline = reader.nextLine();
            i++;

            try {

                //Remove empty rows
                if (rawline.isEmpty()) {
                    i--;
                    continue;
                }
                Row row = new Row(rawline, i);

                if (row.getEventtype() == Eventtype.EnterCombat) {
                    inFight = true;
                    //So we can determine owner
                    owner = row.getSource(); //Owner is only needed to check for Death event targets
                    
                //for some reason,  death events seem to not always register the exitCombat effect correctly (they sometimes do),  so had to add some work arounds for that
                // && in_fight is there to check  if  the exitCombat trigger has already been handled (and the in_fight variable has been set to false)
                //update also have to check that the targets is correct, since logs track kills as well (which are eventtype Death, but with a diffrent target)

                } else if ((row.getEventtype() == Eventtype.ExitCombat || row.getEventtype() == Eventtype.Death) && row.getTarget().equals(owner) && inFight) {

                    inFight = false;
                    lines.add(row); //adding the exitcombat line (to get exit time)

                    Fight fight = new Fight(lines);

                    //adding fight, as long as it's not empty, and combining it with the previous fight, if the start time is withing 30 seconds of it's end time
                    if (!fights.isEmpty()) {
                        Fight previousFight = fights.get(fights.size() - 1);
                        if (Stats.getDiffrence(previousFight.getEnd(), fight.getStart()) < 30000) {
                            fights.remove(fights.get(fights.size() - 1));
                            previousFight.combineFights(fight);
                            fights.add(previousFight);
                        } else {
                            fights.add(fight);
                        }
                    } else {

                        fights.add(fight);

                    }

                    //lines.clear();    idk why,  but clear doesn't work here
                    lines = new ArrayList();

                }

                if (inFight) {
                    lines.add(row);
                }

            } catch (NoOwnerException e) {
                //TODO no owner exception
                //Though this should not happen

            } catch (Exception e) {
                //for debuging
                //in case we fail to read a row,  it's because the log owner has changed it for some reason, 
                //So we can safely discard it

//                System.out.println("Rivin " + i + " luku ei onnistunut" + e.getMessage());
            }

        }
        return fights;
    }

    //this is an old method you could use to get a date from the logfile name, ( log files have default names which contain the date)
    public static LocalDate dateFromName(String fileName) throws IllegalFormatException, IndexOutOfBoundsException {
        int index = fileName.indexOf("_");
        String date = fileName.substring(index + 1, index + 11);
        //If this fails,  date not in a valid format

        return LocalDate.parse(date);
    }

    //this is an old method you could use to get a date from the logfile path, ( log files have default names which contain the date)
    //It is not used in the current version,  but I'm not yet sure if  it is completelly unnecessary
    public static LocalDate dateFromPath(String path) throws IllegalFormatException, IndexOutOfBoundsException {
        String fileName = path.substring(path.lastIndexOf("/") + 1);
        int index = fileName.indexOf("_");
        String date = fileName.substring(index + 1, index + 11);
        LocalDate.parse(date);

        return LocalDate.parse(date);
    }

    //Get the log file name from the path name
    public static String nameFromPath(String path) throws IllegalFormatException, IndexOutOfBoundsException {
        return path.substring(path.lastIndexOf("/") + 1);

    }

}
