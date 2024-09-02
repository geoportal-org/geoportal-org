package com.eversis.esa.geoss.contents.parser;

import com.eversis.esa.geoss.contents.domain.Content;
import com.eversis.esa.geoss.contents.parser.model.ContentCsv;
import com.eversis.esa.geoss.contents.parser.model.ContentDataCsv;
import com.eversis.esa.geoss.contents.parser.model.ContentTitleCsv;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for parsing CSV files related to content.
 */
@Slf4j
@Service
public class ContentCsvParser extends CsvParser {

    // CSV file paths
    @Value("classpath:db/init-data/content.csv")
    private Resource contentCsvFile;

    @Value("classpath:db/init-data/content-title.csv")
    private Resource contentTitleCsvFile;

    @Value("classpath:db/init-data/content-data.csv")
    private Resource contentDataCsvFile;

    /**
     * Parses the CSV files and returns a list of Content objects.
     *
     * @param siteId The ID of the site for which the content is to be parsed.
     * @return A list of Content objects.
     */
    public List<Content> getContentsFromCsv(Long siteId) {

        // Parse the title CSV and group by content ID
        Map<Long, Map<Locale, String>> titlesMap = parseCsv(ContentTitleCsv.class, contentTitleCsvFile).stream()
                .collect(Collectors.groupingBy(ContentTitleCsv::getContentId,
                        Collectors.toMap(ContentTitleCsv::getLocale, ContentTitleCsv::getTitle)));

        // Parse the data CSV and group by content ID
        Map<Long, Map<Locale, String>> dataMap = parseCsv(ContentDataCsv.class, contentDataCsvFile).stream()
                .collect(Collectors.groupingBy(ContentDataCsv::getContentId,
                        Collectors.toMap(ContentDataCsv::getLocale, ContentDataCsv::getData)));

        // Parse the content CSV and map to Content objects
        return parseCsv(ContentCsv.class, contentCsvFile).stream().map(contentCsv -> {
            Content content = new Content();
            content.setPublished(contentCsv.isPublished());
            content.setSiteId(siteId);
            content.setTitle(titlesMap.getOrDefault(contentCsv.getId(), new HashMap<>()));
            content.setData(dataMap.getOrDefault(contentCsv.getId(), new HashMap<>()));
            return content;
        }).collect(Collectors.toList());
    }

}
