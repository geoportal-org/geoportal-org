// import { makeRequest } from './general.api.service'
// let captchaID: string | null = null

import apiClient from '@/api/apiClient'
import geossPersonaldata from '@/api/module/geoss-personaldata'

export const findLabelForEachInput = (input: any) => {
    const inputName = input.name
    const labels = document.getElementsByTagName('label')
    const labelsArr: any = Array.from(labels)

    const filteredLabelsArr = labelsArr.filter((label: any) => {
        return label.htmlFor !== 'enhanced-form-submit'
    })

    for (const label of filteredLabelsArr) {
        if (label.htmlFor === inputName) {
            const inputLabels = label.innerText
            return inputLabels
        }
    }
}

export const getFeedbackQuestionsAndAnswers = (feedbackData: any) => {
    let description = ''

    Object.entries(feedbackData).map(([key, value]) => {
        description += `${key}\n${value}\n\n`
    })

    return description
}

export const postFeedback = (
    feedbackData: any
) => {
    const url = `${geossPersonaldata.feedbacks}`
    return apiClient.$post(url, feedbackData, {
        headers: {
            Authorization: '',
        },
    })
}

// export const createJiraIssue = (
//     issueTitle: string,
//     issueDescription: string
// ) => {
//     const endpointUrl = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=JIRA_ISSUE`

//     return makeRequest('post', endpointUrl, {
//         issueTitle,
//         issueDescription
//     }).catch(() => {
//         return false
//     })
// }

// export const generateCaptcha = (
//     captchaIMGTagID: string,
//     captchaLoaderSelector: string
// ) => {
//     const captchaEndpoint =
//         '/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=SERVE_CAPTCHA'

//     return makeRequest('get', captchaEndpoint)
//         .then((res: any) => {
//             const { captchaId, captchaImg } = res
//             captchaID = captchaId
//             const captchaLoader: HTMLElement | null = document.querySelector(
//                 captchaLoaderSelector
//             )
//             const captchaIMGTag: any = document.getElementById(captchaIMGTagID)

//             if (captchaImg !== undefined) {
//                 captchaIMGTag.src = `data:image/png;base64, ${captchaImg}`
//                 captchaLoader!.classList.add('hidden')
//             }
//         })
//         .catch(() => {
//             return false
//         })
// }

// export const reloadCaptcha = (captchaIMGTagID: string) => {
//     const captchaReloadEndpoint = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=RELOAD_CAPTCHA&_geossresources_WAR_geossportlet_previousCaptchaId=${captchaID}`

//     return makeRequest('get', captchaReloadEndpoint)
//         .then((res: any) => {
//             const { captchaId, captchaImg } = res
//             captchaID = captchaId
//             const captchaIMGTag: any = document.getElementById(captchaIMGTagID)
//             captchaIMGTag.src = `data:image/png;base64, ${captchaImg}`
//         })
//         .catch(() => {
//             return false
//         })
// }

// export const verifyCaptcha = (
//     captchaIMGTagID: string,
//     captchaInputID: string,
//     captchaErrorElement: string,
//     submitFormFunction: any,
//     captchaErrorClass: string,
//     captchaErrorStyle: string
// ) => {
//     const captchaInput: any = document.getElementById(captchaInputID)
//     const captchaInputValue = captchaInput.value
//     const captchaErrorMsg: HTMLElement | null =
//         document.querySelector(captchaErrorElement)
//     const captchaVerificationEndpoint = `/community/guest/geoss-resources?p_p_id=geossresources_WAR_geossportlet&p_p_lifecycle=2&p_p_resource_id=VERIFY_CAPTCHA&_geossresources_WAR_geossportlet_captchaId=${captchaID}&_geossresources_WAR_geossportlet_captchaAnswer=${captchaInputValue}`

//     makeRequest('get', captchaVerificationEndpoint)
//         .then((res: any) => {
//             const { challange } = res
//             if (challange === 'success') {
//                 submitFormFunction()
//             } else {
//                 reloadCaptcha(captchaIMGTagID)
//                 captchaInput.classList.add(captchaErrorClass)
//                 captchaErrorMsg!.style.display = captchaErrorStyle
//             }
//         })
//         .catch(() => {
//             return false
//         })
// }
