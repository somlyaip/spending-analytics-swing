package hu.somlyaip.pets.spendinganalytics.swing.categories.observer;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;

public interface ISelectedCategoryUpdatedObserver {
    void onSelectedCategoryUpdated(ISelectableCategory selectedCategory);
}
