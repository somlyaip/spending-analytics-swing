package hu.somlyaip.pets.spendinganalytics.swing.categories;

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
public class Category {
    private String name;
    private List<String> sellers;
}
