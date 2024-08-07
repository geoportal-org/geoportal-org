<template>
	<div v-if="data" class="dashboard-display">
		<div v-if="preview" class="dashboard-display__preview">
			<span>{{ $tc('popupContent.dashboardInPreview') }} <a @click="closePreview()">{{ $tc('popupContent.closePreview') }}</a> {{ $tc('popupContent.goBackToCreator') }}.</span>
			<button class="blue-btn-default" @click="closePreview()">{{ $tc('popupContent.closePreview') }}</button>
		</div>
		<div class="dashboard-display__header">
			<div>
				<div class="dashboard-display__title">
					{{ data.title }}
				</div>
				<div class="dashboard-display__run-id">
					{{ $tc('popupContent.dashboardBasedOn') }}: {{ data.runId }}
				</div>
			</div>
			<DashboardPagination :currentPage="currentPage" :pageCount="data.pages.length" @previous="previousPage()" @next="nextPage()" />
		</div>
		<template v-for="(page, pageIndex) of data.pages">
			<div v-if="(pageIndex + 1 === Number(currentPage))" class="layout" :class="'layout-' + page.layoutClass" :key="pageIndex">
				<section v-for="(field, fieldIndex) of page.fields" :key="fieldIndex">
					<DashboardRichText v-if="field.type === 'richtext'" :textData="field.value" mode="display"/>
					<DashboardMap v-if="field.type === 'map'" :mapData="field.value"/>
					<DashboardChart v-if="field.type === 'chart'" :chartData="field.value" mode="display"/>
				</section>
			</div>
		</template>
		<div class="dashboard-display__footer">
			<DashboardPagination :currentPage="currentPage" :pageCount="data.pages.length" @previous="previousPage()" @next="nextPage()" />
		</div>
	</div>
</template>

<script lang="ts">
import { Prop, Component, Vue } from 'nuxt-property-decorator';
import DashboardRichText from './DashboardRichText.vue';
import DashboardMap from './DashboardMap.vue';
import DashboardChart from './DashboardChart.vue';
import DashboardPagination from './DashboardPagination.vue';

@Component({
	components: {
		DashboardRichText,
		DashboardMap,
		DashboardChart,
		DashboardPagination
	}
})
export default class DashboardDisplayComponent extends Vue {
	@Prop(Object) public data!: any;
	@Prop({required: false, type: Boolean}) public preview!: boolean;

	public currentPage = 1;

	public previousPage() {
		if (this.currentPage > 1) {
			this.currentPage--;
		}
	}

	public nextPage() {
		if (this.currentPage < this.data.pages.length) {
			this.currentPage++;
		}
	}

	public closePreview() {
		this.$emit('closePreview');
	}
}
</script>

<style lang="scss" scoped>
.dashboard-display {
	padding: 30px 25px;

	&__preview {
		width: calc(100% + 60px);
		display: flex;
		justify-content: space-between;
		align-items: center;
		color: $white;
		padding: 30px 30px 30px 80px;
		margin: -30px -30px 0 -30px;
		background: url('/svg/info.svg') 25px 18px no-repeat $green;
		background-size: 40px;

		a {
			text-decoration: underline;
			cursor: pointer;
		}

		.blue-btn-default {
			text-transform: uppercase;
			font-size: 0.85em;
		}
	}

	&__title {
		font-weight: bold;
		margin-bottom: 5px;
		font-size: 1.2em;
	}

	&__run-id {
		color: $grey-dark;
	}

	&__header,
	&__footer {
		border-bottom: 1px solid $grey;
		padding: 10px 0;
		margin: 10px 0;
		display: flex;
		justify-content: space-between;

		@media(max-width: $breakpoint-lg) {
			display: flex;
			flex-direction: column;
		}
	}

	&__footer {
		border-top: 1px solid $grey;
		border-bottom: none;
		justify-content: flex-end;
	}

	.layout {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		grid-column-gap: 10px;
		grid-row-gap: 10px;

		&-1 {
			section:nth-child(1) { grid-area: 1 / 1 / 3 / 3; }
		}
		&-2 {
			section:nth-child(1) { grid-area: 1 / 1 / 3 / 2; }
			section:nth-child(2) { grid-area: 1 / 2 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
			}
		}
		&-3 {
			section:nth-child(1) { grid-area: 1 / 1 / 3 / 2; display: grid; align-items: center; }
			section:nth-child(2) { grid-area: 1 / 2 / 2 / 3; }
			section:nth-child(3) { grid-area: 2 / 2 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
		&-4 {
			section:nth-child(1) { grid-area: 1 / 1 / 2 / 2; }
			section:nth-child(2) { grid-area: 2 / 1 / 3 / 2; }
			section:nth-child(3) { grid-area: 1 / 2 / 3 / 3; display: grid; align-items: center; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
		&-5 {
			section:nth-child(1) { grid-area: 1 / 1 / 2 / 2; }
			section:nth-child(2) { grid-area: 1 / 2 / 2 / 3; }
			section:nth-child(3) { grid-area: 2 / 1 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
		&-6 {
			section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
			section:nth-child(2) { grid-area: 2 / 1 / 3 / 2; }
			section:nth-child(3) { grid-area: 2 / 2 / 3 / 3; }
			@media(max-width: $breakpoint-lg) {
				section:nth-child(1) { grid-area: 1 / 1 / 2 / 3; }
				section:nth-child(2) { grid-area: 2 / 1 / 3 / 3; }
				section:nth-child(3) { grid-area: 3 / 1 / 4 / 3; }
			}
		}
	}
}
</style>
