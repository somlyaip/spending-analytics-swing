package hu.somlyaip.pets.spendinganalytics.swing.transaction;

import hu.somlyaip.pets.spendinganalytics.swing.view.DateFormatter;
import hu.somlyaip.pets.spendinganalytics.swing.view.HufFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public class TransactionsUiComponent extends JPanel {

    private final HufFormatter hufFormatter;
    private final DateFormatter dateFormatter;

    private JTable transactionTable;

    public TransactionsUiComponent(HufFormatter hufFormatter, DateFormatter dateFormatter) {
        super(new GridLayout(1, 1));

        this.hufFormatter = hufFormatter;
        this.dateFormatter = dateFormatter;

        createTransactionPanel();
    }

    private void createTransactionPanel() {
        transactionTable = createTransactionTable(Collections.emptyList());
        this.add(transactionTable, BorderLayout.CENTER);
    }

    private JTable createTransactionTable(List<MoneyTransaction> transactions) {
        return new JTable(
                transactions.stream().map(moneyTransaction ->
                        new Object[]{
                                moneyTransaction.getSeller(),
                                hufFormatter.format(moneyTransaction.getAmount()),
                                dateFormatter.format(moneyTransaction.getDate())
                        }
                ).toList().toArray(new Object[][]{}),
                new Object[]{"Seller", "Amount", "Date"}
        );
    }

    public void updateTransactions(List<MoneyTransaction> transactions) {
        remove(transactionTable);
        transactionTable = createTransactionTable(transactions);
        add(transactionTable, BorderLayout.CENTER);
        // Refresh pane
        revalidate();
        repaint();
    }
}
