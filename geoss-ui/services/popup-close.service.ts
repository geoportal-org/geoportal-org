import Vue from 'vue'

const PopupCloseService = {
    eventBus: new Vue(),

    closePopup: (contentId: string, response?: any) => {
        PopupCloseService.eventBus.$emit('close', { contentId, response })
    },
}

export default PopupCloseService
