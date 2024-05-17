package com.eversis.esa.geoss.contents.parser.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * The type Content csv.
 */
@Data
public class ContentCsv {

    @CsvBindByName(column = "id")
    private Long id;
    @CsvBindByName(column = "published")
    private boolean published;
}
