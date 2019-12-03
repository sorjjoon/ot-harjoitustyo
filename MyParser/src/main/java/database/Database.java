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
 * pls don't look too closely at the column nates.. I know they are
 * inconsistent, but I caused some problems fixing this project to fit
 * checkstyle... I'll fix them later
 */
public class Database {

    private final Connection con;

    public Database() throws SQLException {
        try {
            Class.forName("org.h2.Driver");

        } catch (ClassNotFoundException e) {
            System.out.println("Can't locate h2 driver");
            System.out.println(e);
        }
        this.con = DriverManager.getConnection("jdbc:h2:./data/saved_logs", "sa", "");
//        this.createTables();

    }

    //Calling this method will create the tables needed for  the database to function (in case the file gets deleted for  some reason)
    //Note we are not puting boolean miss here, because we can get it from dmg (it's 0 for  miss, null for  non attacks)
    public void createTables() throws SQLException {
        con.prepareStatement("CREATE TABLE IF NOT EXISTS Log (id int NOT NULL AUTO_INCREMENT,date date, type varchar(20),owner varchar(30),log_name varchar(60))").executeUpdate();
        con.prepareStatement("CREATE TABLE IF NOT EXISTS Fight (id int NOT NULL AUTO_INCREMENT primary key,logId int NOT NULL,FOREIGN KEY(logId) REFERENCES Log(id),  PRIMARY KEY (id));").executeUpdate();
        con.prepareStatement("CREATE TABLE IF NOT EXISTS Row (fightId int NOT NULL,row_number int, timestamp time, Source varchar(50),  Target varchar(50),  Ability_name varchar(60),  type varchar (20),  Event_effect_type varchar (50),  Dmg_heal varchar(10),  crit boolean,shield boolean,FOREIGN KEY(fightId) REFERENCES Fight(id) );").executeUpdate();
        con.prepareStatement("CREATE TABLE IF NOT EXISTS Stats (fightId int NOT NULL, dps int, hps int,FOREIGN KEY(fightId) REFERENCES Fight(id));").executeUpdate();

    }

    public void close() throws SQLException {
        this.con.close();
    }

    //Deletes all data in the database and recreates tables
    public void reset() throws SQLException {
        con.prepareStatement("DROP TABLE Row IF EXISTS ").executeUpdate();
        con.prepareStatement("DROP TABLE Stats IF EXISTS").executeUpdate();
        con.prepareStatement("DROP TABLE Fight IF EXISTS").executeUpdate();
        con.prepareStatement("DROP TABLE Log IF EXISTS").executeUpdate();
        createTables();
    }

    public ArrayList<Fight> getFightsFromLog(String logName) throws SQLException, NoOwnerException {
        Integer id = getLogId(logName);
        ArrayList<Integer> fightIds = getFightIds(id);
        ArrayList<Fight> fights = new ArrayList();
        for (Integer fightId : fightIds) {
            fights.add(new Fight(getRowsFromFight(fightId)));
        }
        return fights;

    }

    public Integer getLogId(String logname) throws SQLException {
        String sql = "SELECT id FROM Log WHERE log_name LIKE ?";
        PreparedStatement stmnt = con.prepareStatement(sql);
        stmnt.setString(1, logname);
        ResultSet rs = stmnt.executeQuery();
        Integer id = null;
        //This is excepting that there is only one log of a specfic name in the database, TODO add functionality that you can't add the same log into the database
        if (rs.next()) {
            id = rs.getInt(1);
        }
        //if not found id is null
        return id;
    }

    public ArrayList<Integer> getFightIds(Integer logId) throws SQLException {
        ArrayList<Integer> ids = new ArrayList();
        String sql = "SELECT id FROM Fight WHERE logId = ?";
        PreparedStatement stmnt = con.prepareStatement(sql);
        stmnt.setInt(1, logId);
        ResultSet rs = stmnt.executeQuery();
        while (rs.next()) {
            ids.add(rs.getInt(1));
        }
        
        return ids;
        
    }
    
    /**
     * Creates a new row object based on a Result set and params. Params are there to fit checkstyle
     * TODO remove params from this method after the course has ended 
     * @param rs
     * @param rowNumber
     * @param timestamp
     * @param source
     * @param target
     * @param abilityName
     * @param type
     * @return
     * @throws SQLException 
     */
    private Row createRow(ResultSet rs, int rowNumber, LocalTime timestamp, String source, String target, String abilityName, Type type) throws SQLException {

        Eventtype eventtype = null;
        String effecttype = null;
        if (type == Type.Event) {
            eventtype = Eventtype.valueOf(rs.getString("Event_effect_type"));
            effecttype = null;    //not needed ofc, but here for  clarity
        } else if (type == Type.ApplyEffect || type == Type.RemoveEffect) {
            eventtype = null;
            effecttype = rs.getString("Event_effect_type");
        }
        int dmgHeal = rs.getInt("Dmg_heal");
        boolean crit = rs.getBoolean("Crit");
        boolean shielded = rs.getBoolean("Shield");
        //seting miss
        boolean miss = false;
        if (effecttype != null && effecttype.equals("Damage") && dmgHeal == 0) {
            miss = true;
        }
        return new Row(timestamp, source, target, type, effecttype, eventtype, abilityName, dmgHeal, crit, shielded, miss, rowNumber);

    }

    public ArrayList<Row> getRowsFromFight(Integer id) throws SQLException {
        String sql = "SELECT * FROM Row WHERE FightId = ? ORDER BY Row_number";
        PreparedStatement stmnt = con.prepareStatement(sql);
        stmnt.setInt(1, id);
        ResultSet rs = stmnt.executeQuery();
        ArrayList<Row> rows = new ArrayList();
        while (rs.next()) {
            //In order to fit checkstyle, had to split creating row into 2 methods (even though it's about 100 times clearer to just do everything in 1 method...)
            int rowNumber = rs.getInt("Row_number");
            LocalTime timestamp = rs.getTime("Timestamp").toLocalTime();
            String source = rs.getString("Source");
            String target = rs.getString("Target");
            String abilityName = rs.getString("Ability_name");
            Type type = Type.valueOf(rs.getString("Type"));
            Row row = createRow(rs, rowNumber, timestamp, source, target, abilityName, type);
            rows.add(row);
        }
        return rows;
    }

    private int createLogEntry(LocalDate date, String type, String logFileName, String owner) throws SQLException {
        String sql = "INSERT INTO Log (date,type,owner,log_name) VALUES (?, ?, ?, ?)";
        PreparedStatement stmnt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        stmnt.setObject(1, date);
        stmnt.setString(2, type);
        stmnt.setString(3, owner);        //All fights have the same owner so doesn't matter which one we use
        stmnt.setString(4, logFileName);
        stmnt.executeUpdate();

        ResultSet rs = stmnt.getGeneratedKeys();
        int logKey = 0;
        if (rs.next()) {
            logKey = rs.getInt(1);
        }
        return logKey;
    }

    private int createFightEntry(Fight fight, int logKey) throws SQLException {

        String sql = "INSERT INTO Fight (logId) VALUES (?)";

        PreparedStatement stmnt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        stmnt.setInt(1, logKey);
        stmnt.executeUpdate();

        ResultSet rs = stmnt.getGeneratedKeys();
        int fightKey = 0;
        if (rs.next()) {
            fightKey = rs.getInt(1);
//                System.out.println(key);
        }
        return fightKey;

    }

    private void addRow(Row r, int fightKey) throws SQLException {
        String sql = "INSERT INTO Row (fightId, timestamp, source,target, ability_name, type, event_effect_type, Dmg_heal ,  crit, shield,row_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmnt = con.prepareStatement(sql);
        stmnt.setInt(1, fightKey);
        stmnt.setObject(2, r.getTimestamp());
        stmnt.setString(3, r.getSource());
        stmnt.setString(4, r.getTarget());
        stmnt.setString(5, r.getAbilityName());
        stmnt.setString(6, r.getType().toString());
        if (r.getEffecttype() != null) {
            stmnt.setString(7, r.getEffecttype());
        } else {
            stmnt.setString(7, String.valueOf(r.getEventtype()));  //If both are null, we could choose to not put the row in at all (these rows are energy management rows),  but we could theoretically use those some day (tho it would be really complicated),  so at least in this version we are putting them in

        }
        stmnt.setString(8, String.valueOf(r.getDmgHeal()));
        stmnt.setBoolean(9, r.isCrit());
        stmnt.setBoolean(10, r.isShielded());
        stmnt.setInt(11, r.getRowNumber());
        stmnt.execute();

    }

    public void addListOfFights(ArrayList<Fight> fights, LocalDate date, String type, String logFileName) throws SQLException {
        if (fights.isEmpty()) {
            throw new IllegalArgumentException("list can't be empty");
        }

        int logKey = createLogEntry(date, type, logFileName, fights.get(0).getOwner());

        for (Fight f : fights) {
            int fightKey = createFightEntry(f, logKey);
            for (Row r : f.getRows()) { //This is a really stupid way to do this (we are setting fightId more times than is needed), but the only way I could get this method to pass Checkstyle
                addRow(r, fightKey);
            }

        }

    }

    public ArrayList<String> getSavedLogs() throws SQLException {
        ArrayList<String> files = new ArrayList();
        String sql = "SELECT log_name FROM Log ORDER BY date DESC";
        PreparedStatement stmnt = con.prepareStatement(sql);

        ResultSet rs = stmnt.executeQuery();
        while (rs.next()) {
            files.add(rs.getString(1));
        }
        return files;
    }

}
