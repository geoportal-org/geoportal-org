package com.eversis.esa.geoss.curated.extensions.model;

import com.eversis.esa.geoss.curated.common.model.DataSourceModel;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    private String username;

    private List<TransferOptionExtensionModel> transferOptionExtensions = new ArrayList<>();

}
