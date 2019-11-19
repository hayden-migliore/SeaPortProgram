//Author: Hayden Migliore
//Program: PortTime
//Date: 5/10/19
//Purpose: record times

package seaportprogram;
import java.util.*;

public class PortTime {
    int time;
    
    PortTime(int time){
        this.time = time;
    }//end constructor
    
    
    public String toString(){
        String st = "PortTime: " + time;
        return st;
    }//end toString
    
    public int getTime(){
        return time;
    }//end getTime
}