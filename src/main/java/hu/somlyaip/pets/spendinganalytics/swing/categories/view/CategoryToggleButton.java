package hu.somlyaip.pets.spendinganalytics.swing.categories.view;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.ISelectableCategory;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.function.Consumer;

public class CategoryToggleButton extends JButton {

    private final ISelectableCategory category;
    private final Consumer<ISelectableCategory> onCategorySelected;
    private final Consumer<ISelectableCategory> onCategoryUnselected;
    private boolean isSelected;

    private final Font defaultFont;
    private final Font selectedFont;

    public CategoryToggleButton(
            ISelectableCategory category, Consumer<ISelectableCategory> onCategorySelected,
            Consumer<ISelectableCategory> onCategoryUnselected
    ) {
        this.category = category;
        this.onCategorySelected = onCategorySelected;
        this.onCategoryUnselected = onCategoryUnselected;

        setText(category.getName());

        defaultFont = getFont();
        selectedFont = createSelectedFontFrom(defaultFont);
        addActionListener(l -> {
            // check last state
            if (isSelected) {
                unselect();
            } else {
                select();
            }
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private Font createSelectedFontFrom(Font originalFont) {
        Map attributes = originalFont.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
        return originalFont.deriveFont(attributes);
    }

    void select() {
        setUiSelected();
        onCategorySelected.accept(category);
    }

    private void unselect() {
        setUiUnselected();
        onCategoryUnselected.accept(category);
    }

    private void setUiSelected() {
        if (isSelected) {
            throw new IllegalStateException(
                    "'%s' CategoryToggleButton is already in 'selected' state".formatted(category.getName())
            );
        }
        isSelected = true;
        setFont(selectedFont);
    }

    void setUiUnselected() {
        if (! isSelected) {
            throw new IllegalStateException(
                    "'%s' CategoryToggleButton is already in 'unselected' state".formatted(category.getName())
            );
        }

        isSelected = false;
        setFont(defaultFont);
    }
}
