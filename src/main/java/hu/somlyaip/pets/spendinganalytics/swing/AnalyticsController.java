package hu.somlyaip.pets.spendinganalytics.swing;

import hu.somlyaip.pets.spendinganalytics.swing.categories.dto.Category;
import hu.somlyaip.pets.spendinganalytics.swing.view.AnalyticsView;
import hu.somlyaip.pets.spendinganalytics.swing.view.DateFormatter;
import hu.somlyaip.pets.spendinganalytics.swing.view.HufFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author somlyaip
 * created at 2022. 10. 19.
 */
@Component
@Slf4j
public class AnalyticsController implements IAnalyticsController {

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
        this.view = new AnalyticsView(model, this);
        this.view.createUiElements(hufFormatter, dateFormatter);
        this.model.registerTransactionLoadedObserver(this.view);
        this.model.registerCategoriesUpdatedObserver(this.view);
        this.model.registerSelectedCategoryUpdatedObserver(this.view);
    }

    @Override
    public void browseTransactionDataFile() {
        view.browseTransactionDataFile().ifPresent(file -> new Thread(() -> {
            try {
                model.loadCategories();

                view.disableBrowseButton();
                try {
                    model.loadTransactionDataFile(file);
                } finally {
                    view.enableBrowseButton();
                }

                view.selectAllCategories();
                view.updateChart();

            } catch (Exception e) {
                view.notifyUserFromFailedToLoadDataFile(e);
                log.error("Failed to load datafile", e);
            }
        }).start());
    }

    @Override
    public void askToAndAddNewCategory() {
        view.askToNewCategoryName().ifPresent(categoryName -> {
            var newCategory = Category.builder()
                    .name(categoryName)
                    .sellers(new ArrayList<>())
                    .build();
            try {
                model.addNewCategory(newCategory);

            } catch (CategoryAlreadyExistsException e) {
                view.notifyCategoryAlreadyExists();
                return;
            }

            view.updateChart();
        });
    }

    @Override
    public void removeSelectedCategory() {
        if (! model.hasSelectedCategory()) {
            view.notifyUserSelectACategoryToRemove();
            return;
        }

        try {
            model.removeSelectedCategory();
        } catch (CannotRemoveLogicalCategoryException e) {
            view.notifyUserCannotRemoveLogicalCategory();
        }
        view.updateChart();
    }

    @Override
    public void askCategoryToSelectAndAddSelectedTransactionToIt() {
        if (! model.hasSelectedTransaction()) {
            view.notifyUserShouldSelectAnUncategorizedTransactionToAddItToAnyCategory();
            return;
        }

        view.askCategoryToSelect().ifPresent(model::addSelectedTransactionTo);
        view.updateTransactionsTable(
                model.getTransactionsOf(model.getSelectedCategory()).orElse(Collections.emptyList())
        );
        view.updateChart();
    }

    public void removeSelectedTransactionFromSelectedCategory() {
        if (! model.hasSelectedTransaction()) {
            view.notifyUserShouldSelectATransactionToRemove();
            return;
        }

        model.removeSelectedTransactionFromSelectedCategory();
        view.updateTransactionsTable(
                model.getTransactionsOf(model.getSelectedCategory()).orElse(Collections.emptyList())
        );
        view.updateChart();
    }
}
