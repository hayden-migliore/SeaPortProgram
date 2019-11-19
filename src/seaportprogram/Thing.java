//Author: Hayden Migliore
//Program: Thing
//Date: 5/10/19
//Purpose: Create basic thing, Used as basis for ships, persons, world, and seaports

package seaportprogram;
import java.util.*;

public class Thing implements Comparable<Thing>{
    int index;
    String name = "";
    int parent;
    
    public Thing(int index, String name, int parent){
        this.index = index;
        this.name = name;
        this.parent = parent;
    }//end constructor for world
    
    public Thing(Scanner sc){
        if(sc.hasNext()) name = sc.next();
        if(sc.hasNextInt()) index = sc.nextInt();
        if(sc.hasNextInt()) parent = sc.nextInt();
    }//end scanner constructor
    
    public String toString(){
        String st = name + " " + index;
        return st;
    }//end toString
    
    public String getName(){
        return name;
    }//end getName
    
    public int getIndex(){
        return index;
    }//end getIndex
    
    public int getParent(){
        return parent;
    }//end getParent 
    
    public int compareTo(Thing compareThing){
        int compareIndex = compareThing.getIndex();
        return this.index - compareIndex;
    }//end compareTo
    
    public static Comparator<Thing> ThingNameComparator = new Comparator<Thing>(){
       public int compare(Thing thing1, Thing thing2){
           String thingName1 = thing1.getName().toUpperCase();
           String thingName2 = thing2.getName().toUpperCase();
           return thingName1.compareTo(thingName2);
       } //end compare
    };//end constructor for ThingNameComparator
    
}

