package hu.somlyaip.pets.spendinganalytics.swing;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
@Component
public class DateFormatter {

    public String format(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }
}
