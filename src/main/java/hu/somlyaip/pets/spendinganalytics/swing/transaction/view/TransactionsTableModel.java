package hu.somlyaip.pets.spendinganalytics.swing.transaction.view;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import hu.somlyaip.pets.spendinganalytics.swing.view.DateFormatter;
import hu.somlyaip.pets.spendinganalytics.swing.view.HufFormatter;

import javax.swing.table.AbstractTableModel;
import java.util.List;

/**
 * Mostly to disable editing cells because AbstractTableModel's isCellEditable() returns false.
 * And it looks fun, so I have to try it.
 */
public class TransactionsTableModel extends AbstractTableModel {

    private final List<MoneyTransaction> transactions;
    private final HufFormatter hufFormatter;
    private final DateFormatter dateFormatter;

    public TransactionsTableModel(
            List<MoneyTransaction> transactions, HufFormatter hufFormatter, DateFormatter dateFormatter
    ) {
        this.transactions = transactions;
        this.hufFormatter = hufFormatter;
        this.dateFormatter = dateFormatter;
    }

    @Override
    public int getRowCount() {
        return transactions.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MoneyTransaction transaction = transactions.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> transaction.getSeller();
            case 1 -> hufFormatter.format(transaction.getAmount());
            case 2 -> dateFormatter.format(transaction.getDate());
            default -> throw new IllegalStateException("Unexpected value: " + columnIndex);
        };
    }

    @Override
    public String getColumnName(int column) {
        return switch (column) {
            case 0 -> "Seller";
            case 1 -> "Amount";
            case 2 -> "Date";
            default -> throw new IllegalStateException("Unexpected value: " + column);
        };
    }

    public MoneyTransaction getTransactionAt(int rowIndex) {
        return transactions.get(rowIndex);
    }
}
