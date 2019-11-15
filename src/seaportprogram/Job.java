//Author: Hayden Migliore
//Program: Job
//Date: 5/10/19
//Purpose: keep track of Jobs, run, stop, suspend, and resume

package sea.port.project;
import java.util.*;

public class Job extends Thing implements Runnable{
    double duration;
    ArrayList<String> requirements = new ArrayList<String>();
    Person worker = null;
    Person worker2 = null;
    Person worker3 = null;
    Ship ship = null;
    boolean suspended = false;
    boolean completed = false;
    boolean exit = false;
    
    Job(Scanner sc){
        super(sc);
        if(sc.hasNextDouble()) duration = sc.nextDouble();
        while (sc.hasNext())
            requirements.add(sc.next());
    }//end constructor
    
    public void run(){
        synchronized(worker){
            System.out.println("\nJob " + index + " has started.");
                try{
                    worker.working = true;
                    System.out.println("\nJob " + index + " is running.");
                    System.out.println("\nUsing " + worker.name);
                    Thread.sleep(10000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            ship.jobsCompleted = true;
            completed = true;
            worker.working = false;
            System.out.println("\nJob " + index + " has ended.");
            worker.notify();
        }
    }//end run
    
    void stop(){
        exit = true;
        System.out.println("Job stopped.");
    }//end stop
    
    void suspend(){
        suspended = true; 
        System.out.println("Job suspended.");
    }//end suspend
    
    public void resume(){
        suspended = false;
        System.out.println("Job resumed.");
    }//end resume
    
    public void setWorker(Person person){
        worker = person;
    }//end setWorker
    
    public void setShip(Ship ship){
        this.ship = ship;
    }//end setShip
    
    
    public String toString(){
        String st = "Index: " + index + "\n Name: " + name + "\n Parent: " + parent +
                "\n Duration: " + duration + "\n Requirements: ";
        for(int i = 0; i < requirements.size(); i++){
            st = st.concat(requirements.get(i));
        }
        return st;
    }//end toString
    
    public double getDuration(){
        return duration;
    }//end getDuration
}

