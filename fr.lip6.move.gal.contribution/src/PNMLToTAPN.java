 

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fr.lip6.move.gal.nupn.NotAPTException;
import fr.lip6.move.gal.nupn.PTNetReader;
import fr.lip6.move.pnml.ptnet.Arc;
import fr.lip6.move.pnml.ptnet.Page;
import fr.lip6.move.pnml.ptnet.PetriNet;
import fr.lip6.move.pnml.ptnet.Place;
import fr.lip6.move.pnml.ptnet.PnObject;
import fr.lip6.move.pnml.ptnet.Transition;

public class PNMLToTAPN {
    
	public PetriNet net;
	public ArrayList<Arc> input_arcs;
	public ArrayList<Arc> output_arcs;
	public PrintWriter pw;
	public List<String> orders;
	
	
	public PNMLToTAPN(String path,String newpath, List<String> orders){
	    this.orders=orders;
		File file = new File(path);
		File file_tmp = new File(newpath);
		try {
            this.pw = new PrintWriter(file_tmp);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		PTNetReader ptreader = new PTNetReader();
		PetriNet net = null;
		try {
            this.net = ptreader.loadFromXML(new BufferedInputStream(new FileInputStream(file.getPath())));
        } catch (NotAPTException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
	}
	
	
	public void toTAPN(){
		if(null==net){
			System.out.println("Pas de net en présence.\n");
			return;
		}
		pw.println("<pnml>\n");
		pw.println("<net id=\"" + net.getName() + "\" type=\"P/T net\">\n"); 
				
		for (Page p : net.getPages()) {
			handlePage(p,pw);
		}
		for (Arc a : input_arcs){
			pw.print("<inputArc ");
			pw.print("inscription=\""+a.getInscription().toString()+"\" ");
			pw.print("source=\"" + a.getSource().toString() + "\" ");
			pw.println("target=\"" + a.getTarget().toString() + "\" />");
		}
		for (Arc a : output_arcs){
			pw.print("<outputArc ");
			pw.print("inscription=\""+a.getInscription().toString()+"\" ");
			pw.print("source=\"" + a.getSource().toString() + "\" ");
			pw.println("target=\"" + a.getTarget().toString() + "\" />");
		}
		pw.println("</net>\n");
		pw.println("</pnml>");
	}
	
	public void handlePage(Page page, PrintWriter pw){
	    boolean places=false;
	    ArrayList<Place> places_array = new ArrayList<>();
	    ArrayList<Transition> transitions = new ArrayList<>();
		for (PnObject n : page.getObjects()) {
			if (n instanceof Place) {
				Place p = (Place) n;
				places_array.add(p);
				pw.print("<place ");
	            pw.print("id=\"" + p.getId() + "\" ");
	            pw.print("name=\"" + p.getId() + "\" ");
	            pw.print("invariant=\"&lt; inf\" ");
	            pw.print("initialMarking=\""+p.getInitialMarking().getText()+"\"");
	            pw.println("/>");
			}
			if (n instanceof Transition) {
				Transition t = (Transition) n;
				transitions.add(t);
				
				for(Arc a : ((Transition) n).getInArcs()){
					input_arcs.add(a);
				}
				for(Arc a : ((Transition) n).getOutArcs()){
					output_arcs.add(a);
				}
				
			}
		}
//		if(!places.isEmpty()) {
//		    String orders="";
//		    handleOrder(places,orders);
//		}
		if(!transitions.isEmpty()) {
		    for(Transition t: transitions) {
		        pw.print("<transition ");
                pw.print("id=\"" + t.getId() + "\" ");
                pw.print("name=\"" + t.getId() + "\" ");
                pw.println("urgent=\"false\" />");
		    }
		}
	}
	
	public void handleOrder(ArrayList<Place> places) {
	    while(!orders.isEmpty()) {
    	    for(Place place : places) {
    	        if(orders.get(0).equals(place.getName())) {
        	        pw.print("<place ");
        	        pw.print("id=\"" + place.getId() + "\" ");
        	        pw.print("name=\"" + place.getId() + "\" ");
        	        pw.print("invariant=\"&lt; inf\" ");
        	        pw.print("initialMarking=\""+place.getInitialMarking().getText()+"\"");
        	        pw.println("/>");
        	        orders.remove(0);
    	        }
    	    }
	    }
	}
	
	
	
}
