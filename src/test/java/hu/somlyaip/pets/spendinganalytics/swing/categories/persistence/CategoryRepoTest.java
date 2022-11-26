package hu.somlyaip.pets.spendinganalytics.swing.categories.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepoTest {

    @Test
    void testLoad_usingCategoriesJsonFromResources_shouldReturnsAllCategories() {
        @SuppressWarnings("ConstantConditions") File dataFile =
                new File(getClass().getClassLoader().getResource("categories.json").getPath());
        var underTest = new CategoryRepo(new ObjectMapper(), dataFile);

        List<Category> categories = underTest.load();

        Assertions.assertEquals(
                List.of(
                    Category.builder()
                            .name("Fun")
                            .sellers(new ArrayList<>(List.of("Sony Play Station")))
                            .build(),
                    Category.builder()
                            .name("Car")
                            .sellers(new ArrayList<>(List.of("OMV Fuel Station")))
                            .build(),
                    Category.builder()
                            .name("Food")
                            .sellers(new ArrayList<>(List.of("Aldi", "Foodpanda")))
                            .build()
                ),
                categories
        );
    }
}
