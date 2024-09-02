package com.eversis.esa.geoss.contents.parser.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.util.Locale;

/**
 * The type Page title csv.
 */
@Data
public class PageTitleCsv {

    @CsvBindByName(column = "page_id")
    private Long pageId;
    @CsvBindByName(column = "title")
    private String title;
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
