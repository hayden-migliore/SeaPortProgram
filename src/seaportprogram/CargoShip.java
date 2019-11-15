//Author: Hayden Migliore
//Program: CargoShip
//Date: 5/10/19
//Purpose: Create Cargo Ship and get values

package sea.port.project;
import java.util.*;

public class CargoShip extends Ship{
    double cargoValue;
    double cargoVolume;
    double cargoWeight;
    ArrayList<Integer> jobIds = new ArrayList<Integer>();
    
    CargoShip(Scanner sc){
        super(sc);
        if(sc.hasNextDouble()) cargoWeight = sc.nextDouble();
        if(sc.hasNextDouble()) cargoVolume = sc.nextDouble();
        if(sc.hasNextDouble()) cargoValue = sc.nextDouble();
        while(sc.hasNextInt())
            if(sc.hasNextInt()) jobIds.add(sc.nextInt());
    }//end constructor
    
    @Override
    public String toString(){
        String st = "\nCargo Ship: " + super.toString();
        if(jobs.size() == 0)
            return st;
        for(Job mj: jobs) st += "\n -" + mj;
        return st;
    }//end toString
    
    public double getCargoValue(){
        return cargoValue;
    }//end getCargoValue
    
    public double getCargoVolume(){
        return cargoVolume;
    }//end getCargoVolume
    
    public double getCargoWeight(){
        return cargoWeight;
    }//end getCargoWeight
}

