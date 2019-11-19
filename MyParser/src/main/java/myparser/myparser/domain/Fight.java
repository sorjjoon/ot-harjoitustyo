
package myparser.myparser.domain;

import myparser.myparser.types.Eventtype;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *contains all the rows to a specific fight
 */
public class Fight {
    private ArrayList<Row> rows;
    private final String owner;

    public Fight(ArrayList<Row> rows) throws NoOwnerException {
        this.rows = rows;
        //Determine the "owner" of the log, this method should work everytime?
        for(Row r:rows){
            //EnterCombat should always be the first row, so the loop is not "necessary", but there just in case, also the ExitCombat tag is here, in case we started logging midfight for some reason, 
            //or the user deleted rows for some reason 
            if(r.getEventtype()==Eventtype.EnterCombat||r.getEventtype()==Eventtype.ExitCombat){
                this.owner=r.getSource();
                return;
            }
        }
        //If we can't determine an owner for the log, we raise an exception, though I don't see how this is possible, but it's here just in case
        throw new NoOwnerException("NoOwnerException");
    }
        //In case we can't determine the owner automatically this constructor can be used
        public Fight(ArrayList<Row> rows, String Owner) throws NoOwnerException {
        this.rows = rows;
        this.owner=Owner;
    }

    public String getOwner() {
        return owner;
    }
    
    @Override
    public String toString(){
        StringBuilder output = new StringBuilder();
        for(Row r:this.rows){
            output.append(r.toString());
            output.append("\n");
        }
        return output.toString();
        
        
    }

    public ArrayList<Row> getRows() {
        return rows;
    }
    
    public int getSize(){
        return this.rows.size();
    }
    
    
    public Fight rowsInTimeFrame(LocalTime start, LocalTime end) throws NoOwnerException{
        ArrayList<Row> specfic_rows =new ArrayList();
        for(Row r:this.rows){
            if(r.getTimestamp().isAfter(start)){
                if(r.getTimestamp().isBefore(end)){
                    break;
                }else{
                    specfic_rows.add(r);
                }
                
            }
        }
        return new Fight(specfic_rows,this.owner);
    }
    
    
    //parameter is the later fight, this method in case we want to add pvp stats (combine all the fights of a match)
    public void combineFights(Fight later){
        for(Row r:later.getRows()){
            this.rows.add(r);
        }
    }
    
}
