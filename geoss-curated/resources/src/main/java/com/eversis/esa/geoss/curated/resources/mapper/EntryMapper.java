package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.Entry;
import com.eversis.esa.geoss.curated.resources.model.EntryModel;
import com.eversis.esa.geoss.curated.resources.service.AccessPolicyService;
import com.eversis.esa.geoss.curated.resources.service.DashboardContentsService;
import com.eversis.esa.geoss.curated.resources.service.DataSourcesService;
import com.eversis.esa.geoss.curated.resources.service.DefinitionTypeService;
import com.eversis.esa.geoss.curated.resources.service.OrganisationService;
import com.eversis.esa.geoss.curated.resources.service.SourceService;
import com.eversis.esa.geoss.curated.resources.service.TypeService;
import com.eversis.esa.geoss.curated.resources.service.impl.DataSourcesServiceImpl;
import com.eversis.esa.geoss.curated.resources.service.impl.SourceServiceImpl;
import com.eversis.esa.geoss.curated.resources.util.CodeGeneratorUtil;
import org.springframework.stereotype.Component;

/**
 * The type Entry mapper.
 */
@Component
public class EntryMapper {

    private DataSourcesService dataSourceService;

    private SourceService sourceService;

    private AccessPolicyService accessPolicyService;

    private TypeService typeService;

    private DefinitionTypeService definitionTypeService;

    private OrganisationService organisationService;

    private DashboardContentsService dashboardContentsService;

    /**
     * Instantiates a new Entry mapper.
     *
     * @param dataSourceService the data source service
     * @param sourceService the source service
     * @param accessPolicyService the access policy service
     * @param typeService the type service
     * @param definitionTypeService the definition type service
     * @param organisationService the organisation service
     * @param dashboardContentsService the dashboard contents service
     */
    public EntryMapper(DataSourcesService dataSourceService, SourceService sourceService,
            AccessPolicyService accessPolicyService, TypeService typeService,
            DefinitionTypeService definitionTypeService, OrganisationService organisationService,
            DashboardContentsService dashboardContentsService) {
        this.dataSourceService = dataSourceService;
        this.sourceService = sourceService;
        this.accessPolicyService = accessPolicyService;
        this.typeService = typeService;
        this.definitionTypeService = definitionTypeService;
        this.organisationService = organisationService;
        this.dashboardContentsService = dashboardContentsService;
    }

    /**
     * Map to entry entry.
     *
     * @param model the model
     * @return the entry
     */
    public Entry mapToEntry(EntryModel model) {
        return getEntry(model);
    }

    /**
     * Map to entry entry.
     *
     * @param model the model
     * @param entry the entry
     * @return the entry
     */
    public Entry mapToEntry(EntryModel model, Entry entry) {
        return getEntry(model, entry);
    }

    private Entry getEntry(EntryModel model) {
        if (model == null) {
            return null;
        }
        Entry entry = new Entry();
        entry.setTitle(model.getTitle());
        entry.setSummary(model.getSummary());
        entry.setLogo(model.getLogo());
        entry.setCoverage(model.getCoverage());
        entry.setType(typeService.getOrCreateType(model.getType()));
        entry.setDashboardContents(dashboardContentsService.getOrCreateDashboardContents(model.getDashboardContents()));
        entry.setAccessPolicy(accessPolicyService.getOrCreateAccessPolicy(model.getAccessPolicy()));
        entry.setKeywords(model.getKeywords());
        entry.setTags(model.getTags());
        entry.setCode(CodeGeneratorUtil.generateCode(model.getTitle(), model.getUserId()));
        entry.setOrganisation(organisationService.getOrCreateOrganisation(model.getOrganisation()));
        entry.setSource(sourceService.getOrCreateSource(model.getSource()));
        entry.setDataSources(dataSourceService.getOrCreateDataSource(model.getDataSources()));
        entry.setDisplayDataSources(dataSourceService.getOrCreateDataSource(model.getDisplayDataSources()));
        entry.setDefinitionType(definitionTypeService.getOrCreateDefinitionType(model.getDefinitionType()));
        entry.setScoreWeight(1.0);
        entry.setDeleted(0);
        return entry;
    }

    private Entry getEntry(EntryModel model, Entry entry) {
        if (model == null) {
            return null;
        }
        entry.setTitle(model.getTitle());
        entry.setSummary(model.getSummary());
        entry.setLogo(model.getLogo());
        entry.setCoverage(model.getCoverage());
        entry.setType(typeService.getOrCreateType(model.getType()));
        entry.setDashboardContents(dashboardContentsService.getOrCreateDashboardContents(model.getDashboardContents()));
        entry.setAccessPolicy(accessPolicyService.getOrCreateAccessPolicy(model.getAccessPolicy()));
        entry.setKeywords(model.getKeywords());
        entry.setTags(model.getTags());
        entry.setOrganisation(organisationService.getOrCreateOrganisation(model.getOrganisation()));
        entry.setSource(sourceService.getOrCreateSource(model.getSource()));
        entry.setDataSources(dataSourceService.getOrCreateDataSource(model.getDataSources()));
        entry.setDisplayDataSources(dataSourceService.getOrCreateDataSource(model.getDisplayDataSources()));
        entry.setDefinitionType(definitionTypeService.getOrCreateDefinitionType(model.getDefinitionType()));
        return entry;
    }

}
