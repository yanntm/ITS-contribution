import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;

import dk.aau.cs.util.MemoryMonitor;
import dk.aau.cs.verification.ProcessRunner;

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

        // Creating the file names we need 
        String home_tmp = "/home/tmp";             // tmp for the converted net for tapaal
        String[] tmp = file_origin.split("/");      //
        String real_name = tmp[tmp.length-1];       // we get the name of the file only
        Random rn = new Random();                   //
        int rand_name= rn.nextInt();                // we had a little bit of randomization
        String path_pnml = file_origin;
        String file_model = home_tmp + real_name.replace(".pnml","")+"-"+rand_name+".xml"; //
        File file_tmp = null;
        try {// Creating a temporary file for the verifypn64 program
            /// temporary file is a .xml version of the .pnml chosen, for tapaal engine 

            file_tmp = File.createTempFile(real_name.replace(".pnml",""), ".xml");
            file_model = file_tmp.getAbsolutePath();

            // file_model = "/home/justin/Documents/testeststetes.xml";
            // File test = new File(file_model);
            // test.createNewFile();

            // Exporting the file path_pnml to file_model using our Parser/Writer PNMLToTAPN for tapaal
            PNMLToTAPN exporter = new PNMLToTAPN(path_pnml,file_model,null);
            exporter.toTAPN();


            // defining query file path and options
            String file_query = "/home/justin/Documents/verify_query_deadlock.xml";
            String options = "-k 0 -s BestFS -r 0 -q 0 -ctl czero -x 1";




            //TODO find and define the path to the verifypn64 engine
            // defining engine path and arguments
            String verifypath  = "/home/justin/tapaal-3.4.0-linux64/bin/verifypn64";
            String arguments = options+" "+file_model+" "+file_query;


            // creating the process runner for verifypn64
            ProcessRunner runner = new ProcessRunner(verifypath,arguments);

            System.out.println("Launching runner ...");
            runner.run();

            if (runner.error()) {
                System.err.println("An error occured with the runner");
                return;
            } else {
                String errorOutput = readOutput(runner.errorOutput());
                String standardOutput = readOutput(runner.standardOutput()).replace(",",",\n");
                //                standardOutput=standardOutput.replace(">", ">\n"); //comment to reduce console print length
                System.out.println("Peak Memory : "+MemoryMonitor.getPeakMemory());
                System.out.println("the verification ran for : "+runner.getRunningTime()+"ms");
                System.out.println(standardOutput);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=file_tmp) { // If the program run without error, delete the temporary file before exiting
                file_tmp.delete();
            }
        }
    }


    // TAPAAL team's code
    private static String readOutput(BufferedReader reader) {
        try {
            if (!reader.ready())
                return "";
        } catch (IOException e1) {
            System.err.println("I/O Error\n");
        }
        StringBuffer buffer = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append(System.getProperty("line.separator"));
            }
        } catch (IOException e) {
            System.err.println("I/O Error\n");
        }
        return buffer.toString();
    }
}
