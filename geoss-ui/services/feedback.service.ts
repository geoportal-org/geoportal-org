import { makeRequest } from './general.api.service';
let captchaID = null;

export const findLabelForEachInput = input => {
	const inputName = input.name;
	const labels = document.getElementsByTagName('label');
	const labelsArr: any = Array.from(labels);

	const filteredLabelsArr = labelsArr.filter(label => {
		return label.htmlFor !== 'enhanced-form-submit';
	});

	for (const label of filteredLabelsArr) {
		if (label.htmlFor === inputName) {
			const inputLabels = label.innerText;
			return inputLabels;
		}
	}
};

export const getFeedbackQuestionsAndAnswers = (feedbackData: any) => {
	let description = '';

	Object.entries(feedbackData).map(([key, value]) => {
		description += `${key}\n${value}\n\n`;
	});

	return description;
};

export const createJiraIssue = (
	issueTitle: string,
	issueDescription: string
) => {
	const endpointUrl = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=JIRA_ISSUE`;

	return makeRequest('post', endpointUrl, {
		issueTitle,
		issueDescription
	}).catch(() => {
		return false;
	});
};

export const generateCaptcha = (captchaIMGTagID, captchaLoaderSelector) => {
	const captchaEndpoint = '/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=SERVE_CAPTCHA';

	return makeRequest('get', captchaEndpoint).then(res => {
		const { captchaId, captchaImg } = res;
		captchaID = captchaId;
		const captchaLoader = document.querySelector(captchaLoaderSelector);
		const captchaIMGTag: any = document.getElementById(captchaIMGTagID);

		if (captchaImg !== undefined) {
			captchaIMGTag.src = `data:image/png;base64, ${captchaImg}`;
			captchaLoader.classList.add('hidden');
		}
	}).catch(() => {
		return false;
	});
};

export const reloadCaptcha = captchaIMGTagID => {
	const captchaReloadEndpoint = `https://geoss.devel.esaportal.eu/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=RELOAD_CAPTCHA&_geossresources_WAR_geossportlet_previousCaptchaId=${captchaID}`;

	return makeRequest('get', captchaReloadEndpoint).then(res => {
		const { captchaId, captchaImg } = res;
		captchaID = captchaId;
		const captchaIMGTag: any = document.getElementById(captchaIMGTagID);
		captchaIMGTag.src = `data:image/png;base64, ${captchaImg}`;
	}).catch(() => {
		return false;
	});
};

export const verifyCaptcha = (captchaIMGTagID, captchaInputID, captchaErrorElement, submitFormFunction, captchaErrorClass, captchaErrorStyle) => {
	const captchaInput: any = document.getElementById(captchaInputID);
	const captchaInputValue = captchaInput.value;
	const captchaErrorMsg: HTMLElement = document.querySelector(captchaErrorElement);
	const captchaVerificationEndpoint = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=VERIFY_CAPTCHA&_geossresources_WAR_geossportlet_captchaId=${captchaID}&_geossresources_WAR_geossportlet_captchaAnswer=${captchaInputValue}`;

	makeRequest('get', captchaVerificationEndpoint).then(res => {
		const { challange } = res;
		if (challange === 'success') {
			submitFormFunction();
		} else {
			reloadCaptcha(captchaIMGTagID);
			captchaInput.classList.add(captchaErrorClass);
			captchaErrorMsg.style.display = captchaErrorStyle;
		}
	}).catch(() => {
		return false;
	});
};