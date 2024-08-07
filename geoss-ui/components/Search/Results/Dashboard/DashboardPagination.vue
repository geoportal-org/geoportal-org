<template>
	<div v-if="pageCount > 1" class="dashboard-pagination">
		<button class="dashboard-pagination__button" :class="{disabled: currentPage === 1}" @click="previous()">&olt;</button>
		<span class="dashboard-pagination__text">{{ `${$tc('popupContent.page')} ${currentPage} ${$tc('popupContent.of')} ${pageCount}` }}</span>
		<button class="dashboard-pagination__button"  :class="{disabled: currentPage === pageCount}" @click="next()">&ogt;</button>
	</div>
</template>

<script lang="ts">
import { Prop, Component, Vue, Emit } from 'nuxt-property-decorator';

@Component({})
export default class DashboardPaginationComponent extends Vue {
	@Prop({default: 1, type: Number}) public currentPage!: number;
	@Prop({default: 1, type: Number}) public pageCount!: number;

	@Emit()
	public previous() {
		return true;
	}

	@Emit()
	public next() {
		return true;
	}
}
</script>

<style lang="scss" scoped>
.dashboard-pagination {
	display: flex;
	align-items: center;

	@media(max-width: $breakpoint-lg) {
		align-self: flex-end;
	}

	&__button {
		font-size: 1.5em;

		&.disabled {
			opacity: 0.33;
		}
	}

	&__text {
		margin: 0 5px;
		font-size: 0.85em;
		display: inline-block;
		padding-top: 2px;
	}
}
</style>
