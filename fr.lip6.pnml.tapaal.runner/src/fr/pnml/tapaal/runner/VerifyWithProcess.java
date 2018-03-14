package fr.pnml.tapaal.runner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

import javax.swing.JFileChooser;

import fr.lip6.move.gal.process.CommandLine;
import fr.lip6.move.gal.process.Runner;

//import dk.aau.cs.util.MemoryMonitor;
//import dk.aau.cs.verification.ProcessRunner;

public class VerifyWithProcess {


    public List<String> orders;

    //TODO add a name to the heuristic
    // Constructor assigning a list of orders, calculated with a specified heuristic 
    public VerifyWithProcess(List<String> orders) {
        this.orders = orders;
    }

    public void verify(){

        // User link the file 
        JFileChooser dialogue = new JFileChooser();
        dialogue.showOpenDialog(null);
        String file_origin=dialogue.getSelectedFile().getAbsolutePath();
        String verifypath  = "/home/justin/tapaal-3.4.0-linux64/bin/verifypn64";
        String file_query = "/home/justin/Documents/verify_query_deadlock.xml";

        doVerify(file_origin, verifypath, file_query);
    }

    public void doVerify(String file_origin, String verifypath, String file_query) {
        // Creating the file names we need 
        String[] tmp = file_origin.split("/");      //
        String real_name = tmp[tmp.length-1];       // we get the name of the file only
        String path_pnml = file_origin;

        File file_tmp = null;
        try {// Creating a temporary file for the verifypn64 program
            /// temporary file is a .xml version of the .pnml chosen, for tapaal engine 

            file_tmp = File.createTempFile(real_name.replace(".pnml",""), ".xml");
            String file_model = file_tmp.getAbsolutePath();


            // Exporting the file path_pnml to file_model using our Parser/Writer PNMLToTAPN for tapaal
            PNMLToTAPN exporter = new PNMLToTAPN(path_pnml,file_model,null);
            exporter.toTAPN();


            // defining query file path and options            
            String options = "-k 0 -s BestFS -r 0 -q 0 -ctl czero -x 1";


            //TODO find and define the path to the verifypn64 engine
            // defining engine path and arguments

            String arguments = options+" "+file_model+" "+file_query;

            String commandLine = verifypath+" "+arguments;
            String[] commands = commandLine.split(" ");

            // creating the process runner for verifypn64
            Runner runner = new Runner();

            // creating command line for the runner
            CommandLine cl = new CommandLine();
            for(String str : commands) {
                cl.addArg(str);
            }
            File tempo_file =new File("/home/justin/test");

            long timeout = 300000;
            boolean errToOut = false;
            System.out.println("Launching runner ...");
            
            runner.runTool(timeout, cl,tempo_file,errToOut);
            
            // displaying the memory consumption of the runtime environment
            System.gc();
            Runtime rt = Runtime.getRuntime();
            long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
            String str_tmp ="Memory usage : " +usedMB+"Mb";
            System.out.println(str_tmp);            


        } catch (IOException | TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(null!=file_tmp) { // If the program run without error, delete the temporary file before exiting
                file_tmp.delete();
            }
        }
    }
}
