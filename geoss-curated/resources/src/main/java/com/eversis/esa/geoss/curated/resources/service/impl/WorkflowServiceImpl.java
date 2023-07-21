package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Log4j2
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Validated
public class WorkflowServiceImpl implements WorkflowService {

    @Override
    public void approveUserResource(long userResourceId) {

    }

    @Override
    public void denyUserResource(long userResourceId) {

    }

}
