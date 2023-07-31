package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.web.constants.ResponseConstants;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.GeographicMetaDataModule;

import com.rometools.rome.feed.atom.Category;
import com.rometools.rome.feed.module.Module;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements.CATEGORY_LABEL_SERIES;
import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements.CATEGORY_TERM_DATASOURCE;
import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements.CATEGORY_TERM_DISPLAYDATASOURCE;
import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements.CATEGORY_TERM_HLEVEL;
import static com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants.OpensearchResponseElements.CATEGORY_TERM_KEYWORDS;

/**
 * The type Open search entry factory.
 */
@Component
public class OpenSearchEntryFactory {

    /**
     * Create entry com . rometools . rome . feed . atom . entry.
     *
     * @param resourceEntry the resource entry
     * @return the com . rometools . rome . feed . atom . entry
     */
    public com.rometools.rome.feed.atom.Entry createEntry(Entry resourceEntry) {
        final List<Module> modules = new ArrayList<>();

        com.rometools.rome.feed.atom.Entry atomEntry = new com.rometools.rome.feed.atom.Entry();
        atomEntry.setId(resourceEntry.getId());
        atomEntry.setTitle(resourceEntry.getTitle());
        atomEntry.setSummary(ContentFactory.createTextContent(resourceEntry.getSummary()));
        atomEntry.setForeignMarkup(Collections.singletonList(AcquisitionElementFactory.createAcquisitionElement()));

        List<Category> categories = new ArrayList<>();
        categories.add(
                CategoryFactory.createCategory(resourceEntry.getDataSource().getName(), CATEGORY_TERM_DATASOURCE));
        categories.add(CategoryFactory.createCategory(resourceEntry.getDisplayDataSource().getName(),
                CATEGORY_TERM_DISPLAYDATASOURCE));
        resourceEntry.getTypes().forEach(entryType -> categories.add(
                CategoryFactory.createCategory(entryType.getName(), DataSource.GEOSS_CR.getName())));
        resourceEntry.getKeywords().forEach(
                keyword -> categories.add(CategoryFactory.createCategory(keyword.getTitle(), CATEGORY_TERM_KEYWORDS)));
        Optional.ofNullable(resourceEntry.getChildrenTypes()).ifPresent(childrenTypes -> {
            if (childrenTypes.contains(EntryType.DATA.getName())) {
                categories.add(CategoryFactory.createCategory(CATEGORY_LABEL_SERIES, ResponseConstants.DATA_HUB));
            }
            if (childrenTypes.contains(EntryType.SERVICE.getName())) {
                categories.add(CategoryFactory.createCategory(CATEGORY_LABEL_SERIES, ResponseConstants.SERVICE_HUB));
            }
            if (childrenTypes.contains(EntryType.INFORMATION.getName())) {
                categories.add(
                        CategoryFactory.createCategory(CATEGORY_LABEL_SERIES, ResponseConstants.INFORMATION_HUB));
            }
            for (EntryType entryType : resourceEntry.getTypes()) {
                if (childrenTypes.contains(entryType.getName())) {
                    categories.add(CategoryFactory.createCategory(CATEGORY_LABEL_SERIES, CATEGORY_TERM_HLEVEL));
                    break;
                }
            }
        });
        atomEntry.setCategories(categories);

        Optional.ofNullable(resourceEntry.getOrganisation())
                .ifPresent(organisation -> modules.add(ContributorFactory.createContributorModule(organisation)));
        Optional.ofNullable(resourceEntry.getCoverage())
                .ifPresent(boundingBox -> modules.add(GeosRSSFactory.createBoundingBox(boundingBox)));
        Optional.ofNullable(resourceEntry.getLogo())
                .ifPresent(logo -> modules.add(LinkFactory.createAtomLogoLink(logo)));

        GeographicMetaDataModule geographicMetaDataModule = GeographicModuleFactory.createGeographicMetaDataModule(
                resourceEntry.getTransferOptions(), resourceEntry.getOrganisation(), resourceEntry.getCoverage());
        modules.add(geographicMetaDataModule);

        Optional.ofNullable(resourceEntry.getDashboardContents())
                .ifPresent(dashboardContents -> modules.add(DashboardFactory.createDashboardModule(dashboardContents)));

        atomEntry.setModules(modules);
        return atomEntry;
    }

}
