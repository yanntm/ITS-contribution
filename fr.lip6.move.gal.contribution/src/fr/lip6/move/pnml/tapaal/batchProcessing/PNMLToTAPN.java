 package fr.lip6.move.gal.contribution.orders;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;


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
	
	
	public PNMLToTAPN(String path){
		File file = new File(path);
		this.pw = new PrintWriter(file);
		PTNetReader ptreader = new PTNetReader();
		PetriNet net = null; 
		this.net = ptreader.loadFromXML(new BufferedInputStream(new FileInputStream(file.getPath())));
		
	}
	
	
	public void toTAPN(){
		if(net==null){
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
		for (PnObject n : page.getObjects()) {
			if (n instanceof Place) {
				Place p = (Place) n;
				pw.print("<place ");
				pw.print("id=\"" + p.getId() + "\" ");
				pw.print("name=\"" + p.getId() + "\" ");
				pw.print("invariant=\"&lt; inf\" ");
				pw.print("initialMarking=\""+p.getInitialMarking().getText()+"\"");
				pw.println("/>");
			}
			else if (n instanceof Transition) {
				Transition t = (Transition) n;
				pw.print("<transition ");
				pw.print("id=\"" + t.getId() + "\" ");
				pw.print("name=\"" + t.getId() + "\" ");
				pw.println("urgent=\"false\" />");
				
				for(Arc a : ((Transition) n).getInArcs()){
					input_arcs.add(a);
				}
				for(Arc a : ((Transition) n).getOutArcs()){
					output_arcs.add(a);
				}
				
			}
		}
	}
	
}
