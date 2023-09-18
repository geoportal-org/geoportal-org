package com.eversis.esa.geoss.curated.extensions.model;

import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.common.model.DataSourceModel;
import lombok.Data;

/**
 * The type Entry extension model.
 */
@Data
public class EntryExtensionModel {

    @NotNull
    private String code;

    @NotNull
    private DataSourceModel dataSource;

    @NotNull
    private String title;

    private String summary;

    private String keywords;

    private String tags;

    private String userId;

    private List<TransferOptionExtensionModel> transferOptionExtensions = new ArrayList<>();

}
