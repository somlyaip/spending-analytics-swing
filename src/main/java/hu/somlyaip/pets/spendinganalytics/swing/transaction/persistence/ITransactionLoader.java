package hu.somlyaip.pets.spendinganalytics.swing.transaction.persistence;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;

import java.io.File;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public interface ITransactionLoader {
    List<MoneyTransaction> loadTransactionsFrom(File dataFile);
}
