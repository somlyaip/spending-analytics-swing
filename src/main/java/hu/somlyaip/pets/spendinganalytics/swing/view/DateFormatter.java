package hu.somlyaip.pets.spendinganalytics.swing.view;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
@Component
public class DateFormatter {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public String format(LocalDate date) {
        return date.format(DATE_TIME_FORMATTER);
    }
}
