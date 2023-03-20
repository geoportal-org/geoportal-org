<template>
    <div class="survey"
        :class="{ 'hide-navigation': currentPage > 8, 'hide-next-button': currentPage === 8, 'hide-prev-button': currentPage === 0, 'survey-complete': currentPage === 9 }"
        :style="`background: url('/svg/survey-background.svg') center center no-repeat;`">
        <!-- <carousel class="survey" ref="slider" v-model="currentPage" :scrollPerPage="false" :mouse-drag="false"
            :paginationEnabled="true" :per-page="1" :navigationNextLabel="''" :navigationPrevLabel="''"
            :navigationEnabled="true">
            <slide class="survey__slide_item question question--impression">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text one-liner">{{ $tc('survey.generalImpression.question') }}</div>
                <div class="question--impression__container">
                    <div class="question--impression__choice" @click="selectImpression('negative')">
                        <img class="question--impression__img" :src="`/svg/survey-face_negative.svg`"
                            :alt="$tc('survey.generalImpression.negative')" />
                        <span class="question--impression__caption">{{
                            $tc('survey.generalImpression.negative')
                        }}</span>
                    </div>
                    <div class="question--impression__choice" @click="selectImpression('neutral')">
                        <img class="question--impression__img" :src="`/svg/survey-face_neutral.svg`"
                            :alt="$tc('survey.generalImpression.neutral')" />
                        <span class="question--impression__caption">{{ $tc('survey.generalImpression.neutral') }}</span>
                    </div>
                    <div class="question--impression__choice" @click="selectImpression('positive')">
                        <img class="question--impression__img" :src="`/svg/survey-face_positive.svg`"
                            :alt="$tc('survey.generalImpression.positive')" />
                        <span class="question--impression__caption">{{
                            $tc('survey.generalImpression.positive')
                        }}</span>
                    </div>
                </div>
            </slide>
            <slide class="survey__slide_item question question--did-you-find">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text">
                    {{ $tc('survey.didYouFind.thankYou') }}<br />{{ $tc('survey.didYouFind.question') }}</div>
                <div class="question__answers question__answers--did-you-find">
                    <button type="button" class="answer--no" @click="answerDidYouFind('no')">{{
                        $tc('survey.didYouFind.no')
                    }}</button>
                    <button type="button" class="answer--partially" @click="answerDidYouFind('partially')">{{
                        $tc('survey.didYouFind.partially')
                    }}</button>
                    <button type="button" class="answer--yes" @click="answerDidYouFind('yes')">{{
                        $tc('survey.didYouFind.yes')
                    }}</button>
                </div>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text one-liner">{{ $tc('survey.whatWereYouLookingFor.question') }}</div>
                <textarea class="question__answers--textarea"
                    :placeholder="$tc('survey.whatWereYouLookingFor.placeholder')"
                    v-model="form.what_looking_for"></textarea>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text">{{ $tc('survey.interest.question1') }}
                    <br />{{ $tc('survey.interest.question2') }}
                </div>
                <div class="question__answers question__answers--single_choice">
                    <label class="answer" v-for="option in $tc('survey.interest.options')" :key="option.value">
                        <input type="radio" @change="selectRadio('interest', $event)" name="interest"
                            :value="option.value">
                        <span class="checkmark"></span> {{ option.label }}
                    </label>
                    <label class="answer">
                        <input type="radio" @change="selectRadio('interest', $event)" name="interest" value="other">
                        <span class="checkmark"></span> {{ $tc('survey.other') }}:
                        <input type="text" :disabled="form.interest !== 'other'" v-model="form.interestText"
                            :placeholder="$tc('survey.whatWereYouLookingFor.placeholder')" />
                    </label>
                </div>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text one-liner">{{ $tc('survey.classification.question') }}</div>
                <div class="question__answers question__answers--single_choice">
                    <label class="answer" v-for="option in $tc('survey.classification.options')" :key="option.value">
                        <input type="radio" @change="selectRadio('classification', $event)" name="classification"
                            :value="option.value">
                        <span class="checkmark"></span> {{ option.label }}
                    </label>
                    <label class="answer">
                        <input type="radio" @change="selectRadio('classification', $event)" name="classification"
                            value="other">
                        <span class="checkmark"></span> {{ $tc('survey.other') }}:
                        <input type="text" :disabled="form.classification !== 'other'"
                            v-model="form.classificationText">
                    </label>
                </div>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text one-liner">{{ $tc('survey.organized.title') }}</div>
                <div class="question__quote">"{{ $tc('survey.organized.question') }}"</div>
                <div class="question__answers question__answers--single_horizontal_choice"
                    :style="`background: url('/img/survey-barometer.png') center bottom no-repeat;`">
                    <label class="strongly-disagree">{{ $tc('survey.organized.stronglyDisagree') }}</label>
                    <label class="answer" v-for="value in [1, 2, 3, 4, 5]" :key="value">
                        <input type="radio" @change="selectRadio('organized', $event)" name="organized" :value="value">
                        <span class="checkmark"></span>
                    </label>
                    <label class="strongly-agree">{{ $tc('survey.organized.stronglyAgree') }}</label>
                </div>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text one-liner">{{ $tc('survey.adequately.title') }}</div>
                <div class="question__quote">"{{ $tc('survey.adequately.question') }}"</div>
                <div class="question__answers question__answers--single_horizontal_choice"
                    :style="`background: url('/img/survey-barometer.png') center bottom no-repeat;`">
                    <label class="strongly-disagree">{{ $tc('survey.adequately.stronglyDisagree') }}</label>
                    <label class="answer" v-for="value in [1, 2, 3, 4, 5]" :key="value">
                        <input type="radio" @change="selectRadio('adequately', $event)" name="adequately"
                            :value="value">
                        <span class="checkmark"></span>
                    </label>
                    <label class="strongly-agree">{{ $tc('survey.adequately.stronglyAgree') }}</label>
                </div>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text one-liner">{{ $tc('survey.search_criteria.question') }}</div>
                <textarea name="search_criteria" class="question__answers--textarea"
                    :placeholder="$tc('survey.whatWereYouLookingFor.placeholder')"
                    v-model="form.search_criteria"></textarea>
            </slide>
            <slide class="survey__slide_item question">
                <div class="question__number">{{ $tc('survey.question') }} {{ currentPage + 1}}/9</div>
                <div class="question__text">{{ $tc('survey.visualization.question') }}</div>
                <textarea name="visualization" class="question__answers--textarea"
                    :placeholder="$tc('survey.whatWereYouLookingFor.placeholder')"
                    v-model="form.visualization"></textarea>
            </slide>
            <slide class="survey__slide_item question question--survey-complete">
                <div class="question__text">{{ $tc('survey.thankYouForCompleting') }}</div>
            </slide>
            for some reason carousel don't see last 2 slides (they exists in the HTML structure but can not be scrolled to)
        <slide></slide>
        <slide></slide>
        </carousel> -->
        <div class="d-flex flex--justify-between buttons-wrapper">
            <div class="checkbox">
                <input id="hide-in-future" type="checkbox" @change="toggleHideInFuture($event)" />
                <label for="hide-in-future">
                    <i class="icomoon-tick"></i>
                    <span>{{ $tc('survey.hideInFuture') }}</span>
                </label>
            </div>
            <button v-if="currentPage !== 9" type="button" class="green-btn-default" @click="submit()">{{
                $tc('survey.submit')
            }}</button>
            <button v-else type="button" class="green-btn-default" @click="closePopup()">{{ $tc('survey.ok') }}</button>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'nuxt-property-decorator';
// import { Carousel, Slide } from 'vue-carousel';

import GeossSearchApiService from '@/services/geoss-search.api.service';
import PopupCloseService from '@/services/popup-close.service';
// import { PopupGetters } from '@/store/popup/popup-getters';

@Component({
    components: {
    }
})
export default class SurveyComponent extends Vue {
    public currentPage: number = 0;
    public form: any = {
        impression: '',
        did_found_what_looking_for: '',
        what_looking_for: '',
        interest: '',
        interestText: '',
        classification: '',
        classificationText: '',
        organized: '',
        adequately: '',
        search_criteria: '',
        visualization: ''
    };

    public answerDidYouFind(didYouFind: string) {
        this.form.did_found_what_looking_for = didYouFind;
        this.switchToNextQuestion();
    }

    public selectRadio(prop: string, event: Event) {
        if (!this.form[prop] && (event.currentTarget as HTMLInputElement).value !== 'other') {
            this.switchToNextQuestion();
        }
        this.form[prop] = (event.target as HTMLInputElement).value;
    }

    public switchToNextQuestion() {
        if (this.currentPage < 8) {
            this.currentPage += 1;
        }
    }

    public switchToPrevQuestion() {
        if (this.currentPage > 2) {
            this.currentPage -= 1;
        }
    }

    public selectImpression(impression: string) {
        this.form.impression = impression;
        this.switchToNextQuestion();
    }

    public closePopup() {
        PopupCloseService.closePopup('survey');
    }

    public submit(closePopup: boolean = false) {
        if (this.form.interest === 'other') {
            this.form.interest = this.form.interestText;
        }

        if (this.form.classification === 'other') {
            this.form.classification = this.form.classificationText;
        }

        const data = {
            impression: this.form.impression,
            did_found_what_looking_for: this.form.did_found_what_looking_for,
            what_looking_for: this.form.what_looking_for,
            interest: this.form.interest,
            classification: this.form.classification,
            organized: this.form.organized,
            adequately: this.form.adequately,
            search_criteria: this.form.search_criteria,
            visualization: this.form.visualization
        };

        this.form = {
            impression: '',
            did_found_what_looking_for: '',
            what_looking_for: '',
            interest: '',
            interestText: '',
            classification: '',
            classificationText: '',
            organized: '',
            adequately: '',
            search_criteria: '',
            visualization: ''
        };

        GeossSearchApiService.sendSurveyData(data);

        if (closePopup) {
            this.closePopup();
        } else {
            this.currentPage = 9;
        }
    }

    public toggleHideInFuture(event: Event) {
        const value = (event.target as HTMLInputElement).checked;

        if (value) {
            const date = new Date();
            date.setFullYear(date.getFullYear() + 1);
            this.$cookies.set('hide-survey', 'true', { expires: date });
        } else {
            this.$cookies.remove('hide-survey');
        }
    }

    private arrowButtonsListener(event: KeyboardEvent) {
        if (event.which === 37) {
            this.switchToPrevQuestion();
        } else if (event.which === 39) {
            this.switchToNextQuestion();
        }
    }

    private created() {
        const date = new Date();
        date.setDate(date.getDate() + 1);
        this.$cookies.set('hide-survey', 'true', { expires: date });
    }

    private mounted() {
        document.addEventListener('keyup', this.arrowButtonsListener);
    }

    private beforeDestroy() {
        document.removeEventListener('keyup', this.arrowButtonsListener);
    }
}
</script>

<style lang="scss">
.survey {
    padding: 10px 30px;
    position: static;
    background-size: 0 0 !important;

    @media(max-width: $breakpoint-lg) {
        padding: 10px;
    }

    @media(max-width: $breakpoint-lg) {
        &-popup {
            max-width: calc(100% - 30px) !important;

            .vb-content {
                width: 100% !important;
            }
        }
    }

    &.survey-complete {
        background-size: cover !important;

        .buttons-wrapper {
            border-top: 1px solid transparent;
        }
    }

    &.continue {

        .slick-prev,
        .slick-next,
        button.submit,
        .slick-dots {
            display: block !important;
        }
    }

    &.hide-navigation {

        .VueCarousel-navigation-prev,
        .VueCarousel-navigation-next,
        .VueCarousel-dot {
            visibility: hidden;
        }
    }

    &.hide-next-button .VueCarousel-navigation-next {
        display: none;
    }

    &.hide-prev-button .VueCarousel-navigation-prev {
        display: none;
    }

    .buttons-wrapper {
        border-top: 1px solid $grey;
        padding: 25px 0 10px;

        .green-btn-default {
            padding: 8px 40px;
            font-size: 18px;
        }
    }

    .checkbox {
        label {
            font-weight: normal;

            &:before {
                border: 1px solid;
                border-radius: 3px;
            }
        }
    }

    .VueCarousel {
        &.survey {
            @media(max-width: $breakpoint-lg) {
                padding: 0;
            }
        }

        &-navigation {

            &-prev,
            &-next {
                top: 60px;
                transform: none !important;

                @media(max-width: $breakpoint-lg) {
                    top: 3px;
                }
            }

            &-prev {
                left: 40px !important;

                @media(max-width: $breakpoint-lg) {
                    left: 10px !important;
                }
            }

            &-next {
                right: 40px !important;

                @media(max-width: $breakpoint-lg) {
                    right: 10px !important;
                }
            }
        }

        &-pagination {
            position: absolute;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            width: auto;

            @media(max-width: $breakpoint-lg) {
                position: static;
                transform: none;
            }
        }

        &-dot {
            margin-top: 0 !important;

            button {
                outline: 0;
                background: rgb(214, 214, 214) !important;
            }

            &--active {
                button {
                    background: black !important;
                }
            }

            &:nth-child(10) {
                display: none;
            }
        }

        &-navigation-prev,
        &-navigation-next {
            &[disabled] {
                opacity: 0.5;
                cursor: default;
            }

            &:before,
            &:after {
                background-color: $green;
            }
        }
    }

    .question {
        padding: 0 15px;
        box-shadow: none;
        outline: 0;

        @media(max-width: $breakpoint-lg) {
            width: 100%;
            padding: 0;
        }

        &__number {
            text-align: center;
            color: $grey-darker;
            margin: 10px 0 15px;

            @media(max-width: $breakpoint-lg) {
                margin-bottom: 25px;
            }
        }

        &__text,
        &__title {
            text-align: center;
            font-size: 22px;
            font-weight: bold;
            margin-bottom: 20px;

            @media(max-width: $breakpoint-lg) {
                padding: 0 5px;
            }

            &.one-liner {
                padding: 0.6em 0;

                @media(max-width: $breakpoint-lg) {
                    padding: 0.6em 5px;
                }
            }

            @media(max-width: $breakpoint-lg) {
                br {
                    display: none;
                }
            }
        }

        &__title {
            font-size: 19px;
            text-decoration: underline;
        }

        &__quote {
            font-style: italic;
            text-align: center;
            font-size: 22px;
            margin: 20px 0;

            @media(max-width: $breakpoint-lg) {
                padding: 0 5px;
            }
        }
    }

    .question--impression {
        &__container {
            display: flex;
            justify-content: center;
            align-items: center;
        }

        &__choice {
            cursor: pointer;
            display: flex;
            flex-direction: column;
            align-items: center;
            margin: 10px 27px 27px;

            @media(max-width: $breakpoint-lg) {
                margin: 10px 10px 20px;
            }
        }

        &__img {
            margin-bottom: 15px;
        }

        &__caption {
            color: $grey;
        }
    }

    .question--survey-complete {
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .question__answers--did-you-find,
    .question__answers--finish {
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 65px;

        button {
            background: white;
            color: $green;
            border: 1px solid $green;
            padding: 7px 25px;
            font-size: 18px;
            border-radius: 5px;
            transition: 0.2s ease all;
            flex-basis: 17%;

            &:hover {
                background: $green;
                color: white;
            }

            &+button {
                margin-left: 15px;
            }
        }
    }

    .question__answers--single_choice {
        margin-bottom: 20px;
    }

    .question__answers--single_horizontal_choice {
        max-width: 490px;
        margin: 0 auto;
        padding-bottom: 10px;
        margin-bottom: 50px;
        position: relative;

        @media(max-width: $breakpoint-lg) {
            max-width: 280px;
            background-size: 93% auto !important;
        }

        .strongly {
            &-agree {
                position: absolute;
                bottom: -30px;
                right: 0;
                transform: translateX(50%);
                color: $grey;

                @media(max-width: $breakpoint-lg) {
                    transform: none;
                }
            }

            &-disagree {
                position: absolute;
                bottom: -30px;
                left: 0;
                transform: translateX(-50%);
                color: $grey;

                @media(max-width: $breakpoint-lg) {
                    transform: none;
                }
            }
        }

        .answer {
            .checkmark {
                margin-right: 0 !important;
            }
        }
    }

    .question__answers--single_choice,
    .question__answers--multiple_choice,
    .question__answers--single_horizontal_choice {
        .answer {
            display: block;
            position: relative;
            margin-bottom: 12px;
            cursor: pointer;
            font-size: 16px;
            user-select: none;
            display: flex;
            align-items: center;

            input[type="radio"],
            input[type="checkbox"] {
                position: absolute;
                z-index: -1;
                opacity: 0;
                width: 0;
                height: 0;
                opacity: 0;
            }

            input[type="text"] {
                margin: -5px 10px;
                border: 1px solid $grey;
                background: none;
                box-shadow: none;
                padding: 5px;
                font-size: 15px;
                width: 250px;

                &:focus {
                    border: 1px solid $green;
                }
            }

            .checkmark {
                width: 20px;
                height: 20px;
                display: block;
                position: relative;
                margin-right: 15px;

                &:before {
                    content: '';
                    width: 18px;
                    height: 18px;
                    border-radius: 50%;
                    position: absolute;
                    left: 0px;
                    top: -3px;
                    border: 2px solid black;
                }

                &:after {
                    content: '';
                    width: 10px;
                    height: 10px;
                    border-radius: 50%;
                    position: absolute;
                    left: 6px;
                    top: 3px;
                    border: none;
                    background-color: black;
                    display: none;
                }
            }

            input:checked~.checkmark:after {
                display: block;
            }
        }
    }

    .question__answers--multiple_choice {
        .answer {
            .checkmark {
                &:before {
                    border-radius: 3px;
                }

                &:after {
                    left: 7px;
                    top: 2px;
                    width: 7px;
                    height: 13px;
                    border: 1px solid $green;
                    border-radius: 0;
                    border-width: 0px 3px 3px 0px;
                    transform: rotate(45deg);
                    background-color: transparent;
                }
            }
        }
    }

    .question__answers--single_horizontal_choice {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 50px;

        .question__answers>label:not(.answer) {
            color: $green;
            font-size: 17px;
            text-align: center;
        }
    }

    .question__answers--textarea {
        width: calc(100% - 2px);
        height: 150px;
        border: 1px solid $grey;
        border-radius: 5px;
        resize: none;
        padding: 10px;

        &:focus {
            border: 1px solid $green;
            outline: none;
        }
    }

    input,
    textarea {
        &::placeholder {
            color: $grey;
            font-style: italic;
            font-size: 14px;
            font-weight: 400;
        }
    }
}
</style>
