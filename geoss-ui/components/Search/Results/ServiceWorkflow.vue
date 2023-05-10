<template>
    <div class="service-workflow">
        <div class="service-workflow__title">
            <span>{{ workflow.name }}</span>
        </div>
        <div class="service-workflow__switcher">
            <button :class="{ active: showDetails }" @click="toggleShowDetails(true)"
                :title="$tc('popupContent.seeThisWorkflow')">
                <img :src="`/svg/see-doc.svg`" :alt="$tc('popupContent.seeThisWorkflow')" />
                <span>{{ $tc('popupContent.seeThisWorkflow') }}</span>
            </button>
            <button :class="{ active: !showDetails }" @click="toggleShowDetails(false)" :title="$tc('popupContent.runs')">
                <img :src="`/svg/run-doc.svg`" :alt="$tc('popupContent.runs')" />
                <span>{{ $tc('popupContent.runs') }}</span>
            </button>
        </div>

        <div class="service-workflow__details" v-show="showDetails">
            <div class="margin-bottom-30">
                <div class="d-flex flex--justify-center flex--align-end flex--wrap">
                    <div ref="bpmn-canvas"></div>
                </div>
                <div class="bpmn-legend d-flex flex--justify-center flex--align-center flex--wrap">
                    <span class="required">{{ $tc('popupContent.requiredFields') }}</span>
                    <span class="optional">{{ $tc('popupContent.optionalFields') }}</span>
                    <span class="expert">{{ $tc('popupContent.expertOptions') }}</span>
                    <span class="output">{{ $tc('popupContent.outputs') }}</span>
                </div>
            </div>

            <div class="service-workflow__run-title">
                <div class="service-workflow__title">
                    <span>{{ $tc('popupContent.workflowInput') }}</span>
                </div>
            </div>

            <div class="service-workflow__resource" v-if="workflowInputs.length">
                <div class="service-workflow__resource-header">
                    <div>{{ $tc('popupContent.inputName') }}</div>
                    <div>{{ $tc('popupContent.chosenResources') }}</div>
                    <div>{{ $tc('popupContent.actions') }}</div>
                </div>
                <div class="service-workflow__resource-data" v-for="input of workflowInputs" :key="input.id"
                    v-show="!isExpert(input.id) || (isExpert(input.id) && showExpertOptions)">
                    <div class="d-flex flex--align-center">{{ input.name }} <span v-if="isRequired(input.id)">*</span></div>
                    <div>
                        <div
                            v-if="workflowResource && workflowResource[input.id] && workflowResource[input.id].length && !workflowResource[input.id][0].dataInput">
                            <div class="d-flex flex--align-center" v-for="resource of workflowResource[input.id]"
                                :key="resource.id">
                                <button @click="removeWorkflowResource(resource, input.id)"
                                    :title="$tc('popupContent.removeResource')">
                                    <i class="cross"></i>
                                </button>
                                <span>{{ resource.title }}</span>
                            </div>
                        </div>
                        <div v-else-if="isRequired(input.id)">
                            <span class="field-required">
                                {{ $tc('popupContent.thisFieldIsRequired') }}
                            </span>
                        </div>
                        <div v-else>
                            <div v-if="getDefaultValue(input.id)">
                                <div v-if="activeEdit(input.id)" class="d-flex flex--align-center">
                                    <input v-if="getValueSchema(input.id) === 'bbox'"
                                        :placeholder="$tc('popupContent.coordinatesWSEN')" :id="input.id"
                                        @change="updateResourceValue(input.id, $event)" />
                                    <input v-else :placeholder="getDefaultValue(input.id, true)" :id="input.id"
                                        @change="updateResourceValue(input.id, $event)" />
                                    <i class="restore-default-value" @click="delEditResource(input.id)">&#10006;</i>
                                </div>
                                <div v-else-if="getValueSchema(input.id) === 'bbox'" class="d-flex flex--align-center">
                                    <span v-html="getDefaultValue(input.id)"></span>
                                </div>
                                <div v-else @click="addEditResource(input.id)"
                                    class="default-value d-flex flex--align-center">
                                    <i class="pencil">&#9998;</i>
                                    <span v-html="getDefaultValue(input.id)"></span>
                                </div>
                            </div>
                            <input v-else />
                        </div>
                    </div>
                    <div class="service-workflow__resource-actions flex--justify-center">
                        <button v-if="getValueSchema(input.id) === 'bbox'" class="service-workflow__select-resources"
                            @click="setBoundingBox(input.id)">{{ $tc('popupContent.setBoundingBox') }}</button>
                        <span v-else-if="isExpert(input.id)" class="expert-option-info">{{
                            $tc('popupContent.additionalExpertOption') }}</span>
                        <button v-else class="service-workflow__select-resources"
                            @click="selectResources(input.id)"><span>{{ $tc('popupContent.selectResources') }}</span> <span
                                class="arrow"></span></button>
                    </div>
                </div>
            </div>

            <div v-if="hasRequiredFields"
                class="service-workflow__required-fields d-flex flex--align-center flex--wrap flex--justify-between">
                <div>
                    * {{ $tc('popupContent.requiredFields') }}
                </div>
                <div>
                    <button v-if="!showExpertOptions && hasExpertOptions" class="service-workflow__expert-options show"
                        @click="toggleExpertOptions()">
                        {{ $tc('popupContent.showExpertOptions') }}
                    </button>
                    <button v-if="showExpertOptions && hasExpertOptions" class="service-workflow__expert-options hide"
                        @click="toggleExpertOptions()">
                        {{ $tc('popupContent.hideExpertOptions') }}
                    </button>
                </div>
            </div>
            <div class="service-workflow__platforms">
                <div class="service-workflow__title">
                    <span>{{ $tc('popupContent.cloudPlatformSelection') }}</span>
                    <small v-if="platformDataLoading !== 0" class="platform-loading">{{
                        $tc('popupContent.fetchingPlatformsData') }}<span>.</span><span>.</span><span>.</span></small>
                </div>
                <div class="service-workflow__platforms-choice">
                    <label v-for="platform of cloudPlatforms" :key="platform" :for="`platform_${platform}`"
                        :class="{ active: platform === selectedPlatform, optimal: platform === optimalPlatform, disabled: !allRequiredFilled }">

                        <small>{{ $tc('popupContent.optimal') }}</small>

                        <input name="platform" v-model="selectedPlatform" :id="`platform_${platform}`" type="radio"
                            :value="platform" />

                        <span>{{ platform }}</span>
                    </label>
                </div>
            </div>
            <div class="service-workflow__run-title">
                <div class="service-workflow__title">
                    <span>{{ $tc('popupContent.runName') }}</span>
                </div>
                <div class="service-workflow__run-title-input">
                    <input type="text" id="run-title" :placeholder="$tc('popupContent.runName')"
                        v-model="workflowRunName" />
                </div>
            </div>
            <div class="d-flex flex--justify-end flex--wrap">
                <button class="service-workflow__run" :class="{ disabled: !allRequiredFilled }"
                    :disabled="!allRequiredFilled" @click="run()">{{ $tc('popupContent.run') }}</button>
            </div>
        </div>
        <div v-show="!showDetails">
            <div class="d-flex flex--justify-between flex--align-center service-workflow__add-run">
                <input placeholder="Name" class="service-workflow__add-run-name" type="text" v-model="addRunName" />
                <input placeholder="Run ID" class="service-workflow__add-run-id" type="text" v-model="addRunId" />
                <button class="service-workflow__add-run-btn" :disabled="!addRunId || !addRunName" @click="addRun()">{{
                    $tc('popupContent.add') }}</button>
            </div>
            <div v-show="!savedRuns || !savedRuns.length">{{ $tc('popupContent.noRunsAvailable') }}</div>
            <div class="service-workflow__saved-runs" v-if="savedRuns">
                <div>
                    <div class="service-workflow__saved-run" v-for="savedRun of savedRuns" :key="savedRun.id">
                        <div class="service-workflow__saved-run-name">{{ savedRun.name }}</div>
                        <div class="service-workflow__saved-run-id">{{ $tc('popupContent.id') }}: {{ savedRun.id }}</div>
                        <div class="service-workflow__saved-run-status">{{ $tc('popupContent.status') }}:
                            {{ savedRun.status }}<span v-if="savedRun.result"> - {{ savedRun.result }}</span></div>
                        <div class="service-workflow__saved-run-button-holder d-flex">
                            <button class="service-workflow__saved-run-btn" :class="{ active: savedRun.showLogs }"
                                @click="toggleLogs(savedRun)" v-if="savedRun.messageList && savedRun.messageList.length">{{
                                    $tc('popupContent.messageLog') }}</button>
                            <button class="service-workflow__saved-run-btn" :class="{ active: savedRun.showOutputs }"
                                @click="toggleOutputs(savedRun)"
                                v-if="savedRun.outputs && savedRun.outputs.length && savedRun.result !== 'FAIL'">{{
                                    $tc('popupContent.outputs') }}</button>
                        </div>
                        <CollapseTransition v-if="savedRun.messageList && savedRun.messageList.length">
                            <div v-show="savedRun.showLogs" class="service-workflow__saved-logs">
                                <div v-for="log of savedRun.messageList" :key="log">{{ log }}</div>
                            </div>
                        </CollapseTransition>
                        <CollapseTransition
                            v-if="savedRun.outputs && savedRun.status === 'COMPLETED' && savedRun.result !== 'FAIL'">
                            <div v-show="savedRun.showOutputs" class="service-workflow__saved-outputs">
                                <div v-for="output of savedRun.outputs" :key="output.id"
                                    class="service-workflow__saved-output">
                                    <div v-if="output.value && output.valueSchema === 'url'">
                                        <a :href="output.value" target="_blank"
                                            class="service-workflow__saved-output-link">{{ output.name }}</a>
                                        <div class="service-workflow__saved-output-link-description">{{ output.description
                                        }}
                                        </div>
                                    </div>
                                    <div v-else-if="output.value && output.value.url" class="d-flex flex--align-center">
                                        <button @click="showOnMap(savedRun.runId, output)"
                                            :title="$tc('dabResult.showOnMap')">
                                            <i class="icomoon-show-on-map"></i>
                                        </button>
                                        <span>{{ output.name }}</span>
                                    </div>
                                    <div v-else class="d-flex flex--align-center">
                                        <button disabled="true" :title="$tc('dabResult.showOnMap')">
                                            <i class="icomoon-show-on-map"></i>
                                        </button>
                                        <span>{{ output.name }}</span>
                                    </div>
                                </div>
                            </div>
                        </CollapseTransition>
                    </div>
                </div>
                <Pagination v-if="savedRuns && savedRuns.length" :start-index="runsStartIndex"
                    :results-per-page="runsResultsPerPage" :total="runsTotal"
                    @on-start-index-change="onRunsStartIndexChange($event)" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
// import { runsTest } from '@/data/saved-runs-test';
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';

import Pagination from '@/components/Pagination.vue';
// import BpmnViewer from 'bpmn-js';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import PopupCloseService from '@/services/popup-close.service';
import { SearchGetters } from '@/store/search/search-getters';
import { SearchActions } from '@/store/search/search-actions';
import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions';
import { FacetedFiltersActions } from '@/store/facetedFilters/faceted-filters-actions';
import { GranulaFiltersActions } from '@/store/granulaFilters/granula-filters-actions';
import { IrisFiltersActions } from '@/store/irisFilters/iris-filters-actions';
import { DataSources } from '@/interfaces/DataSources';
import UtilsService from '@/services/utils.service';
import { PopupActions } from '@/store/popup/popup-actions';
import NotificationService from '@/services/notification.service';
import { MapActions } from '@/store/map/map-actions';
import { LayerTypes } from '@/interfaces/LayerTypes';
import { Timers } from '@/data/timers';
import LogService from '@/services/log.service';
import to from '@/utils/to';
import { UserGetters } from '@/store/user/user-getters';
import LayersUtils from '@/services/map/layer-utils';
import ErrorPopup from '@/components/ErrorPopup.vue';
import CollapseTransition from '@/plugins/CollapseTransition';

@Component({
    components: {
        Pagination,
        CollapseTransition
    }
})
export default class ServiceWorkflowComponent extends Vue {
    @Prop({ required: true, type: Object }) public workflow!: any;
    @Prop({ required: true, type: String }) public workflowUrl!: string;
    @Prop({ required: false, type: String }) public urlToResource!: string;

    public showDetails = true;
    public selectedInputName = '';
    public savedRuns = null;

    public addRunId = '';
    public addRunName = '';

    public runsResultsPerPage = 3;
    public runsStartIndex = 0;
    public runsTotal = 0;

    public showExpertOptions = false;
    public currentlyBeingEdited = [];
    public allRequiredFilled = false;

    public uniquePlatformSettings = this.$store.getters[SearchGetters.workflowPredefined].uniquePlatformSettings;
    public defaultInputValues = this.$store.getters[SearchGetters.workflowPredefined].defaultInputValues;
    public workflowViews = this.$store.getters[SearchGetters.workflowPredefined].workflowViews;

    public workflowSavedData = {};

    public timeStart = 0;
    public timeEnd = 0;

    get workflowRunName() {
        return this.$store.getters[SearchGetters.workflowRunName];
    }

    get cloudPlatforms() {
        return this.$store.getters[SearchGetters.workflowCloudPlatforms];
    }

    get selectedPlatform() {
        return this.$store.getters[SearchGetters.workflowSelectedPlatform];
    }

    get optimalPlatform() {
        return this.$store.getters[SearchGetters.workflowOptimalPlatform];
    }

    get isSignedIn() {
        return this.$store.getters[UserGetters.isSignedIn];
    }

    get hasRequiredFields() {
        return this.workflowRequiredInputs.length;
    }

    get hasExpertOptions() {
        return this.workflowExpertInputs.length;
    }

    get inputId() {
        return this.$store.getters[SearchGetters.workflowInputId];
    }

    get platformData() {
        return this.$store.getters[SearchGetters.workflowPlatformData];
    }

    get platformDataLoading() {
        return this.$store.getters[SearchGetters.workflowPlatformDataLoading];
    }

    get workflowCoordinates() {
        return this.$store.getters[SearchGetters.workflowCoordinates];
    }

    get workflowInputs() {
        return this.workflow.inputs;
    }

    get workflowRegularInputs() {
        return this.workflow.inputs.filter(input => input.obligation);
    }

    get workflowRequiredInputs() {
        return this.workflow.inputs.filter(input => input.obligation && !input.defaultValue);
    }

    get workflowExpertInputs() {
        return this.workflow.inputs.filter(input => !input.obligation);
    }

    get filledInputs() {
        return this.workflow.inputs.filter(input => this.workflowResource[input.id] && this.workflowResource[input.id].length);
    }

    get parentRef() {
        return this.$store.getters[SearchGetters.parentRef];
    }

    set workflowRunName(value: string) {
        this.saveWorkflowData('workflowRunName', value);
        this.$store.dispatch(SearchActions.setWorkflowRunName, value);
    }

    set inputId(value: string) {
        this.$store.dispatch(SearchActions.setWorkflowInputId, value);
    }

    set cloudPlatforms(value: any) {
        this.saveWorkflowData('workflowCloudPlatforms', value);
        this.$store.dispatch(SearchActions.setWorkflowCloudPlatforms, value);
    }

    set selectedPlatform(value: string) {
        this.saveWorkflowData('workflowSelectedPlatform', value);
        this.$store.dispatch(SearchActions.setWorkflowSelectedPlatform, value);
    }

    set optimalPlatform(value: string) {
        this.$store.dispatch(SearchActions.setWorkflowOptimalPlatform, value);
    }

    get workflowResource() {
        return this.$store.getters[SearchGetters.workflowResource];
    }

    get workflowDispatched() {
        return this.$store.getters[SearchGetters.workflow];
    }

    public checkRequiredFilled() {
        let validation = true;
        for (const input of this.workflowRequiredInputs) {
            if (this.isRequired(input.id) && !this.isFilled(input.id)) {
                validation = false;
                break;
            }
        }
        this.allRequiredFilled = validation;
    }

    public getValueSchema(id) {
        return this.workflowInputs.find(input => input.id === id).valueSchema;
    }

    public getDefaultValue(id, placeholder?: boolean) {
        if (placeholder) {
            return this.workflowInputs.find(input => input.id === id).defaultValue;
        } else {
            if (this.getValueSchema(id) === 'bbox') {
                if (this.workflowCoordinates && this.workflowCoordinates.W && this.workflowCoordinates.S && this.workflowCoordinates.E && this.workflowCoordinates.N) {
                    return `W: ${this.workflowCoordinates.W}<br />S: ${this.workflowCoordinates.S}<br />E: ${this.workflowCoordinates.E}<br />N: ${this.workflowCoordinates.N}`;
                } else {
                    return this.$tc('popupContent.worldwide');
                }
            } else if (this.workflowResource && this.workflowResource[id] && this.workflowResource[id].length) {
                return UtilsService.getPropByString(this.workflowResource[id][0], 'dm:workflowinput.value');
            } else {
                return this.workflowInputs.find(input => input.id === id).defaultValue;
            }
        }
    }

    public isFilled(id) {
        return this.workflowResource && this.workflowResource[id] && this.workflowResource[id].length ? true : false;
    }

    public isRequired(id) {
        return this.workflowRequiredInputs.map(input => input.id).includes(id) && this.getValueSchema(id) !== 'bbox';
    }

    public isExpert(id) {
        return this.workflowExpertInputs.map(input => input.id).includes(id);
    }

    public addEditResource(id) {
        this.currentlyBeingEdited.push(id);
    }

    public delEditResource(id) {
        this.currentlyBeingEdited = this.currentlyBeingEdited.filter(item => item !== id);
        this.updateResourceValue(id, null);
    }

    public activeEdit(id) {
        return this.currentlyBeingEdited.includes(id);
    }

    public setBoundingBox(id) {
        this.inputId = id;
        let workflowResource = JSON.parse(JSON.stringify(this.workflowResource));
        if (!this.workflowResource) {
            workflowResource = {
                [this.inputId]: []
            };
        } else if (!this.workflowResource[this.inputId]) {
            workflowResource = {
                ...this.workflowResource,
                [this.inputId]: []
            };
        }

        this.$store.dispatch(SearchActions.setWorkflowResource, workflowResource);

        UtilsService.rememberState();

        const input = this.workflow.inputs.find(input => input.id === this.inputId);
        this.$store.dispatch(SearchActions.setWorkflowInputType, input.inputType);
        this.$store.dispatch(SearchActions.setWorkflow, this.workflow);

        this.$store.dispatch(GeneralFiltersActions.reset);
        this.$store.dispatch(FacetedFiltersActions.reset);
        this.$store.dispatch(GranulaFiltersActions.reset);
        this.$store.dispatch(IrisFiltersActions.reset);
        this.$store.dispatch(SearchActions.setDataSource, { value: DataSources.DAB });
        this.$store.dispatch(SearchActions.setParentRefs, null);

        this.close();

        this.$store.dispatch(SearchActions.setDabResults, null);
        this.$store.dispatch(GeneralFiltersActions.setInChangeProcess, false);
        this.$store.dispatch(GeneralFiltersActions.setWorkflowMapDraw, true);
    }

    public async getSavedRuns() {
        // this.isSignedIn
        const [, data] = await to(GeossSearchApiService.getSavedRuns(this.workflowUrl, this.isSignedIn, this.runsStartIndex, this.runsResultsPerPage));

        // test data
        // data = runsTest;

        if (data) {
            this.savedRuns = data.items;
        }
        return data;
    }

    public async onRunsStartIndexChange(startIndex: number) {
        this.runsStartIndex = startIndex;
        this.getSavedRuns();
    }

    public close() {
        PopupCloseService.closePopup('workflow');
    }

    public toggleExpertOptions(run: any) {
        this.showExpertOptions = !this.showExpertOptions;
    }

    public toggleLogs(run: any) {
        run.showLogs = !run.showLogs;
        run.showOutputs = false;
    }

    public toggleOutputs(run: any) {
        run.showOutputs = !run.showOutputs;
        run.showLogs = false;
    }

    public async toggleShowDetails(value: boolean) {
        if (this.showDetails !== value) {
            this.showDetails = value;
            if (!this.showDetails) {
                if (!this.savedRuns) {
                    const data = await this.getSavedRuns();
                    if (data) {
                        this.runsTotal = data.totalCount;
                    }
                }
            }
        }
    }

    public fetchPlatformData(workflow) {
        if (!workflow && this.workflowResource[this.inputId] && this.workflowResource[this.inputId][0]) {
            const uniqueId = `${this.inputId}_${this.getSceneId(this.inputId)}`;
            if (!this.platformData[uniqueId]) {
                const platforms = {
                    ...this.platformData,
                    [uniqueId]: {
                        init: true
                    }
                };
                this.$store.dispatch(SearchActions.setWorkflowPlatformData, platforms);
                this.fetchPlatform(this.inputId, uniqueId);
            }
        }
    }

    public fetchPlatform(inputId, uniqueId) {
        const sceneId = this.getSceneId(inputId);
        GeossSearchApiService.fetchCloudPlatform(inputId, sceneId, uniqueId).then(() => {
            this.markOptimalPlatform();
        });
    }

    public markOptimalPlatform() {
        const allPlatforms = [];
        const platformRating = {};

        // gather all arrays in one
        allPlatforms.push(this.cloudPlatforms);
        for (const i in this.workflowResource) {
            if (this.workflowResource[i][0] && this.workflowResource[i][0].platforms && this.workflowResource[i][0].platforms.length) {
                const input = this.workflowResource[i][0];
                const lowercaseArray = input.platforms.join('|').toLowerCase().split('|').filter(item => item !== 'dhus'); // dhus cannot be choosen
                allPlatforms.push(lowercaseArray);
            }
        }

        // make it correct order of recent array
        const recentArray = allPlatforms[allPlatforms.length - 1];
        const missingPlatforms = this.cloudPlatforms.filter(item => !recentArray.includes(item));
        this.cloudPlatforms = recentArray.concat(missingPlatforms);

        // get ratings for all fetched platforms
        for (let i = 1; i < allPlatforms.length; i++) {
            for (const platform of allPlatforms[i]) {
                platformRating[platform] = platformRating[platform] ? platformRating[platform] + 1 : 1;
            }
        }

        // get first most rated platform
        const maxRating = Object.keys(platformRating).length ? Object.keys(platformRating).reduce((a, b) => platformRating[a] > platformRating[b] ? a : b) : 'aws';
        for (const platform of recentArray) {
            if (platformRating[platform] === platformRating[maxRating]) {
                this.optimalPlatform = platform;
                this.selectedPlatform = platform;
                break;
            }
        }
    }

    public selectResources(inputId) {
        this.inputId = inputId;
        let workflowResource = JSON.parse(JSON.stringify(this.workflowResource));
        const targetSource = DataSources.DAB;
        if (!this.workflowResource) {
            workflowResource = {
                [this.inputId]: []
            };
        } else if (!this.workflowResource[this.inputId]) {
            workflowResource = {
                ...this.workflowResource,
                [this.inputId]: []
            };
        }
        this.$store.dispatch(SearchActions.setWorkflowResource, workflowResource);

        UtilsService.rememberState();
        const input = this.workflow.inputs.find(input => input.id === this.inputId);
        this.$store.dispatch(SearchActions.setWorkflowInputType, input.inputType);
        this.$store.dispatch(SearchActions.setWorkflow, this.workflow);

        if (this.uniquePlatformSettings.some(e => e.fieldModel === this.getValueSchema(this.inputId))) {
            const uniqueSettings = this.uniquePlatformSettings.filter(predefined => {
                return predefined.fieldModel === this.getValueSchema(this.inputId);
            })[0].settings;
            this.$store.dispatch(SearchActions.setWorkflowParents, uniqueSettings.parents);
            this.$store.dispatch(SearchActions.setWorkflowProdType, uniqueSettings.prodType);
        }

        if (this.workflowViews.some(e => e.workflowId === this.workflow.id)) {
            const workflowView = this.workflowViews.filter(predefined => {
                return predefined.workflowId === this.workflow.id;
            })[0].workflowView;
            if (workflowView.sources && workflowView.sources !== '') {
                this.$store.dispatch(SearchActions.setWorkflowSources, workflowView.sources);
            }
        }

        this.$store.dispatch(GeneralFiltersActions.reset);
        this.$store.dispatch(FacetedFiltersActions.reset);
        this.$store.dispatch(GranulaFiltersActions.reset);
        this.$store.dispatch(IrisFiltersActions.reset);

        if (this.workflowCoordinates) {
            this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, this.workflowCoordinates);
            this.$store.dispatch(GeneralFiltersActions.setLocationType, 'coordinates');
        } else if (this.parentRef && this.parentRef.entry) {
            const entry = this.parentRef.entry;
            const bbox = entry['georss:box'] ? entry['georss:box'].split(' ') : null;
            if (bbox && bbox.length) {
                const W = bbox[1] * 1;
                const S = bbox[0] * 1;
                const E = bbox[3] * 1;
                const N = bbox[2] * 1;
                this.$store.dispatch(SearchActions.setWorkflowCoordinates, { W, S, E, N });
                this.$store.dispatch(GeneralFiltersActions.setSelectedAreaCoordinates, { W, S, E, N });
                this.$store.dispatch(GeneralFiltersActions.setLocationType, 'coordinates');
            }
        }

        this.$store.dispatch(SearchActions.setDataSource, { value: DataSources.DAB });
        this.$store.dispatch(SearchActions.setParentRefs, null);

        this.close();
        this.$store.dispatch(SearchActions.getResults, { noPushToHistory: true, targetSource, theSameSource: true }).finally(() => {
            this.$store.dispatch(GeneralFiltersActions.setInChangeProcess, false);
        });
    }

    public updateResourceValue(inputId, event) {
        let workflowResource = JSON.parse(JSON.stringify(this.workflowResource));
        if (!this.workflowResource) {
            workflowResource = {
                [inputId]: []
            };
        } else if (!this.workflowResource[inputId]) {
            workflowResource = {
                ...this.workflowResource,
                [inputId]: []
            };
        }
        if (event) {
            workflowResource[inputId] = [{
                'dataInput': true,
                'dm:workflowinput': {
                    value: event.target.value
                }
            }];
        } else {
            delete workflowResource[inputId];
        }
        this.$store.dispatch(SearchActions.setWorkflowResource, workflowResource);
    }

    public getSceneId(inputId) {
        // Temporary VLAB Sentinel2-only Support
        const satProdIdArray = UtilsService.getPropByString(this.workflowResource[inputId][0], 'title').split('(');
        return satProdIdArray[satProdIdArray.length - 1].slice(0, -1);
    }

    public getRunName() {
        return this.workflowRunName !== '' ? this.workflowRunName : `${new Date().toLocaleString('en-GB').replace(/,/g, '')} ${this.workflow.name}`;
    }

    public saveWorkflowData(key, value) {
        let serviceWorkflowObject = {};
        if (sessionStorage.getItem('SERVICE_WORKFLOW')) {
            serviceWorkflowObject = JSON.parse(sessionStorage.getItem('SERVICE_WORKFLOW'));
        }
        if (!serviceWorkflowObject[this.workflowUrl]) {
            serviceWorkflowObject[this.workflowUrl] = {};
        }
        serviceWorkflowObject[this.workflowUrl][key] = value;
        sessionStorage.setItem('SERVICE_WORKFLOW', JSON.stringify(serviceWorkflowObject));
    }

    public getWorkflowSavedData() {
        this.workflowSavedData = {
            workflowResource: null,
            workflowCoordinates: null,
            workflowCloudPlatforms: null,
            workflowSelectedPlatform: this.cloudPlatforms[0],
            workflowRunName: ''
        };
        const resultToHighlight = this.$store.getters[SearchGetters.highlightResult] || true;
        if (resultToHighlight) {
            if (sessionStorage.getItem('SERVICE_WORKFLOW')) {
                const serviceWorkflowObject = JSON.parse(sessionStorage.getItem('SERVICE_WORKFLOW'));
                if (serviceWorkflowObject[this.workflowUrl]) {
                    this.workflowSavedData = serviceWorkflowObject[this.workflowUrl];
                }
            }
            if (Object.keys(this.workflowSavedData).length && this.workflowSavedData.constructor === Object && resultToHighlight) {
                for (const key in this.workflowSavedData) {
                    if (this.workflowSavedData[key]) {
                        this.$store.dispatch(SearchActions[`set${key.charAt(0).toUpperCase() + key.slice(1)}`], this.workflowSavedData[key]);
                    }
                }
            } else {
                this.$store.dispatch(SearchActions.setWorkflowResource, null);
            }
        } else {
            this.$store.dispatch(SearchActions.setWorkflowResource, null);
        }
        this.checkRequiredFilled();
    }

    public prepareWorkflowInputs() {
        const inputs = JSON.parse(JSON.stringify(this.workflow.inputs));
        for (const input of inputs) {
            if (input.inputType === 'individual' && this.workflowResource && this.workflowResource[input.id]) {
                // unique for EODESM
                if (this.getValueSchema(input.id) === 'sat_product') {
                    input.value = this.getSceneId(input.id);
                } else if (this.getValueSchema(input.id) === 'bbox') {
                    if (this.workflowCoordinates) {
                        input.value = `${this.workflowCoordinates.W},${this.workflowCoordinates.S},${this.workflowCoordinates.E},${this.workflowCoordinates.N}`;
                    } else {
                        input.value = '';
                    }
                    // unique for Trends.Earth
                } else if (this.workflow.id === 'http://eu.floraresearch.essi.core/workflow/autogenerated-1556784738669-process') {
                    const selectedValue = UtilsService.getPropByString(this.workflowResource[input.id][0], 'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine.gmd:CI_OnlineResource.gmd:linkage.gmd:URL');
                    if (selectedValue) {
                        input.value = selectedValue;
                    } else {
                        input.value = UtilsService.getPropByString(this.workflowResource[input.id][0], 'dm:workflowinput.value'); // defaults
                    }
                } else {
                    input.value = UtilsService.getPropByString(this.workflowResource[input.id][0], 'dm:workflowinput.value');
                }
            } else if (input.inputType === 'array' && this.workflowResource && this.workflowResource[input.id] && this.workflowResource[input.id].length) {
                for (const resource of this.workflowResource[input.id]) {
                    if (!input.valueArray) {
                        input.valueArray = [];
                    }
                    let key = UtilsService.getPropByString(resource, 'dm:workflowinput.valuekey');
                    if (key && typeof key === 'string') {
                        key += '=';
                    } else {
                        key = '';
                    }
                    // unique for EODESM
                    if (this.getValueSchema(input.id) === 'sat_product') {
                        input.valueArray.push(`${key}${this.getSceneId(input.id)}`);
                    } else if (this.getValueSchema(input.id) === 'bbox') {
                        if (this.workflowCoordinates) {
                            input.valueArray.push(`${this.workflowCoordinates.W},${this.workflowCoordinates.S},${this.workflowCoordinates.E},${this.workflowCoordinates.N}`);
                        } else {
                            input.valueArray.push('');
                        }
                        // unique for Trends.Earth
                    } else if (this.workflow.id === 'http://eu.floraresearch.essi.core/workflow/autogenerated-1556784738669-process') {
                        const selectedValue = UtilsService.getPropByString(this.workflowResource[input.id][0], 'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine.gmd:CI_OnlineResource.gmd:linkage.gmd:URL');
                        if (selectedValue) {
                            input.valueArray.push(`${key}${selectedValue}`);
                        } else {
                            input.valueArray.push(`${key}${UtilsService.getPropByString(resource, 'dm:workflowinput.value')}`); // defaults
                        }
                    } else {
                        input.valueArray.push(`${key}${UtilsService.getPropByString(resource, 'dm:workflowinput.value')}`);
                    }
                }
            }
        }
        return inputs;
    }

    public async run() {
        if (!this.isSignedIn) {
            const fullLoginUrl = `/c/portal/login?redirect=${this.urlToResource}`;
            const message = `${this.$tc('popupContent.mustBeLoggedIn1')}<a href="${fullLoginUrl}">${this.$tc('popupContent.mustBeLoggedIn2')}</a> ${this.$tc('popupContent.mustBeLoggedIn3')}`;

            return NotificationService.show(
                `${this.$tc('general.info')}`,
                message,
                100000,
                'must-be-logged-in',
                9999,
                'info'
            );
        }

        this.timeEnd = performance.now();
        const workflowDuration = ((this.timeEnd - this.timeStart) / 1000).toFixed();
        this.timeStart = performance.now();
        LogService.logRecommendationData('Search result', 'Workflow - Run', 'internal', { workflowDuration });
        const inputs = this.prepareWorkflowInputs();

        let workflowFinalUrl = this.workflowUrl;
        if (this.selectedPlatform !== '' && this.selectedPlatform !== 'aws') {
            workflowFinalUrl = workflowFinalUrl.replace('/aws/', `/${this.selectedPlatform}/`);
        }
        const [err, data] = await to(GeossSearchApiService.runWorkflow(`${workflowFinalUrl}/run`, {
            inputs,
            name: this.getRunName()
        }));
        if (!err) {
            const [, result] = await to(GeossSearchApiService.addSavedRun(this.getRunName(), this.workflow, data.runid));
            if (result) {
                NotificationService.show(
                    `${this.$tc('popupTitles.savedRuns')}`,
                    `${this.$tc('popupContent.saveRunSuccess')}`,
                    10000,
                    null,
                    9999,
                    'success'
                );
                LogService.logElementClick(null, null, null, null, 'RunId saved in My Workspace', null, null, this.getRunName());
            } else {
                NotificationService.show(
                    `${this.$tc('popupTitles.savedRuns')}`,
                    `${this.$tc('popupContent.saveFail')}`,
                    10000,
                    null,
                    9999,
                    'error'
                );
                LogService.logElementClick(null, null, null, null, 'RunId removed from My Workspace', null, null, this.getRunName());
            }
        } else {
            const message = UtilsService.getPropByString(err, 'response.data.message') || this.$tc('popupContent.serverResponseTimeout');
            const props = {
                title: this.$tc('general.error'),
                subtitle: message ? message : err
            };
            return this.$store.dispatch(PopupActions.openPopup, { contentId: 'error', component: ErrorPopup, props });
        }
    }

    public async showOnMap(runId, output) {
        const { url, name, protocol, legend } = output.value;
        const [err, coordinate] = await to(GeossSearchApiService.getRunCoordinates(runId));

        this.close();

        let version = '1.1.1';
        if (protocol) {
            if (protocol.indexOf('WebMapService') > -1) {
                const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(protocol);
                if (match) {
                    version = match[0];
                }
            }
        }

        const layerUrl = `${url}VERSION=${version}&LAYERS=${name}&TILED=true&`;
        const layer = LayersUtils.createWMS(name, layerUrl);

        this.$store.dispatch(MapActions.addLayer, {
            layer,
            id: name,
            type: LayerTypes.CUSTOM,
            coordinate,
            title: output.name,
            legend: {
                type: 'img',
                data: legend
            }
        });

        this.$store.dispatch(MapActions.changeLayerVisibility, { id: LayerTypes.BOUNDING, value: false });
        this.$store.dispatch(MapActions.setShowFull, true);

        setTimeout(() => {
            this.$store.dispatch(MapActions.zoomInLayer, name);
        }, (Timers.hideSearchContainer > Timers.closePopup ? Timers.hideSearchContainer : Timers.closePopup));
    }

    public async addRun() {
        const run: any = {};
        const [err, { status, messageList, result }] = await to(GeossSearchApiService.getRunStatus(this.workflowUrl, this.addRunId));
        if (!err) {
            GeossSearchApiService.addSavedRun(this.workflowRunName, this.workflow, this.addRunId).then(() => {
                run.status = status;
                run.messageList = messageList;
                run.result = result;
                run.showLogs = false;
                if (status === 'COMPLETED' || status === 'SUCCESS') {
                    return GeossSearchApiService.getRunStatus(this.workflowUrl, this.addRunId).then(({ outputs }) => {
                        run.outputs = outputs;
                        run.showOutputs = false;
                    });
                }
            });
        } else {
            NotificationService.show(
                `${this.$tc('popupTitles.savedRuns')}`,
                `Analysis with run ID ${this.addRunId} does not exist.`,
                10000,
                null,
                9999,
                'error'
            );
        }
    }

    public removeWorkflowResource(resource, inputId) {
        const workflowResource = this.workflowResource;
        workflowResource[inputId] = workflowResource[inputId].filter(worflowResource => worflowResource !== resource);
        this.$store.dispatch(SearchActions.setWorkflowResource, workflowResource);
    }

    public bpmnStyling() {
        let inputStyles = '';
        for (const input of this.workflowRequiredInputs) {
            if (this.isRequired(input.id)) {
                inputStyles += `.bjs-container [data-element-id="${input.id}-ref"].djs-shape path {stroke: #E31E24 !important;}`;
            } else if (this.getValueSchema(input.id) === 'bbox') {
                inputStyles += `.bjs-container [data-element-id="${input.id}-ref"].djs-shape path {stroke: #e8b012 !important;}`;
            }
        }
        const styles = `
			.bjs-container [data-element-id].djs-shape path {stroke: #cccccc !important;}
			.bjs-container [data-element-id^="autogenerated"].djs-shape path {stroke: black !important;}
			.bjs-container [data-element-id^="DataObject"].djs-shape path {stroke: black !important;}
			${inputStyles}
		`;
        const css = document.createElement('style');
        css.innerHTML = styles;
        document.querySelector('head').appendChild(css);
    }

    public setDefaultInputValues() {
        if (this.defaultInputValues.some(e => e.workflowId === this.workflow.id)) {
            const defaultValues = this.defaultInputValues.filter(predefined => {
                return predefined.workflowId === this.workflow.id;
            })[0].defaultValues;
            let workflowResource = JSON.parse(JSON.stringify(this.workflowResource));
            if (!workflowResource) {
                workflowResource = {};
            }
            for (const input in defaultValues) {
                if (!workflowResource[input]) {
                    workflowResource[input] = [{
                        'id': defaultValues[input],
                        'title': this.$tc('popupContent.default'),
                        'dm:workflowinput': {
                            value: defaultValues[input]
                        }
                    }];
                }
            }
            this.$store.dispatch(SearchActions.setWorkflowResource, workflowResource);
        }
    }

    private async mounted() {
        await this.$nextTick();
        this.timeStart = performance.now();
        // const viewer = new BpmnViewer({ container: this.$refs['bpmn-canvas'], width: this.$el.offsetWidth, height: 330 });
        const addTimestamp = true;
        const [, xml] = await to(GeossSearchApiService.getBpmn(this.workflow.bpmn_url, addTimestamp));
        // if (xml) {
        //     viewer.importXML(xml, err => {
        //         if (!err) {
        //             viewer.get('canvas').zoom('fit-viewport');
        //         }
        //     });
        //     this.bpmnStyling();
        // }

        if (this.inputId) {
            this.selectedInputName = this.workflow.inputs.find(input => input.id === this.inputId).name;
        }

        Object.freeze(this.selectedInputName);
        this.setDefaultInputValues();
        this.getWorkflowSavedData();
    }


    @Watch('workflowResource', { deep: true })
    private onWorkflowResource() {
        this.saveWorkflowData('workflowResource', this.workflowResource);
        this.checkRequiredFilled();
    }

    @Watch('workflowDispatched')
    private onWorkflowDispatch(val) {
        this.fetchPlatformData(val);
    }
}
</script>

<style lang="scss">
.popup__wrapper.workflow-popup .popup__content {
    max-height: 560px;
    min-height: 560px;
}

.service-workflow {
    padding: 15px;
    padding-bottom: 45px;

    &__title {
        margin: 10px 0 20px;
        position: relative;
        display: flex;
        align-items: center;

        span {
            font-size: 20px;
            font-weight: bold;
            color: $black;
            text-transform: uppercase;
        }

        .cross {
            position: absolute;
            right: 0px;
            top: 0;

            &:before,
            &:after {
                background-color: $blue;
            }
        }
    }

    &__title:nth-child(n+2) {
        span {
            font-size: 18px;
        }
    }

    &__run-title {
        margin-bottom: 20px;

        input {
            width: 100%;
            height: 40px;
            padding: 10px;
            border: 1px solid #e0e0e0;
        }
    }

    &__switcher {
        display: flex;
        width: 95%;
        margin: 0 auto 20px;
        padding-top: 3px;

        button {
            width: 50%;
            padding: 15px 20px;
            width: 100%;
            text-align: center;
            background: white;
            border-bottom: 3px solid $blue;
            font-size: 16px;
            font-weight: bold;
            transition: font-weight cubic-bezier(0.215, 0.610, 0.355, 1) .3s;
            transition: border-bottom cubic-bezier(0.215, 0.610, 0.355, 1) .3s;

            &:not(.active) {
                font-weight: normal;
                border-bottom: 1px solid $grey;
            }

            img {
                width: 25px;
                margin-right: 10px;
            }
        }
    }

    &__required-fields {
        font-size: 0.85em;
        color: $grey-dark;
        margin-bottom: 30px;
    }

    &__resource {
        border-bottom: none;
        margin-bottom: 20px;

        &-header,
        &-data {
            display: flex;

            >div {
                width: 33.333%;
                padding: 10px 15px;
                border: 1px solid $grey-lighter;
                border-right: none;
                margin-bottom: -1px;
                display: flex;
                align-items: center;
                min-height: 55px;
                text-align: center;

                &:last-child {
                    border-right: 1px solid $grey-lighter;
                }

                div {
                    +div {
                        margin-top: 10px;
                    }

                    flex: 0 0 100%;
                    justify-content: center;
                }

                span {
                    word-break: break-all;
                    font-size: 0.91em;
                    line-height: 1.3em;
                    margin-top: -2px;
                }

                .default-value {
                    cursor: pointer;
                }

                i {
                    font-style: normal;
                    margin-left: -3px;

                    &.pencil {
                        transform: scaleX(-1);
                        display: inline-block;
                        margin-right: 7px;
                        margin-left: 0;
                    }

                    &.restore-default-value {
                        margin-left: 5px;
                        width: 15px;
                        display: inline-block;
                        cursor: pointer;
                    }
                }
            }

            button {
                margin-right: 5px;
                font-size: 14px;

                .cross {

                    &:before,
                    &:after {
                        background: black;
                        width: 15px;
                        left: 4px;
                    }
                }
            }

            input {
                width: calc(100% - 20px);
                padding: 5px;
                border: 1px solid $grey-light;
            }
        }

        &-header {
            div {
                font-weight: bold;
                color: white;
                background: $grey-blue-tint;
                justify-content: center;
                min-height: 40px;
            }
        }

        .field-required {
            font-style: italic;
            font-size: 0.85em;
            color: $red;
        }

        .expert-option-info {
            font-style: italic;
            font-size: 0.85em;
        }
    }

    &__expert-options {
        margin-right: 15px;
        color: $blue;
        font-size: 1em;

        &:hover {
            color: black;
        }
    }

    &__platforms {
        margin: 30px 0;

        &-choice {
            display: flex;
            justify-content: space-between;

            label {
                width: calc(25% - 10px);
                border: 1px solid $grey-lighter;
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                position: relative;
                padding: 15px 10px;
                cursor: pointer;

                input {
                    margin-bottom: 7px;
                }

                small {
                    display: none;
                    padding: 0 3px;
                    background: white;
                    position: absolute;
                    top: -7px;
                    right: 5px;
                    font-size: 0.76em;
                    font-style: italic;
                    text-transform: capitalize;
                }

                span {
                    text-transform: uppercase;
                }

                &.active {
                    border: 1px solid $blue;

                    small,
                    span {
                        color: $blue;
                    }
                }

                &.optimal {
                    small {
                        display: block;
                    }
                }

                &.disabled {
                    opacity: 0.5;
                    pointer-events: none;
                }
            }
        }

    }

    &__select-resources,
    &__run,
    &__add-run-btn {
        height: 37px;
        color: $blue;
        font-size: 17px;
        padding: 0 25px;
        position: relative;
        transition: background-color 150ms ease-in-out;

        &:hover:not(:disabled),
        &:active:not(:disabled) {
            &:after {
                border-left-color: white;
            }
        }

    }

    &__select-resources,
    &__select-input .custom-select__trigger,
    &__run,
    &__add-run-btn {
        width: 200px;

        span {
            display: inline-block;
            vertical-align: middle;
        }

        .arrow {
            position: relative;
            width: 15px;
            height: 15px;
            border: 1px solid #0661a9;
            border-radius: 50%;
            display: inline-block;
            margin-left: 3px;

            &:before,
            &:after {
                content: '';
                width: 5px;
                height: 2px;
                background: #0661a9;
                position: absolute;
                left: 4px;
                top: 4px;
                -webkit-transform: rotate(45deg);
                transform: rotate(45deg);
            }

            &:after {
                top: 7px;
                -webkit-transform: rotate(-45deg);
                transform: rotate(-45deg);
            }
        }
    }

    &__select-input .custom-select__trigger,
    &__run,
    &__add-run-btn {
        background-color: $blue;
        color: white;
        font-size: 17px;
        text-transform: capitalize;

        &:after {
            display: block;
            width: 0;
            height: 0;
            border-top: 8px solid transparent;
            border-bottom: 8px solid transparent;
            border-left: 8px solid $white;
            content: '';
            margin-left: 10px;
            position: absolute;
            right: 7px;
            top: calc(50% - 8px);
            transition: border-left-color 150ms ease-in-out;
        }
    }

    &__select-input .custom-select__trigger {
        &:after {
            border-top: 8px solid white;
            border-right: 8px solid transparent;
            border-left: 8px solid transparent;
            top: 15px;
        }

        &.active:after {
            top: 6px;
        }

        span {
            white-space: nowrap;
            display: block !important;
        }
    }

    &__saved-run {
        border-bottom: 1px solid $grey-lighter;
        padding: 20px 10px;

        &:last-child {
            border-bottom: none;
        }

        &-name,
        &-id,
        &-status {
            font-weight: bold;
            margin-bottom: 5px;
        }

        &-name {
            color: $blue;
            font-size: 1.2em;
        }

        &-button-holder {
            margin-top: 15px;
        }

        &-btn {
            font-size: 17px;
            padding: 7px 20px;
            border: 1px solid #efefef;
            margin-right: -1px;

            &.active {
                color: $blue;
                background: #efefef;
            }

            &:hover {
                color: $blue;
            }
        }
    }

    &__saved-logs {
        padding: 15px;
        background: #efefef;

        div {
            padding-bottom: 5px;

            &:first-child {
                padding-top: 10px;
            }

            &:last-child {
                padding-bottom: 10px;
            }
        }
    }

    &__saved-outputs {
        padding: 15px;
        background: #efefef;
    }

    &__saved-output {
        padding-bottom: 5px;

        &:first-child {
            padding-top: 10px;
        }

        &:last-child {
            padding-bottom: 10px;
        }

        a {
            color: $blue;
        }

        button {
            background-color: $yellow;
            color: white;
            padding: 3px;
            border-radius: 50%;
            width: 25px;
            height: 25px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 5px;
        }
    }

    &__add-run {
        margin-bottom: 15px;
        border-bottom: 1px solid $grey-lighter;
        padding: 15px 0;

        &-id,
        &-name,
        &-btn {
            width: 30%;

            @media(max-width: $breakpoint-sm) {
                width: 100%;
            }
        }

        &-id,
        &-name {
            border: 2px solid $blue;
            height: 37px;
            outline: 0;
            padding: 0 5px;
            font-size: 17px;
        }
    }

    .pagination {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        background-color: rgba($green, 0.95);
        margin-bottom: 0;
    }

    .bjs-powered-by {
        display: none;
    }

    .bpmn-legend {
        margin-top: 20px;
        padding-top: 15px;
        border-top: 1px solid $grey-lighter;
        font-size: 0.85em;

        >span {
            margin: 0 5px;

            &.required {
                color: $red;
            }

            &.optional {
                color: $yellow;
            }

            &.expert {
                color: $grey;
            }

            &:first-letter {
                text-transform: uppercase;
            }
        }
    }


    @keyframes loadingDots {
        0% {
            opacity: .1;
        }

        20% {
            opacity: 1;
        }

        100% {
            opacity: .1;
        }
    }

    .platform-loading {
        margin-left: 10px;
        font-size: 0.85em;
        line-height: 1.5em;

        span {
            color: black;
            animation-name: loadingDots;
            animation-duration: 1s;
            animation-iteration-count: infinite;
            animation-fill-mode: both;

            &:nth-child(2) {
                animation-delay: .2s;
            }

            &:nth-child(3) {
                animation-delay: .4s;
            }
        }
    }

    svg[data-element-id] {
        position: absolute;
    }

    svg[data-element-id="autogenerated-1559206811037-process"],
    // EODESM
    svg[data-element-id="autogenerated-1540301452629-process"] {
        // EO-SDM
        left: 70px;
    }

    svg[data-element-id="autogenerated-1523951879929-process"],
    // LandMetrics
    svg[data-element-id="autogenerated-1528478431956-process"] {
        // PHENOLOGYMETRICS
        left: 190px;
    }

    svg[data-element-id="autogenerated-1527757840088-process"] {
        // COINS
        left: 240px;
    }

    svg[data-element-id="autogenerated-1556784738669-process"],
    // Trends Earth v1
    svg[data-element-id="autogenerated-1528362962211-process"],
    // INSTAR
    svg[data-element-id="autogenerated-1543344870684-process"],
    // WIMMED
    svg[data-element-id="autogenerated-1523461201638-process"] {
        // HYDROMAP
        left: 270px;
    }
}
</style>
