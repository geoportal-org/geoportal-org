import Vue from 'vue'
import { AppVueObj } from '@/data/global'
import { SearchActions } from '@/stores/search/search-actions'

const SpinnerService = {
    emitter: new Vue(),

    showSpinner(text?: string | null | undefined, showCancel?: boolean) {
        SpinnerService.emitter.$emit('show', { text, showCancel })
    },

    hideSpinner(force?: boolean) {
        SpinnerService.emitter.$emit('hide', force)
    },

    setLongRequestInfo(enable: boolean) {
        let longRequestInfo = null
        if (enable) {
            longRequestInfo = AppVueObj.app.$t(
                'fileDownloadsPopup.requestTakesLongerThanExpected'
            )
        }
        AppVueObj.app.$store.dispatch(
            SearchActions.setLongRequestInfo,
            longRequestInfo
        )
    },
}

export default SpinnerService
