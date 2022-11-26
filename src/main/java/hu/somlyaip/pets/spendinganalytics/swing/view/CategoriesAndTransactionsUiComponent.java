package hu.somlyaip.pets.spendinganalytics.swing.view;

import hu.somlyaip.pets.spendinganalytics.swing.categories.view.CategoriesUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.view.TransactionsUiComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public class CategoriesAndTransactionsUiComponent extends JPanel {

    private JLabel categoryNameLabel;
    private JButton addToCategoryButton;
    private JButton removeFromCategoryButton;

    public CategoriesAndTransactionsUiComponent(
            CategoriesUiComponent categoriesUiComponent, TransactionsUiComponent transactionsUiComponent,
            ActionListener onAddToCategoryButtonClicked, ActionListener onRemoveFromCategoryButtonClicked
    ) {
        super(new BorderLayout());

        add(
                createCategoriesAndControlsPanel(
                        categoriesUiComponent, onAddToCategoryButtonClicked, onRemoveFromCategoryButtonClicked
                ),
                BorderLayout.PAGE_START
        );

        transactionsUiComponent.setBorder(new EmptyBorder(0,10,0,0));
        add(transactionsUiComponent);
    }

    private JPanel createCategoriesAndControlsPanel(
            CategoriesUiComponent categoriesUiComponent, ActionListener onAddToCategoryButtonClicked,
            ActionListener onRemoveFromCategoryButtonClicked
    ) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(categoriesUiComponent);
        panel.add(createCategoryRelationshipPanel(onAddToCategoryButtonClicked, onRemoveFromCategoryButtonClicked));
        return panel;
    }

    private Component createCategoryRelationshipPanel(
            ActionListener onAddToCategoryButtonClicked, ActionListener onRemoveFromCategoryButtonClicked
    ) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel categoryNameLabel = createCategoryNameLabel();
        panel.add(categoryNameLabel);
        panel.add(
                createCategoryRelationshipButtonsPanel(onAddToCategoryButtonClicked, onRemoveFromCategoryButtonClicked)
        );
        return panel;
    }

    private JLabel createCategoryNameLabel() {
        categoryNameLabel = new JLabel();
        categoryNameLabel.setBorder(new EmptyBorder(0,10,0,0));
        return categoryNameLabel;
    }

    private Component createCategoryRelationshipButtonsPanel(
            ActionListener onAddToCategoryButtonClicked, ActionListener onRemoveFromCategoryButtonClicked
    ) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addToCategoryButton = new JButton("Add to");
        addToCategoryButton.addActionListener(onAddToCategoryButtonClicked);
        panel.add(addToCategoryButton);
        removeFromCategoryButton = new JButton("Remove from");
        removeFromCategoryButton.addActionListener(onRemoveFromCategoryButtonClicked);
        panel.add(removeFromCategoryButton);
        return panel;
    }

    void setCategoryName(String categoryName) {
        this.categoryNameLabel.setText(categoryName);
    }

    void enableAddToCategory() {
        this.addToCategoryButton.setEnabled(true);
    }

    void disableAddToCategory() {
        this.addToCategoryButton.setEnabled(false);
    }

    void enableRemoveFromCategory() {
        this.removeFromCategoryButton.setEnabled(true);
    }

    void disableRemoveFromCategory() {
        this.removeFromCategoryButton.setEnabled(false);
    }
}
