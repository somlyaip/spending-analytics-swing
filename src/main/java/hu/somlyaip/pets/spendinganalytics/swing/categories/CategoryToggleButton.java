package hu.somlyaip.pets.spendinganalytics.swing.categories;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.function.Consumer;

public class CategoryToggleButton extends JButton {

    private final Category category;
    private boolean isSelected;

    private final Font defaultFont;
    private final Font selectedFont;

    public CategoryToggleButton(
            Category category, Consumer<Category> onCategorySelected, Consumer<Category> onCategoryUnselected
    ) {
        this.category = category;

        setText(category.getName());

        defaultFont = getFont();
        selectedFont = createSelectedFontFrom(defaultFont);
        addActionListener(l -> {
            // check last state
            if (isSelected) {
                unselect();
                onCategoryUnselected.accept(category);
            } else {
                select();
                onCategorySelected.accept(category);
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

    private void select() {
        if (isSelected) {
            throw new IllegalStateException(
                    "'%s' CategoryToggleButton is already in 'selected' state".formatted(category.getName())
            );
        }
        isSelected = true;
        setFont(selectedFont);
    }

    void unselect() {
        if (! isSelected) {
            throw new IllegalStateException(
                    "'%s' CategoryToggleButton is already in 'unselected' state".formatted(category.getName())
            );
        }

        isSelected = false;
        setFont(defaultFont);
    }
}
