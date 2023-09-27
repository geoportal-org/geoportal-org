package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The type Term response dto.
 */
@XmlRootElement(name = "vocabularyservices")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class TermResponseDto {

    @XmlElement(name = "result")
    private Result result;

    /**
     * Gets terms.
     *
     * @return the terms
     */
    public List<TermDto> getTerms() {
        if (result != null) {
            return result.getTerms() != null ? result.getTerms() : Collections.emptyList();
        }

        return Collections.emptyList();
    }

    /**
     * Gets single term.
     *
     * @return the single term
     */
    public Optional<TermDto> getSingleTerm() {
        if (result != null) {
            return result.getTerms().stream().findFirst();
        }

        return Optional.empty();
    }

    /**
     * The type Result.
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @Getter
    @Setter
    static class Result {

        @XmlElement(name = "term")
        private List<TermDto> terms;

    }

}
