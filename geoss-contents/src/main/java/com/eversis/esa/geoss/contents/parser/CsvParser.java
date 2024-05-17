package com.eversis.esa.geoss.contents.parser;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;

/**
 * This class provides a utility method to parse CSV files into a list of objects.
 * It uses the OpenCSV library to parse the CSV file.
 */
public class CsvParser {

    /**
     * Generic method to parse CSV files.
     *
     * @param type The class type of the objects to be parsed from the CSV file.
     * @param csvFile The resource file to be parsed.
     * @return A list of objects of the specified type.
     */
    @SneakyThrows
    <T> List<T> parseCsv(Class<T> type, Resource csvFile) {
        return new CsvToBeanBuilder<T>(
                new InputStreamReader(csvFile.getInputStream(), StandardCharsets.UTF_8))
                .withType(type)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator('|')
                .build()
                .parse();
    }

}
