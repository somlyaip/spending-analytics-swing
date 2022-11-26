package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.AllTransactions;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Uncategorized;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ICategoriesUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ISelectedCategoryUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.persistence.ICategoryRepo;
import hu.somlyaip.pets.spendinganalytics.swing.chart.PieChartSeries;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.observer.ITransactionsLoadedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.persistence.ITransactionLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
public class AnalyticsModel {
    private final ITransactionLoader transactionLoader;
    private final ICategoryRepo categoryRepo;

    private List<MoneyTransaction> transactions;
    private final Map<ISelectableCategory, List<MoneyTransaction>> mapCategoryToTransactions;
    private ISelectableCategory selectedCategory;

    private final List<ITransactionsLoadedObserver> transactionsLoadedObservers;
    private final List<ICategoriesUpdatedObserver> categoriesUpdatedObservers;
    private final List<ISelectedCategoryUpdatedObserver> selectedCategoryUpdatedObservers;
    private List<MoneyTransaction> selectedTransactions;

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
        notifyTransactionLoadedObservers(transactionDataFile);

        rebuildMapCategoryToTransactions();
        notifyCategoriesUpdatedObservers();
    }

    private void rebuildMapCategoryToTransactions() {
        List<Category> categories = getCategories();
        mapCategoryToTransactions.clear();
        mapCategoryToTransactions.put(AllTransactions.getInstance(), new ArrayList<>());
        mapCategoryToTransactions.put(Uncategorized.getInstance(), new ArrayList<>());
        for (MoneyTransaction transaction : transactions) {
            Optional<Category> matchedCategoryOptional = categories.stream().
                    filter(c -> c.contains(transaction)).findFirst();
            if (matchedCategoryOptional.isPresent()) {
                Category matchedCategory = matchedCategoryOptional.get();
                if (! mapCategoryToTransactions.containsKey(matchedCategory)) {
                    mapCategoryToTransactions.put(matchedCategory, new ArrayList<>());
                }
                mapCategoryToTransactions.get(matchedCategory).add(transaction);
            } else {
                mapCategoryToTransactions.get(Uncategorized.getInstance()).add(transaction);
            }
            mapCategoryToTransactions.get(AllTransactions.getInstance()).add(transaction);
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

    public List<Category> getCategories() {
        return mapCategoryToTransactions.keySet().stream()
                .filter(c -> c instanceof Category)
                .map(c -> (Category) c)
                .collect(Collectors.toList());
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

    public void updateSelectedCategory(ISelectableCategory selectedCategory) {
        this.selectedCategory = selectedCategory;
        notifySelectedCategoryUpdatedObservers();
    }

    public Optional<List<MoneyTransaction>> getTransactionsOf(ISelectableCategory category) {
        List<MoneyTransaction> moneyTransactions = mapCategoryToTransactions.get(category);
        return Optional.ofNullable(moneyTransactions);
    }

    public void addNewCategory(Category newCategory) {
        mapCategoryToTransactions.put(newCategory, new ArrayList<>());
        saveCategories();
        notifyCategoriesUpdatedObservers();
    }

    private void saveCategories() {
        categoryRepo.save(getCategories());
    }

    public boolean hasSelectedCategory() {
        return selectedCategory != null;
    }

    public void removeSelectedCategory() throws CannotRemoveLogicalCategoryException {
        if (! (selectedCategory instanceof Category)) {
            throw new CannotRemoveLogicalCategoryException(
                    "Cannot remove an instance of '%s'".formatted(selectedCategory.getClass().toGenericString())
            );
        }

        mapCategoryToTransactions.remove(selectedCategory);
        saveCategories();
        rebuildMapCategoryToTransactions();
        notifyCategoriesUpdatedObservers();
    }

    public boolean hasSelectedTransaction() {
        return ! selectedTransactions.isEmpty();
    }

    public void addSelectedTransactionTo(Category category) {
        mapCategoryToTransactions.get(Uncategorized.getInstance()).removeAll(selectedTransactions);
        mapCategoryToTransactions.get(category).addAll(selectedTransactions);
        category.addSellers(getSelectedTransactionsSellers());
        saveCategories();
    }

    private List<String> getSelectedTransactionsSellers() {
        return selectedTransactions.stream().map(MoneyTransaction::getSeller).collect(Collectors.toList());
    }

    public void removeSelectedTransactionFromSelectedCategory() {
        if (! (selectedCategory instanceof Category)) {
            throw new RuntimeException("Cannot remove from category '%s'".formatted(selectedCategory.getClass()));
        }
        mapCategoryToTransactions.get(selectedCategory).removeAll(selectedTransactions);
        ((Category) selectedCategory).removeSellers(getSelectedTransactionsSellers());
        mapCategoryToTransactions.get(Uncategorized.getInstance()).addAll(selectedTransactions);
        saveCategories();
    }

    public void updateSelectedTransactions(List<MoneyTransaction> selectedTransactions) {
        this.selectedTransactions = selectedTransactions;
    }

    ISelectableCategory getSelectedCategory() {
        return selectedCategory;
    }

    public List<PieChartSeries> getCategoriesPieChartSeriesList() {
        BigDecimal overallAmount = mapCategoryToTransactions.entrySet().stream()
                .filter(e -> ! (e.getKey() instanceof AllTransactions))
                .map(e -> e.getValue().stream()
                        .map(MoneyTransaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (overallAmount.equals(BigDecimal.ZERO)) {
            return Collections.emptyList();
        }

        final var precision = new MathContext(2);
        return mapCategoryToTransactions.entrySet().stream()
                .filter(e -> ! (e.getKey() instanceof AllTransactions))
                .map(e -> {
                    BigDecimal sumAmountOfCategory = e.getValue().stream()
                            .map(MoneyTransaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new PieChartSeries(
                            e.getKey().getName(),
                            sumAmountOfCategory.divide(overallAmount, precision)
                                    .multiply(new BigDecimal(100))
                    );
                })
                .collect(Collectors.toList());
    }
}
