/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.io.Closeable;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import myparser.myparser.domain.Fight;
import myparser.myparser.domain.NoOwnerException;
import myparser.myparser.stats.Tuple;

/**
 *
 * Can be implemented by any form of data storage for combat logs. Extends Closeable
 */
public interface LogStorage extends Closeable {

    public void addListOfFights(List<Fight> fights, LocalDate date, String type, String logFileName) throws SQLException;

    public void reset() throws SQLException;

    public void close();

    public ArrayList<Fight> getFightsFromLog(String logName) throws SQLException, NoOwnerException;

    public List<String>[] getSavedLogsAndMessages() throws SQLException;

}
