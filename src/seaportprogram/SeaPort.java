//Author: Hayden Migliore
//Program: SeaPort
//Date: 5/10/19
//Purpose: Create SeaPort, keep track of docks, ships, ships in que, 
//          people, and jobs at Seaport

package seaportprogram;
import java.util.*;

public class SeaPort extends Thing{
    ArrayList<Dock> docks = new ArrayList<Dock>(); 
    ArrayList<Ship> ships = new ArrayList<Ship>();
    ArrayList<Ship> que = new ArrayList<Ship>();
    ArrayList<Person> persons = new ArrayList<Person>();
    ArrayList<Job> jobs = new ArrayList<Job>();
    
    public HashMap<Integer, Dock> hmDocks = new HashMap<Integer, Dock>();
    public HashMap<Integer, Ship> hmShips = new HashMap<Integer, Ship>();
    public HashMap<Integer, Ship> hmQue = new HashMap<Integer, Ship>();
    public HashMap<Integer, Person> hmPersons = new HashMap<Integer, Person>();
    public HashMap<Integer, Job> hmJobs = new HashMap<Integer, Job>();
    
    SeaPort(Scanner sc){
        super(sc);
    }//end constructor
    
    @Override
    public String toString(){
        String st = "\n\nSeaPort:" + super.toString();
        for (Dock md: docks) st += "\n" + md;
        st += "\n\n -- List of all ships in que:";
        for (Ship ms: que) st += "\n >" + ms;
        st += "\n\n -- List of all ships:";
        for (Ship ms: ships) st += "\n >" + ms;
        st += "\n\n -- List of all persons:";
        for (Person mp: persons) st += "\n >" + mp;
        return st;
    }//end toString
    
    public String toStringShort(){
        String st = "\n" + name + " " + index;
        return st;
    }//end toStringShort
    
    public ArrayList<Dock> getDocks(){
        return docks;
    }//end getDocks
    
    public ArrayList<Ship> getShips(){
        return ships;
    }//end getShips
    
    public ArrayList<Ship> getQue(){
        return que;
    }//end getQue
    
    public ArrayList<Person> getPersons(){
        return persons;
    }//end getPersons
    
    public ArrayList<Job> getJobs(){
        return jobs;
    }//end getJobs
}
