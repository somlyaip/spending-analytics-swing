package hu.somlyaip.pets.spendinganalytics.swing.categories.view;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Optional;

public class SelectCategoryModal {
    private JDialog frame;

    private ISelectableCategory selectedCategory;
    private boolean isSelectButtonPressed;

    public SelectCategoryModal(Frame parentFrame, java.util.List<ISelectableCategory> categories) {
        createFrame(parentFrame, categories);
    }

    private void createFrame(Frame parentFrame, java.util.List<ISelectableCategory> categories) {
        frame = new JDialog(parentFrame, "Select category", true);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        contentPane.add(new JLabel("Select the target category, where selected transaction will be placed"));
        contentPane.add(
                new CategoriesListComponent(categories, selectedCategory -> this.selectedCategory = selectedCategory)
        );

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(e -> {
            isSelectButtonPressed = true;
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        });
        contentPane.add(selectButton);

        frame.pack();
        frame.setVisible(true);
    }

    public Optional<ISelectableCategory> getSelectedCategory() {
        if (! isSelectButtonPressed) {
            return Optional.empty();
        }

        return Optional.ofNullable(selectedCategory);
    }
}
