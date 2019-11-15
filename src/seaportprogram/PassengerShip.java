//Author: Hayden Migliore
//Program: PassengerShip
//Date: 5/10/19
//Purpose: Create passenger Ship and get values

package sea.port.project;
import java.util.*;

public class PassengerShip extends Ship{
    int numberOfOccupiedRooms;
    int numberOfPassengers;
    int numberOfRooms;
    ArrayList<Integer> jobIds = new ArrayList<Integer>();
    
    PassengerShip(Scanner sc){
        super(sc);
        if(sc.hasNextInt()) numberOfPassengers = sc.nextInt();
        if(sc.hasNextInt()) numberOfRooms = sc.nextInt();
        if(sc.hasNextInt()) numberOfOccupiedRooms = sc.nextInt();
        while(sc.hasNextInt())
            if(sc.hasNextInt()) jobIds.add(sc.nextInt());
    }//end constructor
    
    @Override
    public String toString(){
        String st = "\nPassenger Ship: " + super.toString();
        if(jobs.size() == 0)
            return st;
        for(Job mj: jobs) st += "\n -" + mj;
        return st;
    }//end toString
    
    public int getNumberOfOccupiedRooms(){
        return numberOfOccupiedRooms;
    }
    public int getNumberOfPassengers(){
        return numberOfPassengers;
    }
    public int getNumberOfRooms(){
        return numberOfRooms;
    }
}
