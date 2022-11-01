package hu.somlyaip.pets.spendinganalytics.swing.categories;

import java.util.List;

public interface ICategoriesUpdatedObserver {
    void onCategoriesModified(List<Category> categories);
}
