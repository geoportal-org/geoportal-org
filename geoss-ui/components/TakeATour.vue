<template>
    <a target="_blank" class="take-a-tour" :href="tourUrl">
		<i class="icomoon-tour"></i>
	</a>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { SearchEngineGetters } from '@/stores/search-engine/search-engine-getters';

@Component
export default class TakeATourComponent extends Vue {
	get tourUrl() {
		return this.$store.getters[SearchEngineGetters.tourUrl];
	}
}
</script>

<style lang="scss" scoped>
.take-a-tour {
	position: fixed;
	bottom: 10px;
	right: 10px;
	z-index: 10;
	color: white;
	display: flex;
	align-items: center;
	text-decoration: none;

	@media (max-width: $breakpoint_sm) {
		bottom: 40px;
	}

	@media (max-width: $breakpoint_xs) {
		display: none;
	}
	
	i {
		text-shadow: $text-shadow-black-darker;
		font-size: 22px;
	}

	&:hover {
		i {
			animation: wobble 0.5s both;
		}
	}
}

.yellow-pages,
.bookmarks {
	.take-a-tour {
		@media (max-width: $breakpoint_xs) {
			display: block;
			bottom: 8px;
		}
	}
}
</style>