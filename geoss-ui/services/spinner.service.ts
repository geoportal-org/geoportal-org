import Vue from 'vue'
import { AppVueObj } from '@/data/global'
import { SearchActions } from '@/store/search/search-actions'
import { $tc } from '@/plugins/i18n'

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
            longRequestInfo = $tc(
                'fileDownloadsPopup.requestTakesLongerThanExpected'
            )
        }
        AppVueObj.app.$store.dispatch(
            SearchActions.setLongRequestInfo,
            longRequestInfo
        )
    }
}

export default SpinnerService
