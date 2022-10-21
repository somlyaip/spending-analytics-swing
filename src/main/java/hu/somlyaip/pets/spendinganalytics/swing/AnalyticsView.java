package hu.somlyaip.pets.spendinganalytics.swing;

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

    private JButton buttonBrowseTransactionFile;
    private JLabel labelLoadedTransactionFilename;

    public AnalyticsView(AnalyticsModel model, AnalyticsController controller) {
        this.model = model;
        this.controller = controller;
    }

    public void createUiComponents() {
        createFrame();
        Container rootPane = createRootPane();
        createFilePanel(rootPane);
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

    private Container createRootPane() {
        Container rootPane = viewFrame.getContentPane();
        rootPane.setLayout(new BorderLayout(5, 5));
        return rootPane;
    }

    private void createFilePanel(Container rootPane) {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rootPane.add(topPanel, BorderLayout.PAGE_START);

        buttonBrowseTransactionFile = new JButton("Browse transactions");
        buttonBrowseTransactionFile.addActionListener(event -> controller.browseTransactionDataFile());
        topPanel.add(buttonBrowseTransactionFile);

        labelLoadedTransactionFilename = new JLabel();
        labelLoadedTransactionFilename.setVisible(false);
        topPanel.add(labelLoadedTransactionFilename, BorderLayout.CENTER);
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
    public void onLoadingTransactionsCompleted(File transactionDataFile, List<Transaction> transactions) {
        labelLoadedTransactionFilename.setText(transactionDataFile.getAbsolutePath());
        labelLoadedTransactionFilename.setVisible(true);
    }
}
