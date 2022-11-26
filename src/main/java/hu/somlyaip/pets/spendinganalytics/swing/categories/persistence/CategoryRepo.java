package hu.somlyaip.pets.spendinganalytics.swing.categories.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public record CategoryRepo(ObjectMapper objectMapper) implements ICategoryRepo {

    private final static File DATAFILE = new File("categories.json");

    @Override
    public List<Category> load() {
        if (! DATAFILE.exists()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(DATAFILE, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(List<Category> categories) {
        try {
            objectMapper.writeValue(DATAFILE, categories);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
