package hu.somlyaip.pets.spendinganalytics.swing.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public class DateFormatterTest {

    @Test
    void testFormat() {
        var underTest = new DateFormatter();
        Assertions.assertEquals(
                "2022.10.21", underTest.format(LocalDate.of(2022, 10, 21))
        );
    }
}
