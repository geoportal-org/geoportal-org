<template>
    <div class="metadata">
        <div class="margin-bottom-25">
            <div v-if="platform === 'ZENODO'" class="margin-bottom-15 top-data">
                <span>{{ publicationDate }}</span>
                <div>
                    <span v-for="(item, index) in zenodoBadges" :key="index" class="badge" :class="item.color">{{
                        item.label }}</span>
                </div>
            </div>
            <div class="metadata__title">{{ title }}</div>
            <div v-if="authors" class="metadata__authors">
                {{ $tc('popupContent.authors') }}: {{ authors }}
            </div>
            <div v-if="useLimitation" class="metadata__license">
                {{ $tc('popupContent.license') }}: {{ useLimitation }}
            </div>
            <div v-if="otherConstraints" class="metadata__citation">
                {{ $tc('popupContent.citation') }}: {{ otherConstraints }}
            </div>
            <div v-if="confidence && confidence.length" class="confidence">
                <div class="confidence__label">
                    {{ $tc('popupContent.confidence') }}:
                </div>
                <div class="confidence__box">
                    <div class="confidence__number">
                        {{ confidence[0] }}
                    </div>
                    <div class="confidence__type">
                        {{ $tc('popupContent.crop') }}
                    </div>
                </div>
                <div class="confidence__box">
                    <div class="confidence__number">
                        {{ confidence[1] }}
                    </div>
                    <div class="confidence__type">
                        {{ $tc('popupContent.irrigation') }}
                    </div>
                </div>
                <div class="confidence__box">
                    <div class="confidence__number">
                        {{ confidence[2] }}
                    </div>
                    <div class="confidence__type">
                        {{ $tc('popupContent.landCover') }}
                    </div>
                </div>
            </div>
            <div class="overflow-hidden">
                <a class="metadata__preview" target="_blank" v-if="getImage(preview) === preview" :href="preview">
                    <img :src="getImage(preview)" @error="imageLoadError(preview)" alt="Resource preview not available"
                        class="big-image" />
                </a>
                <div v-if="!isSatellite" class="metadata__description" v-html="description"></div>
                <UserContributionsMetadata :model="'summary'" :data="{
                    data,
                    isSatellite,
                    resultTitle,
                    resultImage,
                    popupTitle
                }" />
            </div>
        </div>

        <div class="youtube-video margin-bottom-25" v-for="(video, index) in youtubeVideos" :key="index">
            <iframe :src="video" frameborder="0"
                allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen></iframe>
        </div>

        <div class="metadata__info-table" v-if="
            platform !== 'ZENODO' && platform !== 'WIKIPEDIA' && isSatellite
        ">
            <div v-if="isSatellite">
                <div>
                    <b>{{ $tc('popupContent.generalInfo') }}</b>
                </div>
                <div>
                    <b>{{ $tc('popupContent.platform') }}:</b>{{ platform }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.platformId') }}:</b>{{ platformId }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.productType') }}:</b>{{ productType }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.instrument') }}:</b>{{ instrument }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.cloudCoverage') }}:</b>{{ cloudCoverage }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.daytimeStart') }}:</b>{{ daytimeStart }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.daytimeStop') }}:</b>{{ daytimeStop }}
                </div>
            </div>
            <div v-if="isSatellite">
                <div>
                    <b>{{ $tc('popupContent.sceneParameters') }}</b>
                </div>
                <div>
                    <b>{{ $tc('popupContent.size') }}:</b>{{ size }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.relativeOrbit') }}:</b>{{ relativeOrbit }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.instrument') }}:</b>{{ instrumentShort }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.sensorPolarisation') }}:</b>{{ sensorPolarisation }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.aquisitionType') }}: </b>{{ aquisitionType }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.missionDataTaken') }}:</b>{{ missionDataTaken }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.startOrbitNumber') }}:</b>{{ startOrbitNumber }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.stopOrbitNumber') }}:</b>{{ stopOrbitNumber }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.orbitDirection') }}:</b>{{ orbitDirection }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.productClass') }}:</b>{{ productClass }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.productConsolidation') }}:</b>{{ productConsolidation }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.stopRelativeOrbitNumber') }}:</b>{{ stopRelativeOrbitNumber }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.sliceNumber') }}:</b>{{ sliceNumber }}
                </div>
                <div>
                    <b>{{ $tc('popupContent.status') }}:</b>{{ status }}
                </div>
            </div>
        </div>
        <div class="metadata__keywords">
            <div class="metadata__keywords-title">
                {{ $tc('popupContent.descriptiveKeywords') }}
            </div>
            <div class="metadata__keywords-wrap">
                <a class="metadata__keyword" v-for="(keyword, index) of descriptiveKeywords" :key="index"
                    @click="keywordSearch(keyword)">{{ keyword }}</a>
                <div v-if="!descriptiveKeywords.length">&#8212;</div>
            </div>
            <UserContributionsMetadata :model="'keywords'" :data="{
                data,
                isSatellite,
                resultTitle,
                resultImage,
                popupTitle
            }" @keyword-search="keywordSearch($event)" />
        </div>
        <div class="metadata__additional-info" v-if="platform == 'ZENODO'">
            <div class="metadata__additional-info-title">
                {{ $tc('popupContent.additionalInfo') }}
            </div>
            <div class="metadata__additional-info-item">
                <b>{{ $tc('popupContent.publicationDate') }}: </b>{{ publicationDate }}
            </div>
            <div class="metadata__additional-info-item">
                <b>{{ $tc('popupContent.doi') }}: </b><span v-html="zenodoDOI"></span>
            </div>
            <div class="metadata__additional-info-item">
                <b>{{ $tc('popupContent.licenseForFiles') }}: </b>{{ license }}
            </div>
            <div class="metadata__additional-info-item">
                <b>{{ $tc('popupContent.publishedIn') }}: </b>{{ publishedIn }}
            </div>
        </div>
        <div class="metadata__coordinates">
            <div class="metadata__coordinates__title">
                {{ $tc('popupContent.mapDetails') }}
            </div>
            <div>
                <b>{{ $tc('popupContent.boundingRectangle') }}:</b>
                {{ boundingBox }}
            </div>
            <div>
                <b>{{ $tc('popupContent.temporalExtent') }}:</b>
                {{ temporalExtent }}
            </div>
        </div>
        <div id="infoMap" style="max-height: 250px; height: 250px" class="margin-bottom-30"></div>
        <div class="metadata__references" v-if="platform == 'ZENODO' && references.length">
            <div class="metadata__references-title">
                {{ $tc('popupContent.references') }}
            </div>
            <ul class="metadata__references-list">
                <li class="metadata__reference" v-for="(reference, index) of references" :key="index">
                    {{ reference }}
                </li>
            </ul>
        </div>
        <div class="metadata__links" v-if="platform !== 'ZENODO'">
            <div class="metadata__links-title">
                {{ $tc('popupContent.rawOnlineResources') }}
            </div>
            <div v-if="linksEmpty">N.A.</div>
            <div class="metadata__links-section" v-for="(value, key) in links" :key="key">
                <div class="metadata__links-section-title" v-if="value.items.length">
                    <img v-if="value.items.length" :src="`/img/m-link.png`"
                        :alt="$tc('popupContent.onlineResources')" />
                    <span>{{ value.title }}</span>
                </div>
                <div v-for="(link, index) in value.items" :key="index">
                    <div>
                        <div class="metadata__link" :class="link.available" :title="link.scoreText">
                            <a target="_blank" :href="link.linkText" class="link">{{ link.linkTitle }}</a>
                            <div v-if="link.protocol && link.protocol !== ''" class="protocol">
                                {{ link.protocol }}
                            </div>
                            <img v-if="link.linkTextParsed" class="layer" alt="Preview not available"
                                :src="link.linkTextParsed" />
                            <div v-else-if="
                                link.linkDescription &&
                                link.linkDescription !== ''
                            ">
                                {{ link.linkDescription }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <UserContributionsMetadata :model="'transferOptions'" :data="{
                data,
                isSatellite,
                resultTitle,
                resultImage,
                popupTitle
            }" />
        </div>
        <UserContributionsMetadata v-if="showComments" :model="'comment'"
            :data="{ data, isSatellite, resultTitle, resultImage, popupTitle }" />
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop } from 'nuxt-property-decorator'

import MapCoordinatesUtils from '@/services/map/coordinates-utils'
import UtilsService from '@/services/utils.service'
import { ResultService } from '@/services/result.service'
import LayerTilesService from '@/services/map/layer-tiles.service'
import { SearchGetters } from '@/store/search/search-getters'
import { SearchActions } from '@/store/search/search-actions'
import { GeneralGetters } from '@/store/general/general-getters'
import PopupCloseService from '@/services/popup-close.service'
import UserContributionsMetadata from './UserContributionsMetadata.vue'
import GeossSearchApiService from '@/services/geoss-search.api.service'
import { DataOrigin } from '@/interfaces/DataSources'
import { AppVueObj } from '~/data/global'

@Component({
    components: {
        UserContributionsMetadata
    }
})
export default class DabResultMetadataComponent extends Vue {
    [x: string]: any
    @Prop({ default: false, type: Boolean }) public isSatellite!: boolean
    @Prop(Object) private data!: any
    @Prop({ default: '', type: String }) private resultTitle!: string
    @Prop({ default: '', type: String }) private resultImage!: string
    @Prop({ default: '', type: String }) private popupTitle!: string

    public map: any = null
    private zenodoBadges = {}
    private youtubeVideos = []
    private showComments = false

    get dataSource() {
        return this.$store.getters[SearchGetters.dataSource]
    }

    get title() {
        let data = null
        if (
            this.isSatellite ||
            this.platform === 'GEOSS_CR' ||
            this.platform === 'NEXTGEOSS' ||
            this.platform === 'WIKIPEDIA'
        ) {
            data = UtilsService.getPropByString(this.data, 'title')
        } else if (this.platform === 'ZENODO') {
            data = UtilsService.getPropByString(this.data, 'metadata.title')
        } else {
            data = UtilsService.getPropByString(
                this.data,
                'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:citation.gmd:CI_Citation.gmd:title.gco:CharacterString'
            )
            if (!data) {
                data = UtilsService.getPropByString(
                    this.data,
                    'gmd:identificationInfo.srv:SV_ServiceIdentification.gmd:citation.gmd:CI_Citation.gmd:title.gco:CharacterString'
                )
            }
        }

        if (data) {
            return data
        }
        return '-'
    }

    get authors() {
        const authors = []
        const pointsOfContact = UtilsService.getArrayByString(
            this.data,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:pointOfContact'
        )
        for (const pointOfContact of pointsOfContact) {
            const role = UtilsService.getPropByString(
                pointOfContact,
                'gmd:CI_ResponsibleParty.gmd:role.gmd:CI_RoleCode'
            )
            if (role === 'originator') {
                const author = UtilsService.getPropByString(
                    pointOfContact,
                    'gmd:CI_ResponsibleParty.gmd:individualName.gco:CharacterString'
                )
                const organisation = UtilsService.getPropByString(
                    pointOfContact,
                    'gmd:CI_ResponsibleParty.gmd:organisationName.gco:CharacterString'
                );
                authors.push(organisation || author);
            }
        }
        return authors.join('; ')
    }

    get type() {
        return UtilsService.getPropByString(this.data, 'type')
    }

    get description() {
        let data = UtilsService.getPropByString(
            this.data,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:abstract.gco:CharacterString',
            'string'
        )
        if (!data) {
            data = UtilsService.getPropByString(
                this.data,
                'gmd:identificationInfo.srv:SV_ServiceIdentification.gmd:abstract.gco:CharacterString',
                'string'
            )
        }

        if (data) {
            return data
        }

        if (
            this.platform === 'GEOSS_CR' ||
            this.platform === 'NEXTGEOSS' ||
            this.platform === 'WIKIPEDIA'
        ) {
            const data: any = UtilsService.getPropByString(
                this.data,
                'summary',
                'string'
            )
            if (data) {
                return data
            }
        }

        if (this.platform === 'ZENODO') {
            const data: any = UtilsService.getPropByString(
                this.data,
                'metadata.description',
                'string'
            )
            if (data) {
                return data
            }
        }
        return '-'
    }

    get preview() {
        if (this.platform === 'WIKIPEDIA') {
            return this.data.logo
        }
        let data = UtilsService.getPropByString(
            this.data,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:graphicOverview.gmd:MD_BrowseGraphic.gmd:fileName.gco:CharacterString'
        )
        if (!data) {
            data = UtilsService.getPropByString(
                this.data,
                'gmd:identificationInfo.srv:SV_ServiceIdentification.gmd:graphicOverview.gmd:MD_BrowseGraphic.gmd:fileName.gco:CharacterString'
            )
        }

        return data
    }

    // START: satellite props
    get platform() {
        return this.getStringData('acquisition.platform')
    }

    get platformId() {
        return this.getStringData('acquisition.platformid')
    }

    get productType() {
        return this.getStringData('acquisition.productType')
    }

    get instrument() {
        return this.getStringData('acquisition.instrument')
    }

    get cloudCoverage() {
        return this.getStringData('acquisition.cloud_cover_percentage')
    }

    get daytimeStart() {
        return this.getStringData('dtstart')
    }

    get daytimeStop() {
        return this.getStringData('dtstop')
    }

    get size() {
        return this.getStringData(
            'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:transferSize.gco:Real'
        )
    }

    get relativeOrbit() {
        return this.getStringData('acquisition.relativeOrbit')
    }

    get instrumentShort() {
        return this.getStringData('acquisition.s3InstrumentIdx')
    }

    get sensorPolarisation() {
        return this.getStringData('acquisition.sensorPolarisation')
    }

    get aquisitionType() {
        return this.getStringData('acquisition.acquisitiontype')
    }

    get missionDataTaken() {
        return this.getStringData('acquisition.missiondatatakeid')
    }

    get startOrbitNumber() {
        return this.getStringData('acquisition.startOrbitNumber')
    }

    get stopOrbitNumber() {
        return this.getStringData('acquisition.stopOrbitNumber')
    }

    get orbitDirection() {
        return this.getStringData('acquisition.orbitdirection')
    }

    get productClass() {
        return this.getStringData('acquisition.productclass')
    }

    get productConsolidation() {
        return this.getStringData('acquisition.productconsolidation')
    }

    get stopRelativeOrbitNumber() {
        return this.getStringData('acquisition.stopRelativeOrbitNumber')
    }

    get sliceNumber() {
        return this.getStringData('acquisition.slicenumber')
    }

    get status() {
        return this.getStringData('acquisition.status')
    }
    // END: satellite props

    // START: not satellite props
    get langLocale() {
        return this.$store.getters[GeneralGetters.langLocale]
    }

    get publicationDate() {
        const data = this.getStringData('metadata.publication_date')
        return this.formatDate(data)
    }

    get zenodoDOI() {
        const doiBadge = this.getStringData('links.badge')
        const doiLink = this.getStringData('links.doi')
        const doiName = this.getStringData('metadata.doi')
        const doi = `<a target="_blank" href="${doiLink}" title="${doiName}"><img src="${doiBadge}" alt="${doiName}"/></a>`
        return doi
    }

    get license() {
        return this.getStringData('metadata.license.id')
    }

    get useLimitation() {
        return UtilsService.getPropByString(this.data, 'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:resourceConstraints.gmd:MD_LegalConstraints.gmd:useLimitation.gco:CharacterString');
    }

    get otherConstraints() {
        return UtilsService.getPropByString(this.data, 'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:resourceConstraints.gmd:MD_LegalConstraints.gmd:otherConstraints.gco:CharacterString');
    }

    get confidence() {
        const reports = UtilsService.getArrayByString(this.data, 'gmd:dataQualityInfo.gmd:DQ_DataQuality.gmd:report');

        if (!reports || !reports.length) {
            return null;
        }

        let crop, irrigation, landCover;

        for (const report of reports) {
            const nameOfMeasure = UtilsService.getPropByString(report, 'gmd:DQ_AccuracyOfATimeMeasurement.gmd:nameOfMeasure.gco:CharacterString');
            if (nameOfMeasure === 'cropConfidence') {
                crop = UtilsService.getPropByString(report, 'gmd:DQ_AccuracyOfATimeMeasurement.gmd:measureDescription.gco:CharacterString');
            }
            if (nameOfMeasure === 'irrigationConfidence') {
                irrigation = UtilsService.getPropByString(report, 'gmd:DQ_AccuracyOfATimeMeasurement.gmd:measureDescription.gco:CharacterString');
            }
            if (nameOfMeasure === 'landCoverConfidence') {
                landCover = UtilsService.getPropByString(report, 'gmd:DQ_AccuracyOfATimeMeasurement.gmd:measureDescription.gco:CharacterString');
            }
        }

        if (crop === '' || irrigation === '' || landCover === '') {
            return null;
        }

        return [crop.toFixed(1), irrigation.toFixed(1), landCover.toFixed(1)];
    }

    get publishedIn() {
        return this.getStringData('metadata.journal.title')
    }

    get references() {
        let data = null
        data = UtilsService.getArrayByString(this.data, 'metadata.references')
        return data
    }

    get contributor() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:individualName.gco:CharacterString'
        )
    }

    get deliveryPoint() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:contactInfo.gmd:CI_Contact.gmd:address.gmd:CI_Address.gmd:deliveryPoint.gco:CharacterString'
        )
    }

    get city() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:contactInfo.gmd:CI_Contact.gmd:address.gmd:CI_Address.gmd:city.gco:CharacterString'
        )
    }

    get postalCode() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:contactInfo.gmd:CI_Contact.gmd:address.gmd:CI_Address.gmd:postalCode.gco:CharacterString'
        )
    }

    get country() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:contactInfo.gmd:CI_Contact.gmd:address.gmd:CI_Address.gmd:postalCode.gco:CharacterString'
        )
    }

    get email() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:contactInfo.gmd:CI_Contact.gmd:address.gmd:CI_Address.gmd:electronicMailAddress.gco:CharacterString'
        )
    }

    get organisationName() {
        const data = []
        if (
            this.data.contributor &&
            this.data.contributor.orgName &&
            this.dataSource !== 'zenodo'
        ) {
            data.push(
                UtilsService.getPropByString(this.data, 'contributor.orgName')
            )
        } else if (this.data['gmd:contact'] && this.dataSource !== 'zenodo') {
            data.push(
                UtilsService.getPropByString(
                    this.data,
                    'gmd:contact.gmd:CI_ResponsibleParty.gmd:organisationName.gco:CharacterString'
                )
            )
        } else if (
            this.data.metadata &&
            this.data.metadata.creators &&
            this.dataSource === 'zenodo'
        ) {
            for (const item of this.data.metadata.creators) {
                data.push(item.name)
            }
        } else if (
            this.data['dc:publisher'] &&
            this.dataSource === 'nextgeoss'
        ) {
            data.push(UtilsService.getPropByString(this.data, 'dc:publisher'))
        }
        return data ? data.join(', ') : '-'
    }

    get organisationUrl() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:contactInfo.gmd:CI_Contact.gmd:onlineResource.gmd:CI_OnlineResource.gmd:linkage.gmd:URL'
        )
    }

    get role() {
        return this.getStringData(
            'gmd:contact.gmd:CI_ResponsibleParty.gmd:role.gmd:CI_RoleCode'
        )
    }

    get fileIdentifier() {
        if (this.platform === 'NEXTGEOSS') {
            return this.getStringData('id')
        } else {
            return this.getStringData('gmd:fileIdentifier.gco:CharacterString')
        }
    }

    get parentIdentifier() {
        return this.getStringData('gmd:parentIdentifier.gco:CharacterString')
    }

    get hierarchyLevel() {
        return this.getStringData('gmd:hierarchyLevel.gco:CharacterString')
    }

    get dateStamp() {
        if (this.platform === 'NEXTGEOSS') {
            return this.getStringData('published')
        } else {
            return this.getStringData('gmd:dateStamp.gco:DateTime')
        }
    }

    get language() {
        return this.getStringData('gmd:language.gco:CharacterString')
    }
    // END: not satellite props

    get descriptiveKeywords() {
        let data = null
        if (this.platform === 'ZENODO') {
            data = UtilsService.getArrayByString(this.data, 'metadata.keywords')
            if (data.length === 1) {
                data = data[0].split(', ')
            }
            return data
        } else {
            data = UtilsService.getArrayByString(
                this.data,
                'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:descriptiveKeywords'
            )
            if (!data.length) {
                data = UtilsService.getArrayByString(
                    this.data,
                    'gmd:identificationInfo.srv:SV_ServiceIdentification.gmd:descriptiveKeywords'
                )
            }
        }
        let filteredData = []
        for (const item of data) {
            const keywords = UtilsService.getArrayByString(
                item,
                'gmd:MD_Keywords.gmd:keyword.gco:CharacterString'
            )
            filteredData.push(...keywords)
        }
        if (!filteredData.length) {
            filteredData = UtilsService.extractCategoriesByAttributeValue(
                this.data,
                'term',
                'keywords'
            )
        }
        return filteredData
    }

    get boundingBox() {
        if (this.platform === 'NEXTGEOSS') {
            const polygonValue = this.getStringData('georss:polygon')
            if (polygonValue !== '-' && polygonValue !== 'None') {
                return `georss:polygon(${polygonValue})`
            }
        } else {
            const { boxes, pins } = ResultService.getBoundingBoxesAndPins(
                this.data
            )
            const outerBox = MapCoordinatesUtils.mergeBoxes(boxes)
            const outerPin = MapCoordinatesUtils.mergeBoxes(pins)
            let coordinate = null
            if (outerBox) {
                coordinate =
                    MapCoordinatesUtils.coordinatesForPresentation(outerBox)
            } else if (outerPin) {
                coordinate =
                    MapCoordinatesUtils.coordinatesForPresentation(outerPin)
            }

            return coordinate ? coordinate : 'N.A.'
        }
    }

    get temporalExtent() {
        if (this.platform === 'NEXTGEOSS') {
            const date = this.getStringData('dc:date')
            if (date !== '-' && date !== '/') {
                const dateSplit = date.split('/')
                return `Start ${dateSplit[0]} End ${dateSplit[1]}`
            } else {
                return 'N.A.'
            }
        }

        let temporalExtentBegin = UtilsService.getPropByString(
            this.data,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:extent.gmd:EX_Extent.gmd:temporalElement.gmd:EX_TemporalExtent.gmd:extent.gml:TimePeriod.gml:beginPosition'
        )
        if (!temporalExtentBegin || typeof temporalExtentBegin !== 'string') {
            temporalExtentBegin = UtilsService.getPropByString(
                this.data,
                'dtstart'
            )
        }
        if (!temporalExtentBegin || typeof temporalExtentBegin !== 'string') {
            temporalExtentBegin = UtilsService.getPropByString(
                this.data,
                'start'
            )
        }
        if (temporalExtentBegin && typeof temporalExtentBegin === 'string') {
            temporalExtentBegin = temporalExtentBegin.trim()
        }

        let temporalExtentEnd = UtilsService.getPropByString(
            this.data,
            'gmd:identificationInfo.gmd:MD_DataIdentification.gmd:extent.gmd:EX_Extent.gmd:temporalElement.gmd:EX_TemporalExtent.gmd:extent.gml:TimePeriod.gml:endPosition'
        )
        if (!temporalExtentEnd || typeof temporalExtentEnd !== 'string') {
            temporalExtentEnd = UtilsService.getPropByString(this.data, 'dtend')
        }
        if (!temporalExtentEnd || typeof temporalExtentEnd !== 'string') {
            temporalExtentEnd = UtilsService.getPropByString(this.data, 'stop')
        }
        if (temporalExtentEnd && typeof temporalExtentEnd === 'string') {
            temporalExtentEnd = temporalExtentEnd.trim()
        }

        if (temporalExtentBegin || temporalExtentEnd) {
            if (!temporalExtentEnd) {
                return `Start ${temporalExtentBegin}`
            } else {
                return `Start ${temporalExtentBegin} End ${temporalExtentEnd}`
            }
        } else {
            return 'N.A.'
        }
    }

    get links() {
        const arrayPdf = []
        const arrayTxt = []
        const arrayHtml = []
        const arrayImg = []
        const arrayXml = []
        const arrayOther = []
        const arrayWCS = []
        const arrayWMSandTMS = []

        if (this.platform === 'NEXTGEOSS') {
            const data: any = UtilsService.getArrayByString(this.data, 'link')
            for (const item of data) {
                if (
                    item._attributes &&
                    item._attributes.rel &&
                    item._attributes.rel === 'enclosure'
                ) {
                    const scoreMapping = this.statusCheckerMapping(-1)
                    const linkObject = {
                        linkText: item._attributes.href.replace('&amp;', '&'),
                        linkTitle: item._attributes.title,
                        linkDescription: '',
                        titleBox: this.resultTitle,
                        scoreText: scoreMapping.scoreMsg,
                        available: scoreMapping.scoreClass
                    }

                    /* Simple resources aggregation */
                    if (item._attributes.type) {
                        if (item._attributes.type === 'text/html') {
                            arrayHtml.push(linkObject)
                        } else if (
                            item._attributes.type.indexOf('image') > -1
                        ) {
                            arrayImg.push(linkObject)
                        } else if (item._attributes.type === 'text/xml') {
                            arrayXml.push(linkObject)
                        } else {
                            arrayOther.push(linkObject)
                        }
                    }
                }
            }
        }

        const data: any = UtilsService.getArrayByString(
            this.data,
            'gmd:distributionInfo.gmd:MD_Distribution.gmd:transferOptions.gmd:MD_DigitalTransferOptions.gmd:onLine'
        )
        for (const item of data) {
            const linkText = UtilsService.getPropByString(
                item,
                'gmd:CI_OnlineResource.gmd:linkage.gmd:URL'
            )
            if (linkText) {
                if (
                    linkText.includes('youtube.com') ||
                    linkText.includes('youtu.be')
                ) {
                    this.youtubeVideos.push(
                        `https://www.youtube.com/embed/${this.extractYoutubeVideoId(
                            linkText
                        )}`
                    )
                }
                let linkTitle = UtilsService.getPropByString(
                    item,
                    'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                )
                const linkTitle2 = UtilsService.getPropByString(
                    item,
                    'gmd:CI_OnlineResource.gmd:function.gmd:ci_onlinefunctioncode'
                )
                let linkScore = UtilsService.getPropByString(
                    item,
                    'gmd:CI_OnlineResource.gmd:status.gmd:score'
                )
                if (!linkScore || linkScore > 100 || linkScore < 0) {
                    linkScore = -1
                }

                let scoreText = ''
                let available = ''
                const mapping = this.statusCheckerMapping(linkScore)
                scoreText = mapping.scoreMsg
                available = mapping.scoreClass

                const titleBox = this.resultTitle

                if (!linkTitle) {
                    if (linkTitle2) {
                        linkTitle = linkTitle2
                    } else {
                        linkTitle = titleBox
                    }
                }

                let linkDescription = UtilsService.getPropByString(
                    item,
                    'gmd:CI_OnlineResource.gmd:description.gco:CharacterString'
                )
                const linkDescription2 = UtilsService.getPropByString(
                    item,
                    'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                )
                const wmsAllLayerName = this.resultTitle
                const protocol = UtilsService.getPropByString(
                    item,
                    'gmd:CI_OnlineResource.gmd:protocol.gco:CharacterString'
                )
                const anchor = UtilsService.getPropByString(
                    item,
                    'gmd:ci_onlineresource.gmd:description'
                )

                if (linkDescription === '') {
                    if (
                        linkDescription2 !== '' &&
                        linkDescription2 !== linkTitle
                    ) {
                        linkDescription = linkDescription2
                    }
                }

                if (
                    linkText.lastIndexOf('.pdf') > -1 &&
                    !(anchor.indexOf('complex') > 0) &&
                    linkText !== ''
                ) {
                    const lastPdf = linkText.substr(linkText.length - 4)
                    if (lastPdf === '.pdf') {
                        arrayPdf.push({
                            linkText,
                            linkTitle,
                            linkDescription,
                            titleBox,
                            scoreText,
                            available
                        })
                    }
                } else if (
                    linkText.lastIndexOf('.txt') > -1 &&
                    !(anchor.indexOf('complex') > 0) &&
                    linkText !== ''
                ) {
                    const lastTxt = linkText.substr(linkText.length - 4)
                    if (lastTxt === '.txt') {
                        arrayTxt.push({
                            linkText,
                            linkTitle,
                            linkDescription,
                            titleBox,
                            scoreText,
                            available
                        })
                    }
                } else if (
                    (linkText.lastIndexOf('.htm') > -1 ||
                        linkText.lastIndexOf('.html') > -1 ||
                        linkText.lastIndexOf('.shtml') > -1 ||
                        linkText.lastIndexOf('.htm?') > -1 ||
                        linkText.lastIndexOf('.html?') > -1 ||
                        linkText.lastIndexOf('.shtml?') > -1) &&
                    !(anchor.indexOf('complex') > 0) &&
                    linkText !== ''
                ) {
                    arrayHtml.push({
                        linkText,
                        linkTitle,
                        linkDescription,
                        titleBox,
                        scoreText,
                        available
                    })
                } else if (
                    (linkText.lastIndexOf('.jpg') > -1 ||
                        linkText.lastIndexOf('.png') > -1) &&
                    linkText.indexOf('tile') === -1 &&
                    !(anchor.indexOf('simple') > 0) &&
                    linkText !== ''
                ) {
                    const lastJpg = linkText.substr(linkText.length - 4)
                    const lastPng = linkText.substr(linkText.length - 4)
                    if (
                        lastJpg === '.jpg' ||
                        (lastPng === '.png' && linkText.indexOf('tile') === -1)
                    ) {
                        arrayImg.push({
                            linkText,
                            linkTitle,
                            linkDescription,
                            titleBox,
                            scoreText,
                            available
                        })
                    }
                } else if (
                    linkText.lastIndexOf('.xml') > -1 &&
                    !(anchor.indexOf('simple') > 0) &&
                    linkText !== ''
                ) {
                    const lastXml = linkText.substr(linkText.length - 4)
                    if (lastXml === '.xml') {
                        arrayXml.push({
                            linkText,
                            linkTitle,
                            linkDescription,
                            titleBox,
                            scoreText,
                            available
                        })
                    }
                } else if (
                    protocol.indexOf('WebMapService') > -1 &&
                    linkText !== ''
                ) {
                    const wmsName = UtilsService.getPropByString(
                        item,
                        'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                    )

                    // Regex to pick WMS version
                    const wmsProtocol = UtilsService.getPropByString(
                        item,
                        'gmd:CI_OnlineResource.gmd:protocol.gco:CharacterString'
                    )
                    let wmsVersion = '1.0'
                    const match = /((\d)+\.(\d)+(\.(\d)+)?)/g.exec(wmsProtocol)
                    if (match) {
                        wmsVersion = match[0]
                    }

                    let wmsAnchor = ''
                    if (anchor !== undefined) {
                        if (anchor.indexOf('simple') > -1) {
                            wmsAnchor = 'simple'
                        } else if (anchor.indexOf('complex') > -1) {
                            wmsAnchor = 'complex'
                        }
                    }

                    if (anchor !== undefined) {
                        if (
                            anchor.indexOf('anchor') > -1 &&
                            (anchor.indexOf('simple') > -1 ||
                                anchor.indexOf('complex') > -1)
                        ) {
                            arrayWMSandTMS.push({
                                linkText,
                                linkTextParsed: linkText
                                    .replace('${z}', '0')
                                    .replace('${x}', '0')
                                    .replace('${y}', '0'),
                                wmsImg: this.resultImage,
                                url: linkText,
                                name: wmsName,
                                version: wmsVersion,
                                anchor: wmsAnchor,
                                allName: wmsAllLayerName,
                                scoreText,
                                available
                            })
                        }
                    }
                } else if (protocol.indexOf('WebCoverageService') > -1) {
                    arrayWCS.push({
                        linkText,
                        linkTitle,
                        linkDescription,
                        titleBox,
                        scoreText,
                        available
                    })
                } else if (
                    protocol.indexOf('TiledMapService') > -1 &&
                    !(anchor.indexOf('complex') > 0) &&
                    linkText !== ''
                ) {
                    arrayWMSandTMS.push({
                        linkText,
                        linkTextParsed: linkText
                            .replace('${z}', '0')
                            .replace('${x}', '0')
                            .replace('${y}', '0'),
                        linkTitle,
                        wmsImg: this.resultImage,
                        LAYERS: UtilsService.getPropByString(
                            item,
                            'gmd:CI_OnlineResource.gmd:name.gco:CharacterString'
                        ),
                        scoreText,
                        available
                    })
                } else if (linkText !== '') {
                    if (linkTitle === '') {
                        linkTitle = linkText
                    }

                    arrayOther.push({
                        linkText,
                        linkTitle,
                        linkDescription,
                        protocol,
                        titleBox,
                        scoreText,
                        available
                    })
                }
            }
        }
        return {
            arrayPdf: { items: arrayPdf, title: 'PDF' },
            arrayTxt: { items: arrayTxt, title: 'Text' },
            arrayHtml: { items: arrayHtml, title: 'Hyperlinks' },
            arrayImg: { items: arrayImg, title: 'Image' },
            arrayXml: { items: arrayXml, title: 'XML' },
            arrayOther: { items: arrayOther, title: 'Other' },
            arrayWCS: { items: arrayWCS, title: 'WCS' },
            arrayWMSandTMS: { items: arrayWMSandTMS, title: 'WMS/TMS' }
        }
    }

    get linksEmpty() {
        for (const prop in this.links) {
            if (this.links[prop].items.length) {
                return false
            }
        }

        return true
    }

    private formatDate(date: string) {
        const dateObj = new Date(date)
        let displayDate = `${dateObj.toLocaleString(
            this.langLocale.replace(/_/g, '-'),
            { month: 'long' }
        )} ${dateObj.getDate()}, ${dateObj.getFullYear()}`
        if (this.langLocale === 'pl_PL') {
            displayDate = `${dateObj.getDate()} ${dateObj.toLocaleString(
                this.langLocale.replace(/_/g, '-'),
                { month: 'long' }
            )} ${dateObj.getFullYear()}`
        }
        return displayDate
    }

    private extractYoutubeVideoId(url) {
        const href = new URL(url)
        return href.searchParams.get('v')
    }

    private keywordSearch(keyword) {
        this.$store.dispatch(SearchActions.setKeyword, keyword)
        PopupCloseService.closePopup('metadata')
    }

    private statusCheckerMapping(score) {
        let scoreMsg = ''
        let scoreClass = ''
        if (score >= 0 && score < 20) {
            scoreMsg = 'Very unreliable (score ' + score + '%)'
            scoreClass = 'av-lowest'
        } else if (score >= 20 && score < 40) {
            scoreMsg = 'Frequently unavailable (score ' + score + '%)'
            scoreClass = 'av-low'
        } else if (score >= 40 && score < 60) {
            scoreMsg = 'Sometimes unavailable (score ' + score + '%)'
            scoreClass = 'av-med'
        } else if (score >= 60 && score < 80) {
            scoreMsg = 'Mostly available (score ' + score + '%)'
            scoreClass = 'av-high'
        } else if (score >= 80 && score <= 100) {
            scoreMsg = 'Very reliable (score ' + score + '%)'
            scoreClass = 'av-highest'
        } else {
            scoreMsg = 'No resource availability information'
            scoreClass = 'av-no-info'
        }
        return {
            scoreMsg,
            scoreClass
        }
    }

    private getStringData(propsPath: string) {
        const data = UtilsService.getPropByString(this.data, propsPath)
        if (data && typeof data === 'string') {
            return data
        }
        return '-'
    }

    private getResourceComments() {
        if (!this.data.userContributions) {
            return
        }
        if (!this.data.userContributions.comments) {
            const resultId =
                this.data.id ||
                UtilsService.getPropByString(
                    this.data,
                    'gmd:fileIdentifier.gco:CharacterString'
                )
            GeossSearchApiService.getComments(
                resultId,
                DataOrigin[this.dataSource]
            ).then((comments) => {
                this.$set(this.data.userContributions, 'comments', comments)
                this.showComments = true
            })
        } else {
            this.showComments = true
        }
    }

    private created() {
        if (this.platform === 'ZENODO') {
            if (
                this.data.metadata.resource_type &&
                this.data.metadata.resource_type.title
            ) {
                this.zenodoBadges[0] = {
                    label: this.data.metadata.resource_type.title,
                    color: 'default'
                }
            }
            if (this.data.metadata.access_right) {
                this.zenodoBadges[1] = {
                    label: this.data.metadata.access_right + ' access',
                    color: this.data.metadata.access_right_category
                }
            }
        }
    }

    private async mounted() {
        await this.$nextTick()
        this.map = new AppVueObj.ol.Map({
            target: 'infoMap',
            layers: [LayerTilesService.WorldStreetBasemap.getLayerTile()[0]],
            view: new AppVueObj.ol.View({
                center: AppVueObj.ol.proj.fromLonLat([10, 20]),
                zoom: 1,
                minZoom: 1,
                maxZoom: 10
            }),
            controls: [],
            interactions: []
        })
        this.getResourceComments()

        const layer = ResultService.getFeature(this.data, 0)
        if (layer) {
            this.map.addLayer(layer)
            const extent = layer.getSource().getExtent()
            this.map.getView().fit(extent)
        }
    }
}
</script>

<style lang="scss">
.metadata {
    padding: 30px 25px;

    .youtube-video {
        position: relative;
        width: 100%;
        height: 0;
        padding-bottom: 56.25%;

        iframe {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
        }
    }

    .top-data {
        align-items: center;
        display: flex;
        font-size: 0.85em;
        justify-content: space-between;

        .badge {
            background-color: #777;
            margin-left: 5px;
            display: inline-block;
            text-transform: capitalize;
            padding: 0.3em 0.6em;
            color: #fff;
            border-radius: 0.25em;

            &.primary {
                background-color: #428bca;
            }

            &.success {
                background-color: #5cb85c;
            }

            &.info {
                background-color: #6aa3d5;
            }

            &.warning {
                background-color: #f0ad4e;
            }

            &.danger {
                background-color: #d9534f;
            }
        }
    }

    &__title {
        color: $black;
        font-weight: bold;
        margin-bottom: 15px;
        font-size: 20px;
    }

    &__authors,
    &__license,
    &__citation {
        margin-bottom: 10px;
        color: #777;
        font-size: 14px;
    }

    &__description {
        margin-top: 5px;
        line-height: normal;
    }

    &__preview {
        float: left;
        margin-right: 10px;
        margin-bottom: 10px;

        img {
            max-width: 100%;
            max-height: 188px;
            width: auto;
            height: auto;
        }
    }

    &__info-table {
        display: flex;
        color: $black;
        margin-left: -25px;
        width: calc(100% + 50px);
        padding: 25px 0;
        position: relative;
        line-height: 20px;
        margin-bottom: 25px;
        word-break: break-word;

        @media (max-width: $breakpoint-sm) {
            flex-wrap: wrap;

            &>div {
                width: 100%;
                flex: unset !important;

                &:first-child {
                    border-bottom: 2px solid white;
                    padding-bottom: 10px;
                    margin-bottom: 20px;
                }
            }
        }

        &:before {
            content: '';
            width: 1px;
            height: 100%;
            background: $grey;
            position: absolute;
            left: calc(50% - 1px);
            top: 0;

            @media (max-width: $breakpoint-sm) {
                display: none;
            }
        }

        &>div {
            flex: 1;
            padding: 0 25px;

            &>div {
                margin-bottom: 5px;

                &:first-child {
                    margin-bottom: 10px;

                    b {
                        font-weight: bold;
                        color: $black;
                        font-size: 18px;
                    }
                }

                b {
                    font-weight: 400;
                    padding-right: 5px;
                    color: $grey-medium;
                }
            }
        }
    }

    &__keywords,
    &__additional-info,
    &__references {
        padding-bottom: 15px;
        margin-bottom: 15px;

        &-title {
            font-weight: bold;
            color: $black;
            font-size: 18px;
            margin-bottom: 15px;
        }

        &-wrap {
            align-items: flex-start;
            display: flex;
            flex-direction: row;
            flex-wrap: wrap;
        }
    }

    &__keyword {
        background: $blue;
        border-radius: 999px;
        color: $white;
        cursor: pointer;
        display: inline-block;
        margin: 0 10px 10px 0;
        padding: 7px 12px;
    }

    &__additional-info-item {
        margin-bottom: 5px;
    }

    &__reference {
        margin-bottom: 10px;
    }

    &__references-list {
        list-style: initial;
        padding-left: 20px;
    }

    &__coordinates {
        padding: 25px;
        margin-left: -25px;
        width: calc(100% + 50px);
        color: $black;
        display: flex;
        flex-wrap: wrap;
        line-height: 20px;
        margin-bottom: 25px;

        &__title {
            color: $black;
            font-size: 18px;
            font-weight: bold;
            flex: 100%;
            margin-bottom: 15px;
        }

        &>div:nth-child(n + 2) {
            flex: 1;
            display: flex;
            flex-direction: column;
            position: relative;

            &>b {
                color: $grey-medium;
            }
        }

        &>div:nth-child(2)::before {
            content: '';
            width: 1px;
            height: 100%;
            background: $grey;
            position: absolute;
            left: calc(100% - 1px);
            top: 0;
        }

        &>div:nth-child(3) {
            padding-left: 20px;
        }
    }

    &__links {
        &-title {
            color: $black;
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
        }
    }

    &__links-section {
        margin-bottom: 20px;
        word-wrap: break-word;

        &-title {
            display: flex;
            align-items: center;
            margin-bottom: 10px;

            img {
                margin-right: 10px;
            }
        }
    }

    &__link {
        margin-left: 40px;
        padding-left: 20px;
        position: relative;
        margin-bottom: 15px;

        &:before {
            content: '';
            position: absolute;
            left: 0;
            top: 2px;
            width: 10px;
            height: 10px;
            background: #aaa;
            border-radius: 50%;
        }

        a,
        div {
            margin-bottom: 5px;
            display: block;

            &.protocol {
                font-size: 0.85em;
                color: $grey-medium;
            }
        }
    }
}


.confidence {
    display: flex;
    align-items: center;
    font-size: 12px;
    gap: 10px;
    margin-bottom: 5px;

    &__number {
        font-size: 14px;
    }

    &__type {
        font-size: 8px;
        text-wrap: nowrap;
    }

    &__box {
        display: flex;
        background: #eee;
        align-items: center;
        flex-direction: column;
        width: 45px;
        height: 45px;
        border-radius: 50%;
        justify-content: center;
    }
}
</style>
