<template>
	<div class="custom-select">
		<button ref="optionsTrigger" class="custom-select__trigger" @click="toggleOpened" :disabled="!options.length || buttonDisabled"
			v-click-outside="{fn: closeContainer, excludeSelectors: '.custom-select__container'}" :class="{active: opened}">
			<slot name="trigger" v-bind:selectedOption="selectedOption">
				<span v-line-clamp:20="2">{{title}}</span>
			</slot>
		</button>
		<div class="custom-select__icons">
			<button v-if="options.length" @click="clear()" class="custom-select__clear cross" :aria-label="$t('generalFilters.cancel')" v-show="clearable && value"></button>
			<span class="custom-select__icon">
				<slot name="icon"></slot>
			</span>
		</div>
		<portal v-if="options.length && opened" to="custom-select-container" :disabled="!appendToBody">
			<div ref="optionsContainer" class="custom-select__container" :class="{appendToBody}" :style="optionsContainerStyles">
				<div class="custom-select__search" v-if="filterable">
					<span class="search__icon">
						<i class="icomoon-search"></i>
					</span>
					<input type="text" :value="searchValue" @input="searchValue = $event.target.value" :placeholder="$t('placeholders.search')" />
					<button class="search__clear" :title="$t('placeholders.clearSearch')" :aria-label="$t('placeholders.clearSearch')" @click="clearSearch()">
						<i class="icomoon-close"></i>
					</button>						
				</div>
				<div class="custom-select__options" v-bar>
					<div>
						<div v-for="option of filteredOptions" class="custom-select__option-wrapper"
							:key="`${option[idProp]}-${option[textProp]}`"
							:class="{'expandable': option[nestedOptionsFieldName] && option[nestedOptionsFieldName].length}"
							@click="toggleLevel1Menu(option, $event)">
							<div v-if="!option[nestedOptionsFieldName] || !option[nestedOptionsFieldName].length"
								class="custom-select__option level1" 
								:class="{'selected': isSelected(option[idProp]), disabled: option.disabled}"
								@click="toggleSelectOption(option, $event)">
								<slot name="option" v-bind:option="option">
									<span>{{option[textProp]}}</span>
								</slot>
							</div>
							<div v-if="option[nestedOptionsFieldName] && option[nestedOptionsFieldName].length" 
								class="custom-select__option level1" 
								:class="{'selected': isSelected(option[idProp]), disabled: option.disabled}">
								<slot name="option" v-bind:option="option">
									<span @click="toggleSelectOption(option, $event)">{{option[textProp]}}</span>
								</slot>
							</div>
							<div v-if="option[nestedOptionsFieldName] && option[nestedOptionsFieldName].length" class="custom-select__suboptions">
								<div v-for="subOption of option[nestedOptionsFieldName]"
									:key="`${subOption[idProp]}-${subOption[textProp]}`">
									<div class="custom-select__option level2"
										:class="{'selected': isSelected(subOption[idProp])}"
										@click="toggleSelectOption(subOption, $event)">
										<span>{{subOption[textProp]}}</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</portal>
	</div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Model } from 'vue-property-decorator';

@Component({})
export default class ExtendedCustomSelectComponent extends Vue {
	@Prop({ default: false, type: Boolean}) public filterable!: boolean;
	@Prop({ default: 'text', type: String}) public textProp!: string;
	@Prop({ default: 'id', type: String}) public idProp!: string;
	@Prop({ default: 'subOptions', type: String}) public nestedOptionsFieldName!: string;
	@Prop({ default: true, type: Boolean}) public clearable!: boolean;
	@Prop({ default: false, type: Boolean}) public hideSelectedOption!: boolean;
	@Prop({ default: false, type: Boolean}) public buttonDisabled!: boolean;
	public opened = false;
	public searchValue = '';
	public optionsContainerStyles = null;

	@Prop({ default: () => [], type: Array }) private options!: any[];
	@Prop({ default: false, type: Boolean}) private appendToBody!: boolean;
	@Prop({ default: '', type: String}) private placeholder!: string;
	@Model('input') private value!: string | number | string[] | number[];
	private scrollableParent: HTMLElement | null = null;

	get title() {
		const subName = this.nestedOptionsFieldName;
		let selectedOption = null;
		for (const option of this.options) {
			if (option[this.idProp] === this.value) {
				selectedOption = option;
				break;
			} else {
				if (option[subName] && option[subName].length) {
					for (const subOption of option[subName]) {
						if (subOption[this.idProp] === this.value) {
							selectedOption = subOption;
							break;
						}
					}
				}
			}
		}
		return selectedOption ? selectedOption[this.textProp] : this.placeholder;
	}

	get filteredOptions() {
		let filteredOptions = this.options.filter(item => item[this.textProp].toLowerCase().indexOf(this.searchValue.toLowerCase()) !== -1);

		if(this.hideSelectedOption && this.value) {
			filteredOptions = filteredOptions.filter(option => {
				return option[this.idProp] !== this.selectedOption[this.idProp];
			});
		}
		return filteredOptions;
	}

	get selectedOption() {
		return this.options.find(option => option[this.idProp] === this.value);
	}

	public closeContainer() {
		this.opened = false;
		this.searchValue = '';
		if(this.appendToBody) {
			this.removeScrollListener();
		}
	}

	public isSelected(optionId: string | number) {
		return this.value === optionId;
	}

	public async toggleOpened() {
		this.opened = !this.opened;
		this.searchValue = '';
		if(this.opened && this.appendToBody) {
			await this.$nextTick();
			this.setPositionAndSize();
			this.addScrollListener();
		} else if(this.appendToBody) {
			this.removeScrollListener();
		}
	}

	public toggleLevel1Menu(option, event) {
		if (option[this.nestedOptionsFieldName] && option[this.nestedOptionsFieldName].length) {
			event.target.parentNode.classList.toggle('active');
		}
	}

	public toggleSelectOption(option, event) {
		if(option.disabled) {
			return;
		}

		event.target.classList.add('active');
		if(this.value !== option[this.idProp]) {
			this.$emit('input', option[this.idProp]);
		}
	}

	public clearSearch() {
		this.searchValue = '';
	}

	public clear() {
		this.$emit('input', '');
	}

	private getScrollParent(node: HTMLElement): HTMLElement | null {
		if (!node) {
			return null;
		}

		if (node.scrollHeight > node.clientHeight) {
			return node;
		} else {
			return this.getScrollParent((node.parentNode as HTMLElement));
		}
	}

	private setPositionAndSize() {
		const optionsContainer = (this.$refs.optionsContainer as HTMLElement);
		const bounds = (this.$refs.optionsTrigger as HTMLElement).getBoundingClientRect();
		const boundsBottom = window.innerHeight - bounds.top - bounds.height;
		const width = bounds.width < 300 ? 300 : bounds.width;
		const left = bounds.left > (window.innerWidth - width) ? (window.innerWidth - width) : bounds.left;
		let top = bounds.top + bounds.height + 3 + 'px';
		let bottom = 'auto';
		if(optionsContainer && boundsBottom < optionsContainer.clientHeight + 5) {
			top = 'auto';
			bottom = boundsBottom + bounds.height + 3 + 'px';
		}
		this.optionsContainerStyles = {
			'width': width + 'px',
			'top': top,
			'bottom': bottom,
			'left':  left + 'px',
			'z-index': 9997
		};
	}

	private addScrollListener() {
		this.scrollableParent = this.getScrollParent((this.$refs.optionsTrigger as HTMLElement));
		if(this.scrollableParent) {
			this.scrollableParent.addEventListener('scroll', this.setPositionAndSize);
		}
	}

	private removeScrollListener() {
		if(this.scrollableParent) {
			this.scrollableParent.removeEventListener('scroll', this.setPositionAndSize);
		}
	}
}
</script>


<style lang="scss" scoped>
.custom-select {
	position: relative;

	&__trigger {
		font-size: 14px;
		padding: 0 50px 0 30px;
		background: white;
		height: 37px;
		display: flex;
		align-items: center;
		width: 100%;

		&:disabled {
			opacity: 0.65;
			color: #333 !important;

			&:hover {
				color: #333 !important;
			}
		}

		&.active:after {
			transform: rotate(180deg);
		}

		&:after {
			display: inline-block;
			width: 0;
			height: 0;
			vertical-align: top;
			border-top: 4px solid black;
			border-right: 4px solid transparent;
			border-left: 4px solid transparent;
			content: '';
			margin-left: 10px;
		}
	}

	&__icons {
		position: absolute;
		right: 5px;
		top: 50%;
		transform: translateY(-50%);
		display: flex;
		align-items: center;
	}

	&__icon i {
		font-size: 21px;
		color: $blue;
		font-weight: bold;
	}

	&__clear {		
		&:before,
		&:after {
			background-color: black;
			width: 18px;
			left: 3px;
		}
	}

	&__container {
		position: absolute;
		max-height: 200px;
		background: white;
		z-index: 6;
		top: calc(100% + 3px);
		left: 0;
		min-width: 100%;
		border: 1px solid rgba(0, 0, 0, 0.2);
		border-radius: 4px;
		display: flex;
		flex-direction: column;
		max-width: 100%;

		&.appendToBody {
			min-width: 0;
		}
	}

	&__search {
		margin: 5px;
		width: calc(100% - 10px);
		display: flex;
		height: 37px;
		align-items: stretch;
		font-size: 14px;
		flex: 1 0 auto;

		.search__icon {
			background-color: #eee;
			padding: 10px 12px;
			border: 1px solid #ccc;
			border-top-left-radius: 4px;
			border-bottom-left-radius: 4px;
		}

		input {
			flex: 1 1 auto;
			border: none;
			outline: 0;
			height: auto;
			padding: 0 12px;
			border-top: 1px solid #ccc;
			border-bottom: 1px solid #ccc;
			min-width: calc(100% - 80px);
		}

		.search__clear {
			padding: 10px 12px;
			border: 1px solid #ccc;
			border-top-right-radius: 4px;
			border-bottom-right-radius: 4px;

			&:hover {
				background-color: #eee;
			}

			i {
				font-size: 14px;
			}
		}
	}

	&__option-wrapper {
		border-bottom: 1px solid #ddd;

		&:last-child {
			border: none;
		}

		&.expandable {
			.level1:after {
				border-top: 4px solid black;
				border-right: 4px solid transparent;
				border-left: 4px solid transparent;
				content: '';
				display: inline-block;
				height: 0;
				margin-left: 10px;
				margin-top: 5px;
				position: absolute;
				right: 25px;
				vertical-align: top;
				width: 0;
			}

			&.active {
				.level1:after {
					transform: rotate(180deg);
				}
				.custom-select__suboptions {
					display: block;
				}
			}
		}
	}

	&__options {
		overflow: auto;
	}
	
	&__suboptions {
		display: none;
	}

	&__option {
		cursor: pointer;
		font-size: 14px;
		padding: 13px 20px 13px 10px;
		position: relative;
		overflow: hidden;
		width: 100%;
		text-overflow: ellipsis;

		&.level2 {
			padding-left: 30px;
		}

		&:hover:not(.disabled):not(.selected) {
			background-color: #eee;
		}

		&.selected {
			background-color: $blue;
			color: white;

			&:before {
				border-color: white;
			}

			&:after {
				display: block;
			}
		}

		&.selected {
			.radio {
				&:after {
					display: block;
				}
			}
		}
	}
}
</style>
