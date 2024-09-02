package com.eversis.esa.geoss.contents.parser;

import com.eversis.esa.geoss.contents.domain.Content;
import com.eversis.esa.geoss.contents.domain.Page;
import com.eversis.esa.geoss.contents.parser.model.PageCsv;
import com.eversis.esa.geoss.contents.parser.model.PageDescriptionCsv;
import com.eversis.esa.geoss.contents.parser.model.PageTitleCsv;
import com.eversis.esa.geoss.contents.repository.ContentRepository;

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
 * Service class for parsing CSV files related to page.
 */
@Slf4j
@Service
public class PageCsvParser extends CsvParser {

    // CSV file paths
    @Value("classpath:db/init-data/page.csv")
    private Resource pageCsvFile;

    @Value("classpath:db/init-data/page-title.csv")
    private Resource pageTitleCsvFile;

    @Value("classpath:db/init-data/page-description.csv")
    private Resource pageDescriptionCsvFile;

    private final ContentRepository contentRepository;

    /**
     * Instantiates a new Page csv parser.
     *
     * @param contentRepository the content repository
     */
    public PageCsvParser(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    /**
     * Parses the CSV files and returns a list of Page objects.
     *
     * @param siteId The ID of the site for which the pages are to be parsed.
     * @return A list of Page objects.
     */
    public List<Page> getPagesFromCsv(Long siteId) {

        // Parse the title CSV and group by page ID
        Map<Long, Map<Locale, String>> titlesMap = parseCsv(PageTitleCsv.class, pageTitleCsvFile).stream()
                .collect(Collectors.groupingBy(PageTitleCsv::getPageId,
                        Collectors.toMap(PageTitleCsv::getLocale, PageTitleCsv::getTitle)));

        // Parse the description CSV and group by page ID
        Map<Long, Map<Locale, String>> descriptionMap = parseCsv(PageDescriptionCsv.class, pageDescriptionCsvFile)
                .stream().collect(Collectors.groupingBy(PageDescriptionCsv::getPageId,
                        Collectors.toMap(PageDescriptionCsv::getLocale, PageDescriptionCsv::getDescription)));

        // Parse the page CSV and map to Page objects
        return parseCsv(PageCsv.class, pageCsvFile).stream().map(pageCsv -> {
            Page page = new Page();
            String slug = pageCsv.getSlug();
            Content content;
            switch (slug) {
                case "general-information":
                    content = contentRepository.findByTitleAndSiteId("General information", siteId);
                    page.setContentId(content.getId());
                    break;
                case "terms-conditions":
                    content = contentRepository.findByTitleAndSiteId("Terms & Conditions", siteId);
                    page.setContentId(content.getId());
                    break;
                case "release-notes":
                    content = contentRepository.findByTitleAndSiteId("Release notes", siteId);
                    page.setContentId(content.getId());
                    break;
                case "help-desk":
                    content = contentRepository.findByTitleAndSiteId("Help desk", siteId);
                    page.setContentId(content.getId());
                    break;
                case "documentation":
                    content = contentRepository.findByTitleAndSiteId("Documentation", siteId);
                    page.setContentId(content.getId());
                    break;
                case "tutorials":
                    content = contentRepository.findByTitleAndSiteId("Tutorials", siteId);
                    page.setContentId(content.getId());
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + slug);
            }
            page.setSlug(pageCsv.getSlug());
            page.setPublished(pageCsv.isPublished());
            page.setSiteId(siteId);
            page.setTitle(titlesMap.getOrDefault(pageCsv.getId(), new HashMap<>()));
            page.setDescription(descriptionMap.getOrDefault(pageCsv.getId(), new HashMap<>()));
            return page;
        }).collect(Collectors.toList());
    }

}
