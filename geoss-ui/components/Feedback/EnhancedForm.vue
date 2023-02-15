<template>
	<div v-bar>
		<form class="form" @submit.prevent="verifyCaptcha()" id="enhanced-form">
			<div class="form__wrapper">
				<div class="form__header">
					<img
						:src="`${staticPath()}/img/geoss-logo-blue.png`"
						alt="GEOSS Portal logo"
						class="form__logo"
					/>
					<h2 class="form__title">{{ $t('feedback.enhancedForm.title') }}</h2>
				</div>

				<template v-if="!questionnaireSubmitted">
					<carousel
						class="enhanced-form"
						v-model="currentPage"
						:scrollPerPage="false"
						:mouse-drag="false"
						:paginationEnabled="true"
						:per-page="1"
						:navigationEnabled="false"
						@pageChange="initialLoad = false"
					>
						<slide :class="{ 'VueCarousel-slide-active': initialLoad }">
							<h3 class="form__subtitle-enhanced uppercase">
								{{ $t('feedback.enhancedForm.introduction') }}
							</h3>
							<div class="slide-content">
								<div class="form__text-content">
									<span class="text-bold">{{
										$t('feedback.enhancedForm.interface')
									}}</span>
									<img
										:src="`${staticPath()}/img/geoss-portal-interface.png`"
										alt="GEOSS Portal interface"
										class="form__interface"
									/>
									<span class="text-bold">{{
										$t('feedback.enhancedForm.page1.title')
									}}</span>
									<p class="form__text-content-paragraph">
										{{ $t('feedback.enhancedForm.page1.description') }}
									</p>

									<p class="form__text-content-paragraph">
										{{ $t('feedback.enhancedForm.page1.descriptionCd') }}
									</p>

									<ul class="form__text-content-list">
										<li>{{ $t('feedback.enhancedForm.page1.li1') }}</li>
										<li>{{ $t('feedback.enhancedForm.page1.li2') }}</li>
										<li>{{ $t('feedback.enhancedForm.page1.li3') }}</li>
									</ul>

									<span class="text-bold">{{
										$t('feedback.enhancedForm.page1.questionnaireHas')
									}}</span>

									<ul class="form__text-content-list">
										<li>{{ $t('feedback.enhancedForm.page1.li4') }}</li>
										<li>{{ $t('feedback.enhancedForm.page1.li5') }}</li>
										<li>{{ $t('feedback.enhancedForm.page1.li6') }}</li>
									</ul>

									<span class="text-bold">{{
										$t('feedback.enhancedForm.page1.measure')
									}}</span>

									<ul class="form__text-content-list">
										<li>{{ $t('feedback.enhancedForm.page1.li7') }}</li>
										<li>{{ $t('feedback.enhancedForm.page1.li8') }}</li>
										<li>{{ $t('feedback.enhancedForm.page1.li9') }}</li>
									</ul>

									<p class="form__text-content-paragraph">
										{{ $t('feedback.enhancedForm.page1.qShouldBe') }}
									</p>

									<p class="form__text-content-paragraph">
										{{ $t('feedback.enhancedForm.page1.estimate') }}
									</p>

									<p class="form__text-content-paragraph">
										{{ $t('feedback.enhancedForm.page1.thankYou') }}
									</p>

									<label for="email" class="form__controls-question">
										{{ $t('feedback.email') }}:
									</label>
									<input
										class="form__controls-text-input"
										name="email"
										id="email"
										type="email"
										:placeholder="$t('feedback.placeholder')"
										required
									/>

									<label for="firstName" class="form__controls-question">
										{{ $t('feedback.simpleForm.firstName') }}:
									</label>
									<input
										class="form__controls-text-input"
										name="firstName"
										type="text"
										:placeholder="$t('feedback.placeholder')"
										required
									/>
								</div>
							</div>
						</slide>
						<!-- PAGE 2 -->
						<slide>
							<div class="slide-content">
								<div class="form__questions">
									<h3 class="form__questions-title">
										{{ $t('feedback.enhancedForm.page2.title') }}
									</h3>
									<p class="form__questions-description">
										{{ $t('feedback.enhancedForm.page2.description') }}
									</p>
								</div>

								<label for="interest" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page2.interest.label') }}
								</label>

								<label
									class="form__controls-label"
									v-for="option in $t(
										'feedback.enhancedForm.page2.interest.options',
									)"
									:key="option.value"
								>
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('interest', $event)"
										name="interest"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>

								<label class="form__controls-label">
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('interest', $event)"
										name="interest"
										value="other"
									/>
									{{ $t('feedback.other') }}

									<input
										type="text"
										class="form__controls-text-input other"
										:disabled="form.interest !== 'other'"
										name="interestText"
										:placeholder="$t('feedback.placeholder')"
									/>
								</label>

								<label for="classify" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page2.classify.label') }}
								</label>

								<label
									class="form__controls-label"
									v-for="option in $t(
										'feedback.enhancedForm.page2.classify.options',
									)"
									:key="option.value"
								>
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('classify', $event)"
										name="classify"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>

								<label class="form__controls-label">
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('classify', $event)"
										name="classify"
										value="other"
									/>
									{{ $t('feedback.other') }}

									<input
										type="text"
										class="form__controls-text-input other"
										:disabled="form.classify !== 'other'"
										name="classifyText"
										:placeholder="$t('feedback.placeholder')"
									/>
								</label>

								<label for="communityOfPractice" class="form__controls-question not-required">
									{{
										$t('feedback.enhancedForm.page2.communityOfPractice.label')
									}}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="communityOfPractice"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="applicationInterests" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page2.applicationInterests.label')
									}}
								</label>

								<label
									class="form__controls-label"
									v-for="option in $t(
										'feedback.enhancedForm.page2.applicationInterests.options',
									)"
									:key="option.value"
								>
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('applicationInterests', $event)"
										name="applicationInterests"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>

								<label class="form__controls-label">
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('applicationInterests', $event)"
										name="applicationInterests"
										value="other"
									/>
									{{ $t('feedback.other') }}

									<input
										type="text"
										class="form__controls-text-input other"
										:disabled="form.applicationInterests !== 'other'"
										name="applicationInterestsText"
										:placeholder="$t('feedback.placeholder')"
									/>
								</label>

								<label for="visited" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page2.visited.label') }}
								</label>

								<label
									class="form__controls-label"
									v-for="option in $t(
										'feedback.enhancedForm.page2.visited.options',
									)"
									:key="option.value"
								>
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('visited', $event)"
										name="visited"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>

								<label for="used" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page2.used.label') }}
								</label>

								<label
									class="form__controls-label"
									v-for="option in $t(
										'feedback.enhancedForm.page2.used.options',
									)"
									:key="option.label"
								>
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('used', $event)"
										name="used"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>

								<label for="specify" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page2.specify.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="specify"
									:placeholder="$t('feedback.placeholder')"
								/>
							</div>
						</slide>

						<!-- PAGE 3 -->
						<slide>
							<div class="slide-content">
								<div class="form__questions">
									<h3 class="form__questions-title">
										{{ $t('feedback.enhancedForm.page3.title') }}
									</h3>
									<p class="form__questions-description">
										{{ $t('feedback.enhancedForm.page3.description') }}
									</p>
								</div>

								<label for="2.1" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page3.goTo.label') }}
								</label>

								<input
									type="text"
									class="hidden"
									value="-"
									name="2.1"
								/>

								<label for="tryToLocate" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.tryToLocate.label') }}
								</label>

								<label
									class="form__controls-label"
									v-for="option in $t(
										'feedback.enhancedForm.page3.tryToLocate.options',
									)"
									:key="option.value"
								>
									<input
										type="radio"
										class="form__controls-radio"
										@change="selectRadio('tryToLocate', $event)"
										name="tryToLocate"
										:value="option.value"
										required
									/>

									{{ option.label }}
								</label>

								<label for="ifYouHaveTime" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page3.ifYouHaveTime.label') }}
								</label>

								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.ifYouHaveTime.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('ifYouHaveTime', $event)"
											name="ifYouHaveTime"
											:value="option.value"
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="didYouFind" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.didYouFind.label') }}
								</label>

								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.didYouFind.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('didYouFind', $event)"
											name="didYouFind"
											:value="option.value"
											required
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="howManyResources" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.howManyResources.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="howManyResources"
									:placeholder="$t('feedback.placeholder')"
									required
								/>

								<label for="withReference" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.withReference.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="withReference"
									:placeholder="$t('feedback.placeholder')"
									required
								/>

								<label for="howManyResults" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.howManyResults.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="howManyResults"
									:placeholder="$t('feedback.placeholder')"
									required
								/>

								<label for="succeedInDownloading" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page3.succedInDownloading.label')
									}}
								</label>

								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.succedInDownloading.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('succedInDownloading', $event)"
											name="succeedInDownloading"
											:value="option.value"
											required
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="tellUsAbout" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page3.tellUsAbout.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="tellUsAbout"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="2.9" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page3.goBack.label') }}
								</label>

								<input
									type="text"
									class="hidden"
									value="-"
									name="2.9"
								/>

								<label for="tryToSignIn" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.tryToSignIn.label') }}
								</label>
								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.tryToSignIn.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('tryToSignIn', $event)"
											name="tryToSignIn"
											:value="option.value"
											required
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="multiSearch" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.multiSearch.label') }}
								</label>
								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.multiSearch.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('multiSearch', $event)"
											name="multiSearch"
											:value="option.value"
											required
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="tryToBookmark" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page3.tryToBookmark.label') }}
								</label>
								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.tryToBookmark.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('tryToBookmark', $event)"
											name="tryToBookmark"
											:value="option.value"
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="lookToWorkspace" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page3.lookToWorkspace.label') }}
								</label>
								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page3.lookToWorkspace.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('lookToWorkspace', $event)"
											name="lookToWorkspace"
											:value="option.value"
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="informationEasyToFind" class="form__controls-question">
									{{
										$t(
											'feedback.enhancedForm.page3.informationEasyToFind.label',
										)
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page3.informationEasyToFind.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('informationEasyToFind', $event)"
												name="informationEasyToFind"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="searchToolsDetailed" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page3.searchToolsDetailed.label')
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page3.searchToolsDetailed.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('searchToolsDetailed', $event)"
												name="searchToolsDetailed"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="iDoNotNeedVideoTut" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page3.iDoNotNeedVideoTut.label')
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page3.iDoNotNeedVideoTut.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('iDoNotNeedVideoTut', $event)"
												name="iDoNotNeedVideoTut"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="contentLogicallyOrganized" class="form__controls-question">
									{{
										$t(
											'feedback.enhancedForm.page3.contentLogicallyOrganized.label',
										)
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page3.contentLogicallyOrganized.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="
													selectRadio('contentLogicallyOrganized', $event)
												"
												name="contentLogicallyOrganized"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="adequatelyDescribed" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page3.adequatelyDescribed.label')
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page3.adequatelyDescribed.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('adequatelyDescribed', $event)"
												name="adequatelyDescribed"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="otherHelpfulSearchCriteria" class="form__controls-question not-required">
									{{
										$t(
											'feedback.enhancedForm.page3.otherHelpfulSearchCriteria.label',
										)
									}}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="otherHelpfulSearchCriteria"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="portalSpeed" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page3.portalSpeed.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page3.portalSpeed.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('portalSpeed', $event)"
												name="portalSpeed"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="anyVisualizationOfData" class="form__controls-question not-required">
									{{
										$t(
											'feedback.enhancedForm.page3.anyVisualizationOfData.label',
										)
									}}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="anyVisualizationOfData"
									:placeholder="$t('feedback.placeholder')"
								/>
							</div>
						</slide>
						<!-- PAGE 4 -->
						<slide>
							<div class="slide-content">
								<div class="form__questions">
									<h3 class="form__questions-title">
										{{ $t('feedback.enhancedForm.page4.title') }}
									</h3>
									<p class="form__questions-description">
										{{ $t('feedback.enhancedForm.page4.description') }}
									</p>
								</div>

								<label for="usePortalFrequently" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page4.usePortalFrequently.label')
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.usePortalFrequently.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('usePortalFrequently', $event)"
												name="usePortalFrequently"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="unnecessarilyComplex" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page4.unnecessarilyComplex.label')
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.unnecessarilyComplex.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('unnecessarilyComplex', $event)"
												name="unnecessarilyComplex"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="easyToUse" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page4.easyToUse.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.easyToUse.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('easyToUse', $event)"
												name="easyToUse"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="needSupport" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page4.needSupport.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.needSupport.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('needSupport', $event)"
												name="needSupport"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="wellIntegratedFunctions" class="form__controls-question">
									{{
										$t(
											'feedback.enhancedForm.page4.wellIntegratedFunctions.label',
										)
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.wellIntegratedFunctions.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('wellIntegratedFunctions', $event)"
												name="wellIntegratedFunctions"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="tooMuchInconsistency" class="form__controls-question">
									{{
										$t('feedback.enhancedForm.page4.tooMuchInconsistency.label')
									}}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.tooMuchInconsistency.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('tooMuchInconsistency', $event)"
												name="tooMuchInconsistency"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="learnQuickly" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page4.learnQuickly.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.learnQuickly.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('learnQuickly', $event)"
												name="learnQuickly"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="cumbersomeToUse" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page4.cumbersomeToUse.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.cumbersomeToUse.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('cumbersomeToUse', $event)"
												name="cumbersomeToUse"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="feltConfident" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page4.feltConfident.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.feltConfident.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('feltConfident', $event)"
												name="feltConfident"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>

								<label for="neededToLearn" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page4.neededToLearn.label') }}
								</label>

								<div class="form__controls-horizontal-radios">
									<span class="form__controls-horizontal-radios-txt"
										>Strongly disagree</span
									>
									<div class="form__controls-horizontal-radios-options">
										<label
											class="form__controls-label"
											v-for="option in $t(
												'feedback.enhancedForm.page4.neededToLearn.options',
											)"
											:key="option.value"
										>
											<input
												type="radio"
												class="form__controls-radio"
												@change="selectRadio('neededToLearn', $event)"
												name="neededToLearn"
												:value="option.value"
												required
											/>

											{{ option.label }}
										</label>
									</div>
									<span class="form__controls-horizontal-radios-txt"
										>Strongly agree</span
									>
								</div>
							</div>
						</slide>
						<!-- PAGE 5 -->
						<slide>
							<div class="slide-content">
								<label for="portalFeature" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page5.portalFeature.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="portalFeature"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="whichFeatures" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page5.whichFeatures.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="whichFeatures"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="suggestions" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page5.suggestions.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="suggestions"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="wouldReturn" class="form__controls-question">
									{{ $t('feedback.enhancedForm.page5.wouldReturn.label') }}
								</label>
								<div>
									<label
										class="form__controls-label"
										v-for="option in $t(
											'feedback.enhancedForm.page5.wouldReturn.options',
										)"
										:key="option.value"
									>
										<input
											type="radio"
											class="form__controls-radio"
											@change="selectRadio('wouldReturn', $event)"
											name="wouldReturn"
											:value="option.value"
											required
										/>

										{{ option.label }}
									</label>
								</div>

								<label for="leaveQuote" class="form__controls-question not-required">
									{{ $t('feedback.enhancedForm.page5.leaveQuote.label') }}
								</label>
								<input
									type="text"
									class="form__controls-text-input"
									name="leaveQuote"
									:placeholder="$t('feedback.placeholder')"
								/>

								<label for="captcha" class="form__controls-question">
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
									<span class="form__controls-captcha-error">
										Please try again
									</span>
							</div>
							</div>
						</slide>
						<!-- for some reason carousel don't see last 2 slides (they exists in the HTML structure but can not be scrolled to) -->
						<slide></slide>
						<slide></slide>
					</carousel>
				</template>
				<template v-else>
					<div class="form__thanks">
						<p class="form__thanks-text">
							{{ $t('feedback.thankYou') }}
						</p>
					</div>
				</template>
			</div>
			<button type="submit" id="enhanced-form-submit" class="hidden" />
		</form>

		<div class="form__footer" v-if="!questionnaireSubmitted">
			<div class="divider" />
			<div class="form__footer-btn-holder">
				<button
					@click="clearForm()"
					type="button"
					class="form__footer-clear-btn"
				>
					{{ $t('feedback.clear') }}
				</button>

				<template v-if="this.currentPage !== 4">
					<button
						type="button"
						class="green-btn-default"
						@click="nextPage()"
					>
						{{ $t('feedback.nextPage') }}
					</button>
				</template>

				<template v-if="this.currentPage === 4">
					<label
						for="enhanced-form-submit"
						role="button"
						class="green-btn-default label-as-btn"
						tabindex="0"
					>
						{{ $t('feedback.submit') }}
					</label>
				</template>
			</div>
		</div>
	</div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import { FeedbackActions } from '@/stores/feedback/feedback-actions';
import { FeedbackGetters } from '@/stores/feedback/feedback-getters';
import { Liferay } from '@/data/global';
import { Carousel, Slide } from 'vue-carousel';
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
		Carousel,
		Slide,
		ReloadIcon,
		Loader
	},
})
export default class EnhancedFormComponent extends Vue {
	public currentPage = 0;
	// for 'other' option only
	public form = {
		interest: '',
		classify: '',
		applicationInterests: '',
	};
	private formElement = null;
	private inputs = null;
	private labels = null;
	private pagination = null;
	private formBtn = null;
	private initialLoad = true;
	private paginationDots = null;
	private scrollable = null;
	private allowPageChange = false;
	private activeSlide = null;
	private reqInputs = null;
	private activeDot = null;
	private vueCarouselInner = null;
	private formFooter = null;
	private activeSlideContent = null;

	get questionnaireSubmitted() {
		return this.$store.getters[FeedbackGetters.questionnaireSubmitted];
	}

	public getAllNextElementSiblings = element => {
		const nextAllFound = [];
		const getAll = element => {
			if (element !== null) {
				nextAllFound.push(element);
				const nextFound = element.nextElementSibling;
				if (nextFound !== null) {
					getAll(nextFound);
				}
			}
		};
		getAll(element.nextElementSibling);
		return nextAllFound;
	}

	public getFormElements() {
		this.$nextTick(() => {
			this.formElement = document.getElementById('enhanced-form');
			this.inputs = this.formElement.querySelectorAll('input');
			this.labels = this.formElement.querySelectorAll('.form__controls-question');
			this.formBtn = document.querySelector('.form__footer-clear-btn');
			this.pagination = document.querySelector('.VueCarousel-pagination');
			this.paginationDots = document.querySelectorAll('.VueCarousel-dot');
			this.scrollable = document.querySelector('#enhanced-form');
			this.vueCarouselInner = document.querySelector('.VueCarousel-inner');
			this.activeSlide = document.querySelector('.VueCarousel-slide-active');
			this.formFooter = document.querySelector('.form__footer');
			this.activeSlideContent = this.activeSlide.querySelector('.slide-content');
			this.reqInputs = this.activeSlide.querySelectorAll('[required]');
			this.activeDot = document.querySelector('.VueCarousel-dot--active');
		});
	}

	public nextPage() {
		const validateEmail = (email: string) => {
			const regex = /^\S+@\S+\.\S+$/;
			return regex.test(email);
		};

		// variable mailInput has type any because of reportValidity()
		const mailInput: any = document.querySelector('input[type="email"]');
		const isMailCorrect: boolean = validateEmail(mailInput.value);

		if (!isMailCorrect) {
			mailInput.reportValidity();
			this.allowPageChange = false;
		} else {
			this.checkInputsInActiveSlide();
		}

		if (this.allowPageChange) {
			this.currentPage += 1;
			this.fixSlideHeight();
			this.getFormElements();
		}
	}

	public handleDots() {
		this.$nextTick(() => {
			const nextSiblings = this.getAllNextElementSiblings(this.activeDot);

			if (!this.allowPageChange) {
				nextSiblings.forEach(sibling => sibling.classList.add('dot-disabled'));
			} else {
				if (this.activeDot.nextElementSibling !== null) {
					this.activeDot.classList.remove('dot-disabled');
				}
			}
		});
	}

	public selectRadio(prop: string, event: Event) {
		if (
			!this.form[prop] &&
			(event.currentTarget as HTMLInputElement).value !== 'other'
		) {
			return;
		}
		this.form[prop] = (event.target as HTMLInputElement).value;
	}

	public addRequiredMark(props) {
		this.$nextTick(() => {
			this.labels.forEach(label => {
				const reqMark = document.createElement('span');
				reqMark.setAttribute('class', props.class);
				reqMark.textContent = '*';
				const hasReqMark = label.textContent.includes('*');
				const notRequired = label.classList.contains('not-required');
				if (hasReqMark || notRequired) {
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

		for (const input of this.inputs) {
			input.checked = false;
		}

		this.currentPage = 0;
		this.getFormElements();
		this.fixSlideHeight();
		this.checkInputsInActiveSlide();
	}

	public submitForm() {
		const formData = {};
		const allInputsArr = Array.from(this.inputs);
		const inputs: any = allInputsArr.filter((input: any) => input.name !== 'captcha');
		const issueTitle: string = 'Enhanced Form';

		inputs.forEach(input => {
			const question = findLabelForEachInput(input);

			if ((input.type === 'radio' || input.type === 'checkbox') && input.name !== '') {
				if (input.checked) {
					if (input.value === 'other') {
						const inputText: any = document.querySelector(`input[name="${input.name}Text"]`);
						formData[question] = inputText.value;
					} else {
						formData[question] = input.parentElement.innerText;
					}
				}
			} else if (!input.classList.contains('other')) {
				formData[question] = input.value;
			}
		});

		const issueDescription = getFeedbackQuestionsAndAnswers(formData);
		createJiraIssue(issueTitle, issueDescription);
		this.$store.dispatch(FeedbackActions.setQuestionnaireSubmitted, true);
	}

	public checkInputsInActiveSlide() {
		let allFieldsFilled = true;
		this.reqInputs.forEach(input => {
			if (!allFieldsFilled) {
				return;
			}
			if (input.type === 'radio') {
				let radioValueCheck = false;
				const radios = this.activeSlide.querySelectorAll(`[name=${input.name}]`);
				radios.forEach(radio => {
					if (radio.checked) {
						radioValueCheck = true;
					} else {
						radio.reportValidity();
					}
				});
				allFieldsFilled = radioValueCheck;

				if (allFieldsFilled) {
					this.allowPageChange = true;
				}
				return;
			}
			if (!input.value) {
				allFieldsFilled = false;
				this.allowPageChange = false;
				input.reportValidity();
				return;
			}
		});
		if (!allFieldsFilled) {
			this.allowPageChange = false;
		} else {
			this.allowPageChange = true;
			this.handleDots();
		}
	}

	private handlePagination() {
		this.$nextTick(() => {
			this.formBtn.parentNode.insertBefore(
				this.pagination,
				this.formBtn.nextSibling,
			);
			this.paginationDots.forEach(dot =>
				dot.addEventListener('click', () => {
					this.fixSlideHeight();
					this.getFormElements();
					this.handleDots();
				}),
			);
		});
	}

	private fixSlideHeight() {
		this.$nextTick(() => {
			setTimeout(() => {
				this.getFormElements();
				const activeSlideContentHeight = this.activeSlideContent.offsetHeight;
				const formFooterHeight = this.formFooter.offsetHeight;
				const dockbarHeight = 40;
				const isLoggedIn = Liferay.ThemeDisplay.isSignedIn();

				if (isLoggedIn) {
					this.vueCarouselInner.style.height =
					`${activeSlideContentHeight + (formFooterHeight / 2) + dockbarHeight}` + 'px';
				} else {
					this.vueCarouselInner.style.height =
					`${activeSlideContentHeight + (formFooterHeight / 2)}` + 'px';
			}}, 300);

			setTimeout(() => {
				this.scrollable.scrollTop = 0;
			}, 500);
		});
	}

	private fixSlideHeightOnWindowResize() {
		window.addEventListener('resize', this.fixSlideHeight);
	}

	private verifyCaptcha() {
		verifyCaptcha('captcha', 'captchaInput', '.form__controls-captcha-error', this.submitForm, 'captcha-error', 'block');
	}

	private reloadCaptcha() {
		reloadCaptcha('captcha');
	}

	private setGeossPortalLinkTarget() {
		const link = document.querySelector('.header__middle a');
		link.setAttribute('target', '_blank');
	}

	private mounted() {
		this.setGeossPortalLinkTarget();
		this.getFormElements();
		this.handlePagination();
		this.addRequiredMark({
			class: 'required-mark',
		});
		this.fixSlideHeight();
		this.handleDots();
		this.fixSlideHeightOnWindowResize();
		generateCaptcha('captcha', '.loader-container');
	}
}
</script>

<style lang="scss">
@import '@/pages/feedback/feedback.scss';

.VueCarousel {
	&.enhanced-form {
		max-width: 430px;
		width: 100%;
	}

	&-slide {
		user-select: text;

		@media (min-width: $breakpoint-md) {
			min-width: 430px;
			max-width: 430px;
		}
	}

	&-wrapper {
		width: calc(100% - 25px);
		margin: auto;

		@media (min-width: $breakpoint-md) {
			width: 100%;
		}
	}

	&-pagination {
		width: fit-content !important;
	}

	&-dot {
		margin: 0 !important;

		button {
			outline: 0;
			background: rgb(214, 214, 214) !important;
		}

		&--active {
			button {
				background: $black !important;
			}
		}
	}
}
</style>
