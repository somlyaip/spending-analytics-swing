package hu.somlyaip.pets.spendinganalytics.swing.view;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsController;
import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsModel;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.AllTransactions;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Uncategorized;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ICategoriesUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ISelectedCategoryUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.view.CategoriesUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.categories.view.SelectCategoryModal;
import hu.somlyaip.pets.spendinganalytics.swing.chart.ChartUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.datafile.DataFileChooser;
import hu.somlyaip.pets.spendinganalytics.swing.datafile.DataFileUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.observer.ISelectedTransactionsChangedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.observer.ITransactionsLoadedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.view.TransactionsUiComponent;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
public class AnalyticsView implements
        ITransactionsLoadedObserver, ICategoriesUpdatedObserver, ISelectedCategoryUpdatedObserver,
        ISelectedTransactionsChangedObserver {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AnalyticsModel model;
    private final AnalyticsController controller;

    private JFrame viewFrame;
    private Container rootPane;

    private DataFileUiComponent dataFileUiComponent;
    private CategoriesUiComponent categoriesUiComponent;
    private TransactionsUiComponent transactionsUiComponent;
    private CategoriesAndTransactionsUiComponent categoriesAndTransactionsUiComponent;
    private ChartUiComponent chartUiComponent;

    private DataFileChooser dataFileChooser;

    public AnalyticsView(AnalyticsModel model, AnalyticsController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void createUiElements(HufFormatter hufFormatter, DateFormatter dateFormatter) {
        createFrame();
        createRootPane();

        this.dataFileUiComponent = new DataFileUiComponent(controller);
        rootPane.add(this.dataFileUiComponent, BorderLayout.PAGE_START);

        this.categoriesUiComponent = new CategoriesUiComponent(model, controller);
        this.transactionsUiComponent = new TransactionsUiComponent(
                hufFormatter, dateFormatter, this
        );
        this.categoriesAndTransactionsUiComponent = new CategoriesAndTransactionsUiComponent(
                this.categoriesUiComponent, this.transactionsUiComponent,
                e -> controller.askCategoryToSelectAndAddSelectedTransactionToIt(),
                e -> controller.removeSelectedTransactionFromSelectedCategory()
        );
        rootPane.add(categoriesAndTransactionsUiComponent, BorderLayout.LINE_START);

        chartUiComponent = new ChartUiComponent(Collections.emptyList());
        rootPane.add(chartUiComponent, BorderLayout.CENTER);

        JFrame.setDefaultLookAndFeelDecorated(true);

        dataFileChooser = new DataFileChooser();
    }

    private void createFrame() {
        viewFrame = new JFrame("View");
        viewFrame.setTitle("Spending Analytics");
        viewFrame.setSize(800, 500);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setVisible(true);
    }

    private void createRootPane() {
        rootPane = viewFrame.getContentPane();
        rootPane.setLayout(new BorderLayout(5, 5));
    }

    public Optional<File> browseTransactionDataFile() {
        return dataFileChooser.browseDataFileOnCenterOf(viewFrame);
    }

    public Optional<String> askToNewCategoryName() {
        String categoryName = JOptionPane.showInputDialog(this.viewFrame, "New category name");
        if (StringUtils.hasText(categoryName)) {
            return Optional.of(categoryName);
        }

        return Optional.empty();
    }

    public void enableBrowseButton() {
        dataFileUiComponent.setEnableButtonBrowseTransactionFile(true);
    }

    public void disableBrowseButton() {
        dataFileUiComponent.setEnableButtonBrowseTransactionFile(false);
    }

    @Override
    public void onLoadingTransactionsCompleted(File transactionDataFile, List<MoneyTransaction> transactions) {
        dataFileUiComponent.setLabelLoadedTransactionFilename(transactionDataFile);
        transactionsUiComponent.updateTransactions(transactions);
    }

    @Override
    public void onCategoriesModified(List<ISelectableCategory> categories) {
        categoriesUiComponent.updateCategories(categories);
    }

    @Override
    public void onSelectedCategoryUpdated(ISelectableCategory selectedCategory) {
        transactionsUiComponent.updateTransactions(
                model.getTransactionsOf(selectedCategory).orElse(Collections.emptyList())
        );
        model.updateSelectedTransactions(Collections.emptyList());

        if (selectedCategory == null) {
            this.categoriesAndTransactionsUiComponent.setCategoryName(null);

            this.categoriesAndTransactionsUiComponent.disableAddToCategory();
            this.categoriesAndTransactionsUiComponent.disableRemoveFromCategory();

        } else {
            this.categoriesAndTransactionsUiComponent.setCategoryName(selectedCategory.getName());

            if (Uncategorized.getInstance().equals(selectedCategory)) {
                this.categoriesAndTransactionsUiComponent.enableAddToCategory();
                this.categoriesAndTransactionsUiComponent.disableRemoveFromCategory();

            } else if (AllTransactions.getInstance().equals(selectedCategory)) {
                this.categoriesAndTransactionsUiComponent.disableAddToCategory();
                this.categoriesAndTransactionsUiComponent.disableRemoveFromCategory();

            } else {
                this.categoriesAndTransactionsUiComponent.disableAddToCategory();
                this.categoriesAndTransactionsUiComponent.enableRemoveFromCategory();
            }
        }
    }

    public void notifyUserSelectACategoryToRemove() {
        JOptionPane.showMessageDialog(
                this.viewFrame, "Select a category to remove before clicking the button",
                "Unselected category", JOptionPane.WARNING_MESSAGE
        );
    }

    public void notifyUserCannotRemoveLogicalCategory() {
        JOptionPane.showMessageDialog(
                this.viewFrame, "Cannot remove 'ALL' or 'Uncategorized' logical category.",
                "Failed to remove category", JOptionPane.WARNING_MESSAGE
        );
    }

    public void notifyUserShouldSelectAnUncategorizedTransactionToAddItToAnyCategory() {
        JOptionPane.showMessageDialog(
                this.viewFrame, "Select an uncategorized transaction to add it to a category.",
                "Failed to add transaction", JOptionPane.WARNING_MESSAGE
        );
    }

    public void notifyUserShouldSelectATransactionToRemove() {
        JOptionPane.showMessageDialog(
                this.viewFrame, "Select a transaction to remove it from this category.",
                "Failed to remove transaction", JOptionPane.WARNING_MESSAGE
        );
    }

    public void selectAllCategories() {
        categoriesUiComponent.selectAllCategories();
    }

    public Optional<Category> askCategoryToSelect() {
        var modal = new SelectCategoryModal(
                viewFrame,
                new ArrayList<>(model.getCategories())
        );
        // There are only Category instances in this case
        return modal.getSelectedCategory().map(selectableCategory -> (Category) selectableCategory);
    }

    @Override
    public void onSelectedTransactionsChanged(List<MoneyTransaction> selectedTransactions) {
        model.updateSelectedTransactions(selectedTransactions);
    }

    public void updateTransactionsTable(List<MoneyTransaction> actualTransactions) {
        transactionsUiComponent.updateTransactions(actualTransactions);
    }

    public void updateChart() {
        chartUiComponent.updateChart(model.getCategoriesPieChartSeriesList());
    }
}
