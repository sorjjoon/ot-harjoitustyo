
package myparser.myparser.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import myparser.myparser.types.Eventtype;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import myparser.myparser.stats.Stats;

/**
 *contains all the rows to a specific fight
 */
public class Fight {
    private final ArrayList<Row> rows;
    private final String owner;  

    public Fight(ArrayList<Row> rows)throws NoOwnerException {
        this.rows = rows;
        //Determine the "owner" of the log, this method should work everytime
        for (Row r:rows) {
            //EnterCombat should always be the first row, so the loop is not "necessary", but there just in case, also the ExitCombat tag is here, in case we started logging midfight for  some reason, 
            //or the user deleted rows for  some reason (though in the current version if the entercombat row h fight ad been deleted the fight wouldnt be saves
            //but in case we ever do a workaround this
            if (r.getEventtype() ==  Eventtype.EnterCombat || r.getEventtype() ==  Eventtype.ExitCombat) {
                this.owner = r.getSource();
                return;
            }
        }
        //If we can't determine an owner for  the log, we raise an exception, though I don't see how this is possible, but it's here just in case
        throw new NoOwnerException("Couldn't determine owner");
    }
        //In case we can't determine the owner automatically this constructor can be used (mainly when reading from database)
    public Fight(ArrayList<Row> rows, String owner) {
        this.rows = rows;
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }
    
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Row r:this.rows) {
            output.append(r.toString());
            output.append("\n");
        }
        return output.toString();
        
        
    }

    public ArrayList<Row> getRows() {
        return rows;
    }
    
    public int getSize() {
        return this.rows.size();
    }
    
    //Not used in the current version, can be used later if we want to ad more advanced statistics later
    public Fight rowsInTimeFrame(LocalTime start, LocalTime end) {
        ArrayList<Row> specficRows  = new ArrayList();
        
        //Here we are using the stats method of determening distance between two times (which works with midnight the way we want to) to make sure the time we are looking at is between end and start time
        long durationMs=Stats.getDurationMs(this);
        
        for(Row r : this.rows){
            LocalTime time=r.getTimestamp();
            //checking distance to start and end
            //TODO if we need to make this more effecient (probably not needed), we could make this loop stop after we have passed end
            if (Stats.getDiffrence(start, time) <= durationMs && Stats.getDiffrence(time, end) <= durationMs) {
                specficRows.add(r);
            }

        }
    
        return new Fight(specficRows, this.owner);
    }
    
    
    //parameter is the later fight
    public void combineFights(Fight later) {
        for (Row r:later.getRows()) {
            this.rows.add(r);
        }
    }
    public LocalTime getStart() {
        return this.rows.get(0).getTimestamp();
    }

    public LocalTime getEnd() {
        return this.rows.get(rows.size()-1).getTimestamp();

    }
    //these are only used for testing (at least in the current version)
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.rows);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if  (this   ==    obj) {
            return true;
        }
        if  (obj   ==    null) {
            return false;
        }
        if  (getClass() !=   obj.getClass()) {
            return false;
        }
        final Fight other = (Fight) obj;
        if  (!Objects.equals(this.rows, other.rows)) {
            return false;
        }
        return true;
    }
    
    
    
}
