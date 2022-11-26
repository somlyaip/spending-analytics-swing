package hu.somlyaip.pets.spendinganalytics.swing.transaction.persistence;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import hu.somlyaip.pets.spendinganalytics.swing.transaction.MoneyTransaction;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author somlyaip
 * created at 2022. 10. 21.
 */
@Component
public class KhCsvTransactionLoader implements ITransactionLoader {
    @Override
    public List<MoneyTransaction> loadTransactionsFrom(File dataFile) {
        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator('\t')
                .withIgnoreQuotations(true)
                .build();
        try (CSVReader csvReader = new CSVReaderBuilder(Files.newBufferedReader(dataFile.toPath()))
                    .withSkipLines(1)
                    .withCSVParser(csvParser)
                    .build()) {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String[] record;
            List<MoneyTransaction> transactions = new ArrayList<>();
            while ((record = csvReader.readNext()) != null) {
                String amountAsText = record[7];
                if (Integer.parseInt(amountAsText) < 0) {
                    // It is a payment
                    MoneyTransaction transaction = MoneyTransaction.builder()
                            // if partner name (6) is empty it is probably a loan repayment which is distinguished by type (2)
                            .seller(StringUtils.hasText(record[6]) ? record[6] : record[2])
                            .date(LocalDate.parse(record[0], dateTimeFormatter))
                            .amount(new BigDecimal(amountAsText.substring(1)))
                            .build();
                    transactions.add(transaction);
                }
            }
            return transactions;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new InvalidDataFileFormatException(e);
        }
    }
}
