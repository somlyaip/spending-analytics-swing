package hu.somlyaip.pets.spendinganalytics.swing.categories.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class CategoriesPersistenceConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ICategoryRepo categoryRepo() {
        return new CategoryRepo(new ObjectMapper(), new File("categories.json"));
    }
}
