package hu.somlyaip.pets.spendinganalytics.swing.view;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsController;
import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsModel;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ICategoriesUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ISelectedCategoryUpdatedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.categories.ui.CategoriesUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.datafile.DataFileUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.ITransactionsLoadedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.TransactionsUiComponent;
import org.springframework.util.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
public class AnalyticsView
        implements ITransactionsLoadedObserver, ICategoriesUpdatedObserver, ISelectedCategoryUpdatedObserver {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AnalyticsModel model;
    private final AnalyticsController controller;

    private JFrame viewFrame;
    private Container rootPane;

    private DataFileUiComponent dataFileUiComponent;
    private CategoriesUiComponent categoriesUiComponent;
    private TransactionsUiComponent transactionsUiComponent;

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
        this.transactionsUiComponent = new TransactionsUiComponent(hufFormatter, dateFormatter);
        rootPane.add(new CategoriesAndTransactionsUiComponent(this.categoriesUiComponent, this.transactionsUiComponent));

        JFrame.setDefaultLookAndFeelDecorated(true);
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
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(viewFrame) == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile());
        }

        return Optional.empty();
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
}
