package hu.somlyaip.pets.spendinganalytics.swing.transaction.persistence;

import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
@Component
@Slf4j
public class DevMockTransactionLoader implements ITransactionLoader {

    private final Random random;

    public DevMockTransactionLoader() {
        random = new Random();
    }

    @Override
    public List<MoneyTransaction> loadTransactionsFrom(File dataFile) {
        try {
            final int waitMillis = random.nextInt(2501) + 500;
            log.debug("Waiting {} ms...", waitMillis);
            Thread.sleep(waitMillis);
            log.debug("Done");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return List.of(
            MoneyTransaction.builder()
                    .seller("Aldi")
                    .amount(new BigDecimal(32_367))
                    .date(LocalDate.now())
                    .build(),
            MoneyTransaction.builder()
                    .seller("Foodpanda")
                    .amount(new BigDecimal(4_987))
                    .date(LocalDate.now())
                    .build(),
            MoneyTransaction.builder()
                    .seller("OMV Fuel Station")
                    .amount(new BigDecimal(18_012))
                    .date(LocalDate.now())
                    .build(),
            MoneyTransaction.builder()
                    .seller("Sony Play Station")
                    .amount(new BigDecimal(28_000))
                    .date(LocalDate.now())
                    .build()
        );
    }
}
