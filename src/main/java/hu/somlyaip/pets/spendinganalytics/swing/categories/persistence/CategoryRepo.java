package hu.somlyaip.pets.spendinganalytics.swing.categories.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public record CategoryRepo(ObjectMapper objectMapper, File dataFile) implements ICategoryRepo {

    @Override
    public List<Category> load() {
        if (! dataFile.exists()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(dataFile, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(List<Category> categories) {
        try {
            objectMapper.writeValue(dataFile, categories);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
