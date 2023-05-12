<template>
    <div class="unsd">
        <div class="unsd__title">{{ $tc('unitedNationsStatisticsPopup.title') }}:</div>
        <div class="unsd__params">
            <div class="unsd__param">
                <label :title="$tc('unitedNationsStatisticsPopup.timeSeriesTooltip')" class="unsd__param-title">{{
                    $tc('unitedNationsStatisticsPopup.timeSeries') }}:</label>
                <CustomSelect class="unsd__param-select" v-model="timeSeries" :clearable="false" :appendToBody="true"
                    :options="options.timeSeriesOptions"
                    :placeholder="$tc('unitedNationsStatisticsPopup.timeSeriesPlaceholder')" />
            </div>
            <div class="unsd__param">
                <label :title="$tc('unitedNationsStatisticsPopup.yearTooltip')" class="unsd__param-title">{{
                    $tc('unitedNationsStatisticsPopup.year') }}:</label>
                <CustomSelect class="unsd__param-select" v-model="year" :clearable="false" :appendToBody="true"
                    :options="options.yearOptions" :placeholder="$tc('unitedNationsStatisticsPopup.yearPlaceholder')" />
            </div>
            <div class="unsd__param">
                <label :title="$tc('unitedNationsStatisticsPopup.attributesTooltip')" class="unsd__param-title">{{
                    $tc('unitedNationsStatisticsPopup.attributes') }}:</label>
                <CustomSelect class="unsd__param-select" v-model="attributesIndex" :clearable="false" :appendToBody="true"
                    :options="options.attributesOptionsStrings"
                    :placeholder="$tc('unitedNationsStatisticsPopup.attributesPlaceholder')" />
            </div>
        </div>
        <div class="text-center margin-top-30">
            <button class="green-btn-default" :disabled="addLayerDisabled" @click="prepareResultsAndCreateLayer">{{
                $tc('unitedNationsStatisticsPopup.addLayer') }}</button>
        </div>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';
import GeossSearchApiService from '@/services/geoss-search.api.service';
import { PopupActions } from '@/store/popup/popup-actions';
import ErrorPopup from '@/components/ErrorPopup.vue';
import PopupCloseService from '@/services/popup-close.service';
import { MapActions } from '@/store/map/map-actions';
import { StaticPath } from '@/data/global';
import { LayerTypes } from '@/interfaces/LayerTypes';
import UtilsService from '@/services/utils.service';
import LayersUtils from '@/services/map/layer-utils';
import to from '@/utils/to';

@Component
export default class UnitedNationsStatisticsComponent extends Vue {
    @Prop({ default: null, type: String }) public indicator!: string;
    @Prop({ required: true, type: Array }) public timeSeriesArray!: any[];

    public options = {
        timeSeriesOptions: [],
        yearOptions: [],
        attributesOptions: [],
        attributesOptionsStrings: [],
    };
    public timeSeries: string = null;
    public year: string = null;
    public attributesIndex: string = null;
    public addLayerDisabled: boolean = true;
    private areaCodes = [];
    private data;
    private supportedCountries = ['Afghanistan', 'Angola', 'Albania', 'United Arab Emirates', 'Argentina', 'Armenia', 'Antarctica', 'French Southern and Antarctic Lands', 'Australia', 'Austria', 'Azerbaijan', 'Burundi', 'Belgium', 'Benin', 'Burkina Faso', 'Bangladesh', 'Bulgaria', 'The Bahamas', 'Bosnia and Herzegovina', 'Belarus', 'Belize', 'Bermuda', 'Bolivia', 'Brazil', 'Brunei', 'Bhutan', 'Botswana', 'Central African Republic',
        'Canada', 'Switzerland', 'Chile', 'China', 'Ivory Coast', 'Cameroon', 'Democratic Republic of the Congo', 'Republic of the Congo', 'Colombia', 'Costa Rica', 'Cuba', 'Northern Cyprus', 'Cyprus', 'Czech Republic', 'Germany', 'Djibouti', 'Denmark', 'Dominican Republic', 'Algeria', 'Ecuador', 'Egypt', 'Eritrea', 'Spain', 'Estonia', 'Ethiopia', 'Finland', 'Fiji', 'Falkland Islands', 'France', 'Gabon', 'United Kingdom', 'Georgia',
        'Ghana', 'Guinea', 'Gambia', 'Guinea Bissau', 'Equatorial Guinea', 'Greece', 'Greenland', 'Guatemala', 'French Guiana', 'Guyana', 'Honduras', 'Croatia', 'Haiti', 'Hungary', 'Indonesia', 'India', 'Ireland', 'Iran', 'Iraq', 'Iceland', 'Israel', 'Italy', 'Jamaica', 'Jordan', 'Japan', 'Kazakhstan', 'Kenya', 'Kyrgyzstan', 'Cambodia', 'South Korea', 'Kosovo', 'Kuwait', 'Laos', 'Lebanon', 'Liberia', 'Libya', 'Sri Lanka', 'Lesotho',
        'Lithuania', 'Luxembourg', 'Latvia', 'Morocco', 'Moldova', 'Madagascar', 'Mexico', 'Macedonia', 'Mali', 'Myanmar', 'Montenegro', 'Mongolia', 'Mozambique', 'Mauritania', 'Malawi', 'Malaysia', 'Namibia', 'New Caledonia', 'Niger', 'Nigeria', 'Nicaragua', 'Netherlands', 'Norway', 'Nepal', 'New Zealand', 'Oman', 'Pakistan', 'Panama', 'Peru', 'Philippines', 'Papua New Guinea', 'Poland', 'Puerto Rico', 'North Korea', 'Portugal',
        'Paraguay', 'Qatar', 'Romania', 'Russia', 'Rwanda', 'Western Sahara', 'Saudi Arabia', 'Sudan', 'South Sudan', 'Senegal', 'Solomon Islands', 'Sierra Leone', 'El Salvador', 'Somaliland', 'Somalia', 'Republic of Serbia', 'Suriname', 'Slovakia', 'Slovenia', 'Sweden', 'Swaziland', 'Syria', 'Chad', 'Togo', 'Thailand', 'Tajikistan', 'Turkmenistan', 'East Timor', 'Trinidad and Tobago', 'Tunisia', 'Turkey', 'Taiwan',
        'United Republic of Tanzania', 'Uganda', 'Ukraine', 'Uruguay', 'United States of America', 'Uzbekistan', 'Venezuela', 'Vietnam', 'Vanuatu', 'West Bank', 'Yemen', 'South Africa', 'Zambia', 'Zimbabwe', 'Åland Islands', 'American Samoa', 'Andorra', 'Anguilla', 'Antigua and Barbuda', 'Aruba', 'Bahrain', 'Barbados', 'British Virgin Islands', 'Bouvet Island', 'Cayman Islands', 'Hong Kong', 'Macau', 'Comoros', 'Cook Islands',
        'Dominica', 'Eswatini', 'Faroe Islands', 'French Polynesia', 'Grenada', 'Guam', 'Heard Island and McDonald Islands', 'Kiribati', 'Liechtenstein', 'Malta', 'Marshall Islands', 'Mauritius', 'Federated States of Micronesia', 'Monaco', 'Montserrat', 'Nauru', 'Niue', 'Norfolk Island', 'Northern Mariana Islands', 'Palau', 'Palestine', 'Pitcairn Islands', 'St Helena Ascension and Tristan da Cunha', 'Saint Kitts and Nevis',
        'St. Lucia', 'Saint Pierre and Miquelon', 'San Marino', 'Sao Tome and Principe', 'Seychelles', 'Singapore', 'South Georgia and the South Sandwich Islands', 'Tonga', 'Turks and Caicos Islands', 'United Kingdom', 'Wallis and Futuna', 'Christmas Island', 'Cocos (Keeling) Islands', 'Cape Verde', 'Gibraltar', 'French Southern and Antarctic Lands', 'British Indian Ocean Territory', 'Guadeloupe', 'Guernsey', 'Holy See (Vatican City)',
        'Isle of Man', 'Jersey', 'Maldives', 'Martinique', 'Mayotte', 'Netherlands Antilles', 'Reunion', 'Samoa', 'Svalbard', 'Tokelau', 'Tuvalu'];

    public prepareResultsAndCreateLayer() {
        if (!this.data || !this.data.data || !this.data.data.length) {
            this.throwError();
            return;
        }
        const statsToVisualization = {};
        let title = `${this.data.data[0].seriesDescription} [${this.year}]`;
        const legendUnit = this.data.data[0].attributes.Units === 'PERCENT' ? '%' : '';

        if (this.attributesIndex) {
            const preferredDimensions = this.options.attributesOptions[this.attributesIndex];
            title += ` [${this.options.attributesOptionsStrings[Number(this.attributesIndex) + 1].text}]`;
            for (const item of this.data.data) {
                for (const prefDim of preferredDimensions) {
                    if (item.dimensions[prefDim.name] && item.dimensions[prefDim.name] === prefDim.values) {
                        if (!isNaN(item.value) && this.supportedCountries.includes(item.geoAreaName)) {
                            statsToVisualization[item.geoAreaName] = item.value;
                        }
                    }
                }
            }
        } else {
            // No attributes-to-select case
            for (const item of this.data.data) {
                if (!isNaN(item.value) && this.supportedCountries.includes(item.geoAreaName)) {
                    statsToVisualization[item.geoAreaName] = item.value;
                }
            }
        }
        if (Object.keys(statsToVisualization).length === 0) {
            this.throwError();
            return;
        }
        let scaleLegend;
        let statsValues = [];
        statsValues = Object.values(statsToVisualization);
        const minValue = Math.min(...statsValues);
        const maxValue = Math.max(...statsValues);

        if (!statsValues.length) {
            scaleLegend = null;
        } else if (minValue === maxValue) {
            scaleLegend = { type: 'one-value-scale', data: { value: UtilsService.roundToFirstDecimal(minValue), unit: legendUnit } };
        } else {
            scaleLegend = { type: 'two-value-scale', data: { min: UtilsService.roundToFirstDecimal(minValue), max: UtilsService.roundToFirstDecimal(maxValue), unit: legendUnit } };
        }

        this.$store.dispatch(MapActions.removeLayer, `UNEP_${this.timeSeries}_${this.year}_${this.attributesIndex}`);
        this.$store.dispatch(MapActions.addLayer, {
            layer: LayersUtils.createStatisticsLayer(title, statsToVisualization, this.year),
            id: `UNEP_${this.timeSeries}_${this.year}_${this.attributesIndex}`,
            type: LayerTypes.CUSTOM,
            title,
            legend: scaleLegend,
            image: `/img/un-logo.png`
        });
        this.$store.dispatch(MapActions.changeLayerVisibility, { id: LayerTypes.BOUNDING, value: false });
        this.$store.dispatch(MapActions.setShowFull, true);
        PopupCloseService.closePopup('unsd');
    }

    private async prepareTimeSeries() {
        this.options.timeSeriesOptions = [{ text: this.$tc('unitedNationsStatisticsPopup.timeSeriesPlaceholder'), id: 'default' }, ...this.timeSeriesArray];
        this.timeSeries = this.options.timeSeriesOptions[0].id;
    }

    private async loadAccessibleYears() {
        this.options.yearOptions = [{ text: this.$tc('unitedNationsStatisticsPopup.yearPlaceholder'), id: 'default' }];

        const [err, accesibleYears] = await to(GeossSearchApiService.getStatisticsTimePeriods([this.timeSeries], this.areaCodes));
        if (err) {
            const props = {
                title: this.$tc('general.error'),
                subtitle: this.$tc('unitedNationsStatisticsPopup.unavailable')
            };
            PopupCloseService.closePopup('unsd');
            this.$store.dispatch(PopupActions.openPopup, { contentId: 'error', component: ErrorPopup, props });
        } else if (accesibleYears) {
            this.options.yearOptions.push(...accesibleYears.map(item => ({ text: item.toString(), id: item })).reverse());
            this.year = this.options.yearOptions[0].id;
        }
    }

    private async loadAccessibleAttributes() {
        [, this.data] = await to(GeossSearchApiService.getStatisticsData([this.timeSeries], this.areaCodes, [this.year]));
        // GeossSearchApiService.getStatisticsData gets only data with Reporting Type = Global
        // Dimension format for API: [{name: 'Location', values: ['ALLAREA']}, {name: 'Reporting Type', values: ['G']}]
        const dimensionsArray = [];
        if (this.data.dimensions.length === 1 && this.data.dimensions[0].id === 'Reporting Type') {
            this.addLayerDisabled = false;
            return;
        }
        for (const dim of this.data.dimensions) {
            if (dim.id === 'Reporting Type') {
                continue;
            }
            if (dimensionsArray.length === 0) {
                for (const dimValue of dim.codes) {
                    dimensionsArray.push([{ name: dim.id, values: dimValue.code, description: dimValue.description }]);
                }
            } else {
                const newDimensionsCumulative = [];
                for (const dimArray of dimensionsArray) {
                    const cleanCopyOfDimensions = UtilsService.cloneArray(dimArray);
                    let dimValueIndex = 0;
                    for (const dimValue of dim.codes) {
                        if (dimValueIndex === 0) {
                            dimArray.push({ name: dim.id, values: dimValue.code, description: dimValue.description });
                        } else {
                            const newDimensions = UtilsService.cloneArray(cleanCopyOfDimensions);
                            newDimensions.push({ name: dim.id, values: dimValue.code, description: dimValue.description });
                            newDimensionsCumulative.push(newDimensions);
                        }
                        dimValueIndex++;
                    }
                }
                if (newDimensionsCumulative) {
                    dimensionsArray.push(...newDimensionsCumulative);
                }
            }
        }

        // Prepare array with pretty-print options for CustomSelect
        const dimensionsArrayStrings = [{ text: this.$tc('unitedNationsStatisticsPopup.attributesPlaceholder'), id: 'default' }];
        let id = 0;
        for (const entry of dimensionsArray) {
            const text = entry.map(a => a.description).join(', ');
            dimensionsArrayStrings.push({ text, id: String(id) });
            id++;
        }

        this.options.attributesOptions = dimensionsArray;
        this.options.attributesOptionsStrings = dimensionsArrayStrings;
        this.attributesIndex = this.options.attributesOptionsStrings[0].id;
    }

    private throwError() {
        const props = {
            title: this.$tc('general.error'),
            subtitle: 'No data found for this layer'
        };
        PopupCloseService.closePopup('unsd');
        this.$store.dispatch(PopupActions.openPopup, { contentId: 'error', component: ErrorPopup, props });
    }

    @Watch('timeSeries')
    private async onTimeSeriesChange() {
        if (!this.timeSeries || this.timeSeries === 'default') {
            this.year = null;
            this.options.yearOptions = [];

            this.attributesIndex = null;
            this.options.attributesOptions = [];
            this.options.attributesOptionsStrings = [];
            this.addLayerDisabled = true;
            return;
        }
        this.loadAccessibleYears();
    }

    @Watch('year')
    private async onYearChange() {
        if (!this.year || this.year === 'default') {
            this.attributesIndex = null;
            this.options.attributesOptions = [];
            this.options.attributesOptionsStrings = [];
            this.addLayerDisabled = true;
            return;
        }
        this.loadAccessibleAttributes();
    }

    @Watch('attributesIndex')
    private async onAttributesChange() {
        if (!this.attributesIndex || this.attributesIndex === 'default') {
            this.addLayerDisabled = true;
            return;
        }
        this.addLayerDisabled = false;
    }

    private mounted() {
        this.prepareTimeSeries();
    }
}
</script>

<style lang="scss">
.unsd {
    padding: 30px 25px;

    &__title {
        font-size: 20px;
        margin-bottom: 25px;
    }

    &__param {
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 15px 0;

        &-title {
            width: 250px;
            font-weight: bold;
            margin-right: 10px;
            text-align: right;
        }

        &-select,
        &-input {
            width: 300px;
        }

        &-select {
            .custom-select__trigger {
                border: 1px solid #ddd;
                border-radius: 5px;
                padding: 0 25px 0 5px;
                height: 37px;

                &:after {
                    position: absolute;
                    right: 15px;
                }

                span {
                    text-align: left;
                }
            }
        }

        &-input {
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 10px 5px;
        }
    }
}
</style>
