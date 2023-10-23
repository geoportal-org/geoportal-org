package com.eversis.esa.geoss.curated.relations.mapper;

import com.eversis.esa.geoss.curated.common.service.DataSourceService;
import com.eversis.esa.geoss.curated.common.service.TypeService;
import com.eversis.esa.geoss.curated.relations.domain.EntryRelation;
import com.eversis.esa.geoss.curated.relations.domain.EntryRelationId;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationIdModel;
import com.eversis.esa.geoss.curated.relations.model.EntryRelationModel;
import com.eversis.esa.geoss.curated.relations.service.RelationTypeService;

import org.springframework.stereotype.Component;

/**
 * The type Entry relation mapper.
 */
@Component
public class EntryRelationMapper {

    private final DataSourceService dataSourceService;

    private final RelationTypeService relationTypeService;

    private final TypeService typeService;

    /**
     * Instantiates a new Entry relation mapper.
     *
     * @param dataSourceService the data source service
     * @param relationTypeService the relation type service
     * @param typeService the types service
     */
    public EntryRelationMapper(DataSourceService dataSourceService,
            RelationTypeService relationTypeService, TypeService typeService) {
        this.dataSourceService = dataSourceService;
        this.relationTypeService = relationTypeService;
        this.typeService = typeService;
    }

    /**
     * Map to entry relation entry relation id.
     *
     * @param model the model
     * @return the entry relation id
     */
    public EntryRelationId mapToEntryRelation(EntryRelationIdModel model) {
        if (model == null) {
            return null;
        }
        return new EntryRelationId(model.getSrcId(), model.getSrcDataSourceId(), model.getDestId(),
                model.getDestDataSourceId(), model.getRelationTypeId());
    }

    /**
     * Map to entry relation entry relation.
     *
     * @param model the model
     * @return the entry relation
     */
    public EntryRelation mapToEntryRelation(EntryRelationModel model) {
        if (model == null) {
            return null;
        }
        EntryRelation entryRelation = new EntryRelation();
        EntryRelationId entryRelationId = new EntryRelationId(
                model.getSrcId(),
                dataSourceService.getOrCreateDataSource(model.getSrcDataSource()).getId().intValue(),
                model.getDestId(),
                dataSourceService.getOrCreateDataSource(model.getDestDataSource()).getId().intValue(),
                relationTypeService.getOrCreateRelationType(model.getRelationType()).getId());
        entryRelation.setId(entryRelationId);
        entryRelation.setSrcType(typeService.getOrCreateType(model.getSrcType()));
        entryRelation.setDestType(typeService.getOrCreateType(model.getDestType()));
        entryRelation.setWorkflowInstanceId(0L);
        entryRelation.setIsCustom(0);
        entryRelation.setDeleted(0);
        return entryRelation;
    }

    /**
     * Map to entry relation entry relation.
     *
     * @param model the model
     * @param entryRelation the entry relation
     * @return the entry relation
     */
    public EntryRelation mapToEntryRelation(EntryRelationModel model, EntryRelation entryRelation) {
        if (model == null) {
            return null;
        }
        EntryRelationId entryRelationId = new EntryRelationId(
                model.getSrcId(),
                dataSourceService.getOrCreateDataSource(model.getSrcDataSource()).getId().intValue(),
                model.getDestId(),
                dataSourceService.getOrCreateDataSource(model.getDestDataSource()).getId().intValue(),
                relationTypeService.getOrCreateRelationType(model.getRelationType()).getId());
        entryRelation.setId(entryRelationId);
        entryRelation.setSrcType(typeService.getOrCreateType(model.getSrcType()));
        entryRelation.setDestType(typeService.getOrCreateType(model.getDestType()));
        return entryRelation;
    }

    /**
     * Map to entry relation id entry relation id.
     *
     * @param model the model
     * @return the entry relation id
     */
    public EntryRelationId mapToEntryRelationId(EntryRelationModel model) {
        if (model == null) {
            return null;
        }
        EntryRelationId entryRelationId = new EntryRelationId(
                model.getSrcId(),
                dataSourceService.getOrCreateDataSource(model.getSrcDataSource()).getId().intValue(),
                model.getDestId(),
                dataSourceService.getOrCreateDataSource(model.getDestDataSource()).getId().intValue(),
                relationTypeService.getOrCreateRelationType(model.getRelationType()).getId());
        return entryRelationId;
    }

}
