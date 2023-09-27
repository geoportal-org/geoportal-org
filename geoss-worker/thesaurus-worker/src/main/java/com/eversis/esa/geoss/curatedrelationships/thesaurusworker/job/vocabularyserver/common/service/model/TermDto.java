package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * The type Term dto.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermDto {

    @XmlElement(name = "term_id")
    private String termId;

    @XmlElement(name = "string")
    private String name;

    @XmlElement(name = "isMetaTerm")
    private boolean isMetaTerm;

}
