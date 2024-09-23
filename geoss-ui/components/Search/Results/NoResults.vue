<template>
	<div>
		<div class="search-no-results">
			No results found
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';

@Component({})
export default class NoResultsComponent extends Vue {}

</script>

<style lang="scss" scoped>
.search-no-results {
	display: flex;
	justify-content: center;
	align-content: center;
	background: url('/svg/survey-background.svg') center center no-repeat $white;
	background-size: cover;
	padding: 140px 20px;
	color: $grey-medium;
}
</style>
