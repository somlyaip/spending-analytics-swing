package hu.somlyaip.pets.spendinganalytics.swing.view;

import hu.somlyaip.pets.spendinganalytics.swing.categories.view.CategoriesUiComponent;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.TransactionsUiComponent;

import javax.swing.*;
import java.awt.*;

/**
 * @author somlyaip
 * created at 2022. 10. 22.
 */
public class CategoriesAndTransactionsUiComponent extends JPanel {
    public CategoriesAndTransactionsUiComponent(
            CategoriesUiComponent categoriesUiComponent, TransactionsUiComponent transactionsUiComponent
    ) {
        super(new BorderLayout(5, 5));

        add(categoriesUiComponent, BorderLayout.PAGE_START);
        add(transactionsUiComponent, BorderLayout.LINE_START);
    }
}
