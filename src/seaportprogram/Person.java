//Author: Hayden Migliore
//Program: Person
//Date: 5/10/19
//Purpose: create person

package sea.port.project;
import java.util.*;

public class Person extends Thing {
    String skill;
    Boolean working = false;
    
    Person(Scanner sc){
        super(sc);
        if(sc.hasNext()) skill = sc.next();
    }//end constructor
    
    @Override
    public String toString(){
        String st = "\nPerson: " + super.toString() + " " + skill;
        return st;
    }//end toString
    
    public String getSkill(){
        return skill;
    }// end getSkill
}
