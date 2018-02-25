import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import dk.aau.cs.TCTL.TCTLPathPlaceHolder;
import dk.aau.cs.model.tapn.TimedArcPetriNet;
import dk.aau.cs.model.tapn.simulation.TimedArcPetriNetTrace;
import dk.aau.cs.translations.ReductionOption;
import dk.aau.cs.util.Tuple;
import dk.aau.cs.verification.NameMapping;
import dk.aau.cs.verification.ProcessRunner;
import dk.aau.cs.verification.QueryResult;
import dk.aau.cs.verification.Stats;
import dk.aau.cs.verification.VerificationOptions;
import dk.aau.cs.verification.VerificationResult;
import dk.aau.cs.verification.VerifyTAPN.VerifyTAPNOptions;
import dk.aau.cs.verification.VerifyTAPN.VerifyTAPNTraceParser;
import fr.lip6.move.pnml.tapaal.batchProcessing.ExportedVerifyTAPNModel;
import pipe.dataLayer.TAPNQuery;
import pipe.dataLayer.TAPNQuery.SearchOption;
import pipe.dataLayer.TAPNQuery.TraceOption;
import pipe.gui.widgets.InclusionPlaces;

public class VerifyPNTest {
	private static boolean ctlOutput = false;

	public static void main(String[] args) {
		System.out.println("okay\n");
		String arguments = "-k 0 -s BestFS -r 0 -q 0 -ctl czero -x 1 "+"/tmp/verifyta6502765114347396497.xml"+" /tmp/verifyta2061918269235983339.xml";
		String verifypath  = "/home/justin/tapaal-3.4.0-linux64/bin/verifypn64";
		ExportedVerifyTAPNModel exportedModel = new ExportedVerifyTAPNModel(verifypath,arguments);
		ProcessRunner runner = new ProcessRunner(verifypath,arguments);
		int bound = 0;
		TAPNQuery query = new TAPNQuery(
				"Query Deadlock",
				bound,
				 new TCTLPathPlaceHolder(),
				 TraceOption.FASTEST,
				 SearchOption.HEURISTIC,
				 ReductionOption.VerifyTAPN,// ou null ?
				false,//reduction symetrique 
				false,//use gcd ?
				false,//use timedart ?
				true,//use PTrie
				false,//use overapprox
				false, // use reduction
				/* hashTableSizeToSet */ null,
				/* extrapolationOptionToSet */null,
				new InclusionPlaces(),//inclusion places ?
				false,//use overapproxselected ?
				false,//use underapproxselected ?
				(Integer) 0//approx value
		);
		
		fr.lip6.move.pnml.tapaal.batchProcessing.TAPNQuery les_querys = new fr.lip6.move.pnml.tapaal.batchProcessing.TAPNQuery(query.getProperty().copy(), query.getCapacity());
		
		VerifyTAPNOptions options = new VerifyTAPNOptions(
				bound,
				query.getTraceOption(),
				query.getSearchOption(),
				query.useSymmetry(),
				query.useOverApproximation(),
				query.discreteInclusion(),
				query.inclusionPlaces(),
				query.isOverApproximationEnabled(),
				query.isUnderApproximationEnabled(),
				query.approximationDenominator()
		);
		
		
		runner.run();
		if (runner.error()) {
			return;
		} else {
			String errorOutput = readOutput(runner.errorOutput());
			String standardOutput = readOutput(runner.standardOutput());

			Tuple<QueryResult, Stats> queryResult = parseQueryResult(standardOutput, model.value1().marking().size() + les_querys.getExtraTokens(), les_querys.getExtraTokens(), query);

			if (queryResult == null || queryResult.value1() == null) {
				new VerificationResult<TimedArcPetriNetTrace>(errorOutput + System.getProperty("line.separator") + standardOutput, runner.getRunningTime());
			} else {
				ctlOutput = queryResult.value1().isCTL;
				boolean approximationResult = queryResult.value2().discoveredStates() == 0;	// Result is from over-approximation
				TimedArcPetriNetTrace tapnTrace = parseTrace(errorOutput, options, model, exportedModel, query, queryResult.value1());
				new VerificationResult<TimedArcPetriNetTrace>(queryResult.value1(), tapnTrace, runner.getRunningTime(), queryResult.value2(), approximationResult);
			}
		}
		System.out.println("okay\n");
	}
	private static String readOutput(BufferedReader reader) {
		try {
			if (!reader.ready())
				return "";
		} catch (IOException e1) {
			return "";
		}
		StringBuffer buffer = new StringBuffer();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
				buffer.append(System.getProperty("line.separator"));
			}
		} catch (IOException e) {
		}

		return buffer.toString();
	}
	
	private TimedArcPetriNetTrace parseTrace(String output, VerificationOptions options, Tuple<TimedArcPetriNet, NameMapping> model, ExportedVerifyTAPNModel exportedModel, TAPNQuery query, QueryResult queryResult) {
		if (((VerifyTAPNOptions) options).trace() == TraceOption.NONE) return null;
		
		VerifyTAPNTraceParser traceParser = new VerifyTAPNTraceParser(model.value1());
		TimedArcPetriNetTrace trace = traceParser.parseTrace(new BufferedReader(new StringReader(output)));

		return trace;
	}
	
}
