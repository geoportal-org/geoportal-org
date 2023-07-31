package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements;

import org.jdom2.Element;

public class AcquisitionElementFactory {

    private AcquisitionElementFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Element createAcquisitionElement() {
        Element platformElement = new Element("platform").addContent("GEOSS_CR");
        return new Element(OpensearchResponseElements.ACQUISITION).addContent(platformElement);
    }

}
