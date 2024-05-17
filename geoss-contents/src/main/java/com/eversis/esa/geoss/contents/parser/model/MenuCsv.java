package com.eversis.esa.geoss.contents.parser.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * The type Menu csv.
 */
@Data
public class MenuCsv {

    @CsvBindByName(column = "id")
    private Long id;
    @CsvBindByName(column = "image_source")
    private String imageSource;
    @CsvBindByName(column = "url")
    private String url;
    @CsvBindByName(column = "priority")
    private int priority;
    @CsvBindByName(column = "level_id")
    private int levelId;
}
