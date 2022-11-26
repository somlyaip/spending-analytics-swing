package hu.somlyaip.pets.spendinganalytics.swing.transaction.observer;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;

import java.io.File;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@FunctionalInterface
public interface ITransactionsLoadedObserver {
    void onLoadingTransactionsCompleted(File transactionDataFile, List<MoneyTransaction> transactions);
}
