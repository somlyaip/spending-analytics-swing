package hu.somlyaip.pets.spendinganalytics.swing.categories;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public class CategoriesUiComponent extends JPanel {
    private final AnalyticsModel model;

    public CategoriesUiComponent(AnalyticsModel model) {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
        this.model = model;
    }

    public void updateCategories(List<Category> categories) {
        removeAll();
        //noinspection Convert2MethodRef
        categories.forEach(category -> add(
                new CategoryToggleButton(
                        category,
                        selectedCategory -> model.updateSelectedCategory(selectedCategory),
                        unselectedCategory -> model.updateSelectedCategory(null))
                )
        );
    }
}
