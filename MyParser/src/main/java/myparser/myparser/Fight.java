
package myparser.myparser;

import java.time.LocalTime;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.util.ArrayList;

/**
 *contains all the rows to a specific fight
 */
public class Fight {
    private final ArrayList<Row> rows;
    private final String owner;

    public Fight(ArrayList<Row> rows) throws NoOwnerException {
        this.rows = rows;
        //Determine the "owner" of the log, this method should work everytime?
        for(Row r:rows){
            if(r.getEventtype()==Eventtype.EnterCombat||r.getEventtype()==Eventtype.ExitCombat){
                this.owner=r.getSource();
                return;
            }
        }
        //If we can't determine an owner for the log, we raise an exception
        throw new NoOwnerException("NoOwnerException");
    }
        //In case we don't need to determine the owner
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
    
    public int getAllDamageByOwner(){
        int sum=0;
        for(Row r : this.rows){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getSource().equals(this.owner)){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    
    
    public long getDuration(){
        LocalTime start=rows.get(0).getTimestamp();
        LocalTime end=rows.get(rows.size()-1).getTimestamp();
        long time=start.until(end, MILLIS);
        if(time<0){
            time+=8.64e+7;
        }
        return time;
    }    
        
}
