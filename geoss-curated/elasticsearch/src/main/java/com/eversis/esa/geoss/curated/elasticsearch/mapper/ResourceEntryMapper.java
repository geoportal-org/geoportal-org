package com.eversis.esa.geoss.curated.elasticsearch.mapper;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.elasticsearch.model.AccessPolicyELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.DashboardContentsELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.OrganisationELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.ResourceEntryELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.SourceELK;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.elasticsearch.core.geo.GeoJsonMultiPoint;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Resource entry mapper.
 */
@Log4j2
@Component
public class ResourceEntryMapper {

    /**
     * Map to document resource entry elk.
     *
     * @param entry the entry
     * @return the resource entry elk
     */
    public ResourceEntryELK mapToDocument(Entry entry) {
        return getResourceEntryELK(entry);
    }

    private ResourceEntryELK getResourceEntryELK(Entry entry) {
        if (entry == null) {
            return null;
        }
        ResourceEntryELK resourceEntryELK = new ResourceEntryELK();
        resourceEntryELK.setId(entry.getCode());
        resourceEntryELK.setTitle(entry.getTitle());
        resourceEntryELK.setSummary(entry.getSummary());
        resourceEntryELK.setLogo(entry.getLogo());

        if ((entry.getCoverage() != null) && (!entry.getCoverage().isEmpty())) {
            resourceEntryELK.setCoverage(
                    GeoJsonMultiPoint.of(convertStringToGeoPointList(entry.getCoverage()))
            );
        }

        resourceEntryELK.setKeywords(parseKeywords(entry.getKeywords()));
        resourceEntryELK.setTags(parseTags(entry.getTags()));
        resourceEntryELK.setCode(entry.getCode());
        resourceEntryELK.setPublishDate(entry.getPublishDate());

        if (entry.getSource() != null) {
            resourceEntryELK.setSource(
                    new SourceELK(
                            entry.getSource().getTerm(),
                            String.valueOf(entry.getSource().getCode())
                    )
            );
        }

        resourceEntryELK.setType(entry.getType().getCode());
        resourceEntryELK.setDataSource(entry.getDataSource().getCode());
        resourceEntryELK.setDisplayDataSource(entry.getDisplayDataSource().getCode());
        resourceEntryELK.setScoreWeight(entry.getScoreWeight());

        if (entry.getOrganisation() != null) {
            resourceEntryELK.setOrganisation(
                    new OrganisationELK(
                            String.valueOf(entry.getOrganisation().getId()),
                            entry.getOrganisation().getTitle(),
                            entry.getOrganisation().getContact(),
                            entry.getOrganisation().getEmail(),
                            entry.getOrganisation().getContactEmail()
                    )
            );
        }

        if (entry.getAccessPolicy() != null) {
            resourceEntryELK.setAccessPolicy(
                    new AccessPolicyELK(
                            entry.getAccessPolicy().getCode()
                    )
            );
        }

        if (entry.getDashboardContents() != null) {
            resourceEntryELK.setDashboardContents(
                    new DashboardContentsELK(
                            String.valueOf(entry.getDashboardContents().getId()),
                            entry.getDashboardContents().getContent()
                    )
            );
        }

        return resourceEntryELK;
    }

    private List<String> parseKeywords(String keywordsString) {
        List<String> keywords = new ArrayList<>();
        if (keywordsString != null && !keywordsString.isEmpty()) {
            String[] keywordArray = keywordsString.split(",");
            for (String keyword : keywordArray) {
                keyword = keyword.trim().toLowerCase();
                keywords.add(keyword);
            }
        }
        return keywords;
    }

    private List<String> parseTags(String tagsString) {
        List<String> tags = new ArrayList<>();
        if (tagsString != null && !tagsString.isEmpty()) {
            String[] tagArray = tagsString.split(",");
            for (String tag : tagArray) {
                tag = tag.trim();
                tags.add(tag);
            }
        }
        return tags;
    }

    private List<Point> convertStringToGeoPointList(String input) {
        List<Point> geoPoints = new ArrayList<>();

        // Remove unnecessary characters from the input string
        input = input.replace("[", "").replace("]", "").replace(" ", "");

        // Split the input into individual coordinate strings
        String[] coordinateStrings = input.split(",");

        // Loop through the coordinate strings and create Point objects
        for (int i = 0; i < coordinateStrings.length; i += 2) {
            double longitude = Double.parseDouble(coordinateStrings[i + 1]);
            double latitude = Double.parseDouble(coordinateStrings[i]);
            geoPoints.add(new Point(latitude, longitude));
        }
        return geoPoints;
    }

}
