package fr.lip6.move.pnml.tapaal.batchProcessing;

public class ExportedVerifyTAPNModel {
	private final String queryFile;
	private final String modelFile;

	public ExportedVerifyTAPNModel(String modelFile, String queryFile) {
		this.modelFile = modelFile;
		this.queryFile = queryFile;
	}

	public String modelFile() {
		return modelFile;
	}

	public String queryFile() {
		return queryFile;
	}

}