<template>
	<div class="my-workspace-header">
		<h2>
			{{ $t('feedback.title') }}
		</h2>
		<button class="close-window" @click="closeWindowAndredirectToSearch()">
			<div class="line-1"></div>
			<div class="line-2"></div>
		</button>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class HeaderComponent extends Vue {
	public closeWindowAndredirectToSearch() {
		if (sessionStorage.getItem('RECENT_SEARCH_PARAMS')) {
			window.location.href =
				window.location.origin + sessionStorage.getItem('RECENT_SEARCH_PARAMS');
		} else {
			window.location.href = window.location.origin;
		}
	}
}
</script>

<style lang="scss" scoped>
@import '@/pages/other-pages/other-pages.scss';
</style>
