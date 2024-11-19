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
            <div>
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

@Component({})
export default class DashboardList extends Vue {
    public dashboardList: any = []
    public loading = false

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

    private async mounted() {
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
                    list.push(dashboard.entry)
                })
            }
        }
		console.log(data)
        this.dashboardList = list
        this.loading = false
    }
}
</script>

<style lang="scss">
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
