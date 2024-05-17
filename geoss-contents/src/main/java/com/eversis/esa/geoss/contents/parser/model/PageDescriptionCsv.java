package com.eversis.esa.geoss.contents.parser.model;

import java.util.Locale;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * The type Page description csv.
 */
@Data
public class PageDescriptionCsv {

    @CsvBindByName(column = "page_id")
    private Long pageId;
    @CsvBindByName(column = "description")
    private String description;
    @CsvBindByName(column = "locale")
    private String locale;

    /**
     * Gets locale.
     *
     * @return the locale
     */
    public Locale getLocale() {
        return Locale.forLanguageTag(this.locale);
    }

}
