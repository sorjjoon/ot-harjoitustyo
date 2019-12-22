package myparser.myparser.domain;

import myparser.myparser.types.EventType;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;
import myparser.myparser.stats.Stats;

/**
 * contains all the rows to a specific fight
 * <p>
 * ArrayList of rows given to the constructor must be ordered by row number
 */
public class Fight {

    private final ArrayList<Row> rows;
    private final String owner;

    /**
     * Create a new fight instance from a set of rows. Determines the Fight
     * owner automatically from EnterCombat row target
     * <p>
     * given ArrayList of rows must be ordered by row number
     *
     * @param rows
     * @throws NoOwnerException in case an owner can't be determined
     * automatically (will only happen, if both enter and exit combat rows have
     * been removed)
     */
    public Fight(ArrayList<Row> rows) throws NoOwnerException {
        this.rows = rows;
        for (Row r : rows) {
            //EnterCombat should always be the first row, so the loop is not "necessary", the ExitCombat tag is here, in case we started logging midfight for  some reason, 
            //or the user deleted rows for  some reason (though in the current version if the entercombat row had been deleted the fight wouldn't be read

            if (r.getEventtype() == EventType.EnterCombat || r.getEventtype() == EventType.ExitCombat) {
                this.owner = r.getSource();
                return;
            }
        }
        //If we can't determine an owner for  the log, we raise an exception 
        throw new NoOwnerException("Couldn't determine owner");
    }

    /**
     * In case we can't determine the owner automatically this constructor can
     * be used (mainly when reading from database)
     *
     * @param rows
     * @param owner
     */
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
        for (Row r : this.rows) {
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

    /**
     * returns a new fight instance which contains only rows in the given time
     * frame
     *
     * @param start
     * @param end
     */
    public Fight rowsInTimeFrame(LocalTime start, LocalTime end) {
        ArrayList<Row> specficRows = new ArrayList();

        long durationMs = Stats.durationMs(this);

        for (Row r : this.rows) {
            LocalTime time = r.getTimestamp();
            //checking distance to start and end
            //TODO if we need to make this more effecient (probably not needed), we could make this loop stop after we have passed end
            if (Stats.getDiffrence(start, time) <= durationMs && Stats.getDiffrence(time, end) <= durationMs) {
                specficRows.add(r);
            }

        }

        return new Fight(specficRows, this.owner);
    }

    /**
     * combine 2 fights into a new fight.
     * <p>
     * Rows in the parameter fight are added after the fight this method is
     * called on
     *
     * @param later
     */
    public void combineFights(Fight later) {
        for (Row r : later.getRows()) {
            this.rows.add(r);
        }
    }

    /**
     * Get the timestamp on the first row in a fight
     *
     * @return
     */
    public LocalTime getStart() {
        return this.rows.get(0).getTimestamp();
    }

    /**
     * get timestamp on the last row in the fight
     *
     * @return
     */
    public LocalTime getEnd() {
        return this.rows.get(rows.size() - 1).getTimestamp();

    }

    /**
     * In case of NoOwnerException, because enter and exit combat rows have been
     * deleted, this method checks for Owner by looking at rows, which have the
     * same source and target
     *
     * @param rows
     * @return
     * @throws NoOwnerException if owner can't be determined
     */
    public static String determineOwner(ArrayList<Row> rows) throws NoOwnerException {
        for (Row r : rows) {
            if (r.getTarget().equals(r.getSource())) {
                return r.getTarget();
            }
        }
        throw new NoOwnerException();
    }

    //these are only used for testing (at least in the current version)
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.rows);
        return hash;
    }

    /**
     * Rows are equal if all rows in them are equal
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Fight other = (Fight) obj;
        if (!Objects.equals(this.rows, other.rows)) {
            return false;
        }
        return true;
    }

}
