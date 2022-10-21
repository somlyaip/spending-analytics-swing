package hu.somlyaip.pets.spendinganalytics.swing.view;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public class HufFormatterTest {

    @Test
    void testFormat_using1456_shouldReturns1_456_HUF() {
        var underTest = new HufFormatter();
        Assertions.assertEquals("1 456 HUF", underTest.format(new BigDecimal(1_456)));
    }

    @Test
    void testFormat_using367_shouldReturns367_HUF() {
        var underTest = new HufFormatter();
        Assertions.assertEquals("367 HUF", underTest.format(new BigDecimal(367)));
    }

    @Test
    void testFormat_using1000000_shouldReturns1_000_000_HUF() {
        var underTest = new HufFormatter();
        Assertions.assertEquals("1 000 000 HUF", underTest.format(new BigDecimal(1_000_000)));
    }
}
