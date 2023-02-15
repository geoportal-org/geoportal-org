<template>
	<div class="dab-request-too-long-popup-content">
		<div class="dab-request-too-long-popup-content__title">
			<i class="icomoon-info"></i>
			<div>{{$t('general.warning')}}</div>
			<button class="close-btn" :title="$t('general.close')" :aria-label="$t('general.close')"></button>
		</div>
		<div class="dab-request-too-long-popup-content__subtitle">{{$t('popupContent.resourceTakesMoreTime')}}<br>{{$t('popupContent.wouldYouLikeToContinue')}}</div>
		<div class="dab-request-too-long-popup-content__actions">
			<button class="blue-btn-default" @click="close(false)">{{$t('general.no')}}</button>
			<button class="blue-btn-default" @click="close(true)">{{$t('general.yes')}}</button>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

import { PopupGetters } from '@/stores/popup/popup-getters';
import PopupCloseService from '@/services/popup-close.service';
import { GeneralApiService } from '@/services/general.api.service';

@Component
export default class DabRequestTooLongPopupComponent extends Vue {

	public showDetails = false;

	public close(response: boolean) {
		if(!response) {
			GeneralApiService.cancelCurrentrequest();
		}
		PopupCloseService.closePopup('dab-request-too-long', response);
	}
}
</script>

<style lang="scss">
.popup__wrapper.dab-request-too-long-popup .popup__content {
	background: none;
}

.dab-request-too-long-popup-content {
	padding: 30px 65px;
	background: rgba(254, 254, 254, 0.9);

	@media (max-width: $breakpoint-sm) {
		padding: 15px;
	}

	&__title {
		color: $blue;
		font-size: 20px;
		font-weight: 900;
		margin-bottom: 30px;
		padding-right: 40px;
		display: flex;
		align-items: center;

		i {
			color: $blue-light;
			border: 2px solid $blue-light;
			padding: 3px;
			font-size: 19px;
			border-radius: 50%;
			margin-right: 10px;
			display: block;
		}

		.close-btn {
			position: absolute;
			top: 15px;
			right: 15px;
			
			&:before,
			&:after {
				background: $blue;
			}
		}
	}

	&__subtitle {
		text-align: center;
		font-size: 20px;
		font-weight: 500;
		margin-bottom: 30px;
	}

	&__actions {
		text-align: right;

		button {
			padding: 5px 35px;

			& + button {
				margin-left: 10px;
			}
		}
	}
}
</style>
