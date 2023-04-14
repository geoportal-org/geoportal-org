<template>
    <div>
        <input :value="generalFiltersGeossDataCore" @input="input($event)" id="geoss-data-core" type="checkbox"
            :disabled="buttonDisabled" />
        <label for="geoss-data-core" :class="{ disabled: buttonDisabled }">
            <i v-show="generalFiltersGeossDataCore" class="icomoon-checkbox--filled"></i>
            <i v-show="!generalFiltersGeossDataCore" class="icomoon-checkbox--empty"></i>
            <img :src="`/img/geoss-data-core.png`" alt="Data Core" />
        </label>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Emit, Prop } from 'nuxt-property-decorator';

import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';

@Component
export default class GeossDataCoreCheckboxComponent extends Vue {
    @Prop({ default: false, type: Boolean }) public buttonDisabled!: boolean;

    get generalFiltersGeossDataCore() {
        return this.$store.getters[GeneralFiltersGetters.state].geossDataCore;
    }

    @Emit()
    public input(value: any) {
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
}
</style>
