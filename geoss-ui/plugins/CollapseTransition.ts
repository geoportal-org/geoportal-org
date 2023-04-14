import TutorialTagsService from '@/services/tutorial-tags.service'

const Transition = {
    transitionForceVisible(el: any) {
        return el.classList.contains('transition-force-visible')
    },

    beforeEnter(el: any) {
        el.classList.add('collapse-transition')
        if (!el.dataset) {
            el.dataset = {}
        }

        el.dataset.oldPaddingTop = el.style.paddingTop
        el.dataset.oldPaddingBottom = el.style.paddingBottom

        el.style.height = '0'
        el.style.paddingTop = 0
        el.style.paddingBottom = 0
        TutorialTagsService.refreshTagsAll()
    },

    enter(el: any) {
        el.dataset.oldOverflow = el.style.overflow
        if (el.scrollHeight !== 0) {
            el.style.height = el.scrollHeight + 'px'
            el.style.paddingTop = el.dataset.oldPaddingTop
            el.style.paddingBottom = el.dataset.oldPaddingBottom
        } else {
            el.style.height = ''
            el.style.paddingTop = el.dataset.oldPaddingTop
            el.style.paddingBottom = el.dataset.oldPaddingBottom
        }

        el.style.overflow = 'hidden'
    },

    afterEnter(el: any) {
        // for safari: remove class then reset height is necessary
        el.classList.remove('collapse-transition')
        el.style.height = ''
        el.style.overflow = Transition.transitionForceVisible(el)
            ? 'visible'
            : el.dataset.oldOverflow
        // window.dispatchEvent(new Event('resize'));
    },

    beforeLeave(el: any) {
        if (!el.dataset) {
            el.dataset = {}
        }
        el.dataset.oldPaddingTop = el.style.paddingTop
        el.dataset.oldPaddingBottom = el.style.paddingBottom
        el.dataset.oldOverflow = Transition.transitionForceVisible(el)
            ? 'visible'
            : el.style.overflow

        el.style.height = el.scrollHeight + 'px'
        el.style.overflow = 'hidden'
        TutorialTagsService.refreshTagsAll()
    },

    leave(el: any) {
        if (el.scrollHeight !== 0) {
            // for safari: add class after set height, or it will jump to zero height suddenly, weired
            el.classList.add('collapse-transition')
            el.style.height = 0
            el.style.paddingTop = 0
            el.style.paddingBottom = 0
        }
    },

    afterLeave(el: any) {
        el.classList.remove('collapse-transition')
        el.style.height = ''
        el.style.overflow = Transition.transitionForceVisible(el)
            ? 'visible'
            : el.dataset.oldOverflow
        el.style.paddingTop = el.dataset.oldPaddingTop
        el.style.paddingBottom = el.dataset.oldPaddingBottom
        // window.dispatchEvent(new Event('resize'));
    },
}

export default {
    functional: true,
    render(h: any, { children }: any) {
        const data = {
            on: Transition,
        }

        return h('transition', data, children)
    },
}
