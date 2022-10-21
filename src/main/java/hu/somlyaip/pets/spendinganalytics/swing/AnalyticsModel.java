package hu.somlyaip.pets.spendinganalytics.swing;

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

    private final List<Transaction> transactions;

    private final List<ITransactionsLoadedObserver> transactionsLoadedObservers;

    public AnalyticsModel() {
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
        // TODO: first of all load transactions
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        notifyTransactionLoadedObservers(transactionDataFile);
    }
}
