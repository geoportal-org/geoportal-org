package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.UrlType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto.Workflow;

import lombok.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Workflow transfer option factory.
 */
@Component
class WorkflowTransferOptionFactory {

    private final VlabDabProperties vlabDabProperties;

    /**
     * Instantiates a new Workflow transfer option factory.
     *
     * @param vlabDabProperties the vlab dab configuration
     */
    public WorkflowTransferOptionFactory(VlabDabProperties vlabDabProperties) {
        this.vlabDabProperties = vlabDabProperties;
    }

    /**
     * Create transfer option transfer option.
     *
     * @param workflow the workflow
     * @return the transfer option
     */
    TransferOption createTransferOption(@NonNull Workflow workflow) {
        TransferOption transferOption = new TransferOption();
        transferOption.setName(changeUrlToHttps(workflow.getId())); // Do not change without consulting
        transferOption.setTitle(changeUrlToHttps(workflow.getId()));
        transferOption.setProtocol(Protocol.WORKFLOW);
        transferOption.setEndpoint(createWorkflowEndpoint());
        return transferOption;
    }

    private Endpoint createWorkflowEndpoint() {
        return new Endpoint(vlabDabProperties.getBaseUrl() + vlabDabProperties.getWorkflowsEndpoint(),
                UrlType.HTTP.getValue());
    }

    private String changeUrlToHttps(@NonNull String url) {
        return url.replaceFirst("http", "https");
    }

}
