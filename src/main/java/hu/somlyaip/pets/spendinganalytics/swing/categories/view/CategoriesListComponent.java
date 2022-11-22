package hu.somlyaip.pets.spendinganalytics.swing.categories.view;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;
import hu.somlyaip.pets.spendinganalytics.swing.categories.observer.ISelectedCategoryUpdatedObserver;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoriesListComponent extends JPanel {

    private final Map<ISelectableCategory, CategoryToggleButton> mapCategoryToToggleButton;
    private CategoryToggleButton lastSelectedToggleButton;

    private final ISelectedCategoryUpdatedObserver selectedCategoryObserver;

    public CategoriesListComponent(ISelectedCategoryUpdatedObserver selectedCategoryUpdatedObserver) {
        super(new FlowLayout(FlowLayout.LEFT, 1, 1));
        this.mapCategoryToToggleButton = new HashMap<>();

        this.selectedCategoryObserver = selectedCategoryUpdatedObserver;
    }

    public CategoriesListComponent(
            List<ISelectableCategory> categories, ISelectedCategoryUpdatedObserver selectedCategoryUpdatedObserver
    ) {
        this(selectedCategoryUpdatedObserver);
        updateCategories(categories);
    }

    public void updateCategories(List<ISelectableCategory> categories) {
        removeAll();

        categories.forEach(category -> {
            CategoryToggleButton categoryToggleButton = new CategoryToggleButton(
                    category,
                    selectedCategory -> {
                        selectedCategoryObserver.onSelectedCategoryUpdated(selectedCategory);

                        CategoryToggleButton newSelectedToggleButton = mapCategoryToToggleButton.get(selectedCategory);
                        if (lastSelectedToggleButton != null &&
                                lastSelectedToggleButton != newSelectedToggleButton) {
                            lastSelectedToggleButton.setUiUnselected();
                        }

                        lastSelectedToggleButton = newSelectedToggleButton;
                    },
                    unselectedCategory -> {
                        selectedCategoryObserver.onSelectedCategoryUpdated(null);

                        lastSelectedToggleButton = null;
                    });
            mapCategoryToToggleButton.put(category, categoryToggleButton);
            add(categoryToggleButton, BorderLayout.PAGE_START);
        });

        // Refresh pane
        revalidate();
        repaint();
    }

    public CategoryToggleButton getToggleButtonForCategory(ISelectableCategory selectableCategory) {
        return mapCategoryToToggleButton.get(selectableCategory);
    }
}
