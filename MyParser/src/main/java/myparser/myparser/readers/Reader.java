package myparser.myparser.readers;

import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.types.EventType;
import java.io.File;
import java.io.FileNotFoundException;
;
import java.util.ArrayList;
import java.util.Scanner;
import myparser.myparser.stats.Stats;

/**
 * contains only the static method, readFile
 */


public abstract class Reader {

    /**
     * Reads the given file and creates a list of all Fights based on it.
     * <p>
     * Combines all Fights starting and ending within 30 s of each other
     *
     * @param file
     * @return fights 
     * @throws FileNotFoundException
     */
    public static ArrayList<Fight> readFile(File file) throws FileNotFoundException {

        Scanner reader = new Scanner(file, "ISO-8859-1");
        ArrayList<Row> lines = new ArrayList();
        ArrayList<Fight> allFights = new ArrayList();

        boolean inFight = false; //variable to determine the end and start of fights,  and to read only necessary lines
        String owner = "";
        int rowNumber = 0;
        while (reader.hasNext()) {
            String rawline = reader.nextLine();
            rowNumber++;

            try {

                //Remove empty rows
                if (rawline.isEmpty()) {
                    rowNumber--;
                    continue;
                }
                Row row = new Row(rawline, rowNumber);

                if (row.getEventtype() == EventType.EnterCombat) {
                    inFight = true;
                    owner = row.getSource(); //Owner is only needed to check for Death event targets

                    //for some reason,  death events seem to not always register the exitCombat effect correctly (meaning death is not followed by exitCombat trigger in the raw log),  so had to add some work arounds for that
                    // && in_fight is there to check  if  the exitCombat trigger has already been handled (and the in_fight variable has been set to false)
                    //Have to check that the targets is correct, since logs track kills as well (which are eventtype Death, but with a diffrent target)
                } else if (row.getEventtype() == EventType.ExitCombat || (row.getEventtype() == EventType.Death && row.getTarget().equals(owner)) && inFight) {

                    inFight = false;
                    lines.add(row); //adding the exitcombat line (to get exit time)

                    Fight fight = new Fight(lines);

                    //adding a new fight to the allFights list, or combining it with the previous fight, if the start time is withing 30 seconds of the last fight end time
                    if (!allFights.isEmpty()) {
                        Fight previousFight = allFights.get(allFights.size() - 1);
                        if (Stats.getDiffrence(previousFight.getEnd(), fight.getStart()) < 30000) {
                            allFights.remove(allFights.get(allFights.size() - 1));
                            previousFight.combineFights(fight);
                            allFights.add(previousFight);
                        } else {
                            allFights.add(fight);
                        }
                    } else {

                        allFights.add(fight);

                    }

                    lines = new ArrayList();

                }

                if (inFight) {
                    lines.add(row);
                }

            } catch (NoOwnerException e) {
                //This can not happen when reading from a .txt file

            } catch (Exception e) {
                //in case we fail to read a row,  it's because the log owner has changed it for some reason, 
                //So we can safely discard it
            }

        }
        return allFights;
    }

}
