//Author: Hayden Migliore
//Program: Dock
//Date: 5/10/19
//Purpose: Create docks, track ships at docks

package seaportprogram;
import java.util.*;

public class Dock extends Thing{
    Ship dockedShip;
    Ship waitingShip;
    
    Dock(Scanner sc){
        super(sc);
    }//end constructor
    
    @Override
    public String toString(){
        String st = "\nDock: " + super.toString() + dockedShip.toString();
        return st;
    }//end toString
    
    public Ship getShip(){
        return dockedShip;
    }//end getShip
    
    public void changeShips(){
        if (dockedShip.jobsCompleted)
            dockedShip = waitingShip;
    }//end changeShip
}
