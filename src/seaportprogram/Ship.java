//Author: Hayden Migliore
//Program: Ship
//Date: 5/10/19
//Purpose: Create Basic Ship, Used as base for Cargo and Passenger Ship

package seaportprogram;
import java.util.*;

public class Ship extends Thing{
    PortTime arrivalTime;
    PortTime dockTime;
    double draft;
    double length;
    double weight;
    double width;
    ArrayList<Job> jobs = new ArrayList<Job>();
    Boolean jobsCompleted = false;
    
    public Ship(Scanner sc){
        super(sc);
        if(sc.hasNextDouble()) weight = sc.nextDouble();
        if(sc.hasNextDouble()) length = sc.nextDouble();
        if(sc.hasNextDouble()) width = sc.nextDouble();
        if(sc.hasNextDouble()) draft = sc.nextDouble();
    }
    
    @Override
    public String toString(){
        String st = super.toString();
        return st;
    }//end toString
    
    public PortTime getArrivalTime(){
        return arrivalTime;
    }
    
    public PortTime getDockTime(){
        return dockTime;
    }
    
    public double getWeight(){
        return weight;
    }
    public double getLength(){
        return length;
    }
    public double getWidth(){
        return width;
    }
    public double getDraft(){
        return draft;
    }
    public ArrayList<Job> getJobs(){
        return jobs;
    }
    
    public static Comparator<Ship> WeightComparator = new Comparator<Ship>(){
       public int compare(Ship ship1, Ship ship2){
           return Double.compare(ship1.getWeight(), ship2.getWeight());
       } 
    };
    
    public static Comparator<Ship> WidthComparator = new Comparator<Ship>(){
       public int compare(Ship ship1, Ship ship2){
           return Double.compare(ship1.getWidth(), ship2.getWidth());
       } 
    };
    
    public static Comparator<Ship> LengthComparator = new Comparator<Ship>(){
       public int compare(Ship ship1, Ship ship2){
           return Double.compare(ship1.getLength(), ship2.getLength());
       } 
    };
    
    public static Comparator<Ship> DraftComparator = new Comparator<Ship>(){
       public int compare(Ship ship1, Ship ship2){
           return Double.compare(ship1.getDraft(), ship2.getDraft());
       } 
    };
}
