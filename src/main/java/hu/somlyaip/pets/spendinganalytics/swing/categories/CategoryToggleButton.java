package hu.somlyaip.pets.spendinganalytics.swing.categories;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.util.function.Consumer;

public class CategoryToggleButton extends JButton {

    private boolean isPressed;

    private final Font defaultFont;
    private final Font selectedFont;

    public CategoryToggleButton(
            Category category, Consumer<Category> onCategorySelected, Consumer<Category> onCategoryUnselected
    ) {

        setText(category.getName());

        setContentAreaFilled(false);
        defaultFont = getFont();
        selectedFont = createSelectedFontFrom(defaultFont);
        addActionListener(l -> {
            isPressed = ! isPressed;
            if (isPressed) {
                setFont(selectedFont);
                onCategorySelected.accept(category);
            } else {
                setFont(defaultFont);
                onCategoryUnselected.accept(category);
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
}
