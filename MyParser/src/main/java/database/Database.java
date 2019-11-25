/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.domain.Row;
import myparser.myparser.types.Eventtype;
import myparser.myparser.types.Type;

/**
 *
 * @author joona
 */
public class Database {
    private final Connection con;

    public Database() throws SQLException{
        try{
            Class.forName ("org.h2.Driver"); 
        
        }catch(ClassNotFoundException e){
            
        }
        this.con = DriverManager.getConnection("jdbc:h2:./data/saved_logs", "sa", "");
        
    }
    
    //Calling this method will create the tables needed for the database to function (in case the file gets deleted for some reason)
    //Note we are not puting boolean miss here, because we can get it from dmg (it's 0 for miss, null for non attacks)
    
    public void createTables()throws SQLException{
            con.prepareStatement("CREATE TABLE IF NOT EXISTS Log (id int NOT NULL AUTO_INCREMENT,date date, type varchar(20),owner varchar(30),log_file_name varchar(60))").executeUpdate();
            con.prepareStatement("CREATE TABLE IF NOT EXISTS Fight (id int NOT NULL AUTO_INCREMENT primary key,log_id int NOT NULL,FOREIGN KEY(log_id) REFERENCES Log(id), PRIMARY KEY (id));").executeUpdate();
//            con.prepareStatement("ALTER TABLE fight ALTER COLUMN id RESTART WITH 1").executeUpdate();
            con.prepareStatement("CREATE TABLE IF NOT EXISTS Row (fight_id int NOT NULL,row_number int, timestamp time, source varchar(30), target varchar(30), ability_name varchar(40), type varchar (20), event_effect_type varchar (50), dmg_heal varchar(10), crit boolean,shield boolean,FOREIGN KEY(fight_id) REFERENCES Fight(id) );").executeUpdate();
            con.prepareStatement("CREATE TABLE IF NOT EXISTS Stats (fight_id int NOT NULL, dps int, hps int,FOREIGN KEY(fight_id) REFERENCES Fight(id));").executeUpdate();
            
        
        
    }
       public void close()throws SQLException{
           this.con.close();
       }
       
       //Deletes all data in the database and recreates tables
       public void reset()throws SQLException{           
           con.prepareStatement("DROP TABLE Row IF EXISTS ").executeUpdate();           
           con.prepareStatement("DROP TABLE Stats IF EXISTS").executeUpdate();          
           con.prepareStatement("DROP TABLE Fight IF EXISTS").executeUpdate();
            con.prepareStatement("DROP TABLE Log IF EXISTS").executeUpdate(); 
           createTables();
       }
       public ArrayList<Fight> getFightsFromLog(String log_name)throws SQLException,NoOwnerException{
           Integer id=getLogId(log_name);
           ArrayList<Integer> fight_ids=getFightIds(id);
           ArrayList<Fight> fights=new ArrayList();
           for(Integer fight_id : fight_ids){
               fights.add(new Fight(getRowsFromFight(fight_id)));
           }
           return fights;
           
       }
       
       public Integer getLogId(String logname)throws SQLException{
           String sql ="SELECT id FROM Log WHERE log_file_name LIKE ?";
           PreparedStatement stmnt=con.prepareStatement(sql);
           stmnt.setString(1, logname);
           ResultSet rs=stmnt.executeQuery();
           Integer id=null;
           //This is excepting that there is only one log of a specfic name in the database, TODO add functionality that you can't add the same log into the database
           if(rs.next()){
               id=rs.getInt(1);
           }
           //if not found id is null
           return id;
       }
       
       public ArrayList<Integer> getFightIds(Integer log_id)throws SQLException{
           ArrayList<Integer> ids=new ArrayList();
           String sql="SELECT id FROM Fight WHERE log_id=?";
           PreparedStatement stmnt=con.prepareStatement(sql);
           stmnt.setInt(1, log_id);
           ResultSet rs=stmnt.executeQuery();
           while(rs.next()){
               ids.add(rs.getInt(1));
           }
           
           
           return ids;
       }
       
       public ArrayList<Row> getRowsFromFight(Integer id)throws SQLException{
           String sql="SELECT * FROM Row WHERE Fight_id = ? ORDER BY Row_number";
           PreparedStatement stmnt=con.prepareStatement(sql);
           stmnt.setInt(1, id);
           ResultSet rs=stmnt.executeQuery();
           ArrayList<Row> rows=new ArrayList();
           while(rs.next()){
               int row_number=rs.getInt("Row_number");
               LocalTime timestamp=rs.getTime("Timestamp").toLocalTime();
               String source =rs.getString("Source");
               String target=rs.getString("Target");
               String ablity_name=rs.getString("Ability_name");
               Type type =Type.valueOf(rs.getString("Type"));
               
                Eventtype eventtype=null;
                String effecttype =null;
               if(type==Type.Event){
                   eventtype=Eventtype.valueOf(rs.getString("Event_effect_type"));
                   effecttype =null;    //not needed ofc, but here for clarity
               }else if(type==Type.ApplyEffect||type==Type.RemoveEffect){
                   eventtype=null;
                   effecttype=rs.getString("Event_effect_type");
               }
               int dmg_heal=rs.getInt("Dmg_heal");
               boolean crit=rs.getBoolean("Crit");
               boolean shielded=rs.getBoolean("Shield");
               //seting miss
               boolean miss=false;
               if(effecttype!=null&&effecttype.equals("Damage")&&dmg_heal==0){
                   miss=true;
               }
               Row row=new Row(timestamp,source,target,type,effecttype,eventtype,ablity_name,dmg_heal, crit,shielded,miss,row_number);
               rows.add(row);
           }
           return rows;
       }
       
       
       public void addListOfFights(ArrayList<Fight> fights, LocalDate date, String type,String log_file_name)throws SQLException{
           if(fights.isEmpty()){
               throw new IllegalArgumentException("list can't be empty");
           }
           String sql="INSERT INTO Log (date,type,owner,log_file_name) VALUES (?,?,?,?)";
           PreparedStatement stmnt=con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
           stmnt.setObject(1, date);
           stmnt.setString(2, type);
           stmnt.setString(3, fights.get(0).getOwner());        //All fights have the same owner so doesn't matter which one we use
           stmnt.setString(4, log_file_name);
           stmnt.executeUpdate();

           ResultSet rs = stmnt.getGeneratedKeys();
           int key_log=0;
            if (rs.next()) {
                key_log = rs.getInt(1);
//                System.out.println(key);
            }
            
            for(Fight f:fights){
                ArrayList<Row> rows=f.getRows();
                
                sql="INSERT INTO Fight (log_id) VALUES (?)";
                
                stmnt=con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                stmnt.setInt(1, key_log);
                stmnt.executeUpdate();

                rs = stmnt.getGeneratedKeys();
                int key_fight=0;
                if (rs.next()) {
                    key_fight = rs.getInt(1);
    //                System.out.println(key);
                }

                sql="INSERT INTO Row (fight_id, timestamp, source,target, ability_name, type, event_effect_type, dmg_heal , crit, shield,row_number) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                stmnt=con.prepareStatement(sql);
                stmnt.setInt(1,key_fight);            
               for(Row r:rows){
                   stmnt.setObject(2, r.getTimestamp());
                   stmnt.setString(3,r.getSource());
                   stmnt.setString(4,r.getTarget());
                   stmnt.setString(5,r.getAbility_name());
                   stmnt.setString(6, r.getType().toString());
                   if(r.getEffecttype()!=null){
                       stmnt.setString(7, r.getEffecttype());

                   }else{
                       //If both are null, we could choose to not put the row in at all (these rows are energy management rows), but we could theoretically use those some day (tho it would be really complicated), so at least in this version we are putting them in
                       stmnt.setString(7, String.valueOf(r.getEventtype()));
                   }
                   stmnt.setString(8, String.valueOf(r.getDmg_heal()));
                   stmnt.setBoolean(9, r.isCrit());
                   stmnt.setBoolean(10, r.isShielded());
                   stmnt.setInt(11, r.getRow_number());
                   stmnt.execute();
               }
            }
            
           
       }
       
       public ArrayList<String> getSavedLogs()throws SQLException{
           ArrayList<String> files=new ArrayList();
           String sql="SELECT log_file_name FROM Log ORDER BY date DESC";
           PreparedStatement stmnt=con.prepareStatement(sql);
           
           ResultSet rs=stmnt.executeQuery();
           while(rs.next()){
               files.add(rs.getString(1));
           }
           return files;
       }
    
}
