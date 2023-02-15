<template>
	<button class="select-relation-resource" v-if="!workflow && crRelations" @click="setCrRelations(result)">
		<i class="icomoon-checkbox--empty" v-show="!isCrRelationResourceChecked(result)"></i>
		<i class="icomoon-checkbox--filled" v-show="isCrRelationResourceChecked(result)" :class="{srcEntry: isSrcEntry(result)}"></i>
	</button>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
import { SearchGetters } from '@/stores/search/search-getters';
import { SearchActions } from '@/stores/search/search-actions';
import NotificationService from '@/services/notification.service';

@Component
export default class CrRelationCheckboxComponent extends Vue {
	@Prop({ type: Object }) public result!: any;

	get workflow() {
		return this.$store.getters[SearchGetters.workflow];
	}

	get crRelations() {
		return this.$store.getters[SearchGetters.crRelation];
	}

	get dataSource() {
		return this.$store.getters[SearchGetters.dataSource];
	}

	public setCrRelation(result, mode) {
		const crRelations = JSON.parse(JSON.stringify(this.crRelations));
		if (mode === 'src') {
			if (this.isSrcEntry(result)) {
				crRelations.srcEntry = {
					id: null,
					dataSource: null
				};
				crRelations.destEntry = [];
			} else {
				crRelations.srcEntry = result;
				crRelations.srcEntry.dataSource = this.dataSource;
				NotificationService.show(
					`${this.$t('popupTitles.entryRelations')}`,
					`${this.$t('popupContent.entryRelationsSrcSet1')}: <br /><b>"${result.title}"</b><br />${this.$t('popupContent.entryRelationsSrcSet2')}`,
					10000,
					null,
					9999,
					'success'
				);
			}
		} else if (mode === 'dest') {
			if(crRelations.destEntry.find(item => item.id === result.id)) {
				crRelations.destEntry = crRelations.destEntry.filter(item => item.id !== result.id);
			} else {
				crRelations.destEntry = crRelations.destEntry.concat([{
					id: result.id,
					dataSource: this.dataSource
				}]);
			}
		}
		this.$store.dispatch(SearchActions.setCrRelation, crRelations);
	}

	public setCrRelations(result) {
		if (!this.crRelations.srcEntry.id || this.isSrcEntry(result)) {
			this.setCrRelation(result, 'src');
		} else {
			this.setCrRelation(result, 'dest');
		}
	}

	public isSrcEntry(result) {
		return this.crRelations && this.crRelations.srcEntry && this.crRelations.srcEntry.id === result.id;
	}

	public isCrRelationResourceChecked(result) {
		return this.crRelations && (this.crRelations.srcEntry.id === result.id || this.crRelations.destEntry.find(item => item.id === result.id));
	}
}
</script>

<style lang="scss" scoped>
.select-relation-resource {
	position: absolute;
	right: 15px;
	bottom: 10px;
	color: $green;
	font-weight: bold;
	font-size: 17px;

	.icomoon-checkbox--filled {
		font-size: 18px;
		position: relative;
		left: 1px;

		&.srcEntry {
			&:before {
				content: '\e909';
			}
			&:after {
				content: '×';
				position: absolute;
				left: 50%;
				top: -1px;
				transform: translateX(-50%);
				font-size: 30px;
				font-weight: bold;
				line-height: 16px;
			}
		}
	}
}
</style>