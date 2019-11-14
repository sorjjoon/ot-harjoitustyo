
package myparser.myparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is responsible for reading the file passed to it as an argument (meaning path to the file), and creating Row objects based on read rows
 * and returning an ArrayList containing the rows it has read
 */
public class Reader {
    
    public ArrayList<Fight> readFile(String path,String date)throws FileNotFoundException{
        Scanner reader=new Scanner(new File(path));
        ArrayList<Row> lines = new ArrayList();
        ArrayList<Fight> fights = new ArrayList();
        while(reader.hasNext()){
            Row row=new Row(reader.nextLine(),date);
            
        }
        return fights;
    }
    
}
