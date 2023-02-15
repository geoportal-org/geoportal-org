<template>
	<Pagination :start-index="pageOffset"
		:results-per-page="resultsPerPage"
		:total="total"
		@on-start-index-change="onPageOffsetChange($event)"/>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

import Pagination from '@/components/Pagination.vue';

import { YellowPagesFiltersGetters } from '@/stores/yellow-pages-filters/yellow-pages-filters-getters';
import { YellowPagesFiltersActions } from '@/stores/yellow-pages-filters/yellow-pages-filters-actions';

@Component({
	components: {
		Pagination
	}
})
export default class YellowPagesPaginationComponent extends Vue {
	get resultsPerPage() {
		return this.$store.getters[YellowPagesFiltersGetters.perPage];
	}

	get pageOffset() {
		return this.$store.getters[YellowPagesFiltersGetters.pageOffset];
	}

	get total() {
		return this.$store.getters[YellowPagesFiltersGetters.resultsTotal];
	}

	public onPageOffsetChange(pageOffset: number) {
		this.$store.dispatch(YellowPagesFiltersActions.setPageOffset, pageOffset);
		this.$store.dispatch(YellowPagesFiltersActions.getResults);
	}
}
</script>