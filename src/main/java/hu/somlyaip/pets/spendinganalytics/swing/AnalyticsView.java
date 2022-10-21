package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
public class AnalyticsView implements ITransactionsLoadedObserver {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final AnalyticsModel model;
    private final AnalyticsController controller;
    private final HufFormatter hufFormatter;
    private final DateFormatter dateFormatter;

    private JFrame viewFrame;
    private Container rootPane;

    private JButton buttonBrowseTransactionFile;
    private JLabel labelLoadedTransactionFilename;
    private JTable transactionTable;

    public AnalyticsView(
            AnalyticsModel model, AnalyticsController controller,
            HufFormatter hufFormatter, DateFormatter dateFormatter
    ) {
        this.model = model;
        this.controller = controller;
        this.hufFormatter = hufFormatter;
        this.dateFormatter = dateFormatter;
    }

    public void createUiComponents() {
        createFrame();
        createRootPane();
        createFilePanel();
        createTransactionPanel();
        JFrame.setDefaultLookAndFeelDecorated(true);
    }

    private void createFrame() {
        viewFrame = new JFrame("View");
        viewFrame.setTitle("Spending Analytics");
        viewFrame.setSize(800, 500);
        viewFrame.setLocationRelativeTo(null);
        viewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewFrame.setVisible(true);
    }

    private void createRootPane() {
        rootPane = viewFrame.getContentPane();
        rootPane.setLayout(new BorderLayout(5, 5));
    }

    private void createFilePanel() {
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rootPane.add(filePanel, BorderLayout.PAGE_START);

        buttonBrowseTransactionFile = new JButton("Browse transactions");
        buttonBrowseTransactionFile.addActionListener(event -> controller.browseTransactionDataFile());
        filePanel.add(buttonBrowseTransactionFile);

        labelLoadedTransactionFilename = new JLabel();
        labelLoadedTransactionFilename.setVisible(false);
        filePanel.add(labelLoadedTransactionFilename, BorderLayout.CENTER);
    }

    private void createTransactionPanel() {
        transactionTable = createTransactionTable(Collections.emptyList());
        rootPane.add(transactionTable, BorderLayout.CENTER);
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

    public Optional<File> browseTransactionDataFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(viewFrame) == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile());
        }

        return Optional.empty();
    }

    public void enableBrowseButton() {
        buttonBrowseTransactionFile.setEnabled(true);
    }

    public void disableBrowseButton() {
        buttonBrowseTransactionFile.setEnabled(false);
    }

    @Override
    public void onLoadingTransactionsCompleted(File transactionDataFile, List<MoneyTransaction> transactions) {
        labelLoadedTransactionFilename.setText(transactionDataFile.getAbsolutePath());
        labelLoadedTransactionFilename.setVisible(true);

        updateTransactionTable(transactions);
    }

    private void updateTransactionTable(List<MoneyTransaction> transactions) {
        rootPane.remove(transactionTable);
        transactionTable = createTransactionTable(transactions);
        rootPane.add(transactionTable, BorderLayout.CENTER);
        // Refresh pane
        rootPane.revalidate();
        rootPane.repaint();
    }
}
