package com.eversis.esa.geoss.contents.parser.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

import java.util.Locale;

/**
 * The type Content title csv.
 */
@Data
public class ContentTitleCsv {

    @CsvBindByName(column = "content_id")
    private Long contentId;
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
