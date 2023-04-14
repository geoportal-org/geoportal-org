<template>
    <div :style="style" :class="disabled ? [className] : [{
        active: enabled,
        dragging,
        resizing,
        draggable,
        resizable,
        dragged,
        resized
    }, className]" @mousedown="elementDown" @touchstart="elementTouchDown">
        <div v-for="handle in actualHandles" :key="handle" :class="['handle', 'handle-' + handle]"
            :style="{ display: enabled ? 'block' : 'none' }" @mousedown.stop.prevent="handleDown(handle, $event)"
            @touchstart.stop.prevent="handleTouchDown(handle, $event)">
            <slot name="handle"></slot>
        </div>
        <slot></slot>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Prop, Watch } from 'nuxt-property-decorator';
import { matchesSelectorToParentElements, addEvent, removeEvent } from '@/utils/dom';
import { returnStatement } from '@babel/types';

const events = {
    mouse: {
        start: 'mousedown',
        move: 'mousemove',
        stop: 'mouseup'
    },
    touch: {
        start: 'touchstart',
        move: 'touchmove',
        stop: 'touchend'
    }
};

const userSelectNone = {
    userSelect: 'none',
    MozUserSelect: 'none',
    WebkitUserSelect: 'none',
    MsUserSelect: 'none'
};

const userSelectAuto = {
    userSelect: 'auto',
    MozUserSelect: 'auto',
    WebkitUserSelect: 'auto',
    MsUserSelect: 'auto'
};

let eventsFor = events.mouse;

@Component
export default class DraggableResizable extends Vue {
    @Prop({ type: Number }) public offsetTop!: number;
    @Prop({ type: Number }) public offsetRight!: number;
    @Prop({ default: false, type: Boolean }) public disabled!: boolean;
    @Prop({ default: 'vdr', type: String }) public className!: string;
    @Prop({ default: true, type: Boolean }) public disableUserSelect!: boolean;
    @Prop({ default: false, type: Boolean }) public active!: boolean;
    @Prop({ default: true, type: Boolean }) public draggable!: boolean;
    @Prop({ default: true, type: Boolean }) public resizable!: boolean;
    @Prop({ default: false, type: Boolean }) public lockAspectRatio!: boolean;
    @Prop({ type: Number, default: 0 }) public minWidth!: number;
    @Prop({ type: Number, default: 0 }) public minHeight!: number;
    @Prop({ type: Number, default: null }) public maxWidth!: number;
    @Prop({ type: Number, default: null }) public maxHeight!: number;
    @Prop({ type: Number, default: 0 }) public x!: number;
    @Prop({ type: Number, default: 0 }) public y!: number;
    @Prop({ type: [String, Number], default: 'auto' }) public z!: [string, number];
    @Prop({ type: Array, default: () => ['tl', 'tm', 'tr', 'mr', 'br', 'bm', 'bl', 'ml'] }) public handles!: string[];
    @Prop({ type: String, default: null }) public dragHandle!: string;
    @Prop({ type: String, default: null }) public dragCancel!: string;
    @Prop({ type: String, default: 'both' }) public axis!: string;
    @Prop({ type: Array, default: () => [1, 1] }) public grid!: number[];
    @Prop({ type: [Boolean, String], default: false }) public parent!: boolean | string;
    @Prop({ default: null }) public onDragStart!: (e: any) => {};
    @Prop({ default: null }) public onResizeStart!: (handle: any, e: any) => {};

    public resized = false;
    public dragged = false;

    private rawWidth = null;
    private rawHeight = null;
    private rawLeft = this.x;
    private rawTop = this.y;

    private left = this.x;
    private top = this.y;

    private aspectFactor = null;

    private parentWidth = null;
    private parentHeight = null;

    private minW = this.minWidth;
    private minH = this.minHeight;

    private maxW = this.maxWidth;
    private maxH = this.maxHeight;

    private handle = null;
    public enabled = this.active;
    public resizing = false;
    public dragging = false;

    private zIndex = this.z;

    private mutationObserver = null;

    private leftBeforeWindowResize = null;
    private topBeforeWindowResize = null;

    private widthBeforeResize = null;
    private heightBeforeResize = null;
    private widthBeforeWindowResize = null;
    private heightBeforeWindowResize = null;

    private bounds = null;
    private mouseClickPosition = null;

    get style() {

        if (this.disabled) return {};

        const dragged = (this.dragged && this.draggable ? {
            position: 'absolute',
            top: this.top + 'px',
            left: this.left + 'px'
        } : {});

        const resized = (this.resized && this.resizable ? {
            width: this.width() + 'px',
            height: this.height() + 'px'
        } : {});

        return {
            ...dragged,
            ...resized,
            zIndex: this.zIndex,
            ...(this.dragging && this.disableUserSelect ? userSelectNone : userSelectAuto)
        };
    }

    get actualHandles() {
        if (!this.resizable) {
            return [];
        }

        return this.handles;
    }

    get resizingOnX() {
        return (Boolean(this.handle) && (this.handle.includes('l') || this.handle.includes('r')));
    }

    get resizingOnY() {
        return (Boolean(this.handle) && (this.handle.includes('t') || this.handle.includes('b')));
    }

    get isCornerHandle() {
        return (Boolean(this.handle) && ['tl', 'tr', 'br', 'bl'].includes(this.handle));
    }

    private resetDimensionsBeforeResize() {
        this.widthBeforeWindowResize = null;
        this.heightBeforeWindowResize = null;
        this.leftBeforeWindowResize = null;
        this.topBeforeWindowResize = null;
    }

    private onResize() {
        this.checkParentSize();
        this.bounds = this.calcDragLimits();

        let leftChanged = false;
        let topChanged = false;

        if (this.dragged) {
            const maxLeft = this.parentWidth - this.width() - this.offsetRight;
            if (this.left > maxLeft) {
                this.leftBeforeWindowResize = this.leftBeforeWindowResize || this.left;
                this.rawLeft = maxLeft;
                leftChanged = true;
            } else if (this.leftBeforeWindowResize !== null) {
                leftChanged = true;
                if (this.leftBeforeWindowResize <= maxLeft) {
                    this.rawLeft = this.leftBeforeWindowResize;
                    this.leftBeforeWindowResize = null;
                } else {
                    this.rawLeft = maxLeft;
                }
            }

            const maxTop = this.parentHeight - this.height();
            if (this.top > maxTop) {
                this.topBeforeWindowResize = this.topBeforeWindowResize || this.top;
                this.rawTop = maxTop;
                topChanged = true;
            } else if (this.topBeforeWindowResize !== null) {
                topChanged = true;
                if (this.topBeforeWindowResize <= maxTop) {
                    this.rawTop = this.topBeforeWindowResize;
                    this.topBeforeWindowResize = null;
                } else {
                    this.rawTop = maxTop;
                }
            }
        }

        if (this.resized) {
            const rect = this.$el.getBoundingClientRect();

            const left = this.left || rect.left;

            if (!this.dragged || (this.dragged && (!leftChanged || !left))) {
                const maxWidth = this.parentWidth - left - this.offsetRight;
                if (this.width() > maxWidth) {
                    this.widthBeforeWindowResize = this.widthBeforeWindowResize || this.width();
                    this.rawWidth = maxWidth;
                } else if (this.widthBeforeWindowResize !== null) {
                    if (this.widthBeforeWindowResize <= maxWidth) {
                        this.rawWidth = this.widthBeforeWindowResize;
                        this.widthBeforeWindowResize = null;
                    } else {
                        this.rawWidth = maxWidth;
                    }
                }
            }

            const top = this.top || rect.top;

            if (!this.dragged || (this.dragged && (!topChanged || !top))) {
                const maxHeight = this.parentHeight - top - this.offsetTop;
                if (this.height() > maxHeight) {
                    this.heightBeforeWindowResize = this.heightBeforeWindowResize || this.width();
                    this.rawHeight = maxHeight;
                } else if (this.heightBeforeWindowResize !== null) {
                    if (this.heightBeforeWindowResize <= maxHeight) {
                        this.rawHeight = this.heightBeforeWindowResize;
                        this.heightBeforeWindowResize = null;
                    } else {
                        this.rawHeight = maxHeight;
                    }
                }
            }
        }
    }

    private width() {
        return this.rawWidth ? this.rawWidth : this.$el.offsetWidth;
    }

    private height() {
        if (this.rawHeight) {
            if (this.minH && this.rawHeight < this.minH) {
                return this.minH;
            } else if (this.maxH && this.rawHeight > this.maxH) {
                return this.maxH;
            }
            return this.rawHeight;
        }
        return this.$el.offsetHeight;
    }

    private resetBoundsAndMouseState() {
        this.mouseClickPosition = { mouseX: 0, mouseY: 0, x: 0, y: 0, w: 0, h: 0 };

        this.bounds = {
            minLeft: null,
            maxLeft: null,
            maxRight: null,
            minTop: null,
            maxTop: null,
            minBottom: null,
            maxBottom: null
        };
    }

    private checkParentSize() {
        if (this.parent) {
            const [newParentWidth, newParentHeight] = this.getParentSize();

            const deltaX = this.parentWidth - newParentWidth;
            const deltaY = this.parentHeight - newParentHeight;

            this.parentWidth = newParentWidth;
            this.parentHeight = newParentHeight;
        }
    }

    private getParentSize() {
        const parent = this.parent;

        if (parent === true) {
            const style = window.getComputedStyle(this.$el.parentNode as HTMLElement, null);

            return [
                parseInt(style.getPropertyValue('width'), 10),
                parseInt(style.getPropertyValue('height'), 10)
            ];
        }

        if (typeof parent === 'string') {
            const parentNode = document.querySelector(parent) as HTMLElement;

            if (!(parentNode instanceof HTMLElement)) {
                throw new Error(`The selector ${parent} does not match any element`);
            }

            return [parentNode.offsetWidth, parentNode.offsetHeight];
        }

        return [null, null];
    }

    public elementTouchDown(e) {
        eventsFor = events.touch;

        this.elementDown(e);
    }

    public elementDown(e) {
        const target = e.target || e.srcElement;

        if (this.$el.contains(target)) {
            if (this.onDragStart && this.onDragStart(e) === false) {
                return;
            }

            if (
                (this.dragHandle && !matchesSelectorToParentElements(target, this.dragHandle, this.$el)) ||
                (this.dragCancel && matchesSelectorToParentElements(target, this.dragCancel, this.$el))
            ) {
                return;
            }

            if (!this.enabled) {
                this.enabled = true;

                this.$emit('activated');
                this.$emit('update:active', true);
            }

            if (this.draggable) {
                this.dragging = true;
            }

            if (!this.dragged) {
                const rect = this.$el.getBoundingClientRect();
                this.left = rect.left;
                this.top = rect.top;
                this.$el.style.transform = 'none';
                this.$el.style.left = rect.left + 'px';
                this.$el.style.top = rect.top + 'px';
            }

            this.mouseClickPosition.mouseX = e.touches ? e.touches[0].pageX : e.pageX;
            this.mouseClickPosition.mouseY = e.touches ? e.touches[0].pageY : e.pageY;

            this.mouseClickPosition.left = this.left;
            this.mouseClickPosition.top = this.top;

            if (this.parent) {
                this.bounds = this.calcDragLimits();
            }

            addEvent(document.documentElement, eventsFor.move, this.move);
            addEvent(document.documentElement, eventsFor.stop, this.handleUp);
        }
    }

    private calcDragLimits() {
        return {
            minLeft: (this.parentWidth + this.left) % this.grid[0],
            maxLeft: Math.floor((this.parentWidth - this.width() - this.left) / this.grid[0]) * this.grid[0] + this.left - this.offsetRight,
            minTop: (this.parentHeight + this.top) % this.grid[1] + this.offsetTop,
            maxTop: Math.floor((this.parentHeight - this.height() - this.top) / this.grid[1]) * this.grid[1] + this.top,
        };
    }

    private deselect(e) {
        const target = e.target || e.srcElement;
        const regex = new RegExp(this.className + '-([trmbl]{2})', '');

        if (!this.$el.contains(target) && !regex.test(target.className)) {
            if (this.enabled) {
                this.enabled = false;

                this.$emit('deactivated');
                this.$emit('update:active', false);
            }

            removeEvent(document.documentElement, eventsFor.move, this.handleMove);
        }

        this.resetBoundsAndMouseState();
    }

    public handleTouchDown(handle, e) {
        eventsFor = events.touch;

        this.handleDown(handle, e);
    }

    public handleDown(handle, e) {
        if (this.onResizeStart && this.onResizeStart(handle, e) === false) {
            return;
        }

        if (e.stopPropagation) {
            e.stopPropagation();
        }

        // Here we avoid a dangerous recursion by faking
        // corner handles as middle handles
        if (this.lockAspectRatio && !handle.includes('m')) {
            this.handle = 'm' + handle.substring(1);
        } else {
            this.handle = handle;
        }

        if (!this.resized) {
            this.rawWidth = this.$el.offsetWidth;
            this.rawHeight = this.$el.offsetHeight;
        }

        if (this.widthBeforeResize === null) {
            this.widthBeforeResize = this.width();
        }

        if (this.heightBeforeResize === null) {
            this.heightBeforeResize = this.height();
        }

        this.resizing = true;

        this.mouseClickPosition.mouseX = e.touches ? e.touches[0].pageX : e.pageX;
        this.mouseClickPosition.mouseY = e.touches ? e.touches[0].pageY : e.pageY;
        this.mouseClickPosition.left = this.left;
        this.mouseClickPosition.top = this.top;

        this.bounds = this.calcResizeLimits();

        addEvent(document.documentElement, eventsFor.move, this.handleMove);
        addEvent(document.documentElement, eventsFor.stop, this.handleUp);
    }

    private calcResizeLimits() {
        let minW = this.minW;
        let minH = this.minH;
        let maxW = this.maxW;
        let maxH = this.maxH;

        const aspectFactor = this.aspectFactor;
        const [gridX, gridY] = this.grid;
        const width = this.width();
        const height = this.height();
        const left = this.left;
        const top = this.top;

        if (this.lockAspectRatio) {
            if (minW / minH > aspectFactor) {
                minH = minW / aspectFactor;
            } else {
                minW = aspectFactor * minH;
            }

            if (maxW && maxH) {
                maxW = Math.min(maxW, aspectFactor * maxH);
                maxH = Math.min(maxH, maxW / aspectFactor);
            } else if (maxW) {
                maxH = maxW / aspectFactor;
            } else if (maxH) {
                maxW = aspectFactor * maxH;
            }
        }

        maxW = maxW - (maxW % gridX);
        maxH = maxH - (maxH % gridY);

        const limits = {
            minLeft: null,
            maxLeft: null,
            minTop: null,
            maxTop: null,
            maxRight: null,
            minBottom: null,
            maxBottom: null
        };

        if (this.parent) {
            limits.minLeft = (this.parentWidth + left) % gridX;
            limits.maxLeft = left + Math.floor((width - minW) / gridX) * gridX;
            limits.minTop = (this.parentHeight + top) % gridY;
            limits.maxTop = top + Math.floor((height - minH) / gridY) * gridY;

            if (maxH) {
                limits.minTop = Math.max(limits.minTop, this.parentHeight - maxH);
            }

            if (this.lockAspectRatio) {
                limits.minLeft = Math.max(limits.minLeft, left - top * aspectFactor);
                limits.minTop = Math.max(limits.minTop, top - left / aspectFactor);
            }
        } else {
            limits.minLeft = null;
            limits.maxLeft = left + Math.floor((width - minW) / gridX) * gridX;
            limits.minTop = null;
            limits.maxTop = top + Math.floor((height - minH) / gridY) * gridY;

            if (maxW) {
                limits.minLeft = -(maxW);
            }

            if (maxH) {
                limits.minTop = -(maxH);
            }

            if (this.lockAspectRatio && (maxW && maxH)) {
                limits.minLeft = Math.min(limits.minLeft, -(maxW));
                limits.minTop = Math.min(limits.minTop, -(maxH));
            }
        }

        return limits;
    }

    private move(e) {
        if (this.resizing) {
            this.handleMove(e);
        } else if (this.dragging) {
            this.elementMove(e);
        }
    }

    private elementMove(e) {
        const axis = this.axis;
        const grid = this.grid;
        const mouseClickPosition = this.mouseClickPosition;

        const tmpDeltaX = axis && axis !== 'y' ? mouseClickPosition.mouseX - (e.touches ? e.touches[0].pageX : e.pageX) : 0;
        const tmpDeltaY = axis && axis !== 'x' ? mouseClickPosition.mouseY - (e.touches ? e.touches[0].pageY : e.pageY) : 0;

        const [deltaX, deltaY] = this.snapToGrid(this.grid, tmpDeltaX, tmpDeltaY);

        if (!deltaX && !deltaY) {
            return;
        }

        this.dragged = true;
        this.resetDimensionsBeforeResize();

        this.rawTop = mouseClickPosition.top - deltaY;
        this.rawLeft = mouseClickPosition.left - deltaX;

        this.$emit('dragging', this.left, this.top);
    }

    private handleMove(e) {
        const handle = this.handle;
        const mouseClickPosition = this.mouseClickPosition;

        const tmpDeltaX = mouseClickPosition.mouseX - (e.touches ? e.touches[0].pageX : e.pageX);
        const tmpDeltaY = mouseClickPosition.mouseY - (e.touches ? e.touches[0].pageY : e.pageY);

        const [deltaX, deltaY] = this.snapToGrid(this.grid, tmpDeltaX, tmpDeltaY);

        if (!deltaX && !deltaY) {
            return;
        }

        this.resized = true;
        this.resetDimensionsBeforeResize();

        let newWidth = this.widthBeforeResize - deltaX;

        if (this.minW && newWidth < this.minW) {
            newWidth = this.minW;
        } else if (this.maxW && newWidth > this.maxW) {
            newWidth = this.maxW;
        }

        this.rawWidth = newWidth;
        this.rawHeight = this.heightBeforeResize - deltaY;

        this.$emit('resizing', this.left, this.top, this.width(), this.height());
    }

    private handleUp() {
        this.handle = null;

        this.resetBoundsAndMouseState();

        this.rawTop = this.top;
        this.rawLeft = this.left;

        if (this.resizing) {
            this.resizing = false;
            this.widthBeforeResize = null;
            this.heightBeforeResize = null;
            this.$emit('resizestop', this.left, this.top, this.width(), this.height());
        }
        if (this.dragging) {
            this.dragging = false;
            this.$emit('dragstop', this.left, this.top);
        }
        if (!this.dragged) {
            this.$el.style.transform = '';
            this.$el.style.left = '';
            this.$el.style.top = '';
        }
        if (!this.resized) {
            this.$el.style.width = '';
            this.$el.style.height = '';
        }

        removeEvent(document.documentElement, eventsFor.move, this.handleMove);
    }

    private snapToGrid(grid, pendingX, pendingY) {
        const x = Math.round(pendingX / grid[0]) * grid[0];
        const y = Math.round(pendingY / grid[1]) * grid[1];

        return [x, y];
    }

    @Watch('active')
    private onActiveChange(val) {
        this.enabled = val;

        if (val) {
            this.$emit('activated');
        } else {
            this.$emit('deactivated');
        }
    }

    @Watch('z')
    private onZChange(val) {
        if (val >= 0 || val === 'auto') {
            this.zIndex = val;
        }
    }

    @Watch('rawLeft')
    private onRawLeftChange(newLeft) {
        const bounds = this.bounds;
        const aspectFactor = this.aspectFactor;
        const lockAspectRatio = this.lockAspectRatio;
        const left = this.left;
        const top = this.top;

        if (bounds.minLeft !== null && newLeft < bounds.minLeft) {
            newLeft = bounds.minLeft;
        } else if (bounds.maxLeft !== null && bounds.maxLeft < newLeft) {
            newLeft = bounds.maxLeft;
        }

        if (lockAspectRatio && this.resizingOnX) {
            this.rawTop = top - (left - newLeft) / aspectFactor;
        }

        this.left = newLeft;
    }

    @Watch('rawTop')
    private onRawTopChange(newTop) {
        const bounds = this.bounds;
        const aspectFactor = this.aspectFactor;
        const lockAspectRatio = this.lockAspectRatio;
        const left = this.left;
        const top = this.top;


        if (bounds.minTop !== null && newTop < bounds.minTop) {
            newTop = bounds.minTop;
        } else if (bounds.maxTop !== null && bounds.maxTop < newTop) {
            newTop = bounds.maxTop;
        }

        if (lockAspectRatio && this.resizingOnY) {
            this.rawLeft = left - (top - newTop) * aspectFactor;
        }

        this.top = newTop;
    }

    @Watch('x')
    private onXChange() {
        if (this.resizing || this.dragging) {
            return;
        }

        if (this.parent) {
            this.bounds = this.calcDragLimits();
        }

        const delta = this.x - this.left;

        if (delta % this.grid[0] === 0) {
            this.rawLeft = this.x;
        }
    }

    @Watch('y')
    private onYChange() {
        if (this.resizing || this.dragging) {
            return;
        }

        if (this.parent) {
            this.bounds = this.calcDragLimits();
        }

        const delta = this.y - this.top;

        if (delta % this.grid[1] === 0) {
            this.rawTop = this.y;
        }
    }

    @Watch('lockAspectRatio')
    private onLockAspectRatioChange(val) {
        if (val) {
            this.aspectFactor = this.width() / this.height();
        } else {
            this.aspectFactor = undefined;
        }
    }

    @Watch('minWidth')
    private onMinWidthChange(val) {
        if (val > 0 && val <= this.width()) {
            this.minW = val;
        }
    }

    @Watch('minHeight')
    private onMinHeightChange(val) {
        if (val > 0 && val <= this.height()) {
            this.minH = val;
        }
    }

    @Watch('maxWidth')
    private onMaxWidthChange(val) {
        const rect = this.$el.getBoundingClientRect();

        const left = this.left || rect.left;

        const parentMaxW = this.parentWidth - left - this.offsetRight;

        this.maxW = val ? Math.min(val, parentMaxW) : parentMaxW;
    }

    @Watch('maxHeight')
    private onMaxHeightChange(val) {
        const rect = this.$el.getBoundingClientRect();

        const top = this.top || rect.top;

        const parentMaxH = this.parentHeight - top;

        let totalChildrenHeight = 0;

        for (const item of Array.from(this.$el.children)) {
            const style = getComputedStyle(item);
            if (style.position === 'relative' || style.position === 'static') {
                if (item.classList.contains('vb')) {
                    totalChildrenHeight += item.querySelector('.vb-content>div').scrollHeight;
                } else {
                    totalChildrenHeight += item.scrollHeight + parseInt(style.marginTop, 10) + parseInt(style.marginBottom, 10);
                }
            }
        }

        this.maxH = val ? Math.min(val, parentMaxH, totalChildrenHeight) : Math.min(parentMaxH, totalChildrenHeight);
    }

    private created() {
        // eslint-disable-next-line
        if (this.maxWidth && this.minWidth > this.maxWidth) {
            throw new Error('[Vdr warn]: Invalid prop: minWidth cannot be greater than maxWidth');
        }
        // eslint-disable-next-line
        if (this.maxWidth && this.minHeight > this.maxHeight) {
            throw new Error('[Vdr warn]: Invalid prop: minHeight cannot be greater than maxHeight');
        }

        this.resetBoundsAndMouseState();
    }

    private mounted() {
        this.$el.ondragstart = () => false;

        [this.parentWidth, this.parentHeight] = this.getParentSize();

        addEvent(document.documentElement, 'mousedown', this.deselect);
        addEvent(document.documentElement, 'touchend touchcancel', this.deselect);

        addEvent(window, 'resize', this.onResize);

        const config = { attributes: true, childList: true, subtree: true };

        // Callback function to execute when mutations are observed
        const callback = () => {
            this.checkParentSize();

            this.onMaxWidthChange(this.maxHeight);
            this.onMaxHeightChange(this.maxHeight);

            if (this.dragged && this.calcDragLimits().maxTop < this.top) {
                const target = this.dragHandle ? this.$el.querySelector(this.dragHandle) : this.$el;
                this.elementDown({ target, pageX: this.left + 1, pageY: this.top + 1 });
                setTimeout(() => {
                    this.elementMove({ pageX: this.left + 2, pageY: this.top + 2 });

                    setTimeout(() => {
                        this.handleUp();
                        this.rawHeight = this.rawHeight;
                    }, 0);
                }, 0);
            } else {
                this.rawHeight = this.rawHeight;
            }
            this.$nextTick(() => {
                window.dispatchEvent(new Event('resize'));
            });
        };

        this.mutationObserver = new MutationObserver(callback);

        this.mutationObserver.observe(this.$el, config);

        callback();
    }

    private beforeDestroy() {
        removeEvent(document.documentElement, 'mousedown', this.deselect);
        removeEvent(document.documentElement, 'touchstart', this.handleUp);
        removeEvent(document.documentElement, 'mousemove', this.move);
        removeEvent(document.documentElement, 'touchmove', this.move);
        removeEvent(document.documentElement, 'mouseup', this.handleUp);
        removeEvent(document.documentElement, 'touchend touchcancel', this.deselect);

        removeEvent(window, 'resize', this.onResize);

        this.mutationObserver.disconnect();
    }
}
</script>

<style lang="scss">
.handle {
    background: transparent;
    color: white;
    display: block !important;
    font-size: 15px;
    height: auto;
    position: absolute;
    width: auto;
    z-index: 1;

    @media (max-width: $breakpoint-xl) {
        display: none !important;
    }

    &-br {
        bottom: 0;
        cursor: se-resize;
        right: 2px;
    }
}
</style>
