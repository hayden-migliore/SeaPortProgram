//Author: Hayden Migliore
//Program: SeaPortProgram
//Date: 5/10/19
//Purpose: Main class, create java.swing 

package seaportprogram;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class SeaPortProgram extends JFrame{
    static final long serialVersionUID = 123L;
    JTextArea jta = new JTextArea();
    JComboBox <String> jcb;
    JComboBox <String> jcbs;
    JTextField jtf;
    PortTime portTime = new PortTime(1000);
    World world = new World(1, "Earth", 0, portTime);
    public JTree tree;
    public DefaultMutableTreeNode worldTree;
    public DefaultMutableTreeNode seaPortNode;
    public DefaultMutableTreeNode dockNode;
    public DefaultMutableTreeNode shipNode;
    public DefaultMutableTreeNode personNode;
    public DefaultMutableTreeNode jobNode;
    JPanel jobPanel;
    JProgressBar jpb;
    JOptionPane failPane = new JOptionPane("No worker with required skill avaialable.");

    
    public SeaPortProgram() {
        //Create base JFrame
        System.out.println("In Constructor");
        setTitle("Sea Port Program");
        setSize(650, 500);
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        
        //Choose File
        JFileChooser jfc = new JFileChooser("C:\\Users\\Hayden\\Documents\\NetBeansProjects\\CMSC335Project4");
        jfc.setDialogTitle("Please choose the data file.");
        
        //Create worldtree
        worldTree = new DefaultMutableTreeNode("World");
        seaPortNode = new DefaultMutableTreeNode("SeaPorts");
        dockNode = new DefaultMutableTreeNode("Docks");
        shipNode = new DefaultMutableTreeNode("Ships");
        personNode = new DefaultMutableTreeNode("Persons");
        jobNode = new DefaultMutableTreeNode("Jobs");
        worldTree.add(seaPortNode);
        worldTree.add(dockNode);
        worldTree.add(shipNode);
        worldTree.add(personNode);
        worldTree.add(jobNode);
        tree = new JTree(worldTree);
        
        //Create mainPanel
        JPanel mainPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane jsp = new JScrollPane(tree);
        mainPanel.add(jsp);
        JScrollPane jsp2 = new JScrollPane(jta);
        mainPanel.add(jsp2);
        add(mainPanel, BorderLayout.CENTER);
        
        //Create buttons and textfield
        JButton jbr = new JButton("Read");
        JButton jbs = new JButton("Search");
        JButton jbo = new JButton("Sort");
        JLabel jls = new JLabel("Search Target");
        jtf = new JTextField(10);
        
        //Create Search combo box
        jcb = new JComboBox <String> ();
        jcb.addItem("Index");
        jcb.addItem("Name");
        jcb.addItem("Skill");
        jcb.addItem("Parent");
        
        //Create Sort combo box
        jcbs = new JComboBox <String> ();
        jcbs.addItem("Weight");
        jcbs.addItem("Length");
        jcbs.addItem("Draft");
        jcbs.addItem("Width");
        jcbs.addItem("Name");
        
        //Add buttons, textfield, and combo boxes to Jpanel
        JPanel jp = new JPanel();
        jp.add(jbr);
        jp.add(jls);
        jp.add(jtf);
        jp.add(jcb);
        jp.add(jbs);
        jp.add(jbo);
        jp.add(jcbs);
        add(jp, BorderLayout.PAGE_START);
        
        //Create jobPanel
        jobPanel = new JPanel();
        JLabel jbl2 = new JLabel("Current Jobs:");
        JButton jbj = new JButton("Cancel");
        JButton jbj2 = new JButton("Suspend");
        JButton jbj3 = new JButton("Resume");
        jpb = new JProgressBar();
        jpb.setStringPainted(true);
        jpb.setValue(0);
        jpb.setMinimum(0);
        jpb.setMaximum(10);
        jobPanel.add(jbl2);
        jobPanel.add(jpb);
        jobPanel.add(jbj);
        jobPanel.add(jbj2);
        add(jobPanel, BorderLayout.SOUTH);
        validate();
        
        jbr.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int returnVal = jfc.showOpenDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION){
                    File selectedFile = jfc.getSelectedFile();
                    readFile(selectedFile);
                }
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
        
        jbj.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                jta.append("Canceling job.");
                world.cancelJob();
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
        
        jbj2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                jta.append("Suspending job.");
                world.suspendJob();
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
        
        jbj3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                jta.append("Resuming job.");
                world.resumeJob();
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
        
        jbs.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                search((String)(jcb.getSelectedItem()),jtf.getText());
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
        
        jbo.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                sort((String)(jcbs.getSelectedItem()));
            }//end required method   
        }//end local definition of inner class
        );//the anonymous inner class
    }// end no-parameter constructor
    
    public void readFile(File file){
        
        jta.setText("");
        jta.append("Reading specified file. Entries added.\n" );
        SwingWorker sw1 = new SwingWorker(){
            @Override
            protected String doInBackground()  
            {
                int progress = 0;
                try {
                    Scanner sc = new Scanner(file);
                    while (sc.hasNextLine()) {
                        String st = sc.nextLine();
                        if (st.startsWith("//"))
                            continue;
                        jta.append(st);
                        jta.append("\n");
                        System.out.println(st);
                        progress = world.Process(st);
                        jpb.setValue(progress);
                        if(st.contains("port"))
                            addSeaPortNode(st);
                        if(st.contains("dock"))
                            addDockNode(st);
                        if(st.contains("pship"))
                            addShipNode(st);
                        if(st.contains("cship"))
                            addShipNode(st);
                        if(st.contains("person"))
                            addPersonNode(st);
                        if(st.contains("job")){
                            addJobNode(st);
                            jta.append("\n");
                            ArrayList<String> workers = world.workerCheck();
                            jta.append("Workers currently working: ");
                            for(String worker : workers)
                                jta.append(worker + ", ");
                            jta.append("\n");
                            ArrayList<String> nonWorkers = world.nonWorkerCheck();
                            jta.append("Workers currently available: ");
                            for(String nonWorker : nonWorkers)
                                jta.append(nonWorker + ", ");
                            jta.append("\n");
                            ArrayList<String> jobs = world.jobCheck();
                            jta.append("Jobs that still need to be completed: ");
                            for(String job : jobs)
                                jta.append(job + ", ");
                            jta.append("\n");
                        }
                
                    }
                    sc.close();
                }//end try 
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }//end catch
                catch (SkillUnavailable e){
                    failPane.showMessageDialog(null, "No worker with required skill avaialable.");
                    return "Error has occurred.";
                }
                ArrayList<String> workers = world.workerCheck();
                jta.append("Workers currently working:");
                for(String worker : workers)
                    jta.append(worker);
                ArrayList<String> nonWorkers = world.nonWorkerCheck();
                jta.append("Workers currently available:");
                for(String nonWorker : nonWorkers)
                    jta.append(nonWorker);
                String res = "Finished Execution"; 
                return res; 
            }
        };
        sw1.execute();
    }//end method readFile
    
    public void search(String type, String target){
        jta.setText("");
        jta.append(String.format("Searching for type: >%s<, target: >%s<\n", type, target));
        if (type.equals("Index")){
           int targetI = Integer.parseInt(target);
           ArrayList<String> ports = new ArrayList<String>();
           ports = world.getPortsByIndex(targetI);
           for (String msp: ports)
               jta.append(msp);
           ArrayList<String> docks = new ArrayList<String>();
           docks = world.getDocksByIndex(targetI);
           for (String md: docks)
               jta.append(md);
           ArrayList<String> ships = new ArrayList<String>();
           ships = world.getShipsByIndex(targetI);
           for (String ms: ships)
               jta.append(ms);
           ArrayList<String> persons = new ArrayList<String>();
           persons = world.getPersonsByIndex(targetI);
           for (String mp: persons)
               jta.append(mp);
           ArrayList<String> jobs = new ArrayList<String>();
           jobs = world.getJobsByIndex(targetI);
           for (String mj: jobs)
               jta.append(mj);
        }//End if index
        
        if(type.equals("Name")){
           ArrayList<String> ports = new ArrayList<String>();
           ports = world.getPortsByName(target);
           for (String msp: ports)
               jta.append(msp);
           ArrayList<String> docks = new ArrayList<String>();
           docks = world.getDocksByName(target);
           for (String md: docks)
               jta.append(md);
           ArrayList<String> ships = new ArrayList<String>();
           ships = world.getShipsByName(target);
           for (String ms: ships)
               jta.append(ms);
           ArrayList<String> persons = new ArrayList<String>();
           persons = world.getPersonsByName(target);
           for (String mp: persons)
               jta.append(mp);
           ArrayList<String> jobs = new ArrayList<String>();
           jobs = world.getJobsByName(target);
           for (String mj: jobs)
               jta.append(mj);
        }//end if Name
        
        if(type.equals("Skill")){
            ArrayList<String> persons = new ArrayList<String>();
            persons = world.getPersonsBySkill(target);
            for (String mp: persons)
                jta.append(mp);
        }//end if Skill
        
        if(type.equals("Parent")){
            int targetI = Integer.parseInt(target);
           ArrayList<String> ports = new ArrayList<String>();
           ports = world.getPortsByParent(targetI);
           for (String msp: ports)
               jta.append(msp);
           ArrayList<String> docks = new ArrayList<String>();
           docks = world.getDocksByParent(targetI);
           for (String md: docks)
               jta.append(md);
           ArrayList<String> ships = new ArrayList<String>();
           ships = world.getShipsByParent(targetI);
           for (String ms: ships)
               jta.append(ms);
           ArrayList<String> persons = new ArrayList<String>();
           persons = world.getPersonsByParent(targetI);
           for (String mp: persons)
               jta.append(mp);
        }//end if Parent
    }//end method search
    
    public void sort(String type){
        jta.setText("");
        jta.append(String.format("Sorting by type: >%s<\n", type));
        if (type.equals("Draft")){
            ArrayList<String> ships = new ArrayList<String>();
            ships = world.sortShipsByDraft();
            for (String ms: ships)
                jta.append(ms);
        }//end if Draft
        
        if (type.equals("Length")){
            ArrayList<String> ships = new ArrayList<String>();
            ships = world.sortShipsByLength();
            for (String ms: ships)
                jta.append(ms);
        }//end if Length
        
        if (type.equals("Width")){
            ArrayList<String> ships = new ArrayList<String>();
            ships = world.sortShipsByWidth();
            for (String ms: ships)
                jta.append(ms);
        }//end if Width
        
        if (type.equals("Weight")){
            ArrayList<String> ships = new ArrayList<String>();
            ships = world.sortShipsByWeight();
            for (String ms: ships)
                jta.append(ms);
        }//end if Weight
        
        if (type.equals("Name")){
            ArrayList<String> ports = new ArrayList<String>();
            ports = world.sortPortsByName();
            for (String msp: ports)
                jta.append(msp);
        }//end if Name      
    }//end Sort
    
    //Used to populate JTree
    public void addSeaPortNode(String name){
        seaPortNode.add(new DefaultMutableTreeNode(name));
    }
    public void addDockNode(String name){
        dockNode.add(new DefaultMutableTreeNode(name)); 
    }
    public void addShipNode(String name){
        shipNode.add(new DefaultMutableTreeNode(name));  
    }
    public void addPersonNode(String name){
        personNode.add(new DefaultMutableTreeNode(name));  
    }
    public void addJobNode(String name){
        jobNode.add(new DefaultMutableTreeNode(name));  
    }
    //End used to populate JTree
    
    public static void main (String [] args) {
        SeaPortProgram sp = new SeaPortProgram();
    } // end main    
}//end class SeaPortProgram

