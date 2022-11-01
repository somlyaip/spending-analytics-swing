package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.categories.Category;
import hu.somlyaip.pets.spendinganalytics.swing.categories.ICategoriesUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.ICategoryRepo;
import hu.somlyaip.pets.spendinganalytics.swing.categories.ISelectedCategoryUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.ITransactionLoader;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.ITransactionsLoadedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
public class AnalyticsModel {
    private final ITransactionLoader transactionLoader;
    private final ICategoryRepo categoryRepo;

    private List<MoneyTransaction> transactions;
    private final Map<Category, List<MoneyTransaction>> mapCategoryToTransactions;
    private Category selectedCategory;

    private final List<ITransactionsLoadedObserver> transactionsLoadedObservers;
    private final List<ICategoriesUpdatedObserver> categoriesUpdatedObservers;
    private final List<ISelectedCategoryUpdatedObserver> selectedCategoryUpdatedObservers;

    public AnalyticsModel(ITransactionLoader transactionLoader, ICategoryRepo categoryRepo) {
        this.transactionLoader = transactionLoader;
        this.categoryRepo = categoryRepo;

        transactionsLoadedObservers = new ArrayList<>();
        categoriesUpdatedObservers = new ArrayList<>();
        selectedCategoryUpdatedObservers = new ArrayList<>();

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
        rebuildMapCategoryToTransactions();
        notifyTransactionLoadedObservers(transactionDataFile);
    }

    private void rebuildMapCategoryToTransactions() {
        Set<Category> categories = new HashSet<>(mapCategoryToTransactions.keySet());
        mapCategoryToTransactions.clear();
        for (MoneyTransaction transaction : transactions) {
            for (Category category : categories) {
                if (category.contains(transaction)) {
                    if (! mapCategoryToTransactions.containsKey(category)) {
                        mapCategoryToTransactions.put(category, new ArrayList<>());
                    }
                    mapCategoryToTransactions.get(category).add(transaction);
                    break;
                }
            }
        }
    }

    public void registerCategoriesUpdatedObserver(ICategoriesUpdatedObserver categoriesUpdatedObserver) {
        categoriesUpdatedObservers.add(categoriesUpdatedObserver);
    }

    private void notifyCategoriesUpdatedObservers() {
        categoriesUpdatedObservers.forEach(
                o -> o.onCategoriesModified(mapCategoryToTransactions.keySet().stream().toList())
        );
    }

    public void registerSelectedCategoryUpdatedObserver(
            ISelectedCategoryUpdatedObserver selectedCategoryUpdatedObserver
    ) {
        selectedCategoryUpdatedObservers.add(selectedCategoryUpdatedObserver);
    }

    private void notifySelectedCategoryUpdatedObservers() {
        selectedCategoryUpdatedObservers.forEach(o -> o.onSelectedCategoryUpdated(selectedCategory));
    }

    public void loadCategories() {
        categoryRepo.load().forEach(category -> mapCategoryToTransactions.put(category, new ArrayList<>()));
        notifyCategoriesUpdatedObservers();
    }

    public void updateSelectedCategory(Category selectedCategory) {
        this.selectedCategory = selectedCategory;
        notifySelectedCategoryUpdatedObservers();
    }

    public Optional<List<MoneyTransaction>> getTransactionsOf(Category category) {
        List<MoneyTransaction> moneyTransactions = mapCategoryToTransactions.get(category);
        return Optional.ofNullable(moneyTransactions);
    }
}
