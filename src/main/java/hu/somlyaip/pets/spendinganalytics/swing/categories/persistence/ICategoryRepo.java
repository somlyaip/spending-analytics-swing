package hu.somlyaip.pets.spendinganalytics.swing.categories.persistence;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;

import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public interface ICategoryRepo {
    List<Category> load();
    void save(List<Category> categories);
}
