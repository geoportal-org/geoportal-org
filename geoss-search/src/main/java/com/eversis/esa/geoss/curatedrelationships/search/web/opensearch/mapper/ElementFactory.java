package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import org.jdom2.Element;
import org.jdom2.Namespace;

/**
 * The type Element factory.
 */
public class ElementFactory {

    private ElementFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create element element.
     *
     * @param name the name
     * @param contentValue the content value
     * @return the element
     */
    public static Element createElement(String name, String contentValue) {
        Element element = new Element(name);
        element.addContent(contentValue);
        return element;
    }

    /**
     * Create element element.
     *
     * @param name the name
     * @param contentValue the content value
     * @param namespace the namespace
     * @return the element
     */
    public static Element createElement(String name, String contentValue, Namespace namespace) {
        Element element = new Element(name, namespace);
        element.addContent(contentValue);
        return element;
    }

}
