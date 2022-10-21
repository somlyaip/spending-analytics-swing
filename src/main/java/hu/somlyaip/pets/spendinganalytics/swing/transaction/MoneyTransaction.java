package hu.somlyaip.pets.spendinganalytics.swing.transaction;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Builder
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class MoneyTransaction {
    private String seller;
    private LocalDate date;
    private BigDecimal amount;
}
