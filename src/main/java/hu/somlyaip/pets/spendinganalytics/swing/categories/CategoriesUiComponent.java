package hu.somlyaip.pets.spendinganalytics.swing.categories;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsModel;

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

    private final Map<Category, CategoryToggleButton> mapCategoryToToggleButton;

    private CategoryToggleButton lastSelectedToggleButton;

    public CategoriesUiComponent(AnalyticsModel model) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.model = model;
        this.mapCategoryToToggleButton = new HashMap<>();
    }

    public void updateCategories(List<Category> categories) {
        removeAll();
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
            add(categoryToggleButton);
        });
    }
}
