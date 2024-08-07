<template>
	<div v-bar>
		<template v-if="showSimpleForm">
			<SimpleForm />
		</template>
		<template v-else-if="showEnhancedForm">
			<EnhancedForm />
		</template>
		<template v-else>
			<form class="form" @submit.prevent="switchForm()">
				<div class="form__wrapper">
					<img
						:src="`/img/geoss-logo-blue.png`"
						alt="GEOSS Portal logo"
						class="form__logo"
					/>
					<h2 class="form__title">{{ $tc('feedback.feedback') }}</h2>
					<h3 class="form__subtitle mw">
						{{ $tc('feedback.simpleOrEnhanced') }}
					</h3>
					<div class="form__controls">
						<label for="simple" class="form__controls-label">
							<input
								class="form__controls-radio"
								v-model="formType"
								name="formType"
								id="simple"
								type="radio"
								value="simple"
							/>
							{{ $tc('feedback.simpleFormTitle') }}
						</label>
						<label for="enhanced" class="form__controls-label">
							<input
								class="form__controls-radio"
								v-model="formType"
								name="formType"
								id="enhanced"
								type="radio"
								value="enhanced"
							/>
							{{ $tc('feedback.enhancedFormTitle') }}
						</label>
					</div>
					<div class="divider" />
					<div class="form__footer">
						<div class="form__footer-btn-holder">
							<button type="submit" class="green-btn-default center">
								{{ $tc('feedback.start') }}
							</button>
						</div>
					</div>
				</div>
			</form>
		</template>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'nuxt-property-decorator';
import { FeedbackActions } from '@/store/feedback/feedback-actions';
import { FeedbackGetters } from '@/store/feedback/feedback-getters';

import SimpleForm from '@/components/Feedback/SimpleForm.vue';
import EnhancedForm from '@/components/Feedback/EnhancedForm.vue';

@Component({
	components: {
		SimpleForm,
		EnhancedForm,
	},
})
export default class FeedbackStarterComponent extends Vue {
	get formType() {
		return this.$store.getters[FeedbackGetters.formType];
	}

	set formType(value: string) {
		this.$store.dispatch(FeedbackActions.setFormType, value);
	}

	get showSimpleForm() {
		return this.$store.getters[FeedbackGetters.showSimpleForm];
	}

	get showEnhancedForm() {
		return this.$store.getters[FeedbackGetters.showEnhancedForm];
	}

	public switchForm() {
		if (this.formType === 'simple') {
			this.$store.dispatch(FeedbackActions.setShowSimpleForm, true);
		}
		if (this.formType === 'enhanced') {
			this.$store.dispatch(FeedbackActions.setShowEnhancedForm, true);
		}
	}
}
</script>

<style lang="scss" scoped>
@import "~/assets/scss/feedback.scss";
</style>
