<template>
	<div class="image-preview" :class="{active, 'with-title': sourceImageTitle}" @click="close($event)">
		<div class="image-preview__image" ref="imagePreview">
			<img :src="imagePreviewUrl" :style="initialImageDimensions" :alt="sourceImageTitle || 'Preview'" />
		</div>
		<button @click="close()" class="image-preview__close-btn cross" :aria-label="$t('general.close')"></button>
		<div class="image-preview__title" v-line-clamp:20="3">{{sourceImageTitle}}</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { GeneralGetters } from '@/stores/general/general-getters';
import { GeneralActions } from '@/stores/general/general-actions';
import { Timers } from '@/data/timers';

@Component
export default class ImagePreviewComponent extends Vue {
	public active = false;
	public imagePreviewUrl: string = null;
	public initialImageDimensions = null;
	public sourceImage = null;
	public sourceImageTitle = null;
	private sourceImageElem = null;

	get imagePreview(): HTMLImageElement {
		return this.$store.getters[GeneralGetters.imagePreview];
	}

	public close(event?: Event) {
		if(event) {
			if(!(event.target as HTMLElement).closest('.image-preview__image img')) {
				this.$store.dispatch(GeneralActions.setImagePreview, null);
			}
		} else {
			this.$store.dispatch(GeneralActions.setImagePreview, null);
		}
	}

	private escEventListener(event: KeyboardEvent) {
		const key = event.which || event.keyCode;
		if(key === 27) {
			this.close();
		}
	}

	@Watch('imagePreview')
	private onImagePreview() {
		const imagePreviewElem = (this.$refs.imagePreview as HTMLElement);

		if(!!this.imagePreview) {
			this.sourceImageElem = this.imagePreview;
			this.sourceImageTitle = this.imagePreview.getAttribute('alt');
			const rect = this.imagePreview.getBoundingClientRect();

			this.initialImageDimensions = {
				width: this.imagePreview.offsetWidth + 'px',
				height: this.imagePreview.offsetHeight + 'px',
				top: rect.top + document.body.scrollTop - 50 + 'px',
				left: rect.left + document.body.scrollLeft - 50 + 'px',
				transform: 'none'
			};

			this.imagePreviewUrl = this.imagePreview.getAttribute('src');
			this.sourceImage = new Image();
			this.sourceImage.src = this.imagePreviewUrl;
			this.sourceImage.onload = async () => {
				this.active = true;

				const sourceImageRatio = this.sourceImage.width / this.sourceImage.height;
				const sourceImageWidth = this.sourceImage.width;
				const sourceImageHeight = this.sourceImage.height;

				let width = sourceImageWidth;
				let height = sourceImageHeight;
				let left = 0;
				let top = 0;

				if(sourceImageWidth > imagePreviewElem.offsetWidth) {
					width = imagePreviewElem.offsetWidth;
				}

				if(sourceImageHeight > imagePreviewElem.offsetHeight) {
					height = imagePreviewElem.offsetHeight;
				}

				if(width === imagePreviewElem.offsetWidth && height !== imagePreviewElem.offsetHeight) {
					height = width / sourceImageRatio;
				} else if(height === imagePreviewElem.offsetHeight && width !== imagePreviewElem.offsetWidth) {
					width = height * sourceImageRatio;
				} else if(width === imagePreviewElem.offsetWidth && height === imagePreviewElem.offsetHeight) {
					if(width < height) {
						height = width / sourceImageRatio;
					} else {
						width = height * sourceImageRatio;
					}
				}

				if(imagePreviewElem.offsetWidth > width) {
					left = (imagePreviewElem.offsetWidth - width) / 2;
				}

				if(imagePreviewElem.offsetHeight > height) {
					top = (imagePreviewElem.offsetHeight - height) / 2;
				}

				this.initialImageDimensions = {
					width: width + 'px',
					height: height + 'px',
					top: top + 'px',
					left: left + 'px',
					transform: 'none'
				};

				await this.$nextTick();
				setTimeout(() => {
					this.initialImageDimensions = {
						left: '50%',
						top: '50%',
						transform: 'translate(-50%, -50%)',
						width: 'auto',
						height: 'auto',
						transition: 'none'
					};
				}, Timers.imagePreview);
			};

			document.addEventListener('keyup', this.escEventListener);
		} else {
			const imagePreviewImgElem = (this.$refs.imagePreview as HTMLElement).querySelector('img');

			this.initialImageDimensions = {
				width: imagePreviewImgElem.offsetWidth + 'px',
				height: imagePreviewImgElem.offsetHeight + 'px',
				top: (imagePreviewElem.offsetHeight - imagePreviewImgElem.offsetHeight) / 2 + 'px',
				left: (imagePreviewElem.offsetWidth - imagePreviewImgElem.offsetWidth) / 2 + 'px',
				transform: 'none',
				transition: 'none'
			};

			this.$nextTick(() => {
				delete this.initialImageDimensions.transition;

				const rect = this.sourceImageElem.getBoundingClientRect();

				this.active = false;

				this.initialImageDimensions = {
					width: this.sourceImageElem.offsetWidth + 'px',
					height: this.sourceImageElem.offsetHeight + 'px',
					top: rect.top + document.body.scrollTop - 50 + 'px',
					left: rect.left + document.body.scrollLeft - 50 + 'px',
					transform: 'none'
				};

				setTimeout(() => {
					this.imagePreviewUrl = null;
					this.initialImageDimensions = null;
					this.sourceImage = null;
					this.sourceImageElem = null;
				}, Timers.imagePreview);
			});

			document.removeEventListener('keyup', this.escEventListener);
		}
	}
}
</script>

<style lang="scss" scoped>
$transitionTime: 500ms;

.image-preview {
	position: fixed;
	top: 0;
	left: -1000%;
	width: 100%;
	height: 100%;
	z-index: 9995;
	background-color: transparent;
	transition: background-color $transitionTime ease-in-out, left 0ms $transitionTime ease-in-out;
	display: flex;
	justify-content: center;
	align-items: center;
	padding: 50px;
	
	&.with-title {
		padding-bottom: 100px;
	}

	&.active {
		left: 0;
		background-color: rgba(black, 0.85);
		transition: background-color $transitionTime ease-in-out;

		button {
			opacity: 1;
		}
	}

	&__image {
		width: 100%;
		height: 100%;
		position: relative;

		img {
			position: absolute;
			max-width: 100%;
			max-height: 100%;
			transition: 
				width $transitionTime ease-in-out,
				height $transitionTime ease-in-out,
				left $transitionTime ease-in-out,
				top $transitionTime ease-in-out;
		}
	}

	&__close-btn {
		opacity: 0;
		position: absolute;
		background-color: black;
		right: 0;
		top: 0;
		padding: 25px;
		transition: opacity 500ms ease-in-out;

		&:hover {
			&:before,
			&:after {
				background-color: white;
			}
		}

		&:before,
		&:after {
			left: 13px;
			top: 23px;
			background-color: rgba(white, 0.8);
			transition: background-color 250ms ease-in-out;
		}
	}

	&__title {
		position: absolute;
		bottom: 30px;
		color: white;
		left: 50%;
		transform: translateX(-50%);
		width: 100%;
		font-size: 20px;
		padding: 0 20px;
		text-align: center;
	}
}
</style>