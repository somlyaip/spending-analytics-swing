package hu.somlyaip.pets.spendinganalytics.swing;

import org.springframework.stereotype.Component;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
public class AnalyticsController {

    private final AnalyticsModel model;
    private final HufFormatter hufFormatter;
    private final DateFormatter dateFormatter;
    private AnalyticsView view;

    public AnalyticsController(AnalyticsModel model, HufFormatter hufFormatter, DateFormatter dateFormatter) {
        this.model = model;
        this.hufFormatter = hufFormatter;
        this.dateFormatter = dateFormatter;
    }

    public void createAndInitializeView() {
        // This is ugly, but without this there is a circular references between beans
        this.view = new AnalyticsView(model, this, hufFormatter, dateFormatter);
        this.view.createUiComponents();
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
