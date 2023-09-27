package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The type Protocol.
 */
public class Protocol {

    /**
     * The constant DOWNLOAD.
     */
    public static final Protocol DOWNLOAD = new Protocol("download");
    /**
     * The constant INFORMATION.
     */
    public static final Protocol INFORMATION = new Protocol("information");
    /**
     * The constant INFORMATION_HTML.
     */
    public static final Protocol INFORMATION_HTML = new Protocol("information-html");
    /**
     * The constant SDG.
     */
    public static final Protocol SDG = new Protocol("gwp_un_sd_/v1/sdg");
    /**
     * The constant WMS.
     */
    public static final Protocol WMS = new Protocol("OGC WMS 1.3.0,urn:ogc:serviceType:WebMapService:1.1.1:HTTP,"
            + "urn:ogc:serviceType:WebMapService:1.3.0:HTTP,"
            + "urn:ogc:serviceType:WebMapService:HTTP");
    /**
     * The constant WCS.
     */
    public static final Protocol WCS = new Protocol("urn:ogc:serviceType:WebCoverageService:2.0.1:HTTP");
    /**
     * The constant KML.
     */
    public static final Protocol KML = new Protocol("kml");
    /**
     * The constant WORKFLOW.
     */
    public static final Protocol WORKFLOW = new Protocol("ecopotential_workflow_api");

    private String value;

    /**
     * Instantiates a new Protocol.
     *
     * @param value the value
     */
    public Protocol(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
