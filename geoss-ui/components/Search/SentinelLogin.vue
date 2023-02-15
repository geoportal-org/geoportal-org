<template>
	<form @submit="closePopup()" :action="formUrl" target="_blank" method="POST" name="_geosssearch_WAR_geossportlet_fm-download" autocomplete="new-password" class="sentinel-login">
		<div class="sentinel-login__input-wrapper">
			<label>{{$t('sentinelLogin.username')}}:</label>
			<input name="_geosssearch_WAR_geossportlet_dhus-username" type="text" autocomplete="new-password" :placeholder="$t('sentinelLogin.username')" />
		</div>
		<div class="sentinel-login__input-wrapper">
			<label>{{$t('sentinelLogin.password')}}:</label>
			<input name="_geosssearch_WAR_geossportlet_dhus-password" type="password" autocomplete="new-password" :placeholder="$t('sentinelLogin.password')" />
		</div>
		<div class="sentinel-login__bottom">
			<a href="https://scihub.copernicus.eu/dhus/#/self-registration" target="_blank">{{$t('sentinelLogin.sentinelDataAccessRegistration')}}</a>
			<button type="submit" class="green-btn-default" @click="downloadLinkClicked()">{{$t('sentinelLogin.download')}}</button>
		</div>
	</form>
</template>

<script lang="ts">
import { Component, Vue, Prop } from 'vue-property-decorator';
import PopupCloseService from '@/services/popup-close.service';
import SearchEngineService from '../../services/search-engine.service';
import LogService from '@/services/log.service';

@Component
export default class SentinelLoginComponent extends Vue {
	@Prop({required: true, type: String}) private url!: string;
	@Prop({required: true, type: Object}) private result;

	get formUrl() {
		return SearchEngineService.getDhusProxyUrl(this.url);
	}

	public closePopup() {
		PopupCloseService.closePopup('sentinel-login');
	}

	public downloadLinkClicked() {
		LogService.logElementClick(null, null, this.result.id, null, 'Direct download', null, this.result.contributor.orgName, this.result.title);
	}
}
</script>

<style lang="scss" scoped>
.sentinel-login {
	max-width: 400px;
	margin: 30px auto;
	padding: 30px 25px;

	&__input-wrapper {
		margin-bottom: 30px;
		
		label {
			width: 100%;
			display: block;
			color: $green;
			margin-bottom: 10px;
		}

		input {
			width: 100%;
			border: 2px solid #ddd;
			background: white;
			font-size: 20px;
			padding: 5px;
			font-weight: normal;

			&::placeholder {
				color: #ddd;
				font-weight: 300;
			}
		}
	}
	
	&__bottom {
		display: flex;
		justify-content: space-between;
		align-items: center;
		flex-wrap: wrap;

		@media (max-width: $breakpoint-xxs) {
			a {
				order: 1;
				width: 100%;
			}

			button {
				order: 0;
				width: 100%;
				margin-bottom: 15px;
			}
		}
	}
}
</style>