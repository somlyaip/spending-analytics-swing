package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.ITransactionLoader;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
public class AnalyticsModel {
    private final ITransactionLoader transactionLoader;

    private List<MoneyTransaction> transactions;
    private final List<ITransactionsLoadedObserver> transactionsLoadedObservers;

    public AnalyticsModel(ITransactionLoader transactionLoader) {
        this.transactionLoader = transactionLoader;

        transactionsLoadedObservers = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public void registerTransactionLoadedObserver(ITransactionsLoadedObserver observer) {
        transactionsLoadedObservers.add(observer);
    }

    public void notifyTransactionLoadedObservers(File transactionDataFile) {
        transactionsLoadedObservers.forEach(
                observer -> observer.onLoadingTransactionsCompleted(transactionDataFile, transactions)
        );
    }

    public void loadTransactionDataFile(File transactionDataFile) {
        transactions = transactionLoader.loadTransactionsFrom(transactionDataFile);
        notifyTransactionLoadedObservers(transactionDataFile);
    }
}
