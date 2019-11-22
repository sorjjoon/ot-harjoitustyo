/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.Row;

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
            con.prepareStatement("CREATE TABLE IF NOT EXISTS Row (fight_id int NOT NULL, timestamp time, source varchar(30), target varchar(30), ability_name varchar(40), type varchar (20), event_effect_type varchar (50), dmg_heal varchar(10), crit boolean,shield boolean,FOREIGN KEY(fight_id) REFERENCES Fight(id) );").executeUpdate();
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
       
       
       public void addFight(Fight fight, String date, String type,String log_file_name)throws SQLException{
           ArrayList<Row> rows=fight.getRows();
           String sql="INSERT INTO Log (date,type,owner,log_file_name) VALUES (?,?,?,?)";
           PreparedStatement stmnt=con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
           stmnt.setString(1, date);
           stmnt.setString(2, type);
           stmnt.setString(3, fight.getOwner());
           stmnt.setString(4, log_file_name);
           stmnt.executeUpdate();

           ResultSet rs = stmnt.getGeneratedKeys();
           int key_log=0;
            if (rs.next()) {
                key_log = rs.getInt(1);
//                System.out.println(key);
            }
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
            
            sql="INSERT INTO Row (fight_id, timestamp, source,target, ability_name, type, event_effect_type, dmg_heal , crit, shield) VALUES (?,?,?,?,?,?,?,?,?,?)";
            stmnt=con.prepareStatement(sql);
            stmnt.setInt(1,key_fight);            
           for(Row r:rows){
               stmnt.setString(2, r.getTimestamp().toString());
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
               stmnt.execute();
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
