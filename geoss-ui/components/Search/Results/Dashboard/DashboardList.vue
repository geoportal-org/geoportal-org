<template>
    <div class="dashboard-list">
        <div v-show="loading">{{ $tc('popupContent.loadingData') }}...</div>
        <div v-show="!loading && (!dashboardList || !dashboardList.length)">
            {{ $tc('popupContent.noDashboardsAvailable') }}
        </div>
        <div
            v-for="dashboard of dashboardList"
            :key="dashboard.workflowWrapperId"
            class="dashboard-list__item"
        >
            <div class="dashboard-list__item__left">
                <div class="dashboard-list__item__title">
                    {{ dashboard.title || '-' }}
                </div>
                <div class="dashboard-list__item__summary">
                    {{ dashboard.summary || '-' }}
                </div>
            </div>
            <div class="button-container">
                <button
                    class="button-outlined"
                    @click="handleDelete(dashboard)"
                >
                    <span>{{ $tc('popupContent.delete') }}</span>
                </button>
                <button
                    class="blue-btn-default"
                    @click="preview(dashboard.content)"
                >
                    <span>{{ $tc('popupContent.preview') }}</span>
                    <img :src="`/svg/external-link.svg`" />
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator'
import DashboardService from '@/services/dashboard.service'
import NotificationService from '~/services/notification.service'
import { PopupActions } from '~/store/popup/popup-actions'
import GeneralPopup from '@/components/GeneralPopup.vue'
import PopupCloseService from '~/services/popup-close.service'

@Component({})
export default class DashboardList extends Vue {
    public dashboardList: any = []
    public loading = false
    public currentDashboardId = ''

    public preview(content: string) {
        DashboardService.showDashboard(
            content,
            this.$tc('popupTitles.dashboards')
        )
        NotificationService.show(
            `${this.$tc('popupTitles.dashboards')}`,
            `${this.$tc('notifications.dashboardLoadedSuccessfully')}`,
            10000,
            'dashboard-load-success',
            9999,
            'info'
        )
    }

    public handleDelete(content: any) {
        if (content && content.dashboardId) {
            this.currentDashboardId = content.dashboardId
            const props = {
                title: this.$tc('popupContent.deleteDashboardTitle'),
                subtitle: this.$tc('popupContent.deleteDashboardConfirmation'),
                actions: [
                    {
                        event: {
                            id: 'dashboard-remove-yes'
                        },
                        label: this.$tc('general.yes')
                    },
                    {
                        event: {
                            id: 'dashboard-remove-no'
                        },
                        label: this.$tc('general.no')
                    }
                ]
            }
            this.$store.dispatch(PopupActions.openPopup, {
                contentId: 'general',
                component: GeneralPopup,
                props
            })
        }
    }

    public async deleteDashboard() {
        await DashboardService.deleteDashboard(this.currentDashboardId)
        await this.getDashboards()
        NotificationService.show(
            `${this.$tc('popupTitles.dashboards')}`,
            `${this.$tc('notifications.dashboardDeletedSuccessfully')}`,
            4000,
            'dashboard-load-success',
            9999,
            'info'
        )
        this.currentDashboardId = ''
    }

    public async getDashboards() {
        this.loading = true
        const data = await DashboardService.getAllDashboards()
        const list: any[] = []
        if (data && data.content) {
            {
                data.content.forEach((dashboard: any) => {
                    const jsonString =
                        dashboard.entry.dashboardContents.content.replace(
                            /\'/g,
                            '"'
                        )
                    dashboard.entry.content = JSON.parse(
                        JSON.stringify(jsonString)
                    )
                    list.push({ dashboardId: dashboard.id, ...dashboard.entry })
                })
            }
        }
        this.dashboardList = list
        this.loading = false
    }

    private async mounted() {
        this.getDashboards()
        PopupCloseService.eventBus.$on(
            'close',
            ({
                contentId,
                response
            }: {
                contentId: string
                response?: any
            }) => {
                if (response && response.id === 'dashboard-remove-yes') {
                    this.deleteDashboard()
                }
            }
        )
    }
}
</script>

<style lang="scss">
.button-container {
    display: flex;
    flex-direction: row;
    gap: 10px;
}

.button-outlined {
    border-left: 1px solid;
    border-right: 1px solid;
    border-color: lightgray;
    color: #0661a9;
    font-size: large;
    &:focus {
        outline: none;
    }
}
.dashboard-list {
    padding: 20px 30px;

    &__item {
        padding: 15px 0;
        border-bottom: 1px solid $grey;
        margin-bottom: 15px;
        justify-content: space-between;
        display: flex;

        &:last-child {
            margin-bottom: 0;
            border: none;
        }

        &__left {
            margin-right: 15px;
            padding-right: 15px;
            max-width: calc(100% - 115px);

            @media (max-width: $breakpoint-lg) {
                margin-right: 5px;
                padding-right: 5px;
                max-width: calc(100% - 120px);
            }
        }

        &__title {
            font-weight: bold;
            font-size: 1.2em;
            margin-bottom: 5px;
        }

        @media (max-width: $breakpoint-lg) {
            &__summary {
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }
        }

        button {
            display: flex;
            align-items: center;
            min-width: 120px;
            justify-content: center;

            img {
                margin-left: 6px;
                margin-bottom: 2px;
            }
        }
    }
}
</style>
