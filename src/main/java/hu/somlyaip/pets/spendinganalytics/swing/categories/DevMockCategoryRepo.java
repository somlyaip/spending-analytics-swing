package hu.somlyaip.pets.spendinganalytics.swing.categories;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
@Component
public class DevMockCategoryRepo implements ICategoryRepo {
    @Override
    public List<Category> load() {
        return List.of(
                Category.builder()
                        .name("Food")
                        .sellers(List.of("Foodpanda", "Aldi"))
                        .build(),
                Category.builder()
                        .name("Car")
                        .sellers(List.of("OMV Fuel Station"))
                        .build()
        );
    }

    @Override
    public void save(List<Category> categories) {
        // No ops
    }
}
