<template>
	<div class="yellow-pages-header">
		<div class="yellow-pages-header__title">{{$t('yellowPages.title')}}</div>
		<div class="yellow-pages-header__search">
			<input type="text" v-model="search" :placeholder="$t('yellowPages.placeholderSearch')" />
		</div>
		<div class="yellow-pages-header__order-by">
			<div class="yellow-pages-header__order-by-title">{{$t('yellowPages.orderBy')}}</div>
			<CustomSelect
				class="yellow-pages-header__order-by-filter" 
				v-model="orderBy"
				:appendToBody="false"
				:options="orderByOptions"
				:clearable="false" />
		</div>
		<button class="close-window" @click="closeWindowAndredirectToSearch()">
			<div class="line-1"></div>
			<div class="line-2"></div>
		</button>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import { YellowPagesFiltersGetters } from '@/stores/yellow-pages-filters/yellow-pages-filters-getters';
import { YellowPagesFiltersActions } from '@/stores/yellow-pages-filters/yellow-pages-filters-actions';

@Component
export default class YellowPagesHeader extends Vue {

	get orderByOptions() {
		return [
			{id: 'asc', text: this.$t('yellowPages.nameAscending')},
			{id: 'desc', text: this.$t('yellowPages.nameDescending')},
			{id: 'date', text: this.$t('yellowPages.registrationDate')}
		];
	}

	get search(): string {
		return this.$store.getters[YellowPagesFiltersGetters.search];
	}

	set search(value: string) {
		this.$store.dispatch(YellowPagesFiltersActions.setSearch, value);
		this.$store.dispatch(YellowPagesFiltersActions.setPageOffset, 0);
		this.$store.dispatch(YellowPagesFiltersActions.getResults);
	}

	get orderBy(): string {
		return this.$store.getters[YellowPagesFiltersGetters.orderBy];
	}

	set orderBy(value: string) {
		this.$store.dispatch(YellowPagesFiltersActions.setOrderBy, value);
		this.$store.dispatch(YellowPagesFiltersActions.setPageOffset, 0);
		this.$store.dispatch(YellowPagesFiltersActions.getResults);
	}

	public closeWindowAndredirectToSearch() {
		if(sessionStorage.getItem('RECENT_SEARCH_PARAMS')) {
			window.location.href = window.location.origin + sessionStorage.getItem('RECENT_SEARCH_PARAMS');
		} else {
			window.location.href = window.location.origin;
		}
	}
}
</script>

<style lang="scss">
.yellow-pages-header {
	background: $blue-transparent;
	display: flex;
	align-items: center;
	padding: 7px 20px;
	color: white;
	margin-bottom:5px;

	@media(max-width: $breakpoint-sm) {
		flex-direction: column;
		padding: 10px;
		height: 280px;
    	position: relative;
	}

	&__title {
		font-size: 18px;
		padding-right: 20px;
		@media(max-width: $breakpoint-sm) {
			width: 100%;
    		padding: 13px 0 20px;
		}
	}

	&__search {
		flex: 1 1 auto;
		height: 100%;
		padding-right: 20px;

		input {
			height: 37px;
			width: 100%;
			font-size: 16px;
			padding: 13px;
			border: none;
		}

		@media(max-width: $breakpoint-sm) {
			margin-bottom: 10px;
			width: 100%;
			padding-right: 0;
			flex:unset;
			height: auto;

			input {
				padding: 10px;
				font-size: 1em;
			}
		}
	}

	&__order-by {
		display: flex;
		align-items: center;

		@media(max-width: $breakpoint-sm) {
			width: 100%;
			margin-bottom: 0px;
		}

		&-title {
			margin-right: 15px;
			font-size: 18px;

			@media(max-width: $breakpoint-sm) {
				display: none;
			}
		}

		&-filter {
			padding-right: 18px;
			@media(max-width: $breakpoint-sm) {
				width: 100%;
				padding-right:0;
			}
		}

		.custom-select__trigger {
			padding: 0 10px;
			font-size: 1em;
			color: $grey-darker;

			&::after{
				border-top-color: $grey-darker;
			}
		}
	}

	.close-window {
        width: 25px;
        height: 25px;
        position: relative;

		@media(max-width: $breakpoint-sm) {
			position: absolute;
			right:10px;
			top:18px;
		}
        
        &:hover {
            animation: rotateCloseIcon 300ms cubic-bezier(0.645, 0.045, 0.355, 1) both ;

            & > div {
                background-color: #E31E24;
            }
        }

        & > div {
            width: 25px;
            height: 2px;
            background-color: white;
            transform-origin: center;
            position: absolute;
            transition: background-color 300ms cubic-bezier(0.645, 0.045, 0.355, 1);
        }

        .line-1 {
            transform: rotate(45deg);
        }

        .line-2 {
            transform: rotate(-45deg);
		}
		
		@keyframes rotateCloseIcon {
			0% {
				transform: rotate(0deg);
			}

			50% {
				transform: rotate(180deg)
			}

			100% {
				transform: rotate(360deg);
			}
    	}
    }
}
</style>