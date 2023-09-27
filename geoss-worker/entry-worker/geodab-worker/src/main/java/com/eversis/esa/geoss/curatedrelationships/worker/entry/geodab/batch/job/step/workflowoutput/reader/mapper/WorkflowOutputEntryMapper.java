package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.EntryRelation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Organisation;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Source;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.DabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryIdentifiers;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.constants.EntryTypes;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.base.mapper.BaseGeodabMapper;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.WorkflowOutput;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Workflow output entry mapper.
 */
@Component
public class WorkflowOutputEntryMapper extends BaseGeodabMapper {

    private final Organisation geodabOrganisation;
    private final VlabDabProperties vlabDabProperties;
    private final WorkflowOutputTransferOptionFactory transferOptionFactory;

    /**
     * Instantiates a new Workflow output entry mapper.
     *
     * @param dabProperties the dab configuration
     * @param vlabDabProperties the vlab dab configuration
     * @param geoDabProperties the geo dab configuration
     * @param transferOptionFactory the transfer option factory
     */
    @Autowired
    public WorkflowOutputEntryMapper(
            DabProperties dabProperties,
            VlabDabProperties vlabDabProperties,
            GeoDabProperties geoDabProperties,
            WorkflowOutputTransferOptionFactory transferOptionFactory) {
        super(new Source(dabProperties.getSourceTitle(), dabProperties.getSourceCode()),
                dabProperties.getEntryCodePrefix());
        this.geodabOrganisation = new Organisation(geoDabProperties.getOrganisation());
        this.vlabDabProperties = vlabDabProperties;
        this.transferOptionFactory = transferOptionFactory;
    }

    /**
     * Create workflow output entry entry.
     *
     * @param workflowOutput the workflow output
     * @param workflowId the workflow id
     * @return the entry
     */
    public Entry createWorkflowOutputEntry(@NonNull WorkflowOutput workflowOutput, String workflowId) {
        Entry entry = createWorkflowOutputEntry(workflowOutput);

        if (StringUtils.hasLength(workflowId)) {
            EntryRelation workflowWorkflowOutputRelation = createEntryRelation(
                    generateDefaultEntryCode(workflowId),
                    DataSource.GEOSS_CURATED,
                    EntryTypes.WORKFLOW_TYPE,
                    generateEntryCode(vlabDabProperties.getRuns().getEntryCodePrefix(), workflowOutput.getId()),
                    DataSource.GEOSS_CURATED,
                    entry.getType());
            entry.addRelation(workflowWorkflowOutputRelation);
        }

        return entry;
    }

    private Entry createWorkflowOutputEntry(@NonNull WorkflowOutput workflowOutput) {
        Entry entry = createEntry(
                generateEntryCode(vlabDabProperties.getRuns().getEntryCodePrefix(), workflowOutput.getId()),
                workflowOutput.getName(),
                workflowOutput.getDescription(),
                EntryTypes.WORKFLOW_OUTPUT_TYPE
        );
        entry.setKeywords(Stream.of(EntryIdentifiers.WORKFLOW_OUTPUTS).collect(Collectors.toSet()));
        entry.setTags(Stream.of(EntryIdentifiers.WORKFLOW_OUTPUTS.toLowerCase()).collect(Collectors.toSet()));
        entry.setOrganisation(geodabOrganisation);

        transferOptionFactory.createTransferOption(workflowOutput).ifPresent(entry::addTransferOption);

        return entry;
    }

}
