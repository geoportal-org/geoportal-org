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
