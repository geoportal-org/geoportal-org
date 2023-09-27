package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Safelist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Html util.
 */
public class HtmlUtil {

    private HtmlUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Clear html string.
     *
     * @param html the html
     * @return the string
     */
    public static String clearHtml(String html) {
        String text = html;

        // clean links
        Pattern p = Pattern.compile("<a[^']+'([^']+)[^>]+>([^<]+)</a>");
        Matcher m = p.matcher(text);
        text = m.replaceAll(" $2: $1.");

        // preserve line breaks and full stops
        text = text
                .replace("<p>", "\n")
                .replace("<br/>", "\n")
                .replace("</h3>", ".")
                .replace("</p>", ".");

        // remove any html tags left
        text = Jsoup.clean(text, Safelist.none());

        // unescape xml/html special characters
        text = Entities.unescape(text);

        // clean duplicate characters
        text = text
                .replace(" .", ".")
                .replace("   ", " ")
                .replace("  ", " ")
                .replace("...", ".")
                .replace("..", ".");

        return text;
    }

}
