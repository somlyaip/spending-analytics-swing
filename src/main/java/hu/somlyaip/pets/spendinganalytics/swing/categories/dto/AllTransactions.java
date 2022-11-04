package hu.somlyaip.pets.spendinganalytics.swing.categories.dto;

/**
 * There is good enough a naive singleton.
 * It is used only in a HashMap key, where only one instance is enabled at one time.
 */
public class AllTransactions implements ISelectableCategory {

    private static AllTransactions instance;

    private AllTransactions() { }

    public static AllTransactions getInstance() {
        if (instance == null) {
            instance = new AllTransactions();
        }

        return instance;
    }

    @Override
    public String getName() {
        return "All";
    }
}
