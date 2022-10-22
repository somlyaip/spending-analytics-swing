package hu.somlyaip.pets.spendinganalytics.swing.view;

import hu.somlyaip.pets.spendinganalytics.swing.*;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;

import javax.swing.*;
import java.awt.*;
import java.io.File;
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

    private JFrame viewFrame;
    private Container rootPane;

    private DataFileUiComponent dataFileUiComponent;
    private TransactionsUiComponent transactionsUiComponent;

    public AnalyticsView(AnalyticsModel model, AnalyticsController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void createUiElements(HufFormatter hufFormatter, DateFormatter dateFormatter) {
        createFrame();
        createRootPane();

        this.dataFileUiComponent = new DataFileUiComponent(controller);
        rootPane.add(this.dataFileUiComponent, BorderLayout.PAGE_START);
        this.transactionsUiComponent = new TransactionsUiComponent(hufFormatter, dateFormatter);
        rootPane.add(this.transactionsUiComponent);

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

    public Optional<File> browseTransactionDataFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(viewFrame) == JFileChooser.APPROVE_OPTION) {
            return Optional.of(fileChooser.getSelectedFile());
        }

        return Optional.empty();
    }

    public void enableBrowseButton() {
        dataFileUiComponent.setEnableButtonBrowseTransactionFile(true);
    }

    public void disableBrowseButton() {
        dataFileUiComponent.setEnableButtonBrowseTransactionFile(false);
    }

    @Override
    public void onLoadingTransactionsCompleted(File transactionDataFile, List<MoneyTransaction> transactions) {
        dataFileUiComponent.setLabelLoadedTransactionFilename(transactionDataFile);
        transactionsUiComponent.updateTransactions(transactions);
    }
}
