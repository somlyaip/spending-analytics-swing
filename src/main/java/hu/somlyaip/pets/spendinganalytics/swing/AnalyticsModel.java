package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.categories.Category;
import hu.somlyaip.pets.spendinganalytics.swing.categories.ICategoryRepo;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.ITransactionLoader;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
public class AnalyticsModel {
    private final ITransactionLoader transactionLoader;
    private final ICategoryRepo categoryRepo;

    private List<MoneyTransaction> transactions;
    private Map<Category, List<MoneyTransaction>> mapCategoryToTransactions;

    private final List<ITransactionsLoadedObserver> transactionsLoadedObservers;
    private final List<ICategoriesUpdatedObserver> categoriesUpdatedObservers;

    public AnalyticsModel(ITransactionLoader transactionLoader, ICategoryRepo categoryRepo) {
        this.transactionLoader = transactionLoader;
        this.categoryRepo = categoryRepo;

        transactionsLoadedObservers = new ArrayList<>();
        categoriesUpdatedObservers = new ArrayList<>();

        transactions = new ArrayList<>();
        mapCategoryToTransactions = new HashMap<>();
    }

    public void registerTransactionLoadedObserver(ITransactionsLoadedObserver observer) {
        transactionsLoadedObservers.add(observer);
    }

    private void notifyTransactionLoadedObservers(File transactionDataFile) {
        transactionsLoadedObservers.forEach(
                observer -> observer.onLoadingTransactionsCompleted(transactionDataFile, transactions)
        );
    }

    public void loadTransactionDataFile(File transactionDataFile) {
        transactions = transactionLoader.loadTransactionsFrom(transactionDataFile);
        notifyTransactionLoadedObservers(transactionDataFile);
    }

    public void registerCategoriesUpdatedObserver(ICategoriesUpdatedObserver categoriesUpdatedObserver) {
        categoriesUpdatedObservers.add(categoriesUpdatedObserver);
    }

    private void notifyCategoriesUpdatedObservers() {
        categoriesUpdatedObservers.forEach(
                o -> o.onCategoriesModified(mapCategoryToTransactions.keySet().stream().toList())
        );
    }

    public void loadCategories() {
        categoryRepo.load().forEach(category -> mapCategoryToTransactions.put(category, new ArrayList<>()));
        notifyCategoriesUpdatedObservers();
    }
}
