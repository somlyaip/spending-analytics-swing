package hu.somlyaip.pets.spendinganalytics.swing;

import java.io.File;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
public interface ITransactionsLoadedObserver {

    void onLoadingTransactionsCompleted(File transactionDataFile, List<Transaction> transactions);
}
