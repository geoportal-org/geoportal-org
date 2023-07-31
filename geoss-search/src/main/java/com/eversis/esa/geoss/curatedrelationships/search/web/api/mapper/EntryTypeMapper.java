package com.eversis.esa.geoss.curatedrelationships.search.web.api.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;
import com.eversis.esa.geoss.curatedrelationships.search.web.constants.ResponseConstants;

import org.springframework.stereotype.Component;

/**
 * The type Entry type mapper.
 */
@Component
public class EntryTypeMapper {

    /**
     * Maps type to corresponding hub type name. If provided type does not match any known types, null value is
     * returned.
     *
     * @param entryType entry type to map
     * @return hub type name. Null value in case of null or unknown parameter value.
     */
    public String getHubType(EntryType entryType) {
        if (entryType == null) {
            return null;
        }

        switch (entryType) {
            case DATA:
                return ResponseConstants.DATA_HUB;
            case SERVICE:
                return ResponseConstants.SERVICE_HUB;
            case INFORMATION:
                return ResponseConstants.INFORMATION_HUB;
            default:
                throw new IllegalStateException("Unexpected value: " + entryType);
        }
    }
}
