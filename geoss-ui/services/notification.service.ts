import Vue from 'vue'

const NotificationService = {
    eventBus: new Vue(),

    show(
        title: string,
        text: string,
        duration: number = 5000,
        id?: string,
        zIndex?: number,
        style?: 'info' | 'error' | 'success'
    ) {
        NotificationService.eventBus.$emit('show', {
            title,
            text,
            duration,
            id,
            zIndex,
            style,
        })
    },

    hide(id: string) {
        NotificationService.eventBus.$emit('hide', id)
    },
}

export default NotificationService
