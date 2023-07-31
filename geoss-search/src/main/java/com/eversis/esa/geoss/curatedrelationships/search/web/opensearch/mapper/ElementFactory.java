package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import org.jdom2.Element;
import org.jdom2.Namespace;

public class ElementFactory {

    private ElementFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Element createElement(String name, String contentValue) {
        Element element = new Element(name);
        element.addContent(contentValue);
        return element;
    }

    public static Element createElement(String name, String contentValue, Namespace namespace) {
        Element element = new Element(name, namespace);
        element.addContent(contentValue);
        return element;
    }

}
