package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryIdentifiers;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryTypes;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.mapper.BaseGeodabMapper;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto.Workflow;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Workflow entry mapper.
 */
@Component
public class WorkflowEntryMapper extends BaseGeodabMapper {

    private final WorkflowTransferOptionFactory transferOptionFactory;

    /**
     * Instantiates a new Workflow entry mapper.
     *
     * @param dabProperties the dab configuration
     * @param transferOptionFactory the transfer option factory
     */
    @Autowired
    public WorkflowEntryMapper(
            DabProperties dabProperties,
            WorkflowTransferOptionFactory transferOptionFactory) {
        super(new Source(dabProperties.getSourceTitle(), dabProperties.getSourceCode()),
                dabProperties.getEntryCodePrefix());
        this.transferOptionFactory = transferOptionFactory;
    }

    /**
     * Create workflow entry entry.
     *
     * @param workflow the workflow
     * @param storylineId the storyline id
     * @return the entry
     */
    public Entry createWorkflowEntry(@NonNull Workflow workflow, @NonNull String storylineId) {
        Entry entry = createWorkflowEntry(workflow);

        EntryRelation storylineWorkflowRelation = createEntryRelationUsingGeneratedCodes(
                storylineId, DataSource.GEOSS_CURATED, EntryTypes.STORYLINE_TYPE,
                workflow.getId(), DataSource.GEOSS_CURATED, entry.getType());
        entry.addRelation(storylineWorkflowRelation);

        return entry;
    }

    /**
     * Create workflow entry entry.
     *
     * @param workflow the workflow
     * @return the entry
     */
    public Entry createWorkflowEntry(@NonNull Workflow workflow) {
        Entry entry = createEntry(
                generateDefaultEntryCode(workflow.getId()),
                workflow.getName(),
                workflow.getDescription(),
                EntryTypes.WORKFLOW_TYPE
        );
        entry.setLogo(workflow.getBpmnUrl());
        entry.setKeywords(Stream.of(EntryIdentifiers.WORKFLOW).collect(Collectors.toSet()));
        entry.setTags(Stream.of(EntryIdentifiers.WORKFLOW.toLowerCase()).collect(Collectors.toSet()));

        Organisation organisation = getWorkflowOrganisation(workflow);
        entry.setOrganisation(organisation);

        TransferOption transferOption = transferOptionFactory.createTransferOption(workflow);
        entry.addTransferOption(transferOption);

        return entry;
    }

    private Organisation getWorkflowOrganisation(@NonNull Workflow workflow) {
        Organisation organisation = new Organisation(workflow.getModelDeveloperOrg());
        organisation.setContact(getOrganisationContact(workflow.getModelDeveloper()));
        organisation.setContactEmail(workflow.getModelDeveloperEmail());
        return organisation;
    }

    private String getOrganisationContact(String modelDeveloper) {
        if (modelDeveloper == null) {
            return null;
        }

        if (modelDeveloper.contains("(Contact: ")) {
            return modelDeveloper.replaceAll("\\(Contact.*?\\).*", "").trim();
        }

        return modelDeveloper;
    }

}
