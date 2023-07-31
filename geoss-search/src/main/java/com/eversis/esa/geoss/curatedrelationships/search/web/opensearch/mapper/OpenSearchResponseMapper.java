package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel.DataModelModule;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel.impl.DataModelModuleImpl;

import com.rometools.modules.opensearch.OpenSearchModule;
import com.rometools.modules.opensearch.impl.OpenSearchModuleImpl;
import com.rometools.rome.feed.atom.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenSearchResponseMapper {

    private static final String FEED_TYPE = "atom_1.0";

    private OpenSearchEntryFactory entryFactory;

    @Autowired
    public OpenSearchResponseMapper(OpenSearchEntryFactory entryFactory) {
        this.entryFactory = entryFactory;
    }

    public Feed createSearchResultsFeed(Page<? extends Entry> searchResults) {
        Feed feed = new Feed(FEED_TYPE);
        int opensearchStartIndex = searchResults.getStartIndex() + 1;
        OpenSearchModule openSearchModule = createOpenSearchModule(
                opensearchStartIndex,
                (int) searchResults.getTotalElements(),
                searchResults.getSize());

        feed.getModules().add(openSearchModule);
        feed.setEntries(getEntries(searchResults.getContent()));
        return feed;
    }

    public Feed createFacetedSearchResultsFeed(FacetedPage<? extends Entry> searchResults) {
        Feed feed = createSearchResultsFeed(searchResults);
        DataModelModule dataModelModule = new DataModelModuleImpl();
        dataModelModule.setFacets(searchResults.getFacets());
        feed.getModules().add(dataModelModule);
        feed.setEntries(getEntries(searchResults.getContent()));
        return feed;
    }

    private OpenSearchModule createOpenSearchModule(int startIndex, int totalResults, int itemsPerPage) {
        OpenSearchModule openSearchModule = new OpenSearchModuleImpl();
        openSearchModule.setStartIndex(startIndex);
        openSearchModule.setTotalResults(totalResults);
        openSearchModule.setItemsPerPage(itemsPerPage);
        return openSearchModule;
    }

    private List<com.rometools.rome.feed.atom.Entry> getEntries(List<? extends Entry> resourceEntries) {
        return resourceEntries.stream()
                .map(entry -> entryFactory.createEntry(entry))
                .collect(Collectors.toList());
    }

}
