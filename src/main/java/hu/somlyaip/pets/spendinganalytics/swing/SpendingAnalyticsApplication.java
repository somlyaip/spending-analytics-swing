package hu.somlyaip.pets.spendinganalytics.swing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.awt.*;

@SpringBootApplication
public class SpendingAnalyticsApplication implements CommandLineRunner {

    private final AnalyticsController analyticsController;

    public SpendingAnalyticsApplication(AnalyticsController analyticsController) {
        this.analyticsController = analyticsController;
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpendingAnalyticsApplication.class)
                .headless(false)
                .run(args);
    }

    @Override
    public void run(String... args) {
        EventQueue.invokeLater(analyticsController::createAndInitializeView);
    }
}
