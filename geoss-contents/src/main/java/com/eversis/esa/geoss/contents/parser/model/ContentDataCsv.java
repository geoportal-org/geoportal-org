package com.eversis.esa.geoss.contents.parser.model;

import java.util.Locale;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * The type Content data csv.
 */
@Data
public class ContentDataCsv {

    @CsvBindByName(column = "content_id")
    private Long contentId;
    @CsvBindByName(column = "data")
    private String data;
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
