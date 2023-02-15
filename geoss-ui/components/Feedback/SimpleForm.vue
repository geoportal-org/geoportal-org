<template>
	<div v-bar>
		<form class="form" @submit.prevent="verifyCaptcha()" id="simpleForm">
			<div class="form__wrapper">
				<img
					:src="`${staticPath()}/img/geoss-logo-blue.png`"
					alt="GEOSS Portal logo"
					class="form__logo"
				/>
				<h2 class="form__title">{{ $t('feedback.feedback') }}</h2>

				<template v-if="!questionnaireSubmitted">
					<div class="form__controls vertical">
						<label for="email" class="form__controls-label vertical">
							{{ $t('feedback.email') }}:
						</label>
						<input
							class="form__controls-text-input"
							name="email"
							type="email"
							:placeholder="$t('feedback.placeholder')"
							required
						/>

						<label for="firstName" class="form__controls-label vertical">
							{{ $t('feedback.simpleForm.firstName') }}:
						</label>
						<input
							class="form__controls-text-input"
							name="firstName"
							type="text"
							:placeholder="$t('feedback.placeholder')"
							required
						/>

						<label for="simpleFormAction" class="form__controls-label vertical"
							>{{ $t('feedback.simpleForm.wantTo') }}:
						</label>
						<label for="suggest" class="form__controls-label">
							<input
								class="form__controls-radio vertical"
								v-model="simpleFormAction"
								name="simpleFormAction"
								type="radio"
								value="suggest"
							/>

							{{ $t('feedback.simpleForm.suggestRadio') }}
						</label>

						<label for="report" class="form__controls-label">
							<input
								class="form__controls-radio vertical"
								v-model="simpleFormAction"
								name="simpleFormAction"
								type="radio"
								value="report"
							/>

							{{ $t('feedback.simpleForm.report') }}
						</label>
						<div class="divider fullwidth" />
						<template v-if="simpleFormAction == 'suggest'">
							<label
								for="suggestionTitle"
								class="form__controls-label vertical"
							>
								{{ $t('feedback.simpleForm.suggestionTitle') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="suggestionTitle"
								type="text"
								:placeholder="$t('feedback.placeholder')"
								required
							/>

							<label
								for="suggestionDescription"
								class="form__controls-label vertical"
							>
								{{ $t('feedback.simpleForm.suggestionDescription') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="suggestionDescription"
								type="text"
								:placeholder="$t('feedback.placeholder')"
								required
							/>


						</template>
						<template v-if="simpleFormAction == 'report'">
							<label for="problemTitle" class="form__controls-label vertical">
								{{ $t('feedback.simpleForm.problemTitle') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="problemTitle"
								type="text"
								:placeholder="$t('feedback.placeholder')"
								required
							/>

							<label
								for="problemDescription"
								class="form__controls-label vertical"
							>
								{{ $t('feedback.simpleForm.problemDescription') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="problemDescription"
								type="text"
								:placeholder="$t('feedback.placeholder')"
								required
							/>

							<label for="severity" class="form__controls-label vertical">
								{{ $t('feedback.simpleForm.severity.label') }}:
							</label>
							<div>
								<label
									class="form__controls-label"
									v-for="option in $t('feedback.simpleForm.severity.options')"
									:key="option.value"
								>
									<input
										type="radio"
										class="form__controls-radio"
										name="severity"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>
							</div>
						</template>

						<label for="captcha" class="form__controls-label vertical">
							{{ $t('feedback.simpleForm.enterText') }}:
						</label>
						<div class="form__controls-captcha">
							<div class="form__controls-captcha-img-container">
								<Loader />
								<img src="" alt="Captcha" id="captcha" />
							</div>

							<button class="reload-btn" type="button" @click="reloadCaptcha" title="Reload">
								<ReloadIcon />
							</button>

							<input
								class="form__controls-text-input"
								name="captcha"
								id="captchaInput"
								type="text"
								:placeholder="$t('feedback.placeholder')"
								required
							/>
							<span class="form__controls-captcha-error">Please try again</span>
						</div>
					</div>

					<div class="divider" />
					<div class="form__footer">
						<div class="form__footer-btn-holder">
							<button
								@click="clearForm()"
								type="button"
								class="form__footer-clear-btn"
							>
								{{ $t('feedback.clear') }}
							</button>
							<button type="submit" class="green-btn-default">
								{{ $t('feedback.submit') }}
							</button>
						</div>
					</div>
				</template>
				<template v-else>
					<div class="form__thanks">
						<p class="form__thanks-text">
							{{ $t('feedback.thankYou') }}
						</p>
					</div>
				</template>
			</div>
		</form>
	</div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';
import { FeedbackActions } from '@/stores/feedback/feedback-actions';
import { FeedbackGetters } from '@/stores/feedback/feedback-getters';
import {
	getFeedbackQuestionsAndAnswers,
	createJiraIssue,
	findLabelForEachInput,
	generateCaptcha,
	verifyCaptcha,
	reloadCaptcha
} from '@/services/feedback.service';
import ReloadIcon from './ReloadIcon.vue';
import Loader from './Loader.vue';

@Component({
	components: {
		ReloadIcon,
		Loader
	}
})
export default class SimpleFormComponent extends Vue {
	private formElement = null;
	private inputs = null;
	private simpleFormAction = 'suggest';
	private labels = null;

	get questionnaireSubmitted() {
		return this.$store.getters[FeedbackGetters.questionnaireSubmitted];
	}

	public addRequiredMark(props) {
		this.$nextTick(() => {
			this.labels.forEach(label => {
				const reqMark = document.createElement('span');
				reqMark.setAttribute('class', props.class);
				reqMark.textContent = '*';
				const hasReqMark = label.textContent.includes('*');
				if (hasReqMark) {
					return;
				}
				label.appendChild(reqMark);
			});
		});
	}

	public clearForm() {
		this.inputs.forEach(input => {
			if (input.type === 'radio' || input.type === 'checkbox') {
				return;
			}
			input.value = '';
		});
	}

	public submitForm() {
		const formData = {};
		const allInputsArr = Array.from(this.inputs);
		const inputs: any = allInputsArr.filter((input: any) => input.name !== 'captcha');
		let issueTitle: string;

		inputs.forEach(input => {
			const question = findLabelForEachInput(input);

			switch (input.name) {
				case 'suggestionTitle':
					issueTitle = 'Simple Form Suggestion: ' + input.value;
					break;
				case 'problemTitle':
					issueTitle = 'Simple Form Problem Report: ' + input.value;
					break;
			}

			if (input.type === 'radio' || input.type === 'checkbox') {
				if (input.checked) {
					formData[question] = input.value;
				}
			} else {
				formData[question] = input.value;
			}
		});

		const issueDescription = getFeedbackQuestionsAndAnswers(formData);
		createJiraIssue(issueTitle, issueDescription);
		this.$store.dispatch(FeedbackActions.setQuestionnaireSubmitted, true);
	}

	@Watch('simpleFormAction')
	private onSimpleFormActionChange() {
		this.getFormElements();
		this.addRequiredMark({
			class: 'required-mark',
		});
	}

	private getFormElements() {
		this.$nextTick(() => {
			this.formElement = document.getElementById('simpleForm');
			this.inputs = this.formElement.querySelectorAll('input');
			this.labels = this.formElement.querySelectorAll(
				'.form__controls-label.vertical',
			);
		});
	}

	private verifyCaptcha() {
		verifyCaptcha('captcha', 'captchaInput', '.form__controls-captcha-error', this.submitForm, 'captcha-error', 'block');
	}

	private reloadCaptcha() {
		reloadCaptcha('captcha');
	}

	private mounted() {
		this.getFormElements();
		this.simpleFormAction = 'suggest';
		this.addRequiredMark({
			class: 'required-mark',
		});
		generateCaptcha('captcha', '.loader-container');
	}
}
</script>

<style lang="scss">
@import '@/pages/feedback/feedback.scss';
</style>
