package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.categories.Category;

import java.util.List;

public interface ICategoriesUpdatedObserver {
    void onCategoriesModified(List<Category> categories);
}
