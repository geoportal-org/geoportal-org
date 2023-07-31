package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;

import com.rometools.modules.georss.GeoRSSModule;
import com.rometools.modules.georss.SimpleModuleImpl;
import com.rometools.modules.georss.geometries.Envelope;

/**
 * The type Geos rss factory.
 */
public class GeosRSSFactory {

    private GeosRSSFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create bounding box geo rss module.
     *
     * @param boundingBox the bounding box
     * @return the geo rss module
     */
    public static GeoRSSModule createBoundingBox(BoundingBox boundingBox) {
        GeoRSSModule geoRSSModule = new SimpleModuleImpl();
        geoRSSModule.setGeometry(new Envelope(
                boundingBox.getRightBottomPoint().getLatitude(),
                boundingBox.getLeftTopPoint().getLongitude(),
                boundingBox.getLeftTopPoint().getLatitude(),
                boundingBox.getRightBottomPoint().getLongitude()));
        return geoRSSModule;
    }

}
