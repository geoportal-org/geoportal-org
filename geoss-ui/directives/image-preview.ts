import { AppVueObj } from '~/data/global'
import { GeneralActions } from '@/store/general/general-actions'

export default {
    bind(el: any) {
        el.__vueImagePreview__ = {
            handler: (event: Event) => {
                const target = event.target as HTMLImageElement
                if (target.getAttribute('src')) {
                    AppVueObj.app.$store.dispatch(
                        GeneralActions.setImagePreview,
                        target
                    )
                }
            },
        }
        el.style.cursor = 'pointer'
        el.addEventListener('click', el.__vueImagePreview__.handler)
    },

    unbind(el: any) {
        el.style.cursor = ''
        document.removeEventListener('click', el.__vueImagePreview__.handler)
        delete el.__vueImagePreview__
    },
}
