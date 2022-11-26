package hu.somlyaip.pets.spendinganalytics.swing.transaction.observer;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;

import java.util.List;

@FunctionalInterface
public interface ISelectedTransactionsChangedObserver {
    void onSelectedTransactionsChanged(List<MoneyTransaction> selectedTransactions);
}
