<template>
	<Pagination :start-index="startIndex"
		:results-per-page="resultsPerPage"
		:total="total"
		:initial-index="1"
		@on-start-index-change="onStartIndexChange($event)"/>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

import Pagination from '@/components/Pagination.vue';

import { SearchActions } from '@/stores/search/search-actions';
import { GeneralFiltersGetters } from '@/stores/general-filters/general-filters-getters';
import { SearchGetters } from '@/stores/search/search-getters';

@Component({
	components: {
		Pagination
	}
})
export default class SearchResultsPaginationComponent extends Vue {
	get resultsPerPage() {
		return this.$store.getters[GeneralFiltersGetters.state].resultsPerPage || 0;
	}

	get startIndex() {
		return this.$store.getters[SearchGetters.startIndex] || 0;
	}

	get total() {
		return this.$store.getters[SearchGetters.currentResultsTotal] || 0;
	}

	public onStartIndexChange(startIndex: number) {
		this.$store.dispatch(SearchActions.setStartIndex, {value: startIndex, dataSource: this.$store.getters[SearchGetters.dataSource]});
		this.$store.dispatch(SearchActions.getResults, {theSameTab: true});
	}
}
</script>