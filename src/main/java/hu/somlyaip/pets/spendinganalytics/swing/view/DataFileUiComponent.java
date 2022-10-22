package hu.somlyaip.pets.spendinganalytics.swing.view;

import hu.somlyaip.pets.spendinganalytics.swing.AnalyticsController;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
public class DataFileUiComponent extends JPanel {

    private final AnalyticsController controller;
    private JButton buttonBrowseTransactionFile;
    private JLabel labelLoadedTransactionFilename;

    public DataFileUiComponent(AnalyticsController controller) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.controller = controller;

        createComponents();
    }

    private void createComponents() {
        buttonBrowseTransactionFile = new JButton("Browse transactions");
        buttonBrowseTransactionFile.addActionListener(event -> controller.browseTransactionDataFile());
        add(buttonBrowseTransactionFile);

        labelLoadedTransactionFilename = new JLabel();
        labelLoadedTransactionFilename.setVisible(false);
        add(labelLoadedTransactionFilename, BorderLayout.CENTER);
    }

    public void setLabelLoadedTransactionFilename(File transactionFilename) {
        labelLoadedTransactionFilename.setText(transactionFilename.getAbsolutePath());
        labelLoadedTransactionFilename.setVisible(true);
    }

    public void setEnableButtonBrowseTransactionFile(boolean isEnabled) {
        buttonBrowseTransactionFile.setEnabled(isEnabled);
    }
}
