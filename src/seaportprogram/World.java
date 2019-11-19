//Author: Hayden Migliore
//Program: SeaPortProgram
//Date: 5/10/19
//Purpose: Create World, does main processing for SeaPortProgram.java

package seaportprogram;
import java.util.*;

public class World extends Thing{
    PortTime time;
    ArrayList<SeaPort> ports = new ArrayList<SeaPort>();
    ArrayList<String> seaPortNames = new ArrayList<String>();
    ArrayList<String> dockNames = new ArrayList<String>();
    ArrayList<String> shipNames = new ArrayList<String>();
    ArrayList<String> personNames = new ArrayList<String>();
    ArrayList< ArrayList<String> > namesList = new ArrayList< ArrayList<String> >(4);
    static int jobCounter = 0;
    HashMap<Integer, SeaPort> hmPorts = new HashMap<Integer, SeaPort>();
    
    
    World(int index, String name, int parent, PortTime time){
        super(index, name, parent);
        this.time = time;
    }//end constructor
    
    @Override
    public String toString(){
        String st = "Index: " + index + "\n Name: " + name + "\n Parent: " + parent + "\n PortTime " + time;
        return st;
    }//end toString
    
    public PortTime getTime(){
        return time;
    }//end getTime
    
    public ArrayList<SeaPort> getPorts(){
        return ports;
    }//end getPorts
    
    ArrayList<String> workerCheck(){
        ArrayList<String> workers = new ArrayList<String>();
        for(SeaPort msp : ports){
            for(Person person : msp.persons){
                if (person.working){
                    workers.add(person.name + " " + person.skill);
                }
            }
        }
        return workers;
    }//end workerCheck
    
    ArrayList<String> nonWorkerCheck(){
        ArrayList<String> workers = new ArrayList<String>();
        for(SeaPort msp : ports){
            for(Person person : msp.persons){
                if (!person.working){
                    workers.add(person.name + " " + person.skill);
                }
            }
        }
        return workers;
    }//end nonWorkerCheck
    
    ArrayList<String> jobCheck(){
        ArrayList<String> jobs = new ArrayList<String>();
        for(SeaPort msp : ports){
            for(Job job : msp.jobs){
                if (!job.completed){
                    jobs.add(job.name);
                }
            }
        }
        return jobs;
    }//end jobCheck
    
    int Process(String st) throws SkillUnavailable{
        System.out.println("Processing >" + st + "<");
        Scanner sc = new Scanner(st);
        if(!sc.hasNext())
            return jobCounter;
        switch(sc.next()){
            case "port" :addPort(sc);
                break;
            case "dock" :addDock(sc, hmPorts);
                break;
            case "pship" :addPShip(sc, hmPorts);
                break;
            case "cship" :addCShip(sc, hmPorts);
                break;
            case "person" :addPerson(sc, hmPorts);
                break;
            case "job" : addJob(sc, hmPorts);
                break;
        }// end switch
        return jobCounter;
    }//end Process
    
    void suspendJob(){
        for(SeaPort msp : ports){
            for(Job job : msp.jobs){
                if (!job.completed){
                    job.suspend();
                }
            }
        }
    }//end suspendJob
    
    void resumeJob(){
        for(SeaPort msp : ports){
            for(Job job : msp.jobs){
                if (!job.completed){
                    job.resume();
                }
            }
        }
    }//end resumeJob
    
    void cancelJob(){
        for(SeaPort msp : ports){
            for(Job job : msp.jobs){
                if (!job.completed){
                    job.stop();
                }
            }
        }
    }//end cancelJob
    
    void addPort(Scanner sc){
        SeaPort port = new SeaPort(sc);
        ports.add(port);
        hmPorts.put(port.getIndex(), port);
    }//end addPort
    
    void addDock(Scanner sc, HashMap<Integer, SeaPort> hmp){
        Dock dock = new Dock(sc);
        SeaPort port = hmp.get(dock.getParent());
        port.docks.add(dock);
        port.hmDocks.put(dock.getIndex(), dock);
    }//end addDock
    
    void addPShip(Scanner sc, HashMap<Integer, SeaPort> hmp){
        PassengerShip pShip = new PassengerShip(sc);
        Dock dock = getDockByIndex(pShip.getParent());
        SeaPort port = hmp.get(dock.getParent());
        if (dock.dockedShip == null){
            dock.dockedShip = pShip;
        }
        else{
            dock.waitingShip = pShip;
            port.que.add(pShip);
            port.hmQue.put(pShip.getIndex(), pShip);
        }
        port.ships.add(pShip);
        port.hmShips.put(pShip.getIndex(), pShip);
    }//end addPShip
    
    void addCShip(Scanner sc, HashMap<Integer, SeaPort> hmp){
        CargoShip cShip = new CargoShip(sc);
        Dock dock = getDockByIndex(cShip.getParent());
        SeaPort port = hmp.get(dock.getParent());
        if (dock.dockedShip == null){
            dock.dockedShip = cShip;
        }
        else{
            dock.waitingShip = cShip;
            port.que.add(cShip);
            port.hmQue.put(cShip.getIndex(), cShip);
        }
        port.ships.add(cShip);
        port.hmShips.put(cShip.getIndex(), cShip);
    }//end addCShip
    
    void addPerson(Scanner sc, HashMap<Integer, SeaPort> hmp){
        Person person = new Person(sc);
        SeaPort port = hmPorts.get(person.getParent());
        port.persons.add(person);
        port.hmPersons.put(person.getIndex(), person);
    }//end addPerson
    
    void addJob(Scanner sc, HashMap<Integer, SeaPort> hmp) throws SkillUnavailable{
        Job job = new Job(sc);
        Ship ship = getShipByIndex(job.getParent());
        Dock dock = getDockByIndex(ship.getParent());
        SeaPort port = hmp.get(dock.getParent());
        port.jobs.add(job);
        port.hmJobs.put(job.getIndex(), job);
        ArrayList<Person> persons = new ArrayList<Person>();
        Person worker = getPersonsObjBySkill(job.requirements.get(0));
        if (job.requirements.size() > 2){
            Person worker2 = getPersonsObjBySkill(job.requirements.get(1));
            job.worker2 = worker2;
            if(job.requirements.size() > 3){
                Person worker3 = getPersonsObjBySkill(job.requirements.get(2));
                job.worker3 = worker3; 
            }
        }
        if (worker == null){
            throw new SkillUnavailable("No workers with required skill available.");
        }
        job.setWorker(worker);
        job.setShip(ship);
        Thread thread = new Thread(job);
        thread.start();
        synchronized(worker){
            try{
                while(worker.working || job.suspended || job.exit){
                    worker.wait();
                }
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        dock.changeShips();
        port.que.remove(ship);
        jobCounter += 1;
    }//end addJob
    
    //This function is used for mapping after reading the file        
    Dock getDockByIndex(int x){
        for (SeaPort msp: ports){
            return msp.hmDocks.get(x);
        }
        return null;
    }// end getDockByIndex
    
    Ship getShipByIndex(int x){
        for (SeaPort msp: ports){
            return msp.hmShips.get(x);
        }
        return null;
    }// end getShipByIndex
    
    //The functions used below are for the Search Button
    ArrayList<String> getPortsByIndex(int i){
        ArrayList<String> portst = new ArrayList<String>();
        SeaPort port = hmPorts.get(i);
        if (port != null)
            portst.add(port.toString());
        return portst;
    }//end getPortsByIndex
        
    ArrayList<String> getDocksByIndex(int i){
        ArrayList<String> docks = new ArrayList<String>();
        for (SeaPort msp: ports){
            Dock dock = msp.hmDocks.get(i);
            if (dock != null)
                docks.add(dock.toString());
        }
        return docks;
    }//end getDocksbyIndex
    
    ArrayList<String> getShipsByIndex(int i){
        ArrayList<String> ships = new ArrayList<String>();
        for (SeaPort msp: ports){
            Ship ship = msp.hmShips.get(i);
            if (ship != null)
                ships.add(ship.toString());
        }
        return ships;
    }//end getShipsByIndex
    
    ArrayList<String> getPersonsByIndex(int i){
        ArrayList<String> persons = new ArrayList<String>();
        for (SeaPort msp: ports){
            Person person = msp.hmPersons.get(i);
            if (person != null)
                persons.add(person.toString());
        }
        return persons;
    }//end getPersonByIndex
    
    ArrayList<String> getJobsByIndex(int i){
        ArrayList<String> jobs = new ArrayList<String>();
        for (SeaPort msp: ports){
            Job job = msp.hmJobs.get(i);
            if (job != null)
                jobs.add(job.toString());
        }
        return jobs;
    }//end getJobByIndex
    
    ArrayList<String> getPortsByName(String st){
        ArrayList<String> portst = new ArrayList<String>();
        for (SeaPort mp: ports){
            if(mp.name.equals(st))
                portst.add(mp.toString());
        }
        return portst;
    }//end getPortsByName
    
    ArrayList<String> getDocksByName(String st){
        ArrayList<String> docks = new ArrayList<String>();
        for (SeaPort msp: ports){
            for (Dock md: msp.docks){
                if(md.name.equals(st))
                    docks.add(md.toString());
            }
        }
        return docks;
    }//end getDocksbyName
    
    ArrayList<String> getShipsByName(String st){
        ArrayList<String> ships = new ArrayList<String>();
        for (SeaPort msp: ports){
            for (Ship ms: msp.ships){
                if(ms.name.equals(st))
                    ships.add(ms.toString());
            }
        }
        return ships;
    }//end getShipsByName
    
    ArrayList<String> getPersonsByName(String st){
        ArrayList<String> persons = new ArrayList<String>();
        for (SeaPort msp: ports){
            for (Person mp: msp.persons){
                if(mp.name.equals(st))
                    persons.add(mp.toString());
            }
        }
        return persons;
    }//end getPersonByName
    
    ArrayList<String> getJobsByName(String st){
        ArrayList<String> jobs = new ArrayList<String>();
        for (SeaPort msp: ports){
            for (Job mj: msp.jobs){
                if(mj.name.equals(st))
                    jobs.add(mj.toString());
            }
        }
        return jobs;
    }//end getJobsByName

    ArrayList<String> getPersonsBySkill(String st){
        ArrayList<String> persons = new ArrayList<String>();
        for (SeaPort msp: ports){
            for (Person mp: msp.persons){
                if(mp.skill.equals(st))
                    persons.add(mp.toString());
            }
        }
        return persons;
    }//end getPersonBySkill
    
    Person getPersonsObjBySkill(String st){
        for (SeaPort msp: ports){
            for (Person mp: msp.persons){
                if(mp.skill.equals(st))
                    return mp;
            }
        }
        return null;
    }//end getPersonBySkill
    
    ArrayList<String> getPortsByParent(int i){
        ArrayList<String> portst = new ArrayList<String>();
        for (SeaPort mp: ports){
            if(mp.parent == i)
                portst.add(mp.toString());
        }
        return portst;
    }//end getPortsByParent
        
    ArrayList<String> getDocksByParent(int i){
        ArrayList<String> docks = new ArrayList<String>();
        SeaPort port = hmPorts.get(i);
        for(Dock md: port.docks){
            docks.add(md.toString());
        }
        return docks;
    }//end getDocksbyParent
    
    ArrayList<String> getShipsByParent(int i){
        ArrayList<String> ships = new ArrayList<String>();
        SeaPort port = hmPorts.get(i);
        for(Ship ms: port.ships){
            ships.add(ms.toString());
        }
        return ships;
    }//end getShipsByParent
    
    ArrayList<String> getPersonsByParent(int i){
        ArrayList<String> persons = new ArrayList<String>();
        SeaPort port = hmPorts.get(i);
        for(Person mp: port.persons){
            persons.add(mp.toString());
        }
        return persons;
    }//end getPersonByParent
    
    //The functions below are used for the Sort Button
    ArrayList<String> sortPortsByName(){
        ArrayList<String> portst = new ArrayList<String>();
        Collections.sort(ports, ThingNameComparator);
        for (SeaPort msp : ports){
            portst.add(msp.toStringShort());
            ArrayList<String> ships = sortShipsByName(msp);
            for (String ship : ships)
                portst.add(ship);
            ArrayList<String> docks = sortDocksByName(msp);
            for (String dock : docks)
                portst.add(dock);
            ArrayList<String> persons = sortPersonsByName(msp);
            for (String person : persons)
                portst.add(person);
            ArrayList<String> jobs = sortJobsByName(msp);
            for (String job : jobs)
                portst.add(job);
        }
        return portst;
    }//end sortPortsByName
    
    ArrayList<String> sortShipsByName(SeaPort msp){
        ArrayList<String> ships = new ArrayList<String>();
        Collections.sort(msp.ships, ThingNameComparator);
        for (Ship ms : msp.ships){
            ships.add(ms.toString());
        }
        return ships;
    }//end sortShipsByName
    
    ArrayList<String> sortDocksByName(SeaPort msp){
        ArrayList<String> docks = new ArrayList<String>();
        Collections.sort(msp.docks, ThingNameComparator);
        for (Dock md: msp.docks){
            docks.add(md.toString());
        }
        return docks;
    }//end sortDocksByName
    
    ArrayList<String> sortPersonsByName(SeaPort msp){
        ArrayList<String> persons = new ArrayList<String>();
        Collections.sort(msp.persons, ThingNameComparator);
        for (Person mp: msp.persons){
            persons.add(mp.toString());
        }
        return persons;
    }//end sortPersonsByName
    
    ArrayList<String> sortJobsByName(SeaPort msp){
        ArrayList<String> jobs = new ArrayList<String>();
        Collections.sort(msp.jobs, ThingNameComparator);
        for (Job mj: msp.jobs){
            jobs.add(mj.toString());
        }
        return jobs;
    }//end sortDocksByName
    
    ArrayList<String> sortShipsByWeight(){
        ArrayList<String> ships = new ArrayList<String>();
        for (SeaPort msp : ports){
            Collections.sort(msp.ships, Ship.WeightComparator);
            for (Ship ms: msp.ships){
                ships.add(ms.toString() + " Weight: " + ms.getWeight());
            }
        }
        return ships;
    }//end sortShipsByWeight
    
    ArrayList<String> sortShipsByWidth(){
        ArrayList<String> ships = new ArrayList<String>();
        for (SeaPort msp : ports){
            Collections.sort(msp.ships, Ship.WidthComparator);
            for (Ship ms: msp.ships){
                ships.add(ms.toString() + " Width: " + ms.getWidth());
            }
        }
        return ships;
    }//end sortShipsByWidth
    
    ArrayList<String> sortShipsByLength(){
        ArrayList<String> ships = new ArrayList<String>();
        for (SeaPort msp : ports){
            Collections.sort(msp.ships, Ship.LengthComparator);
            for (Ship ms: msp.ships){
                ships.add(ms.toString() + " Length: " + ms.getLength());
            }
        }
        return ships;
    }//end sortShipsByLength
    
    ArrayList<String> sortShipsByDraft(){
        ArrayList<String> ships = new ArrayList<String>();
        for (SeaPort msp : ports){
            Collections.sort(msp.ships, Ship.DraftComparator);
            for (Ship ms: msp.ships){
                ships.add(ms.toString() + " Draft: " + ms.getDraft());
            }
        }
        return ships;
    }//end sortShipsByDraft
}//end World

class SkillUnavailable extends Exception {
    SkillUnavailable(String errorMessage){
        super(errorMessage);
    }//end constructor
}//end Skill Unavaiable