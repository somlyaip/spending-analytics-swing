package hu.somlyaip.pets.spendinganalytics.swing.categories;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public class CategoriesUiComponent extends JPanel {
    public CategoriesUiComponent() {
        super(new FlowLayout(FlowLayout.LEFT, 5, 5));
    }

    public void updateCategories(List<Category> categories) {
        removeAll();
        categories.forEach(c -> add(new JButton(c.getName())));
    }
}
