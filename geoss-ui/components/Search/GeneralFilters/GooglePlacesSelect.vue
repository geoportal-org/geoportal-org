<template>
    <div class="google-places">
        <input v-if="visible" v-model="googlePlacesInput" slot="trigger" type="text"
            :placeholder="$tc('placeholders.googlePlaces')" />
        <button @click="clearGooglePlaces()" :class="{ 'active': isGooglePlacesActive() }">✕</button>
    </div>
</template>

<script lang="ts">
//@ts-nocheck
import { Component, Vue, Watch, Emit } from 'nuxt-property-decorator';

import { GeneralFiltersActions } from '@/store/generalFilters/general-filters-actions';
import { GeneralFiltersGetters } from '@/store/generalFilters/general-filters-getters';
import { MapCoordinate } from '@/interfaces/MapCoordinate';

declare const google: any;

@Component({})
export default class GooglePlacesSelectComponent extends Vue {
    private phraseThreshold = 3;

    get googlePlacesInput() {
        return this.$store.getters[GeneralFiltersGetters.googlePlacesInput];
    }

    set googlePlacesInput(value: string) {
        if (value.length && (value.length < this.phraseThreshold)) {
            return;
        }
        this.$store.dispatch(GeneralFiltersActions.setGooglePlacesInput, value);
    }

    get googlePlacesApiError() {
        return this.$store.getters[GeneralFiltersGetters.googlePlacesApiError];
    }

    set googlePlacesApiError(value: string) {
        this.$store.dispatch(GeneralFiltersActions.setGooglePlacesApiError, value);
    }

    get visible() {
        return typeof google !== 'undefined' && !this.googlePlacesApiError;
    }

    get selectedAreaCoordinates() {
        return this.$store.getters[GeneralFiltersGetters.state].selectedAreaCoordinates;
    }

    public clearGooglePlaces() {
        this.googlePlacesInput = '';
        this.input({ W: null, S: null, E: null, N: null });
    }

    public isGooglePlacesActive() {
        return this.googlePlacesInput !== '';
    }

    @Watch('selectedAreaCoordinates')
    private onSelectedAreaCoordinatesChange() {
        const { W, S, E, N } = this.selectedAreaCoordinates;
        if (W === null && S === null && E === null && N === null) {
            this.googlePlacesInput = '';
        }
    }

    @Emit()
    private input(value: MapCoordinate) {
        return value;
    }

    private mounted() {
        if (this.visible) {
            /**
             * Google Places API quota exceeded error capture
             */
            const defaultConsoleError = console.error;
            console.error = (message, ...args) => {
                if (message.includes('You have exceeded your daily request quota for this API.')) {
                    console.log('We have reached our Google Maps daily requests for today.');
                    this.googlePlacesApiError = 'OVER_QUERY_LIMIT';
                    return false;
                }
                defaultConsoleError.apply(console, args);
            };

            const autocompleteInput = this.$el.querySelector('input');
            autocompleteInput.addEventListener('input', event => {
                if (autocompleteInput.value.length && autocompleteInput.value.length < this.phraseThreshold) {
                    event.stopImmediatePropagation();
                }
            });

            const autocomplete = new google.maps.places.Autocomplete(autocompleteInput, {});
            google.maps.event.addListener(autocomplete, 'place_changed', () => {
                const place = autocomplete.getPlace();

                if (!place.geometry || !place.geometry.viewport) {
                    alert('Ivalid coordinates');
                    return;
                }

                const N = place.geometry.viewport.getNorthEast().lat();
                const E = place.geometry.viewport.getNorthEast().lng();
                const S = place.geometry.viewport.getSouthWest().lat();
                const W = place.geometry.viewport.getSouthWest().lng();

                this.input({ W, S, E, N });
            });
        }
    }
}
</script>

<style scoped lang="scss">
.google-places {
    position: relative;

    button {
        position: absolute;
        top: 50%;
        right: 8px;
        opacity: 0;
        transition: 0.1s ease opacity;
        pointer-events: none;
        transform: translateY(-50%);
        font-size: 20px;
        line-height: 20px;
        font-weight: bold;


        &.active {
            opacity: 1;
            pointer-events: all;
        }
    }

    input {
        font-size: 14px;
        padding: 6px 40px 6px 30px;
        background: white;
        height: 37px;
        display: flex;
        align-items: center;
        width: 100%;
        border: none;
    }
}
</style>
