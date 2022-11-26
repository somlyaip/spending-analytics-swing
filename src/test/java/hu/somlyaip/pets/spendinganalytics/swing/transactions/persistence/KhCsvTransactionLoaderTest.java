package hu.somlyaip.pets.spendinganalytics.swing.transactions.persistence;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.persistence.KhCsvTransactionLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class KhCsvTransactionLoaderTest {

    @Test
    void testLoad_usingSampleHistoryFromResources_shouldReturnsAllPaymentTransactions() {
        @SuppressWarnings("ConstantConditions")
        File dataFile = new File(getClass().getClassLoader().getResource("kh-transaction-history.csv").getPath());
        var underTest = new KhCsvTransactionLoader();

        List<MoneyTransaction> transactions = underTest.loadTransactionsFrom(dataFile);

        Assertions.assertEquals(
                List.of(
                        MoneyTransaction.builder()
                                .seller("OMV 1986")
                                .date(LocalDate.of(2022, 4, 14))
                                .amount(new BigDecimal(5980))
                                .build(),
                        MoneyTransaction.builder()
                                .seller("AUCHAN SOROKSAR       BUDAPEST")
                                .date(LocalDate.of(2022, 4, 9))
                                .amount(new BigDecimal(2596))
                                .build()
                ),
                transactions
        );
    }
}
