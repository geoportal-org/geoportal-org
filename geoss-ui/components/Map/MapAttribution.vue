<template>
    <div class="map-attribution" v-if="attributionDetails" :class="'map-attribution-' + attributionType">
        <a v-if="attributionDetails.icon !== ''" :href="attributionDetails.href" :target="attributionDetails.target"
            :class="'attribution-logo-' + attributionType">
            <img :alt="attributionDetails.alt" :src="`/img/${attributionDetails.icon}`" :title="attributionDetails.title">
        </a>
        <span v-if="attributionDetails.copyright" v-html="attributionDetails.copyright"></span>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator';
import { MapGetters } from '@/store/map/map-getters';

@Component
export default class MapAttributionComponent extends Vue {

    public mapAttributions = {
        osm: {
            href: '',
            target: '',
            alt: '',
            icon: '',
            title: '',
            copyright: '© <a title="OpenStreetMap" rel="nofollow" href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> Contributors to OSM.<br />Administrative boundaries: © EuroGeographics, © FAO (UN), © TurkStat Source: European Commission – Eurostat/GISCO.<br />Disclaimer: The boundaries and names shown and the designations used on this map do not imply official endorsement or acceptance by the European Union.'
        },
        mapbox: {
            href: 'https://mapbox.com/',
            target: '_blank',
            alt: 'Logo Mapbox',
            icon: 'mapbox-logo-white.svg',
            title: 'Powered by Mapbox',
            copyright: '© <a title="OpenStreetMap" rel="nofollow" href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> contributors'
        },
        esri: {
            href: 'http://www.esri.com/',
            target: '_blank',
            alt: 'Logo ESRI',
            icon: 'esri.png',
            title: 'Powered by ESRI',
            copyright: null,
        },
        carto: {
            href: 'https://carto.com/',
            target: '_blank',
            alt: 'Logo Carto',
            icon: 'carto.png',
            title: 'Powered by Carto',
            copyright: null,
        },
    };

    public mapAttributionsAssignment = {
        osm: 'osm',
        addsat: 'mapbox',
        mb_hybrid: 'mapbox',
        addhyb: 'mapbox',
        oceanBasemap: 'esri',
        topographicBasemap: 'esri',
        WorldStreetBasemap: 'esri',
        dark_all: 'carto',
        dark_nolabels: 'carto',
        light_all: 'carto',
        light_nolabels: 'carto',
    };

    get mapActiveLayerTileId() {
        return this.$store.getters[MapGetters.activeLayerTileId];
    }
    get attributionType() {
        return this.mapAttributionsAssignment[this.mapActiveLayerTileId];
    }
    get attributionDetails() {
        return this.mapAttributions[this.attributionType];
    }
}
</script>

<style lang="scss" scoped>
.map-attribution {
    position: fixed;
    bottom: 0px;
    left: 0px;
    z-index: 10;
    color: white;
    display: flex;
    align-items: flex-start;
    font-size: 13px;
    text-decoration: none;
    flex-direction: column;
    right: 420px;

    @media (max-width: $breakpoint_lg) {
        bottom: 7px;
        left: 190px;
        right: 150px;
    }

    @media (max-width: $breakpoint_xs) {
        bottom: 20px;
        left: initial;
        right: 60px;
    }

    &-mapbox {
        @media (max-width: $breakpoint_lg) {
            bottom: -34px;
            left: 200px;
        }

        @media (max-width: $breakpoint_sm) {
            bottom: 10px;
        }

        @media (max-width: $breakpoint_xs) {
            bottom: 17px;
            left: initial;
            right: 56px;
        }
    }

    .attribution-logo {
        &-mapbox {
            padding: 30px;

            @media (max-width: $breakpoint_sm) {
                padding: 0;
            }

            img {
                max-width: none;
                height: 30px;
                width: auto;
            }
        }
    }

    img {
        max-width: 80px;

        @media (max-width: $breakpoint_xs) {
            max-width: 50px;
        }
    }

    span {
        background: rgba(0, 0, 0, 0.5);
        font-size: 12px;
        padding: 3px 10px;
        display: block;
        width: auto;

        @media (max-width: $breakpoint_lg) {
            font-size: 0.71em;
        }

        @media (max-width: $breakpoint_sm) {
            bottom: 0;
            font-size: 0.6em;
            position: fixed;
            right: 0;
        }

        // /deep/ a {
        //     color: white;
        //     text-decoration: underline;
        // }
    }
}
</style>
