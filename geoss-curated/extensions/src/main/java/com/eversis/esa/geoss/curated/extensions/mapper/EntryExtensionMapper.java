package com.eversis.esa.geoss.curated.extensions.mapper;

import com.eversis.esa.geoss.curated.common.service.DataSourceService;
import com.eversis.esa.geoss.curated.extensions.domain.EntryExtension;
import com.eversis.esa.geoss.curated.extensions.model.EntryExtensionModel;

import org.springframework.stereotype.Component;

/**
 * The type Entry extension mapper.
 */
@Component
public class EntryExtensionMapper {

    private final DataSourceService dataSourceService;

    /**
     * Instantiates a new Entry extension mapper.
     *
     * @param dataSourceService the data source service
     */
    public EntryExtensionMapper(DataSourceService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    /**
     * Map to entry extension entry extension.
     *
     * @param model the model
     * @return the entry extension
     */
    public EntryExtension mapToEntryExtension(EntryExtensionModel model) {
        return getEntryExtension(model);
    }

    /**
     * Map to entry extension entry extension.
     *
     * @param model the model
     * @param entryExtension the entry extension
     * @return the entry extension
     */
    public EntryExtension mapToEntryExtension(EntryExtensionModel model, EntryExtension entryExtension) {
        return getEntryExtension(model, entryExtension);
    }

    private EntryExtension getEntryExtension(EntryExtensionModel model) {
        if (model == null) {
            return null;
        }
        EntryExtension entryExtension = new EntryExtension();
        entryExtension.setCode(model.getCode());
        entryExtension.setDataSource(dataSourceService.getOrCreateDataSource(model.getDataSource()));
        entryExtension.setTitle(model.getTitle());
        entryExtension.setSummary(model.getSummary());
        entryExtension.setKeywords(model.getKeywords());
        entryExtension.setTags(model.getTags());
        entryExtension.setUserId(0L);
        entryExtension.setUsername(model.getUsername());
        entryExtension.setEmail("");
        entryExtension.setDeleted(0);
        return entryExtension;
    }

    private EntryExtension getEntryExtension(EntryExtensionModel model, EntryExtension entryExtension) {
        if (model == null) {
            return null;
        }
        entryExtension.setCode(model.getCode());
        entryExtension.setDataSource(dataSourceService.getOrCreateDataSource(model.getDataSource()));
        entryExtension.setTitle(model.getTitle());
        entryExtension.setSummary(model.getSummary());
        entryExtension.setKeywords(model.getKeywords());
        entryExtension.setTags(model.getTags());
        entryExtension.setUserId(0L);
        entryExtension.setUsername(model.getUsername());
        entryExtension.setEmail("");
        return entryExtension;
    }
}
