<template>
<div class="sub-page">
    <div class="sub-page__content">
        <div class="my-workspace-header">
            GEOSS Views
            <NuxtLink to="/" class="close-window">
                <div class="line-1"></div>
                <div class="line-2"></div>
            </NuxtLink>
        </div>
        <div class="my-workspace-tab my-workspace-content geoss-views">
            <div class="geoss-views__column">
                <div class="geoss-views__group">
                    <h2>Token</h2>
                    <select v-model="token" @change="tokenChanged()">
                        <option v-for="token of tokens" :value="token.value" :key="token.value">{{
                            token.value }} - {{
        token.name }}</option>
                    </select>
                </div>
                <!-- <div class="geoss-views__group">
                    <h2>Sources</h2>
                    <select id="sources" @change="sourceChange($event.target)" multiple>
                        <option v-for="availableSource of availableSources" :value="availableSource.id"
                            :key="availableSource.id">{{
                                availableSource.title }}</option>
                    </select>
                </div> -->
                <div class="geoss-views__group">
                    <h2>Your Views</h2>
                    <span v-if="!availableViews.length">None yet</span>
                    <ul id="views" class="geoss-views__views">
                        <li v-for="availableView of availableViews" :value="availableView.id" :key="availableView.id">
                            <span>{{ availableView.label }} [{{ availableView.id }}]</span>
                            <span>
                                <button class="update" @click="selectView(availableView)">update</button>
                                <button class="delete" @click="deleteView(availableView)">delete</button>
                            </span>
                        </li>
                    </ul>

                </div>
            </div>
            <form class="geoss-views__column" @submit="submit($event)">
                <div class="geoss-views__group">
                    <h2>View Editor</h2>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">View ID: *</span>
                        <input placeholder="view ID" v-model="id" type="text" required />
                        <button class="default" @click="generateNewId()">generate new id</button>
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">View Label: *</span>
                        <input placeholder="view label" v-model="label" type="text" required />
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">View Visibility: *</span>
                        <label for="viewVisibleFalse">
                            <input id="viewVisibleFalse" type="radio" value="false" name="viewVisible"
                                @change="setVisible('private')" v-model="visible" />
                            Private
                        </label>
                        <label for="viewVisibleTrue">
                            <input id="viewVisibleTrue" type="radio" value="true" name="viewVisible"
                                @change="setVisible('public')" v-model="visible" />
                            Public
                        </label>
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">Sources:</span>
                        <select id="sources" @change="sourceChange($event.target)" multiple>
                            <option v-for="availableSource of availableSources" :value="availableSource.id"
                                :key="availableSource.id" :selected="sources.indexOf(availableSource.id) > -1">{{
                                    availableSource.title }}</option>
                        </select>
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">Keywords:</span>
                        <input placeholder="keywords (sep: ',')" v-model="keywords" type="text" />
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">Parent View ID:</span>
                        <input placeholder="parent view ID" v-model="parentView" type="text" />
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">Where:</span>
                        <label class="where_label">South:</label><input style="width: 50px" placeholder="-" v-model="south" type="number" />
                        <label class="where_label">West:</label><input style="width: 50px" placeholder="-" v-model="west" type="number" />
                        <label class="where_label">North:</label><input style="width: 50px" placeholder="-" v-model="north" type="number" />
                        <label class="where_label">East:</label><input style="width: 50px" placeholder="-" v-model="east" type="number" />
                    </div>
                    <div class="geoss-views__group__sub">
                        <span class="geoss-views__label">When:</span>
                        <input style="width: 110px" type="date" placeholder="from" v-model="from" />
                        <input style="width: 110px" type="date" placeholder="to" v-model="to" />
                    </div>
                </div>
                <div class="geoss-views__legend">
                    * required fields
                </div>
                <div class="geoss-views__buttons">
                    <button class="blue-btn-default" @click="updateView()">Update View</button>
                    <button class="green-btn-default" @click="createView()">Create View</button>
                </div>
            </form>
        </div>
    </div>
</div>
</template>

<script type="ts">
import SpinnerService from '@/services/spinner.service';
import NotificationService from '@/services/notification.service';

export default {
    data() {
        return {
            id: '',
            label: '',
            visible: false,
            keywords: '',
            parentView: '',
            south: '',
            west: '',
            north: '',
            east: '',
            from: '',
            to: '',
            apiMessage: '',
            views: [],
            availableViews: [],
            sources: [],
            availableSources: [],
            token: '',
            tokens: [{
                value: 'geossWriteTest',
                name: 'this can read/write and make a view visible'
            }, {
                value: 'geossPrivateWriteTest',
                name: 'this can read/write and can NOT make a view visible'
            }, {
                value: 'geossReadTest',
                name: 'this can read'
            }],
        }
    },
    methods: {
        tokenChanged() {
            this.getViews();
        },
        sourceChange(options) {
            this.sources = Array.from(options).filter(e => e.selected).map(e => e._value);
        },
        viewsChange(options) {
            this.views = Array.from(options).filter(e => e.selected).map(e => e._value);
        },
        prepareViewData() {
            const viewData = {
                id: this.id,
                label: this.label,
                visible: this.visible
            };

            if (this.south !== '' && this.west !== '' && this.north !=='' && this.east != '') {
                viewData.where = {
                    south: this.south,
                    west: this.west,
                    north: this.north,
                    east: this.east,
                }
            }

            if (this.sources.length) viewData.sources = this.sources;
            if (this.keywords !== '') viewData.keywords = this.keywords.split(',').map(e => e.trim());
            if (this.parentView !== '') viewData.parentView = this.parentView;
            if (this.from !== '' && this.to !== '') {
                viewData.when = {
                    from: this.from,
                    to: this.to,
                }
            }

            return viewData;
        },
        setVisible(visibility) {
            if (visibility === 'public') {
                this.visible = true;
            } else {
                this.visible = false;
            }
        },
        selectView(viewData) {
            this.id = viewData.id || '';
            this.label = viewData.label || '';
            this.visible = viewData.visible || false;
            this.sources = viewData.sources || [];
            this.keywords = viewData.keywords ? viewData.keywords.join(',') : '';
            this.parentView = viewData.parentView || '';
            this.south = viewData.where ? viewData.where.south : 0;
            this.west = viewData.where ? viewData.where.west : 0;
            this.north = viewData.where ? viewData.where.north : 0;
            this.east = viewData.where ? viewData.where.east : 0;
            this.from = viewData.when ? viewData.when.from : '';
            this.to = viewData.when ? viewData.when.to : '';
        },
        generateNewId() {
            this.id = this.getRandomId();
            return false;
        },
        submit(event) {
            event.preventDefault();
        },
        getRandomId() {
            return Math.random().toString(36).slice(2)
        },
        async getSources() {
            this.fetchWithToken('https://gs-service-preproduction.geodab.eu/gs-service/services/essi/rest-views/sources?', 'availableSources')
        },
        async getViews() {
            this.fetchWithToken('https://gs-service-preproduction.geodab.eu/gs-service/services/essi/rest-views/views?count=9999', 'availableViews')
        },
        createView() {
            if (this.id === '' || this.label === '') return;
            SpinnerService.showSpinner();
            const data = this.prepareViewData();
            try {
                fetch('https://gs-service-preproduction.geodab.eu/gs-service/services/essi/rest-views/views?token=' + this.token, {
                    method: 'POST',
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data)
                })
                    .then(async (r) => {
                        if (!r.ok) {
                            SpinnerService.hideSpinner();
                            NotificationService.show(
                                'Create View',
                                `Error while creating view.
                                Status: ${r.status}
                                ${r.statusText}`,
                                10000,
                                null,
                                9999,
                                'error'
                            );
                            return;
                        }
                        const response = await r.json()
                        if (response.err) this.apiMessage = response.err;
                        if (response.msg) this.apiMessage = response.msg;
                        this.getViews()
                        return response;
                    }).then(r => {
                        SpinnerService.hideSpinner()
                        NotificationService.show(
                            'Create View',
                            this.apiMessage,
                            10000,
                            null,
                            9999,
                            r.err ? 'error' : 'success'
                        );
                    })
                    .then(r => SpinnerService.hideSpinner())
            } catch (err) {
                console.warn(err)
                SpinnerService.hideSpinner()
            }

        },
        updateView() {
            if (this.id === '' || this.label === '') return;
            SpinnerService.showSpinner();
            const data = this.prepareViewData();
            try {
                fetch('https://gs-service-preproduction.geodab.eu/gs-service/services/essi/rest-views/views/' + data.id + '?token=' + this.token, {
                    method: 'PUT',
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(data)
                })
                    .then(async (r) => {
                        const response = await r.json()
                        if (response.err) this.apiMessage = response.err;
                        if (response.msg) this.apiMessage = response.msg;
                        this.getViews()
                        return response;
                    }).then(r => {
                        SpinnerService.hideSpinner()
                        NotificationService.show(
                            'Update View',
                            this.apiMessage,
                            10000,
                            null,
                            9999,
                            r.err ? 'error' : 'success'
                        );
                    })
            } catch (err) {
                console.warn(err)
                SpinnerService.hideSpinner()
            }
        },
        deleteView(viewData) {
            SpinnerService.showSpinner();
            try {
                fetch('https://gs-service-preproduction.geodab.eu/gs-service/services/essi/rest-views/views/' + viewData.id + '?token=' + this.token, {
                    method: 'DELETE',
                    headers: {
                        "Content-Type": "application/json",
                    },
                })
                    .then(async (r) => {
                        const response = await r.json()
                        if (response.err) this.apiMessage = response.err;
                        if (response.msg) this.apiMessage = response.msg;
                        this.getViews()
                        return response;
                    }).then(r => {
                        SpinnerService.hideSpinner()
                        NotificationService.show(
                            'Delete View',
                            this.apiMessage,
                            10000,
                            null,
                            9999,
                            r.err ? 'error' : 'success'
                        );
                    })
            } catch (err) {
                console.warn(err)
                SpinnerService.hideSpinner()
            }
        },
        async getGwpToken() {
            await fetch('https://gs-service-preproduction.geodab.eu/gs-service/services/essi/rest/gpwtoken', {
                headers: {
                    mirrorsiteclient: 'GWP-748f4cca-bed0-4d1f-93f0-31eae3cf7c61'
                }
            })
            .then(r => r.json())
            .then(r => {
                if (r.token) {
                    this.token = r.token;
                    this.tokens.splice(0, 0, {
                        value: r.token,
                        name: 'Auto generated token - ' + (new Date()).toISOString()
                    })
                }
            })
        },
        fetchError(text) {
            console.warn(text);
            if (text.length >= 100) {
                text = text.substr(0, 97) + '...';
            }
            NotificationService.show(
                'Error',
                text,
                10000,
                null,
                9999,
                'error'
            );
        },
        async fetchWithToken(url, data) {
            SpinnerService.showSpinner();
            const response = await fetch(url + '&token=' + this.token)
            if (response.ok) {
                try {
                    const json = await response.json();
                    this[data] = data === 'availableSources' ? json : json.views;
                } catch(error) {
                    this[data] = [];
                    this.fetchError(error)
                }
            } else {
                this[data] = [];
                const text = await response.text();
                this.fetchError(text)
            }
            SpinnerService.hideSpinner();
        }
    },
    async mounted() {
        await this.getGwpToken();
        this.generateNewId();
        this.getSources();
        this.getViews();
    }
}
</script>

<style lang="scss">
.geoss-views {
    display: flex;
    gap: 30px;

    @media (max-width: $breakpoint-lg) {
        flex-direction: column;
    }

    button {
        font-size: 0.8em;
        text-transform: uppercase;

        &.default:hover {
            color: $green;
        }

        &.update:hover {
            color: $blue;
        }

        &.delete:hover {
            color: $red;
        }
    }

    input[type="text"],
    input[type="number"],
    input[type="date"] {
        border: 1px solid grey
    }

    &__views {
        li {
            display: flex;
            justify-content: space-between;
            padding: 5px 10px;
            align-items: center;

            &:hover {
                background: rgba(255, 255, 255, 0.4);
            }

            +li {
                border-top: 1px solid grey;
            }

            > span {
               display: flex;
               gap: 5px;
            }
        }
    }

    &__legend {
        margin: 15px 0;
        padding: 15px 0;
        font-size: 0.85em;
        border-top: 1px solid gray;
    }

    &__buttons {
        display: flex;
        justify-content: flex-end;
        gap: 10px;

        button {
            text-transform: none;
            font-size: 1.1em;
        }
    }

    &__message {
        margin: 15px 0;
    }

    &__column {
        width: 34%;

        &:nth-child(2) {
            width: 66%;
        }

        @media (max-width: $breakpoint-lg) {
            width: 100% !important;
        }
    }

    &__group {
        margin: 0 0 15px 0;

        &__sub {
            display: flex;
            align-items: center;
            gap: 10px;
            margin: 0 0 15px 0;

            input[type="text"],
            select {
                width: 50% !important
            }

            .where_label {
                font-size: 0.85em;
                margin-right: -7px;
            }
        }

        h2 {
            margin: 5px 0 10px;
            font-size: 1.2em;
        }

        select {
            width: 100%;
        }

        select,
        option,
        input {
            padding: 3px 5px;
        }
    }

    &__label {
        min-width: 20%;
    }
}
</style>
