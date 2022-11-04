package hu.somlyaip.pets.spendinganalytics.swing.categories.dto;

/**
 * There is good enough a naive singleton.
 * It is used only in a HashMap key, where only one instance is enabled at one time.
 */
public final class Uncategorized implements ISelectableCategory {
    private static Uncategorized instance;

    private Uncategorized() { }

    public static Uncategorized getInstance() {
        if (instance == null) {
            instance = new Uncategorized();
        }

        return instance;
    }

    @Override
    public String getName() {
        return "Uncategorized";
    }
}
