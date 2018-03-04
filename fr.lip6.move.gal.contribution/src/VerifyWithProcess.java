import java.io.BufferedReader;
import java.io.IOException;

import dk.aau.cs.util.MemoryMonitor;
import dk.aau.cs.verification.ProcessRunner;

public class VerifyWithProcess {
    
    public static void main(String[] args) {
//        String path_pnml = args[0];
//        String file_model = path_pnml.replace(".pnml",".xml");
//        PNMLToTAPN exporter = new PNMLToTAPN(path_pnml,file_model);
//        exporter.toTAPN();
        
        String file_model = "/home/justin/Documents/verifyta6502765114347396497.xml";
        String file_query = "/home/justin/Documents/verify_query_deadlock.xml";
        
        String arguments = "-k 0 -s BestFS -r 0 -q 0 -ctl czero -x 1 "+file_model+" "+file_query;
        String verifypath  = "/home/justin/tapaal-3.4.0-linux64/bin/verifypn64";
        
        ProcessRunner runner = new ProcessRunner(verifypath,arguments);
        
        MemoryMonitor.cumulateMemory();
        runner.run();
        
        if (runner.error()) {
            System.err.println("An error occured");
            return;
        } else {
            String errorOutput = readOutput(runner.errorOutput());
            String standardOutput = readOutput(runner.standardOutput());
            System.out.println(MemoryMonitor.getPeakMemory());
            System.out.println("the verification ran for : "+runner.getRunningTime()+"ms");
            System.out.println(standardOutput);
        }
    }
    
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
