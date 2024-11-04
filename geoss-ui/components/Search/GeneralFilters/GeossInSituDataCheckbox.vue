<template>
    <div>
        <input :value="generalFiltersGeossInSituData" @input="input($event)" id="geoss-insitu-data" type="checkbox"
            :disabled="buttonDisabled" />
        <label for="geoss-insitu-data" :class="{ disabled: buttonDisabled }">
            <i v-show="generalFiltersGeossInSituData" class="icomoon-checkbox--filled"></i>
            <i v-show="!generalFiltersGeossInSituData" class="icomoon-checkbox--empty"></i>
            <span>In-Situ Data</span>
        </label>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Emit, Prop } from 'nuxt-property-decorator'

import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters'

@Component
export default class GeossInSituDataCheckboxComponent extends Vue {
    @Prop({ default: false, type: Boolean }) public buttonDisabled!: boolean;

    get generalFiltersGeossInSituData() {
        return this.$store.getters[GeneralFiltersGetters.state].geossInSituData;
    }

    @Emit()
    public input(value: boolean) {
        return value;
    }
}
</script>

<style lang="scss" scoped>
div {
    position: relative;
}

input[type="checkbox"] {
    width: 0;
    height: 0;
    overflow: hidden;
    margin: 0;
    position: absolute;
    z-index: -1;
    opacity: 0;
}

label {
    cursor: pointer;
    padding-left: 30px;

    &.disabled {
        cursor: default;
        opacity: 0.65;
    }

    i {
        display: block;
        font-size: 20px;
        position: absolute;
        left: 0;
        top: 4px;
        box-sizing: border-box;
        color: white;

        &.icomoon-checkbox--filled {
            font-size: 22px;
            top: 2px;
        }
    }

    span {
        line-height: 30px;
        color: $white
    }
}
</style>
