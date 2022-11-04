package hu.somlyaip.pets.spendinganalytics.swing.categories.ui;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsModel;
import hu.somlyaip.pets.spendinganalytics.swing.IAnalyticsController;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public class CategoriesUiComponent extends JPanel {
    private final AnalyticsModel model;
    private final IAnalyticsController controller;

    private final JPanel categoriesPanel;

    private final Map<ISelectableCategory, CategoryToggleButton> mapCategoryToToggleButton;
    private CategoryToggleButton lastSelectedToggleButton;

    public CategoriesUiComponent(AnalyticsModel model, IAnalyticsController controller) {
        super(new GridLayout(2, 1));

        this.model = model;
        this.controller = controller;
        this.mapCategoryToToggleButton = new HashMap<>();

        this.categoriesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        add(createCommandButtonsPanel());
        add(categoriesPanel);
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
        categoriesPanel.removeAll();
        categories.forEach(category -> {
            CategoryToggleButton categoryToggleButton = new CategoryToggleButton(
                    category,
                    selectedCategory -> {
                        // Should I move updating the model to the controller?
                        model.updateSelectedCategory(selectedCategory);

                        CategoryToggleButton newSelectedToggleButton = mapCategoryToToggleButton.get(selectedCategory);
                        if (lastSelectedToggleButton != null &&
                                lastSelectedToggleButton != newSelectedToggleButton) {
                            lastSelectedToggleButton.unselect();
                        }
                        lastSelectedToggleButton = newSelectedToggleButton;
                    },
                    unselectedCategory -> {
                        // Should I move updating the model to the controller?
                        model.updateSelectedCategory(null);

                        lastSelectedToggleButton = null;
                    });
            mapCategoryToToggleButton.put(category, categoryToggleButton);
            categoriesPanel.add(categoryToggleButton, BorderLayout.PAGE_START);
        });
        // Refresh pane
        revalidate();
        repaint();
    }
}
