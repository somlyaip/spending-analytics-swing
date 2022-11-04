package hu.somlyaip.pets.spendinganalytics.swing.categories.observer;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;

import java.util.List;

public interface ICategoriesUpdatedObserver {
    void onCategoriesModified(List<ISelectableCategory> categories);
}
