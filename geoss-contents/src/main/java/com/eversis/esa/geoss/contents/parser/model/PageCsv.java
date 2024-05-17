package com.eversis.esa.geoss.contents.parser.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * The type Page csv.
 */
@Data
public class PageCsv {

    @CsvBindByName(column = "id")
    private Long id;
    @CsvBindByName(column = "slug")
    private String slug;
    @CsvBindByName(column = "published")
    private boolean published;
}
