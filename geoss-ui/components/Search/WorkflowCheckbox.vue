<template>
    <button class="select-workflow-resource" v-if="workflow" @click="selectWorkflowResource(result)">
        <i class="icomoon-checkbox--empty" v-show="!isWorkflowResourceChecked(result)"></i>
        <i class="icomoon-checkbox--filled" v-show="isWorkflowResourceChecked(result)"></i>
    </button>
</template>

<script lang="ts">
import { Component, Prop, Vue, Watch } from 'nuxt-property-decorator';
import { SearchGetters } from '@/store/search/search-getters';
import { SearchActions } from '@/store/search/search-actions';

@Component
export default class WorkflowCheckboxComponent extends Vue {
    @Prop({ type: Object }) public result!: any;

    get workflow() {
        return this.$store.getters[SearchGetters.workflow];
    }

    get inputId() {
        return this.$store.getters[SearchGetters.workflowInputId];
    }

    get workflowInputType() {
        return this.$store.getters[SearchGetters.workflowInputType];
    }

    get workflowResource() {
        return this.$store.getters[SearchGetters.workflowResource][this.inputId];
    }

    set workflowResource(value) {
        const workflowResource = JSON.parse(JSON.stringify(this.$store.getters[SearchGetters.workflowResource]));
        workflowResource[this.inputId] = value;
        this.$store.dispatch(SearchActions.setWorkflowResource, workflowResource);
    }

    public selectWorkflowResource(result: any) {
        if (this.workflowInputType === 'individual') {
            if (this.isWorkflowResourceChecked(result)) {
                this.workflowResource = [];
            } else {
                this.workflowResource = [result];
            }
        } else {
            if (this.isWorkflowResourceChecked(result)) {
                this.workflowResource = this.workflowResource.filter((item: any) => item.id !== result.id);
            } else {
                this.workflowResource = this.workflowResource.concat([result]);
            }
        }
    }

    public isWorkflowResourceChecked(result: any) {
        return this.workflowResource && this.workflowResource.find((item: any) => item.id === result.id);
    }
}
</script>

<style lang="scss" scoped>
.select-workflow-resource {
    position: absolute;
    right: 15px;
    bottom: 10px;
    color: $blue;
    font-weight: bold;
    font-size: 17px;

    .icomoon-checkbox--filled {
        font-size: 18px;
        position: relative;
        left: 1px;
    }
}
</style>
