import { Component, Prop, Vue } from 'vue-property-decorator';
import { Value, Styles, Position, TooltipProp, TooltipFormatter } from './typings';

import './styles/dot.scss';
import { CreateElement } from 'vue';

@Component
export default class VueSliderDot extends Vue {
	public $refs!: {
		dot: HTMLDivElement
	};

	@Prop() public index!: number;

	@Prop({ default: 0 })
	public value!: Value;

	@Prop() public tooltip!: TooltipProp;

	@Prop() public dotStyle?: Styles;

	@Prop() public tooltipStyle?: Styles;

	@Prop({
		type: String,
		validator: (val: string) => ['top', 'right', 'bottom', 'left'].includes(val),
		required: true,
	})
	public tooltipPlacement!: Position;

	@Prop({ type: [String, Function] })
	public tooltipFormatter?: TooltipFormatter;

	@Prop({ type: Boolean, default: false })
	public focus!: boolean;

	@Prop({ default: false })
	public disabled!: boolean;

	get dotClasses() {
		return [
			'vue-slider-dot',
			{
				'vue-slider-dot-disabled': this.disabled,
				'vue-slider-dot-focus': this.focus,
			},
		];
	}

	get handleClasses() {
		return [
			'vue-slider-dot-handle',
			{
				'vue-slider-dot-handle-disabled': this.disabled,
				'vue-slider-dot-handle-focus': this.focus,
			},
		];
	}

	get tooltipClasses() {
		return [
			'vue-slider-dot-tooltip',
			[`vue-slider-dot-tooltip-${this.tooltipPlacement}`],
			{
				'vue-slider-dot-tooltip-show': this.showTooltip,
			},
		];
	}

	get tooltipInnerClasses() {
		return [
			'vue-slider-dot-tooltip-inner',
			[`vue-slider-dot-tooltip-inner-${this.tooltipPlacement}`],
			{
				'vue-slider-dot-tooltip-inner-disabled': this.disabled,
				'vue-slider-dot-tooltip-inner-focus': this.focus,
			},
		];
	}

	get showTooltip(): boolean {
		switch (this.tooltip) {
			case 'always':
				return true;
			case 'none':
				return false;
			case 'focus':
				return !!this.focus;
			default:
				return false;
		}
	}

	get tooltipValue(): Value {
		if (this.tooltipFormatter) {
			return typeof this.tooltipFormatter === 'string'
				? this.tooltipFormatter.replace(/\{value\}/, String(this.value))
				: this.tooltipFormatter(this.value, this.index);
		} else {
			return this.value;
		}
	}

	private dragStart(e: MouseEvent | TouchEvent) {
		if (this.disabled) {
			return false;
		}

		this.$emit('drag-start');
	}

	private render(this: VueSliderDot, h: CreateElement) {
		return (
			<div
				ref='dot'
				class={this.dotClasses}
				onMousedown={this.dragStart}
				onTouchstart={this.dragStart}
			>
				{this.$slots.dot || <div class={this.handleClasses} style={this.dotStyle} />}
				{this.tooltip !== 'none' ? (
					<div class={this.tooltipClasses}>
						{this.$slots.tooltip || (
							<div class={this.tooltipInnerClasses} style={this.tooltipStyle}>
								<span class={'vue-slider-dot-tooltip-text'}>{this.tooltipValue}</span>
							</div>
						)}
					</div>
				) : null}
			</div>
		);
	}
}
