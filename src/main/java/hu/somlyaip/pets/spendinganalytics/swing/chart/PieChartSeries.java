package hu.somlyaip.pets.spendinganalytics.swing.chart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Getter
@ToString
public class PieChartSeries {
    private String categoryName;
    private BigDecimal percentage;
}
