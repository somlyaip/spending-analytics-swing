package hu.somlyaip.pets.spendinganalytics.swing;

import org.springframework.stereotype.Component;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
public class AnalyticsController {

    private final AnalyticsModel model;
    private AnalyticsView view;

    public AnalyticsController(AnalyticsModel model) {
        this.model = model;
    }

    public void createAndInitializeView() {
        // This is ugly, but without this there is a circular references between beans
        this.view = new AnalyticsView(model, this);
        this.view.createView();
        this.view.createControls();
        this.model.registerTransactionLoadedObserver(this.view);
    }

    public void browseTransactionDataFile() {
        view.browseTransactionDataFile().ifPresent((file) -> new Thread(() -> {
            view.disableBrowseButton();
            model.loadTransactionDataFile(file);
            view.enableBrowseButton();
        }).start());
    }
}
