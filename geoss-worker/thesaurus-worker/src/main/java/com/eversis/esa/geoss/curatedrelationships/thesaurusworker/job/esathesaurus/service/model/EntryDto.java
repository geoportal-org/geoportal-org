package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.esathesaurus.service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Entry dto.
 */
@Data
public class EntryDto {

    private String uri;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<String> type = new ArrayList<>();
    @JsonProperty("skos:definition")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<LabelDto> definition;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<LabelDto> prefLabel;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<UriDto> broader = new ArrayList<>();
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<UriDto> narrower = new ArrayList<>();
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<UriDto> related = new ArrayList<>();

    /**
     * Gets definition value.
     *
     * @return the definition value
     */
    public String getDefinitionValue() {
        if (CollectionUtils.isEmpty(definition)) {
            return "";
        }
        Optional<String> enDefinition = definition.stream()
                .filter(labelDto -> "en".equalsIgnoreCase(labelDto.getLang()))
                .findFirst()
                .map(LabelDto::getValue);

        return enDefinition.orElse(definition.get(0).getValue());
    }

    /**
     * Gets label value.
     *
     * @return the label value
     */
    public String getLabelValue() {
        if (CollectionUtils.isEmpty(prefLabel)) {
            return "";
        }
        Optional<String> prefEnLabel = prefLabel.stream()
                .filter(labelDto -> "en".equalsIgnoreCase(labelDto.getLang()))
                .findFirst()
                .map(LabelDto::getValue);

        return prefEnLabel.orElse(prefLabel.get(0).getValue());
    }

    /**
     * Gets broader uris.
     *
     * @return the broader uris
     */
    public List<String> getBroaderUris() {
        return broader != null ? broader.stream().map(UriDto::getUri).collect(Collectors.toList())
                : Collections.emptyList();
    }

    /**
     * Gets narrower uris.
     *
     * @return the narrower uris
     */
    public List<String> getNarrowerUris() {
        return narrower != null ? narrower.stream().map(UriDto::getUri).collect(Collectors.toList())
                : Collections.emptyList();
    }

    /**
     * Gets related uris.
     *
     * @return the related uris
     */
    public List<String> getRelatedUris() {
        return related != null ? related.stream().map(UriDto::getUri).collect(Collectors.toList())
                : Collections.emptyList();
    }

}
