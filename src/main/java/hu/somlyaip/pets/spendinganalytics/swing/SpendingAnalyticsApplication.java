package hu.somlyaip.pets.spendinganalytics.swing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication
public class SpendingAnalyticsApplication implements CommandLineRunner {

    private final AnalyticsController controller;
    private final AnalyticsModel model;

    public SpendingAnalyticsApplication(AnalyticsController controller, AnalyticsModel model) {
        this.controller = controller;
        this.model = model;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpendingAnalyticsApplication.class)
                .headless(false)
                .run(args);
    }

    @Override
    public void run(String... args) {
        EventQueue.invokeLater(() -> {
            controller.createAndInitializeView();
            model.loadCategories();
        });
    }
}
