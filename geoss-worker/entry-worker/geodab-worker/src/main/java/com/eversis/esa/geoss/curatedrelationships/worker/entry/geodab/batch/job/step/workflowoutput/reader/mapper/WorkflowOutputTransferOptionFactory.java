package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.UrlType;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.WorkflowOutput;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * The type Workflow output transfer option factory.
 */
@Log4j2
@Component
class WorkflowOutputTransferOptionFactory {

    /**
     * Create transfer option optional.
     *
     * @param workflowOutput the workflow output
     * @return the optional
     */
    Optional<TransferOption> createTransferOption(@NonNull WorkflowOutput workflowOutput) {
        try {
            TransferOption transferOption = new TransferOption();
            transferOption.setName(workflowOutput.getId()); // Do not change without consulting
            transferOption.setTitle(workflowOutput.getId());
            transferOption.setDescription(workflowOutput.getDescription());
            transferOption.setProtocol(createWorkflowOutputProtocol(workflowOutput));
            transferOption.setEndpoint(createWorkflowOutputEndpoint(workflowOutput));
            return Optional.of(transferOption);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create transfer option for output: {}. Reason: {}", workflowOutput.getId(),
                    e.getMessage());
            return Optional.empty();
        }
    }

    private Endpoint createWorkflowOutputEndpoint(@NonNull WorkflowOutput workflowOutput) {
        if ("url".equalsIgnoreCase(workflowOutput.getValueSchema()) && workflowOutput.getValue() instanceof String) {
            return new Endpoint((String) workflowOutput.getValue(), UrlType.HTTP.getValue());
        }
        if ("wms".equalsIgnoreCase(workflowOutput.getValueSchema()) && workflowOutput.getValue() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> protocolValueDetails = (Map<String, String>) workflowOutput.getValue();
            return new Endpoint(protocolValueDetails.get("url"), UrlType.ACCESS_TYPE_COMPLEX.getValue());
        }
        throw new IllegalArgumentException("Unable to create endpoint for workflow output: " + workflowOutput.getId());
    }

    private Protocol createWorkflowOutputProtocol(@NonNull WorkflowOutput workflowOutput) {
        if (!StringUtils.hasLength(workflowOutput.getValueSchema())) {
            return Protocol.DOWNLOAD;
        }
        if ("url".equalsIgnoreCase(workflowOutput.getValueSchema())) {
            return Protocol.DOWNLOAD;
        }
        if ("wms".equalsIgnoreCase(workflowOutput.getValueSchema()) && workflowOutput.getValue() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, String> outputValueDetails = (Map<String, String>) workflowOutput.getValue();
            return new Protocol(outputValueDetails.get("protocol"));
        }
        throw new IllegalArgumentException("Unable to create protocol for workflow output: " + workflowOutput.getId());
    }

}
