package hu.somlyaip.pets.spendinganalytics.swing.categories.dto;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import lombok.*;

import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Category implements ISelectableCategory {
    private String name;
    private List<String> sellers;

    public boolean contains(MoneyTransaction transaction) {
        return sellers.contains(transaction.getSeller().trim());
    }
}
