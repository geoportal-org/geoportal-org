package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.rometools.rome.feed.atom.Content;

public class ContentFactory {

    private ContentFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Content createTextContent(String text) {
        return createContent(Content.TEXT, text);
    }

    private static Content createContent(String type, String value) {
        Content content = new Content();
        content.setType(type);
        content.setValue(value);
        return content;
    }

}
