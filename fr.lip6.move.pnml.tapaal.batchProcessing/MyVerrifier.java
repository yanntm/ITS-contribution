package fr.lip6.move.pnml.tapaal.batchProcessing;

import pipe.dataLayer.TAPNQuery.SearchOption;
import pipe.gui.FileFinderImpl;
import pipe.gui.MessengerImpl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.approximation.ApproximationWorker;
import dk.aau.cs.gui.components.BatchProcessingResultsTableModel;
import dk.aau.cs.model.tapn.simulation.TimedArcPetriNetTrace;
import dk.aau.cs.translations.ReductionOption;
import dk.aau.cs.verification.VerificationResult;
import dk.aau.cs.verification.VerifyTAPN.VerifyPN;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions;
import dk.aau.cs.verification.batchProcessing.BatchProcessingWorker;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions.ApproximationMethodOption;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions.QueryPropertyOption;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions.SymmetryOption;

public class MyVerrifier {
	public VerifyPN verif;
	public VerificationResult<TimedArcPetriNetTrace> resultat;
	public ApproximationWorker worker;
	public MyVerrifier(){
		this.verif = new VerifyPN(new FileFinderImpl(), new MessengerImpl());
		this.verif.setup();
		this.resultat = null;
		this.worker = new ApproximationWorker();
	}
	//Voir BatchProcessingDialog dans la methode getVerrificationOption pour le batch process
	
	public void verifyDeadLock(File file){
		ArrayList<File> files = new ArrayList<File>();
		files.add(file);
		//this.resultat = ApproximationWorker.batchWorker();
		BatchProcessingResultsTableModel tableModel = new BatchProcessingResultsTableModel();
		
		//setting ioption
		ReductionOption reductionOption = ReductionOption.BatchProcessingKeepQueryOption;
		QueryPropertyOption optionqueryproperty = QueryPropertyOption.ExistDeadlock;
		boolean querycapacityselected = true; //memory usage ?
		int numberofextratoken=3;//valeur par default dans tapaal
		boolean isDiscreteInclusion = false;
		boolean useTimeDart = false;
		boolean useTimeDartPetri = false;
		boolean usePetri = true;
		SymmetryOption symmetryOption = SymmetryOption.No;
		SearchOption searchOption = SearchOption.HEURISTIC;
		ApproximationMethodOption approxOption = ApproximationMethodOption.None; //.UnderApproximation ou .OverApproximation
		int approximationDenominator = 2; //valeur par default dans tapaal
		ReductionOption reductionOptionList = ReductionOption.BatchProcessingKeepQueryOption;
		
		BatchProcessingVerificationOptions les_options = new BatchProcessingVerificationOptions(optionqueryproperty,querycapacityselected,
				numberofextratoken,searchOption,symmetryOption,reductionOptionList,isDiscreteInclusion,useTimeDartPetri,useTimeDart,usePetri,
				approxOption,approximationDenominator);
		
		 new BatchProcessingWorker(files, tableModel,les_options);
		 
		 // voir methode process !!
	}
	
	
}
