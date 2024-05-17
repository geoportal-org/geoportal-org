package com.eversis.esa.geoss.contents.parser.model;

import java.util.Locale;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

/**
 * The type Menu image title csv.
 */
@Data
public class MenuImageTitleCsv {

    @CsvBindByName(column = "menu_id")
    private Long menuId;
    @CsvBindByName(column = "image_title")
    private String imageTitle;
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
