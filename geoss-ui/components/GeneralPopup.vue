<template>
    <div class="general-popup-content popup-default">
        <div class="popup-default__title">
            <i class="icomoon-info"></i>
            <div>{{ title }}</div>
            <button @click="close()" class="close-btn" :title="$tc('general.close')"
                :aria-label="$tc('general.close')"></button>
        </div>
        <div class="popup-default__subtitle" v-html="subtitle"></div>
        <CollapseTransition v-if="description">
            <div v-show="showDetails">
                <div class="popup-default__description" v-html="description"></div>
            </div>
        </CollapseTransition>
        <div class="popup-default__actions">
            <template v-if="actions">
                <button v-for="(action, index) of actions" :key="index" class="blue-btn-default"
                    @click="close(action.event)">{{ action.label }}</button>
            </template>
            <button v-if="(!actions || !actions.length) && description" class="blue-btn-default"
                @click="toggleDescription()">{{ showDetails ? $tc('general.hideDetails') :
                    $tc('general.showDetails') }}</button>
            <button v-if="!actions || !actions.length" class="blue-btn-default" @click="close()">OK</button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'nuxt-property-decorator';
import CollapseTransition from '@/plugins/CollapseTransition';
import PopupCloseService from '@/services/popup-close.service';

@Component({
    components: {
        CollapseTransition
    }
})
export default class GeneralPopupComponent extends Vue {
    @Prop({ type: String }) public title!: string;
    @Prop({ type: String }) public subtitle!: string;
    @Prop({ type: String }) public description!: string;
    @Prop() public actions!: any;

    public showDetails = false;

    public toggleDescription() {
        this.showDetails = !this.showDetails;
    }

    public close(event?: string) {
        PopupCloseService.closePopup('general', event);
    }
}
</script>

<style lang="scss">
.popup-default__wrapper.general-popup .popup-default__content {
    background: none;
}
</style>

<style lang="scss" scoped>
.popup-default {
    &__title {
        i {
            color: $blue-light;
            border: 2px solid $blue-light;
            padding: 3px;
            font-size: 19px;
            border-radius: 50%;
        }
    }
}
</style>
