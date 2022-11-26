package hu.somlyaip.pets.spendinganalytics.swing.chart;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;

public class ChartUiComponent extends JPanel {

    public ChartUiComponent(List<PieChartSeries> pieChartSeries) {
        updateChart(pieChartSeries);
    }

    public void updateChart(List<PieChartSeries> pieChartSeries) {
        pieChartSeries.sort(Comparator.comparing(PieChartSeries::getPercentage));

        // Create Chart
        PieChart chart = new PieChartBuilder()
                .width(400)
                .height(300)
                .title("Sum amounts of categories")
                .theme(Styler.ChartTheme.GGPlot2)
                .build();

        // Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
        chart.getStyler().setToolTipsEnabled(true);

        // Series
        pieChartSeries.forEach(series -> chart.addSeries(series.getCategoryName(), series.getPercentage()));

        removeAll();
        add(new XChartPanel<>(chart));
        // Refresh pane
        revalidate();
        repaint();
    }
}
