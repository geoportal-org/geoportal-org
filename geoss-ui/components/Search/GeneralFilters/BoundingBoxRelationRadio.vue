<template>
	<div class="general-filters-radio-choice" data-tutorial-tag="filters-general-relation-type">
		<div class="general-filters-radio-choice__title">{{$t('generalFilters.relationToSelectedArea')}}:</div>
		<div class="general-filters-radio-choice__options">
			<div v-for="option of options" :key="option.value" class="general-filters-radio-choice__option">
				<input v-model="value" type="radio" :value="option.value" :id="`boundingBoxRelation${option.title}`" />
				<label :for="`boundingBoxRelation${option.title}`" :data-tutorial-tag="'filters-general-relation-type-' + getLowercaseTitle(option.title)">{{option.title}}</label>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Emit } from 'vue-property-decorator';

import { GeneralFiltersActions } from '@/stores/general-filters/general-filters-actions';
import { GeneralFiltersGetters } from '@/stores/general-filters/general-filters-getters';

@Component
export default class BoundingBoxRelationRadio extends Vue {
	get options() {
		return [
			{
				title: this.$t('generalFilters.overlaps'),
				value: 'OVERLAPS'
			},
			{
				title: this.$t('generalFilters.contains'),
				value: 'CONTAINS'
			},
			{
				title: this.$t('generalFilters.disjoint'),
				value: 'DISJOINT'
			},
		];
	}

	get value() {
		return this.$store.getters[GeneralFiltersGetters.state].boundingBoxRelation;
	}

	@Emit()
	public input(value: any) {
		return value;
	}

	set value(value) {
		this.input(value);
	}

	public getLowercaseTitle(title) {
		return title.toLowerCase();
	}
}
</script>

<style lang="scss">
.general-filters-radio-choice {
	color: white;

	&__title {
		margin-bottom: 10px;
		font-size: 14px;
	}

	&__options {
		display: flex;
		justify-content: space-between;
	}

	&__option {
		flex: 0 1 140px;

		input {
			width: 0;
			height: 0;
			padding: 0;
			margin: 0;
			visibility: hidden;
			overflow: hidden;
			position: absolute;
			z-index: -1;
			opacity: 0;

			&:checked + label:after {
				display: block;
			}
		}

		label {
			position: relative;
			padding-left: 21px;
			font-size: 14px;
			text-transform: capitalize;
			cursor: pointer;
			width: 100%;
			font-style: italic;

			&:before, &:after {
				content: '';
				width: 16px;
				height: 16px;
				box-sizing: border-box;
				border: 2px solid white;
				border-radius: 50%;
				position: absolute;
				left: 0;
				top: 0px;
			}

			&:after {
				border: none;
				top: 4px;
				left: 4px;
				width: 8px;
				height: 8px;
				background: white;
				display: none;
			}
		}
	}
}
</style>