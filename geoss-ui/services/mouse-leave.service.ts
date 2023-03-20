import { PopupActions } from '@/store/popup/popup-actions'
import { AppVueObj } from '@/data/global'
import { GeneralGetters } from '@/store/general/general-getters'
import Survey from '@/components/Survey.vue'

const glio: any = {
    config: {
        screenWidthFragment: 12,
        centerTopHeight: 10,
        heightTopLeft: 30,
        heightTopRight: 30,
    },
    statusTopLeft: 'inactive',
    statusTopRight: 'inactive',
    statusBottomLeft: 'inactive',
    statusBottomRight: 'inactive',
    statusTop: 'inactive',
    init() {
        glio.methods = Array.prototype.slice.call(arguments)
        Array.prototype.forEach.call(glio.methods, (index) => {
            if (glio.getDirection(index[0], 'top-left')) {
                glio.topLeftFn = glio.trigger(index[1])
            } else if (glio.getDirection(index[0], 'top-right')) {
                glio.topRightFn = glio.trigger(index[1])
            } else if (glio.getDirection(index[0], 'bottom-right')) {
                glio.bottomRightFn = glio.trigger(index[1])
            } else if (glio.getDirection(index[0], 'bottom-left')) {
                glio.bottomLeftFn = glio.trigger(index[1])
            } else if (glio.getDirection(index[0], 'top')) {
                glio.TopFn = glio.trigger(index[1])
            }
        })

        document.body.addEventListener('mousemove', (event) => {
            const pointX = event.clientX
            const pointY = event.clientY

            if (
                typeof glio.topLeftFn === 'function' &&
                glio.statusTopLeft === 'inactive'
            ) {
                glio.callTopleft(pointX, pointY, glio.topLeftFn)
            }
            if (
                typeof glio.topRightFn === 'function' &&
                glio.statusTopRight === 'inactive'
            ) {
                glio.callTopRight(pointX, pointY, glio.topRightFn)
            }
            if (
                typeof glio.bottomLeftFn === 'function' &&
                glio.statusBottomLeft === 'inactive'
            ) {
                glio.callBottomLeft(pointX, pointY, glio.bottomLeftFn)
            }
            if (
                typeof glio.bottomRightFn === 'function' &&
                glio.statusBottomRight === 'inactive'
            ) {
                glio.callBottomRight(pointX, pointY, glio.bottomRightFn)
            }
            if (
                typeof glio.TopFn === 'function' &&
                glio.statusTop === 'inactive'
            ) {
                glio.callTop(pointX, pointY, glio.TopFn)
            }
        })
    },
    trigger: (callback: any) => {
        return callback
    },
    getWidthRightValue: () => {
        const screenWidthFragment = glio.getScreenWidthFragment()
        const topRightValue =
            screenWidthFragment * glio.config.screenWidthFragment -
            screenWidthFragment

        return topRightValue
    },
    getTopHeight: () => {
        const sHeight = 50
        return sHeight
    },
    getScreenWidthFragment: () => {
        const screenWidthFragment =
            window.innerWidth / glio.config.screenWidthFragment
        return screenWidthFragment
    },
    getScreenHeightFragment: () => {
        const screenHeightFragment =
            window.innerHeight / glio.config.screenWidthFragment
        return screenHeightFragment
    },
    getBottomHeightValue: () => {
        const screenHeightFragment = glio.getScreenHeightFragment()
        const bottomRightValue =
            screenHeightFragment * glio.config.screenWidthFragment -
            screenHeightFragment

        return bottomRightValue
    },
    getDirection: (directionUser: string, direction: string) => {
        if (directionUser === direction) {
            return true
        }
        return false
    },
    callTopleft: (x: string, y: string, callback: any) => {
        if (
            x <= glio.getScreenWidthFragment() &&
            y <= glio.config.heightTopLeft
        ) {
            glio.statusTopLeft = 'active'
            callback()
        }
    },
    callTopRight: (x: string, y: string, callback: any) => {
        if (x > glio.getWidthRightValue() && y <= glio.config.heightTopRight) {
            glio.statusTopRight = 'active'
            callback()
        }
    },
    callBottomRight: (x: string, y: string, callback: any) => {
        if (
            x >= glio.getWidthRightValue() &&
            y >= glio.getBottomHeightValue()
        ) {
            glio.statusBottomRight = 'active'
            callback()
        }
    },
    callBottomLeft: (x: string, y: string, callback: any) => {
        if (
            x <= glio.getScreenWidthFragment() &&
            y >= glio.getBottomHeightValue()
        ) {
            glio.statusBottomLeft = 'active'
            callback()
        }
    },
    positionsTopY: [],
    callTop: (x: string, y: string, callback: any) => {
        if (y > glio.config.centerTopHeight + 1) {
            glio.positionsTopY.push(y)
        }
        if (
            x > glio.getScreenWidthFragment() &&
            x < glio.getWidthRightValue()
        ) {
            if (
                y <= glio.config.centerTopHeight &&
                glio.positionsTopY[0] > glio.config.centerTopHeight
            ) {
                glio.statusTop = 'active'
                callback()
            }
        }
    },
    start: () => {
        return {
            init: glio.init,
            config: glio.config,
        }
    },
}

const glioInstance = glio.start()

const MouseLeaveService = {
    initSurvey() {
        if (
            !AppVueObj.app.$store.getters[GeneralGetters.isWidget] &&
            window.$nuxt.$cookies.get('hide-survey') !== 'true'
        ) {
            glioInstance.init([
                'top',
                () => {
                    AppVueObj.app.$store.dispatch(PopupActions.openPopup, {
                        contentId: 'survey',
                        title: AppVueObj.app.$t('popupTitles.quickSurvey'),
                        component: Survey,
                    })
                },
            ])
        }
    },
}
export default MouseLeaveService
