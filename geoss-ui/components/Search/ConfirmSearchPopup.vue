<template>
	<div class="confirm-search-popup-content popup-default">
		<div class="popup-default__title">
			<i class="icomoon-info"></i>
			<div>{{$t('popupTitles.confirmSearch')}}</div>
			<button @click="close('cancel')" class="close-btn" :title="$t('general.close')" :aria-label="$t('general.close')"></button>
		</div>
		<div v-if="query" class="popup-default__subtitle">{{$t('popupContent.lookingFor')}}: <b>{{query}}</b>, {{$t('popupContent.inLocation')}}: <b>{{location}}</b>?</div>
		<div v-else class="popup-default__subtitle">{{$t('popupContent.lookingForResultInLocation')}}:<br>{{location}}?</div>
		<div class="popup-default__actions">
			<button class="blue-btn-default" @click="close(false)">{{$t('general.no')}}</button>
			<button class="blue-btn-default" @click="close(true)">{{$t('general.yes')}}</button>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';

import { PopupGetters } from '@/stores/popup/popup-getters';
import PopupCloseService from '@/services/popup-close.service';

@Component
export default class ConfirmSearchPopupComponent extends Vue {
	@Prop({required: true, type: String}) public location!: string;
	@Prop({type: String}) public query!: string;

	public showDetails = false;

	public close(response: boolean) {
		PopupCloseService.closePopup('confirm-search', response);
	}
}
</script>

<style lang="scss">
.popup__wrapper.confirm-search-popup .popup__content {
	background: none;
}
</style>

<style lang="scss" scoped>
.popup-default {
	background: url('#{$staticPath}/img/powered_by_google_on_white.png') center calc(100% - 10px) no-repeat #fff;

	&__title {
		i {
			color: $blue-light;
			border: 2px solid $blue-light;
			padding: 3px;
			font-size: 19px;
			border-radius: 50%;
		}
	}
}
</style>