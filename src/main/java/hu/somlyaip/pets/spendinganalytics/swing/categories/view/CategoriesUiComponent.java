package hu.somlyaip.pets.spendinganalytics.swing.categories.view;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsModel;
import hu.somlyaip.pets.spendinganalytics.swing.IAnalyticsController;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.AllTransactions;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public class CategoriesUiComponent extends JPanel {
    private final IAnalyticsController controller;

    private final CategoriesListComponent categoriesListComponent;

    public CategoriesUiComponent(AnalyticsModel model, IAnalyticsController controller) {
        super(new GridLayout(2, 1));

        this.controller = controller;

        this.categoriesListComponent = new CategoriesListComponent(model::updateSelectedCategory);
        add(this.categoriesListComponent);
        add(createCommandButtonsPanel());
    }

    private JPanel createCommandButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        JButton buttonAdd = new JButton("+");
        buttonAdd.addActionListener(e -> controller.askToAndAddNewCategory());
        JButton buttonRemove = new JButton("-");
        buttonRemove.addActionListener(e -> controller.removeSelectedCategory());
        panel.add(buttonAdd);
        panel.add(buttonRemove);
        return panel;
    }

    public void updateCategories(List<ISelectableCategory> categories) {
        categoriesListComponent.updateCategories(categories);
    }

    public void selectAllCategories() {
        categoriesListComponent.getToggleButtonForCategory(AllTransactions.getInstance()).select();
    }
}
