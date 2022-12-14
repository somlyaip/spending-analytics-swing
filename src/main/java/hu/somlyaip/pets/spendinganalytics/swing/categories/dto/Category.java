package hu.somlyaip.pets.spendinganalytics.swing.categories.dto;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
/*
 mapCategoryToTransactions map's key can be a Category. A Category's sellers can be changed over time,
 so we use only name field for equality check.
 */
@EqualsAndHashCode(of = "name")
@ToString
public class Category implements ISelectableCategory {

    private String name;
    // We need a mutable list
    private ArrayList<String> sellers;

    public boolean contains(MoneyTransaction transaction) {
        return sellers.contains(transaction.getSeller().trim());
    }

    public void addSellers(Collection<String> newSellers) {
        sellers.addAll(newSellers);
    }
    public void removeSellers(Collection<String> toRemove) {
        sellers.removeAll(toRemove);
    }
}
