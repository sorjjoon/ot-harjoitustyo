
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
    
    public static ArrayList<Fight> readFile(String path, String date)throws FileNotFoundException{
        Scanner reader=new Scanner(new File(path));
        
        ArrayList<Row> lines = new ArrayList();
        ArrayList<Fight> fights = new ArrayList();
        
        
        
        int i=0;
        ArrayList<Integer> list=new ArrayList();
        while(reader.hasNext()){
            i++;
            try{
                Row row=new Row(reader.nextLine(),date);
                System.out.println(row);
            }catch(Exception e){
                System.out.println("Rivin "+i+" luku ei onnistunut"+e.getMessage());
                list.add(i);
            }
        }
        
            for(Integer z : list){
                System.out.println(z);
            }
        return fights;
    }
    
    public static String dateFromPath(String path) throws Exception{
        String file_name = path.substring(path.lastIndexOf("/")+1);
        int index=file_name.indexOf("_");
        String date=file_name.substring(index+1,index+11)+" ";
        System.out.println(date);
        return date;
    }
    
}
