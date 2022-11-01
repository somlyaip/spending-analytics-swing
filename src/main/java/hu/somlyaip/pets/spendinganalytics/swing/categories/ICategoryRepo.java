package hu.somlyaip.pets.spendinganalytics.swing.categories;

import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public interface ICategoryRepo {
    List<Category> load();
    void save(List<Category> categories);
}
