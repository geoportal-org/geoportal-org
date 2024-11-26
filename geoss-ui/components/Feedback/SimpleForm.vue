<template>
	<div v-bar>
        <form class="form" @submit.prevent="submitForm()" id="simpleForm">
			<div class="form__wrapper">
				<img
					:src="`/img/geoss-logo-blue.png`"
					alt="GEOSS Portal logo"
					class="form__logo"
				/>
				<h2 class="form__title">{{ $tc('feedback.feedback') }}</h2>

				<template v-if="!questionnaireSubmitted">
					<div class="form__controls vertical">
						<label for="email" class="form__controls-label vertical">
							{{ $tc('feedback.email') }}:
						</label>
						<input
							class="form__controls-text-input"
							name="email"
							type="email"
							:placeholder="$tc('feedback.placeholder')"
							required
						/>

                        <input type="text" name="phone" style="display:none !important" tabindex="-1" autocomplete="off">

						<label for="firstName" class="form__controls-label vertical">
							{{ $tc('feedback.simpleForm.firstName') }}:
						</label>
						<input
							class="form__controls-text-input"
							name="firstName"
							type="text"
							:placeholder="$tc('feedback.placeholder')"
							required
						/>

						<label for="simpleFormAction" class="form__controls-label vertical"
							>{{ $tc('feedback.simpleForm.wantTo') }}:
						</label>
						<label for="suggest" class="form__controls-label">
							<input
								class="form__controls-radio vertical"
								v-model="simpleFormAction"
								name="simpleFormAction"
								type="radio"
								value="suggest"
							/>

							{{ $tc('feedback.simpleForm.suggestRadio') }}
						</label>

						<label for="report" class="form__controls-label">
							<input
								class="form__controls-radio vertical"
								v-model="simpleFormAction"
								name="simpleFormAction"
								type="radio"
								value="report"
							/>

							{{ $tc('feedback.simpleForm.report') }}
						</label>
						<div class="divider fullwidth" />
						<template v-if="simpleFormAction == 'suggest'">
							<label
								for="suggestionTitle"
								class="form__controls-label vertical"
							>
								{{ $tc('feedback.simpleForm.suggestionTitle') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="suggestionTitle"
								type="text"
								:placeholder="$tc('feedback.placeholder')"
								required
							/>

							<label
								for="suggestionDescription"
								class="form__controls-label vertical"
							>
								{{ $tc('feedback.simpleForm.suggestionDescription') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="suggestionDescription"
								type="text"
								:placeholder="$tc('feedback.placeholder')"
								required
							/>


						</template>
						<template v-if="simpleFormAction == 'report'">
							<label for="problemTitle" class="form__controls-label vertical">
								{{ $tc('feedback.simpleForm.problemTitle') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="problemTitle"
								type="text"
								:placeholder="$tc('feedback.placeholder')"
								required
							/>

							<label
								for="problemDescription"
								class="form__controls-label vertical"
							>
								{{ $tc('feedback.simpleForm.problemDescription') }}:
							</label>
							<input
								class="form__controls-text-input"
								name="problemDescription"
								type="text"
								:placeholder="$tc('feedback.placeholder')"
								required
							/>

							<label for="severity" class="form__controls-label vertical">
								{{ $tc('feedback.simpleForm.severity.label') }}:
							</label>

							<div>
								<label
									class="form__controls-label"
									v-for="option of feedbackConfig.severity.options"
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
					</div>

					<div class="divider" />
					<div class="form__footer">
						<div class="form__footer-btn-holder">
							<button
								@click="clearForm()"
								type="button"
								class="form__footer-clear-btn"
							>
								{{ $tc('feedback.clear') }}
							</button>
							<button type="submit" class="green-btn-default">
								{{ $tc('feedback.submit') }}
							</button>
						</div>
					</div>
				</template>
				<template v-else>
					<div class="form__thanks">
						<p class="form__thanks-text">
							{{ $tc('feedback.thankYou') }}
						</p>
					</div>
				</template>
			</div>
		</form>
	</div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue, Watch } from 'nuxt-property-decorator';
import { FeedbackActions } from '@/store/feedback/feedback-actions';
import { FeedbackGetters } from '@/store/feedback/feedback-getters';
import en from '@/translations/en'
import {
	getFeedbackQuestionsAndAnswers,
    postFeedback,
	findLabelForEachInput,
} from '@/services/feedback.service';
import ReloadIcon from './ReloadIcon.vue';
import Loader from './Loader.vue';
import { $tc, $tm } from '~/plugins/i18n'
import feedback from '~/store/feedback';

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

    get feedbackConfig() {
        return en.feedback.simpleForm
    }

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

        if (allInputsArr.find(input => input.name === 'phone').value) {
            return false
        }

		const inputs: any = allInputsArr.filter((input: any) => input.name !== 'phone');
        let fromName = '';
        let fromMail = '';
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

            if (input.name === 'firstName') {
                fromName = input.value
            }

            if (input.name === 'email') {
                fromMail = input.value
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

        const feedbackData = {
            fromName,
            fromMail,
            subject: issueTitle,
            body: issueDescription
        }

        postFeedback(JSON.stringify(feedbackData))
		this.$store.dispatch(FeedbackActions.setQuestionnaireSubmitted, true);

        return false;
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

	private mounted() {
		this.getFormElements();
		this.simpleFormAction = 'suggest';
		this.addRequiredMark({
			class: 'required-mark',
		});
	}
}
</script>

<style lang="scss">
@import "~/assets/scss/feedback.scss";
</style>
