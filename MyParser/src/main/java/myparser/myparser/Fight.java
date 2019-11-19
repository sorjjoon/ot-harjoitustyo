
package myparser.myparser;

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
            //this should always be the first row, so the loop is not "necessary", but there just in case, also the ExitCombat tag is here, in case we started logging midfight for some reason, 
            //or the user deleted rows for some reason 
            if(r.getEventtype()==Eventtype.EnterCombat||r.getEventtype()==Eventtype.ExitCombat){
                this.owner=r.getSource();
                return;
            }
        }
        //If we can't determine an owner for the log, we raise an exception, though I don't see how this is possible, but it's here just in case
        throw new NoOwnerException("NoOwnerException");
    }
        //In case we don't can't determine the owner automatically this constructor can be used (I don't see why it would ever be needed)
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
    
    public int getAllHealingToOwner(){
        int sum=0;
        for(Row r : this.rows){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Heal")&&r.getTarget().equals(this.owner)){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    public int getAllHealingByOwner(){
        int sum=0;
        for(Row r : this.rows){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Heal")&&r.getSource().equals(this.owner)){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    
    public int getAllDamageToOwner(){
        int sum=0;
        for(Row r : this.rows){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getTarget().equals(this.owner)){
                sum+=r.getDmg_heal();
            }
        }
        
        return sum;
    }
    
    public double dps(){
        long time_ms=this.getDuration();
        int dmg=this.getAllDamageByOwner();
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    
    public double htps(){
        long time_ms=this.getDuration();
        int dmg=this.getAllHealingToOwner();
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    public double hps(){
        long time_ms=this.getDuration();
        int dmg=this.getAllHealingByOwner();
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
    }
    
    public double dtps(){
        long time_ms=this.getDuration();
        int dmg=this.getAllDamageToOwner();
        int time_s=(int)time_ms/1000;
        return (double) dmg/time_s;
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
    
    
    //parameter is the later fight
    public void combineFights(Fight later){
        for(Row r:later.getRows()){
            this.rows.add(r);
        }
    }
    
    public HashMap<String,Integer> divideDamageTakenByOwner(){
        HashMap<String,Integer> results=new HashMap();
        for(Row r : this.rows){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getTarget().equals(this.owner)){
                if(results.get(r.getSource())==null){
                    results.put(r.getSource(), r.getDmg_heal());
                }else{
                    Integer helper =results.get(r.getSource());
                    results.put(r.getSource(), helper+r.getDmg_heal());
                }
            }
                
            }
        return results;
    }
    
    public HashMap<String,Integer> divideDamageDealtByTarget(){
        HashMap<String,Integer> results=new HashMap();
        for(Row r : this.rows){
            if(r.getEffecttype()!=null && r.getEffecttype().equals("Damage")&&r.getSource().equals(this.owner)){
                if(results.get(r.getTarget())==null){
                    results.put(r.getTarget(), r.getDmg_heal());
                }else{
                    Integer helper =results.get(r.getTarget());
                    results.put(r.getTarget(), helper+r.getDmg_heal());
                }
            }
                
            }
        return results;
    }
    
    public ArrayList<Row> rowsInTimeFrame(LocalTime start, LocalTime end){
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
        return specfic_rows;
    }
    
        
}
