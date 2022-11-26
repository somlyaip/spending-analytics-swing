package hu.somlyaip.pets.spendinganalytics.swing.transaction.view;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.observer.ISelectedTransactionsChangedObserver;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import hu.somlyaip.pets.spendinganalytics.swing.view.DateFormatter;
import hu.somlyaip.pets.spendinganalytics.swing.view.HufFormatter;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public class TransactionsUiComponent extends JPanel {

    private final HufFormatter hufFormatter;
    private final DateFormatter dateFormatter;
    private final ISelectedTransactionsChangedObserver selectedTransactionChangedObserver;

    private JTable transactionTable;
    private TransactionsTableModel transactionsTableModel;

    public TransactionsUiComponent(
            HufFormatter hufFormatter, DateFormatter dateFormatter,
            ISelectedTransactionsChangedObserver selectedTransactionChangedObserver
    ) {
        super(new GridLayout(1, 1));

        this.hufFormatter = hufFormatter;
        this.dateFormatter = dateFormatter;
        this.selectedTransactionChangedObserver = selectedTransactionChangedObserver;

        createTransactionPanel();
    }

    private void createTransactionPanel() {
        transactionTable = createTransactionTable(Collections.emptyList());
        this.add(new JScrollPane(transactionTable), BorderLayout.CENTER);
    }

    private JTable createTransactionTable(List<MoneyTransaction> transactions) {
        this.transactionsTableModel = new TransactionsTableModel(transactions, hufFormatter, dateFormatter);
        JTable transactionsTable = new JTable(transactionsTableModel);
        transactionsTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        transactionsTable.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }

            DefaultListSelectionModel defaultListSelectionModel = (DefaultListSelectionModel) e.getSource();
            if (defaultListSelectionModel.isSelectionEmpty()) {
                selectedTransactionChangedObserver.onSelectedTransactionsChanged(Collections.emptyList());
            }

            List<MoneyTransaction> selectedTransactions = new ArrayList<>();
            for (int i = 0; i < transactionsTableModel.getRowCount(); i ++) {
                if (defaultListSelectionModel.isSelectedIndex(i)) {
                    selectedTransactions.add(transactionsTableModel.getTransactionAt(i));
                }
            }
            selectedTransactionChangedObserver.onSelectedTransactionsChanged(selectedTransactions);
        });
        return transactionsTable;
    }

    public void updateTransactions(List<MoneyTransaction> transactions) {
        removeAll();
        transactionTable = createTransactionTable(transactions);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);
        // Refresh pane
        revalidate();
        repaint();
    }
}
