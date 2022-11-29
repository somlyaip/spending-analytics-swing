package hu.somlyaip.pets.spendinganalytics.swing.datafile;

import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.File;
import java.util.Optional;
import java.util.prefs.Preferences;

@Component
public class DataFileChooser {

    private static final String LAST_BROWSED_FOLDER_KEY = "last-browsed-data-file-folder";
    private final Preferences userPreferences;

    public DataFileChooser() {
        // On macOS it will be saved into ~/Library/Preferences/hu.somlyaip.pets.plist
        userPreferences = Preferences.userNodeForPackage(getClass());
    }

    public Optional<File> browseDataFileOnCenterOf(java.awt.Component parent) {
        JFileChooser fileChooser = new JFileChooser(
                userPreferences.get(LAST_BROWSED_FOLDER_KEY, new File(".").getAbsolutePath())
        );
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            userPreferences.put(LAST_BROWSED_FOLDER_KEY, fileChooser.getSelectedFile().getParent());
            return Optional.of(fileChooser.getSelectedFile());
        }

        return Optional.empty();
    }
}
