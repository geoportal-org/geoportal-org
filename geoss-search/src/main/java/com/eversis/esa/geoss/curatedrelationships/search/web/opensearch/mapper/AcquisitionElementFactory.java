package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements;

import org.jdom2.Element;

/**
 * The type Acquisition element factory.
 */
public class AcquisitionElementFactory {

    private AcquisitionElementFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create acquisition element element.
     *
     * @return the element
     */
    public static Element createAcquisitionElement() {
        Element platformElement = new Element("platform").addContent("GEOSS_CR");
        return new Element(OpensearchResponseElements.ACQUISITION).addContent(platformElement);
    }

}
