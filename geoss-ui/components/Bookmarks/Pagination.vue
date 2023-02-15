<template>
	<Pagination v-if="results.length"
		:show-summary="true"
		:start-index="pageOffset"
		:results-per-page="resultsPerPage"
		:total="total"
		@on-start-index-change="onPageOffsetChange($event)"/>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';

import Pagination from '@/components/Pagination.vue';

import { BookmarksGetters } from '@/stores/bookmarks/bookmarks-getters';
import { BookmarksActions } from '@/stores/bookmarks/bookmarks-actions';

@Component({
	components: {
		Pagination
	}
})
export default class BookmarksPaginationComponent extends Vue {
	get results() {
		return this.$store.getters[BookmarksGetters.results];
	}

	get resultsPerPage() {
		return this.$store.getters[BookmarksGetters.perPage];
	}

	get pageOffset() {
		return this.$store.getters[BookmarksGetters.pageOffset];
	}

	get total() {
		return this.$store.getters[BookmarksGetters.resultsTotal];
	}

	public onPageOffsetChange(pageOffset: number) {
		this.$store.dispatch(BookmarksActions.setPageOffset, pageOffset);
		this.$store.dispatch(BookmarksActions.getResults);
	}
}
</script>