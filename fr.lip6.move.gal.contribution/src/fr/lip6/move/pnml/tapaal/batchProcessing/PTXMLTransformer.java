package fr.lip6.move.gal.contribution.orders;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.sun.org.apache.xpath.internal.operations.Variable;

import fr.lip6.move.pnml.ptnet.PTMarking;
import fr.lip6.move.pnml.ptnet.Page;
import fr.lip6.move.pnml.ptnet.Arc;
import fr.lip6.move.pnml.ptnet.PetriNet;
import fr.lip6.move.pnml.ptnet.Place;
import fr.lip6.move.pnml.ptnet.PnObject;
import fr.lip6.move.pnml.ptnet.Transition;
import org.eclipse.emf.ecore.resource.*;


public class PTXMLTransformer {
	
	public PTXMLTransformer(){
		
	}

	private static Logger getLog() {
		return Logger.getLogger("fr.lip6.move.gal");
	}

	private boolean reversible=false;
	
	public void transform(PetriNet petriNet, String path) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new File(path));			
			pw.println("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>");
			pw.println("<pnml xmlns=\"http://www.informatik.hu-berlin.de/top/pnml/ptNetb\">");
			pw.println("<net active=\"true\" id=\""+petriNet.getName()+"\" type=\"P/T net\">");
			for (Page p : petriNet.getPages()) {
				handlePage(p, pw);
			}
			// TODO : enlever end xml et fermer les balises
			pw.println("<k-bound bound=\"3\"/>");
			pw.println("</pnml>");
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



	private void handlePage(Page page, PrintWriter pw) {
		Map<Place, Variable> placeMap = new HashMap<Place, Variable>();
		
		
		for (PnObject n : page.getObjects()) {
			if (n instanceof Place) {
				Place p = (Place) n;
				
				pw.println("<place displayName=\"true\" id=\""+p.getId()+"\" initialMarking=\""+p.getInitialMarking().getText()+"\" invariant=\"&lt; inf\" "
						+ "markingOffsetX=\"0.0\" markingOffsetY=\"0.0\" name=\""+p.getName()+"\" "
								+ "nameOffsetX=\"0.0\" nameOffsetY=\"-10.0\" positionX=\"100.0\" "
										+ "positionY=\"100.0\"/>");
			}
		}

		getLog().info("Transformed "+ placeMap.size() + " places.");
		for (PnObject pnobj : page.getObjects()) {
			if (pnobj instanceof Transition) {
				Transition t = (Transition) pnobj;
				String tmp = "<transition angle=\"0\" displayName=\"true\" id=\""+t.getId()+"\" infiniteServer=\"false\" "
						+ "name=\""+t.getName()+"\" nameOffsetX=\"0.0\" nameOffsetY=\"-10.0\" positionX=\"100.0\" "
						+ "positionY=\"100.0\" priority=\"0\" urgent=\"false\"/>";
				pw.println(tmp);
			}
				
			
		}
		for (PnObject pnobj : page.getObjects()) {
			if (pnobj instanceof Transition) {
				Transition t = (Transition) pnobj;
				String tmp = "<transition angle=\"0\" displayName=\"true\" id=\""+t.getId()+"\" infiniteServer=\"false\" "
						+ "name=\""+t.getName()+"\" nameOffsetX=\"0.0\" nameOffsetY=\"-10.0\" positionX=\"100.0\" "
						+ "positionY=\"100.0\" priority=\"0\" urgent=\"false\"/>";
				pw.println(tmp);
//				
				
				for (Arc arc : t.getInArcs()) {
					Place pl = (Place) arc.getSource();
					String tmp_str = "<arc id=\""+arc.getId()+"\" inscription=\""+arc.getInscription().toString()+"\" source=\""+arc.getSource().toString()+"\" target=\""+arc.getTarget().toString()+"\" "
							+ "type=\"normal\" weight=\"1\">"+
      	"<arcpath arcPointType=\"false\" id=\"0\" xCoord=\"106\" yCoord=\"117\"/>"+
      "<arcpath arcPointType=\"false\" id=\"1\" xCoord=\"101\" yCoord=\"122\"/></arc>";
					pw.println(tmp_str);
					
				}
				
			}
		}
		

	}

	private int interpretMarking(PTMarking ptMarking) {
		if (ptMarking == null || ptMarking.getText() == null) {
			return 0;
		}
		return Math.toIntExact(ptMarking.getText());
	}

	public void setReversible(boolean reversible) {
		this.reversible = reversible;
	}

}
