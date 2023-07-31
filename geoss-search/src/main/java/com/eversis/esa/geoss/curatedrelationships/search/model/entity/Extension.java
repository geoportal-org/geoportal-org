package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
public class Extension implements Serializable {

    private final String code;
    private final DataSource dataSource;

    private Set<EntryRelation> relations = new HashSet<>();
    private Set<EntryExtension> entryExtensions = new HashSet<>();
    private Set<TransferOptionExtension> transferOptions = new HashSet<>();

}
