package hu.somlyaip.pets.spendinganalytics.swing;

/**
 * The first time in a long time I needed a checked exception.
 */
public class CannotRemoveLogicalCategoryException extends Exception {
    public CannotRemoveLogicalCategoryException(String message) {
        super(message);
    }
}
