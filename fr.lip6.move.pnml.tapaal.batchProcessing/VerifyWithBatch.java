import java.io.File;
import java.util.ArrayList;

import dk.aau.cs.gui.components.BatchProcessingResultsTableModel;
import dk.aau.cs.translations.ReductionOption;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions.ApproximationMethodOption;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions.QueryPropertyOption;
import dk.aau.cs.verification.batchProcessing.BatchProcessingVerificationOptions.SymmetryOption;
import pipe.dataLayer.TAPNQuery.SearchOption;

public class VerifyWithBatch {
    public static void main(String[] args) {
        BatchProcessingResultsTableModel tableModel = new BatchProcessingResultsTableModel();
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

        ArrayList<File> files = new ArrayList<>();
        files.add(new File("/home/justin/Documents/Angiogenesis/angiogenesis-05.xml"));
        BatchProcessingWorker worker  = new BatchProcessingWorker(files, tableModel, les_options);
        try {
            worker.doInBackground();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
