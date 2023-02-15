import { CreateElement } from 'vue';
import { Component, Model, Prop, Vue, Watch } from 'vue-property-decorator';
import './styles/slider.scss';
import './theme/default.scss';
import { Direction, Dot, DotOption, Mark, MarksProp, Position, Process, ProcessProp, Styles, TooltipFormatter, TooltipProp, Value } from './typings';
import { getKeyboardHandleFunc, getPos, getSize } from './utils';
import Control, { ERROR_TYPE } from './utils/control';
import Decimal from './utils/decimal';
import State, { StateMap } from './utils/state';
import VueSliderDot from './vue-slider-dot';
import VueSliderMark from './vue-slider-mark';



export const SliderState: StateMap = {
	None: 0,
	Drag: 1,
	Focus: 2
};

const DEFAULT_SLIDER_SIZE = 4;

@Component({
	data() {
		return {
			control: null,
		};
	},
	components: {
		VueSliderDot,
		VueSliderMark,
	},
	inheritAttrs: false,
})
export default class VueSlider extends Vue {
	public control!: Control;
	public states: State = new State(SliderState);
	// The width of the component is divided into one hundred, the width of each one.
	public scale: number = 1;
	// Currently dragged slider index
	public focusDotIndex: number = 0;

	public $refs!: {
		container: HTMLDivElement
	};

	public $el!: HTMLDivElement;

	@Model('change', { default: 0 })
	public value!: Value | Value[];

	@Prop({ type: Boolean, default: false })
	public silent!: boolean;

	@Prop({
		default: 'ltr',
		validator: dir => ['ltr', 'rtl', 'ttb', 'btt'].indexOf(dir) > -1,
	})
	public direction!: Direction;

	@Prop({ type: [Number, String] }) public width?: number | string;

	@Prop({ type: [Number, String] }) public height?: number | string;

	// The size of the slider, optional [width, height] | size
	@Prop({ default: 14 })
	public dotSize!: [number, number] | number;

	@Prop({ type: Number, default: 0 })
	public min!: number;

	@Prop({ type: Number, default: 100 })
	public max!: number;

	@Prop({ type: Number, default: 1 })
	public interval!: number;

	@Prop({ type: Boolean, default: false })
	public disabled!: boolean;

	@Prop({ type: Boolean, default: true })
	public clickable!: boolean;

	// The duration of the slider slide, Unit second
	@Prop({ type: Number, default: 0.5 })
	public duration!: number;

	@Prop(Array) public data?: Value[];

	@Prop({ type: Boolean, default: false })
	public lazy!: boolean;

	@Prop({
		type: String,
		validator: val => ['none', 'always', 'focus'].includes(val),
		default: 'focus',
	})
	public tooltip!: TooltipProp;

	@Prop({
		type: [String, Array],
		validator: val => {
			if (typeof val === 'string') {
				return ['top', 'right', 'bottom', 'left'].includes(val);
			} else {
				for (const item of val) {
					if (!['top', 'right', 'bottom', 'left'].includes(item)) {
						return false;
					}
				}
				return true;
			}
		},
	})
	public tooltipPlacement?: Position;

	@Prop({ type: [String, Function] })
	public tooltipFormatter?: TooltipFormatter;

	// Keyboard control
	@Prop({ type: Boolean, default: false })
	public useKeyboard?: boolean;

	// Whether to allow sliders to cross, only in range mode
	@Prop({ type: Boolean, default: true })
	public enableCross!: boolean;

	// Whether to fix the slider interval, only in range mode
	@Prop({ type: Boolean, default: false })
	public fixed!: boolean;

	// Whether to sort values, only in range mode
	// When order is false, the parameters [minRange, maxRange, fixed, enableCross] are invalid
	// e.g. When order = false, [50, 30] will not be automatically sorted into [30, 50]
	@Prop({ type: Boolean, default: true })
	public order!: boolean;

	// Minimum distance between sliders, only in range mode
	@Prop(Number) public minRange?: number;

	// Maximum distance between sliders, only in range mode
	@Prop(Number) public maxRange?: number;

	@Prop({ type: [Boolean, Object, Array, Function], default: false })
	public marks?: MarksProp;

	@Prop({ type: [Boolean, Function], default: true })
	public process?: ProcessProp;

	// If the value is true , mark will be an independent value
	@Prop(Boolean) public included?: boolean;

	// If the value is true , dot will automatically adsorb to the nearest value
	@Prop(Boolean) public adsorb?: boolean;

	@Prop(Boolean) public hideLabel?: boolean;

	@Prop() public dotOptions?: DotOption | DotOption[];

	@Prop() public railStyle?: Styles;

	@Prop() public processStyle?: Styles;

	@Prop() public dotStyle?: Styles;

	@Prop() public tooltipStyle?: Styles;

	@Prop() public stepStyle?: Styles;

	@Prop() public stepActiveStyle?: Styles;

	@Prop() public labelStyle?: Styles;

	@Prop() public labelActiveStyle?: Styles;

	get tailSize() {
		return getSize((this.isHorizontal ? this.height : this.width) || DEFAULT_SLIDER_SIZE);
	}

	get containerClasses() {
		return [
			'vue-slider',
			[`vue-slider-${this.direction}`],
			{
				'vue-slider-disabled': this.disabled,
			},
		];
	}

	get containerStyles() {
		const [dotWidth, dotHeight] = Array.isArray(this.dotSize)
			? this.dotSize
			: [this.dotSize, this.dotSize];
		const containerWidth = this.width
			? getSize(this.width)
			: this.isHorizontal
				? 'auto'
				: getSize(DEFAULT_SLIDER_SIZE);
		const containerHeight = this.height
			? getSize(this.height)
			: this.isHorizontal
				? getSize(DEFAULT_SLIDER_SIZE)
				: 'auto';
		return {
			padding: this.isHorizontal ? `${dotHeight / 2}px 0` : `0 ${dotWidth / 2}px`,
			width: containerWidth,
			height: containerHeight,
		};
	}

	get processArray(): Process[] {
		return this.control.processArray.map(([start, end, style]) => {
			if (start > end) {
				[start, end] = [end, start];
			}
			const sizeStyleKey = this.isHorizontal ? 'width' : 'height';
			return {
				start,
				end,
				style: {
					[this.isHorizontal ? 'height' : 'width']: '100%',
					[this.isHorizontal ? 'top' : 'left']: 0,
					[this.mainDirection]: `${start}%`,
					[sizeStyleKey]: `${end - start}%`,
					transitionProperty: `${sizeStyleKey},${this.mainDirection}`,
					transitionDuration: `${this.animateTime}s`,
					...this.processStyle,
					...style,
				}
			};
		});
	}

	get dotBaseStyle() {
		const [dotWidth, dotHeight] = Array.isArray(this.dotSize)
			? this.dotSize
			: [this.dotSize, this.dotSize];
		let dotPos: { [key: string]: string };
		if (this.isHorizontal) {
			dotPos = {
				transform: `translate(${this.isReverse ? '50%' : '-50%'}, -50%)`,
				WebkitTransform: `translate(${this.isReverse ? '50%' : '-50%'}, -50%)`,
				top: '50%',
				[this.direction === 'ltr' ? 'left' : 'right']: '0',
			};
		} else {
			dotPos = {
				transform: `translate(-50%, ${this.isReverse ? '50%' : '-50%'})`,
				WebkitTransform: `translate(-50%, ${this.isReverse ? '50%' : '-50%'})`,
				left: '50%',
				[this.direction === 'btt' ? 'bottom' : 'top']: '0',
			};
		}
		return {
			width: `${dotWidth}px`,
			height: `${dotHeight}px`,
			...dotPos,
		};
	}

	get mainDirection(): string {
		switch (this.direction) {
			case 'ltr':
				return 'left';
			case 'rtl':
				return 'right';
			case 'btt':
				return 'bottom';
			case 'ttb':
				return 'top';
		}
	}

	get isHorizontal(): boolean {
		return this.direction === 'ltr' || this.direction === 'rtl';
	}

	get isReverse(): boolean {
		return this.direction === 'rtl' || this.direction === 'btt';
	}

	get tooltipDirections(): Position[] {
		const dir = this.tooltipPlacement || (this.isHorizontal ? 'top' : 'left');
		if (Array.isArray(dir)) {
			return dir;
		} else {
			return Array.from(new Array(this.dots.length), () => dir);
		}
	}

	get dots(): Dot[] {
		return this.control.dotsPos.map((pos, index) => ({
			pos,
			index,
			value: this.control.dotsValue[index],
			focus: this.states.has(SliderState.Focus) && this.focusDotIndex === index,
			disabled: false,
			style: this.dotStyle,
			...((Array.isArray(this.dotOptions) ? this.dotOptions[index] : this.dotOptions) || {}),
		}));
	}

	get animateTime(): number {
		if (this.states.has(SliderState.Drag)) {
			return 0;
		}
		return this.duration;
	}

	get canSort(): boolean {
		return this.order && !this.minRange && !this.maxRange && !this.fixed && this.enableCross;
	}

	public getValue() {
		const values = this.control.dotsValue;
		return values.length === 1 ? values[0] : values;
	}

	@Watch('value')
	private onValueChanged() {
		if (!this.states.has(SliderState.Drag) && this.isNotSync) {
			this.control.setValue(this.value);
		}
	}

	private created() {
		this.initControl();
	}

	private mounted() {
		this.bindEvent();
	}

	private beforeDestroy() {
		this.unbindEvent();
	}

	private bindEvent() {
		document.addEventListener('touchmove', this.dragMove, { passive: false });
		document.addEventListener('touchend', this.dragEnd, { passive: false });
		document.addEventListener('mousedown', this.blurHandle);
		document.addEventListener('mousemove', this.dragMove);
		document.addEventListener('mouseup', this.dragEnd);
		document.addEventListener('mouseleave', this.dragEnd);
		document.addEventListener('keydown', this.keydownHandle);
	}

	private unbindEvent() {
		document.removeEventListener('touchmove', this.dragMove);
		document.removeEventListener('touchend', this.dragEnd);
		document.removeEventListener('mousemove', this.dragMove);
		document.removeEventListener('mouseup', this.dragEnd);
		document.removeEventListener('mouseleave', this.dragEnd);
		document.removeEventListener('keydown', this.keydownHandle);
	}

	private setScale() {
		this.scale = new Decimal(
			Math.floor(this.isHorizontal ? this.$el.offsetWidth : this.$el.offsetHeight),
		)
			.divide(100)
			.toNumber();
	}

	private initControl() {
		this.control = new Control({
			value: this.value,
			data: this.data,
			enableCross: this.enableCross,
			fixed: this.fixed,
			max: this.max,
			min: this.min,
			interval: this.interval,
			minRange: this.minRange,
			maxRange: this.maxRange,
			order: this.order,
			marks: this.marks,
			included: this.included,
			process: this.process,
			adsorb: this.adsorb,
			onError: this.emitError,
		});
		[
			'data',
			'enableCross',
			'fixed',
			'max',
			'min',
			'interval',
			'minRange',
			'maxRange',
			'order',
			'marks',
			'process',
			'adsorb',
			'included',
		].forEach(name => {
			this.$watch(name, (val: any) => {
				if (
					name === 'data' &&
					Array.isArray(this.control.data) &&
					Array.isArray(val) &&
					this.control.data.length === val.length &&
					val.every((item, index) => item === (this.control.data as Value[])[index])
				) {
					return false;
				}
				(this.control as any)[name] = val;
				if (['data', 'max', 'min', 'interval'].includes(name)) {
					this.control.syncDotsPos();
				}
			});
		});
	}

	private isDisabledByDotIndex(index: number): boolean {
		return this.dots[index].disabled;
	}

	private syncValueByPos() {
		const values = this.control.dotsValue;
		if (this.isDiff(values, Array.isArray(this.value) ? this.value : [this.value])) {
			this.$emit('change', values.length === 1 ? values[0] : [...values]);
		}
	}

	// Slider value and component internal value are inconsistent
	private get isNotSync() {
		const values = this.control.dotsValue;
		return Array.isArray(this.value)
			? this.value.length !== values.length ||
			this.value.some((val, index) => val !== values[index])
			: this.value !== values[0];
	}

	private isDiff(value1: Value[], value2: Value[]) {
		return value1.length !== value2.length || value1.some((val, index) => val !== value2[index]);
	}

	private emitError(type: ERROR_TYPE, message: string) {
		if (!this.silent) {
			console.error(`[VueSlider error]: ${message}`);
		}
		this.$emit('error', type, message);
	}

	/**
	 * Get the drag range of the slider
	 *
	 * @private
	 * @param {number} index slider index
	 * @returns {[number, number]} range [start, end]
	 * @memberof VueSlider
	 */
	private get dragRange(): [number, number] {
		const prevDot = this.dots[this.focusDotIndex - 1];
		const nextDot = this.dots[this.focusDotIndex + 1];
		return [prevDot ? prevDot.pos : -Infinity, nextDot ? nextDot.pos : Infinity];
	}

	private dragStart(index: number) {
		this.focusDotIndex = index;
		this.setScale();
		this.states.add(SliderState.Drag);
		this.states.add(SliderState.Focus);
		this.$emit('drag-start');
	}

	private dragMove(e: MouseEvent | TouchEvent) {
		if (!this.states.has(SliderState.Drag)) {
			return false;
		}
		e.preventDefault();
		const pos = this.getPosByEvent(e);
		this.isCrossDot(pos);
		this.control.setDotPos(pos, this.focusDotIndex);
		if (!this.lazy) {
			this.syncValueByPos();
		}
		const value = this.control.dotsValue;
		this.$emit('dragging', value.length === 1 ? value[0] : [...value]);
	}

	// If the component is sorted, then when the slider crosses, toggle the currently selected slider index
	private isCrossDot(pos: number) {
		if (this.canSort) {
			const curIndex = this.focusDotIndex;
			let curPos = pos;
			if (curPos > this.dragRange[1]) {
				curPos = this.dragRange[1];
				this.focusDotIndex++;
			} else if (curPos < this.dragRange[0]) {
				curPos = this.dragRange[0];
				this.focusDotIndex--;
			}
			if (curIndex !== this.focusDotIndex) {
				this.control.setDotPos(curPos, curIndex);
			}
		}
	}

	private dragEnd() {
		if (!this.states.has(SliderState.Drag)) {
			return false;
		}
		if (this.lazy) {
			this.syncValueByPos();
		}

		setTimeout(() => {
			if (this.included && this.isNotSync) {
				this.control.setValue(this.value);
			} else {
				// Sync slider position
				this.control.syncDotsPos();
			}

			this.states.delete(SliderState.Drag);
			// If useKeyboard is true, keep focus status after dragging
			if (!this.useKeyboard) {
				this.states.delete(SliderState.Focus);
			}
			const values = this.control.dotsValue;
			this.$emit('drag-end', values.length === 1 ? values[0] : [...values]);
		});
	}

	private blurHandle(e: MouseEvent) {
		if (
			!this.states.has(SliderState.Focus) ||
			!this.$refs.container ||
			this.$refs.container.contains(e.target as Node)
		) {
			return false;
		}
		this.states.delete(SliderState.Focus);
	}

	private clickHandle(e: MouseEvent | TouchEvent) {
		if (!this.clickable) {
			return false;
		}
		if (this.states.has(SliderState.Drag)) {
			return;
		}
		this.setScale();
		const pos = this.getPosByEvent(e);
		this.setValueByPos(pos);
	}

	private focus(index: number = 0) {
		this.states.add(SliderState.Focus);
		this.focusDotIndex = index;
	}

	private blur() {
		this.states.delete(SliderState.Focus);
	}

	private getIndex() {
		const indexes = this.control.dotsIndex;
		return indexes.length === 1 ? indexes[0] : indexes;
	}

	private setValue(value: Value | Value[]) {
		this.control.setValue(Array.isArray(value) ? [...value] : [value]);
		this.syncValueByPos();
	}

	private setIndex(index: number | number[]) {
		const value = Array.isArray(index)
			? index.map(n => this.control.getValueByIndex(n))
			: this.control.getValueByIndex(index);
		this.setValue(value);
	}

	private setValueByPos(pos: number) {
		const index = this.control.getRecentDot(pos);
		if (this.isDisabledByDotIndex(index)) {
			return false;
		}
		this.focusDotIndex = index;
		this.control.setDotPos(pos, index);
		this.syncValueByPos();

		if (this.useKeyboard) {
			this.states.add(SliderState.Focus);
		}

		setTimeout(() => {
			if (this.included && this.isNotSync) {
				this.control.setValue(this.value);
			} else {
				this.control.syncDotsPos();
			}
		});
	}

	private keydownHandle(e: KeyboardEvent) {
		if (!this.useKeyboard || !this.states.has(SliderState.Focus)) {
			return false;
		}

		const handleFunc = getKeyboardHandleFunc(e, {
			direction: this.direction,
			max: this.control.total,
			min: 0,
		});

		if (handleFunc) {
			e.preventDefault();
			const index = this.control.getIndexByValue(this.control.dotsValue[this.focusDotIndex]);
			const newIndex = handleFunc(index);
			const pos = this.control.parseValue(this.control.getValueByIndex(newIndex));
			this.isCrossDot(pos);
			this.control.setDotPos(pos, this.focusDotIndex);
			this.syncValueByPos();
		}
	}

	private getPosByEvent(e: MouseEvent | TouchEvent): number {
		return getPos(e, this.$el, this.isReverse)[this.isHorizontal ? 'x' : 'y'] / this.scale;
	}

	private renderSlot<T>(name: string, data: T, defaultSlot: any, isDefault?: boolean): any {
		const scopedSlot = this.$scopedSlots[name];
		return scopedSlot ? (
			isDefault ? (
				scopedSlot(data)
			) : (
					<template slot={name}>{scopedSlot(data)}</template>
				)
		) : (
				defaultSlot
			);
	}

	private render(this: VueSlider, h: CreateElement) {
		return (
			<div
				ref='container'
				class={this.containerClasses}
				style={this.containerStyles}
				aria-hidden={true}
				onClick={this.clickHandle}
			>
				{/* rail */}
				<div class='vue-slider-rail' style={this.railStyle}>
					{this.processArray.map((item, index) =>
						this.renderSlot<Process>(
							'process',
							item,
							<div class='vue-slider-process' key={`process-${index}`} style={item.style} />,
							true,
						),
					)}
					{/* mark */}
					{this.marks ? (
						<div class='vue-slider-marks'>
							{this.control.markList.map((mark, index) =>
								this.renderSlot<Mark>(
									'mark',
									mark,
									<vue-slider-mark
										key={`mark-${index}`}
										mark={mark}
										hideLabel={this.hideLabel}
										style={{
											[this.isHorizontal ? 'height' : 'width']: '100%',
											[this.isHorizontal ? 'width' : 'height']: this.tailSize,
											[this.mainDirection]: `${mark.pos}%`,
										}}
										stepStyle={this.stepStyle}
										stepActiveStyle={this.stepActiveStyle}
										labelStyle={this.labelStyle}
										labelActiveStyle={this.labelActiveStyle}
										onPressLabel={(pos: number) => this.setValueByPos(pos)}
									>
										{this.renderSlot<Mark>('step', mark, null)}
										{this.renderSlot<Mark>('label', mark, null)}
									</vue-slider-mark>,
									true,
								),
							)}
						</div>
					) : null}
					{/* dot */}
					{this.dots.map((dot, index) => (
						<vue-slider-dot
							ref={`dot-${index}`}
							key={`dot-${index}`}
							index={index}
							value={dot.value}
							disabled={dot.disabled}
							focus={dot.focus}
							dot-style={[
								dot.style,
								dot.disabled ? dot.disabledStyle : null,
								dot.focus ? dot.focusStyle : null,
							]}
							tooltip={dot.tooltip || this.tooltip}
							tooltip-style={[
								this.tooltipStyle,
								dot.tooltipStyle,
								dot.disabled ? dot.tooltipDisabledStyle : null,
								dot.focus ? dot.tooltipFocusStyle : null,
							]}
							tooltip-formatter={this.tooltipFormatter}
							tooltip-placement={this.tooltipDirections[index]}
							style={[
								this.dotBaseStyle,
								{
									[this.mainDirection]: `${dot.pos}%`,
									transition: `${this.mainDirection} ${this.animateTime}s`,
								},
							]}
							onDrag-start={() => this.dragStart(index)}
						>
							{this.renderSlot<Dot>('dot', dot, null)}
							{this.renderSlot<Dot>('tooltip', dot, null)}
						</vue-slider-dot>
					))}
				</div>
				{/* Support screen readers */}
				{this.dots.length === 1 && !this.data ? (
					<input
						class='vue-slider-sr-only'
						type='range'
						value={this.value}
						min={this.min}
						max={this.max}
					/>
				) : null}
			</div>
		);
	}
}
