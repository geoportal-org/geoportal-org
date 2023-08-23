package com.eversis.esa.geoss.curated.relations.mapper;

import com.eversis.esa.geoss.curated.relations.domain.RelationDataSources;
import com.eversis.esa.geoss.curated.relations.domain.RelationType;
import com.eversis.esa.geoss.curated.relations.model.DataSourceModel;
import com.eversis.esa.geoss.curated.relations.model.RelationTypeModel;

public class RelationTypeMapper {

    private RelationTypeMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static RelationType mapRelationType(RelationTypeModel relationTypeModel) {
        RelationType dbRelationType = new RelationType(relationTypeModel.getCode());
        dbRelationType.setName(relationTypeModel.getName());
        return dbRelationType;
    }

}




