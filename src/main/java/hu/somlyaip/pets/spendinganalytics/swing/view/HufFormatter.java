package hu.somlyaip.pets.spendinganalytics.swing.view;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
@Component
public class HufFormatter {

    public String format(BigDecimal amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        DecimalFormat format = new DecimalFormat("###########0.#### HUF");
        format.setDecimalFormatSymbols(symbols);
        format.setGroupingSize(3);
        format.setGroupingUsed(true);
        return format.format(amount);
    }
}
