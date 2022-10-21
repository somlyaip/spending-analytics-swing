package hu.somlyaip.pets.spendinganalytics.swing.transaction;

import java.io.File;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public interface ITransactionLoader {
    List<MoneyTransaction> loadTransactionsFrom(File dataFile);
}
