import { AppVueObj } from '@/data/global'
import SearchEngineService from './search-engine.service'
import { GeneralGetters } from '@/store/general/general-getters'
import { UserGetters } from '@/store/user/user-getters'
import SpinnerService from './spinner.service'
import { GeneralFiltersActions } from '@/store/general-filters/general-filters-actions'

// const requestAnimFrame = window.requestAnimationFrame || window.webkitRequestAnimationFrame || ((callback) => {
//         window.setTimeout(callback, 1000 / 60)
//     })

const UtilsService = {
    getPropByString(
        obj: any,
        propsPath: string,
        targetType?: string,
        multiple?: boolean
    ) {
        let result: any[] = []
        const getResult = (obj: any, propsPath: string) => {
            propsPath = propsPath.replace(/\[(\w+)\]/g, '.$1')
            propsPath = propsPath.replace(/^\./, '')
            const props = propsPath.split('.')
            let objCopy = JSON.parse(JSON.stringify(obj))
            let propsCount = 0
            if (props.length && props[0] !== '') {
                for (const prop of props) {
                    propsCount++
                    if (
                        objCopy &&
                        objCopy[prop] &&
                        objCopy[prop].constructor === Array
                    ) {
                        const propsCopy = props.slice(propsCount)
                        for (const item of objCopy[prop]) {
                            result.push(getResult(item, propsCopy.join('.')))
                            result = result.filter((item) => !!item)
                            if (!multiple && result.length) {
                                return
                            }
                        }
                        return
                    } else if (
                        typeof objCopy === 'object' &&
                        prop in objCopy &&
                        (objCopy[prop] || objCopy[prop] === 0) &&
                        JSON.stringify(objCopy[prop]) !== '{}'
                    ) {
                        objCopy = objCopy[prop]
                    } else {
                        return
                    }
                }
            }

            if (
                (objCopy || objCopy === 0) &&
                JSON.stringify(objCopy) !== '{}'
            ) {
                return objCopy
            }
        }

        result.push(getResult(obj, propsPath))
        result = result.filter((item) => !!item || item === 0)

        if (!result.length || (targetType && typeof result[0] !== targetType)) {
            return ''
        }
        return result[0]
    },

    getArrayByString(obj: any, propsPath: string) {
        let result = []
        const getResult = (obj: any, propsPath: string) => {
            propsPath = propsPath.replace(/\[(\w+)\]/g, '.$1')
            propsPath = propsPath.replace(/^\./, '')
            const props = propsPath.split('.')
            let objCopy = JSON.parse(JSON.stringify(obj))
            let propsCount = 0
            if (props.length && props[0] !== '') {
                for (const prop of props) {
                    propsCount++
                    if (
                        objCopy &&
                        objCopy[prop] &&
                        objCopy[prop].constructor === Array
                    ) {
                        const propsCopy = props.slice(propsCount)
                        for (const item of objCopy[prop]) {
                            result.push(getResult(item, propsCopy.join('.')))
                        }
                        return
                    } else if (
                        typeof objCopy === 'object' &&
                        prop in objCopy &&
                        objCopy[prop] &&
                        JSON.stringify(objCopy[prop]) !== '{}'
                    ) {
                        objCopy = objCopy[prop]
                    } else {
                        return
                    }
                }
            }

            if (objCopy && JSON.stringify(objCopy) !== '{}') {
                return objCopy
            }
        }

        result.push(getResult(obj, propsPath))
        result = result.filter((item) => !!item)

        return result
    },

    extractCategoriesByAttributeValue(
        result: any,
        attribute: string,
        value: string
    ) {
        /*
         * Returns array of unspecifed attribute values of categories that match provided attribute value;
         * Opensearch categories have specified only two attributes: 'term' and 'label'.
         * If 'term' is provided (as attribute), array will contain 'label' values,
         * otherwise if 'label' is provided, array will contain 'term' values.
         */
        let categoriesAll = []
        const categoriesValues = []
        if (result.category && result.category.constructor !== Array) {
            categoriesAll = [result.category]
        } else if (result.category && result.category.constructor === Array) {
            categoriesAll = result.category
        }
        for (const category of categoriesAll) {
            if (category._attributes[attribute] === value) {
                for (const key in category._attributes) {
                    if (category._attributes[key] !== value) {
                        categoriesValues.push(category._attributes[key])
                    }
                }
            }
        }
        return categoriesValues
    },

    scrollY(
        scrollableContainer: HTMLElement,
        scrollTargetY: number,
        speed: number
    ) {
        // scrollTargetY: the target scrollY property of the window
        // speed: time in pixels per second

        const scrollY =
            scrollableContainer.scrollTop || document.documentElement.scrollTop
        const easing = 'easeOutSine'
        let currentTime = 0

        // min time .1, max time .8 seconds
        const time = Math.max(
            0.1,
            Math.min(Math.abs(scrollY - scrollTargetY) / speed, 0.8)
        )

        const easingEquations = {
            easeOutSine(pos: number) {
                return Math.sin(pos * (Math.PI / 2))
            },
            easeInOutSine(pos: number) {
                return -0.5 * (Math.cos(Math.PI * pos) - 1)
            },
            easeInOutQuint(pos: number) {
                pos /= 0.5
                if (pos < 1) {
                    return 0.5 * Math.pow(pos, 5)
                }
                return 0.5 * (Math.pow(pos - 2, 5) + 2)
            },
        }

        return new Promise((resolve) => {
            const tick = () => {
                currentTime += 1 / 60

                const p = currentTime / time
                const t = easingEquations[easing](p)

                if (p < 1) {
                    // requestAnimFrame(tick)

                    scrollableContainer.scrollTop =
                        scrollY + (scrollTargetY - scrollY) * t
                } else {
                    scrollableContainer.scrollTop = scrollTargetY
                    resolve
                }
            }

            tick()
        })
    },

    rememberState() {
        AppVueObj.storeStateBackup = JSON.stringify(
            UtilsService.getCurrentState()
        )
    },

    changeRememberState(path: any, value: any) {
        const storeStateBackup = JSON.parse(AppVueObj.storeStateBackup)
        const changeByPath = (
            storeStateBackup: any,
            path: string,
            value: any
        ) => {
            const parts = path.split('/')
            let o = storeStateBackup
            if (parts.length > 1) {
                for (let i = 0; i < parts.length - 1; i++) {
                    if (!o[parts[i]]) {
                        o[parts[i]] = {}
                    }
                    o = o[parts[i]]
                }
            }
            o[parts[parts.length - 1]] = value
        }
        changeByPath(storeStateBackup, path, value)
        AppVueObj.storeStateBackup = JSON.stringify(storeStateBackup)
    },

    getCurrentState() {
        const storeStateBackup: any = {}
        for (const prop of Object.keys(AppVueObj.app.$store.state)) {
            if (prop !== 'map') {
                storeStateBackup[prop] = AppVueObj.app.$store.state[prop]
            }
        }
        return JSON.parse(JSON.stringify(storeStateBackup))
    },

    setCurrentState(state: any[]) {
        state.map = AppVueObj.app.$store.state.map
        AppVueObj.app.$store.replaceState(state)
        setTimeout(() => {
            AppVueObj.app.$store.dispatch(
                GeneralFiltersActions.setInChangeProcess,
                false
            )
        }, 0)
    },

    pushToHistory(replace?: boolean) {
        this.rememberState()
        if (!UtilsService.isWidget()) {
            let url = SearchEngineService.getShareUrl(null, replace)
            url =
                '/' +
                url.substr(
                    url.indexOf(window.location.host) +
                        window.location.host.length +
                        1
                )
            if (replace) {
                window.history.replaceState(AppVueObj.storeStateBackup, '', url)
            }
            AppVueObj.app.$store.dispatch(
                GeneralFiltersActions.setLastTriggerredState
            )
            sessionStorage.setItem('RECENT_SEARCH_PARAMS', url)
            sessionStorage.setItem(
                'COMMUNITY_SITE_ID',
                AppVueObj.app.$store.getters[UserGetters.groupId]
            )
        }
    },

    popFromHistory(state?: any) {
        state = state
            ? JSON.parse(state)
            : JSON.parse(AppVueObj.storeStateBackup)
        state.map = AppVueObj.app.$store.state.map
        AppVueObj.app.$store.replaceState(state)
        AppVueObj.app.$store.dispatch(
            GeneralFiltersActions.setLastTriggerredState
        )
        setTimeout(() => {
            AppVueObj.app.$store.dispatch(
                GeneralFiltersActions.setInChangeProcess,
                false
            )
        }, 0)
    },

    nativeType(value: any) {
        const nValue = Number(value)
        if (!isNaN(nValue)) {
            return nValue
        }

        const bValue = value.toLowerCase()
        if (bValue === 'true') {
            return true
        } else if (bValue === 'false') {
            return false
        }

        return value
    },

    getUrlParams(url?: string): Array<{ name: string; value: any }> {
        let params
        if (url) {
            params = url
                .substr(url.indexOf('?') + 1)
                .split('&')
                .filter((item) => item)
        } else {
            params = window.location.search
                .substr(1)
                .split('&')
                .filter((item) => item)
        }
        const urlParams = []
        for (const param of params) {
            const paramName = param.split('=')[0]
            const paramValue: any = UtilsService.nativeType(
                decodeURIComponent(param.split('=')[1])
            )
            urlParams.push({ name: paramName, value: paramValue })
        }
        return urlParams
    },

    getUrlParam(paramName: string) {
        const param = UtilsService.getUrlParams().find(
            (param) => param.name === paramName
        )
        if (param) {
            return UtilsService.nativeType(param.value)
        }
        return null
    },

    areObjectsShallowEqual(a: any, b: any) {
        for (const key in a) {
            if (!(key in b) || a[key] !== b[key]) {
                return false
            }
        }
        for (const key in b) {
            if (!(key in a) || a[key] !== b[key]) {
                return false
            }
        }
        return true
    },

    cloneObject(obj: any) {
        return Object.assign({}, obj)
    },

    cloneArray(array: any[]) {
        return array.map((a) => ({ ...a }))
    },

    roundToFirstDecimal(num: number) {
        return Math.round((+num + 0.0001) * 10) / 10
    },

    createAndOpenFile(data: any, filename: string, type: string) {
        const file = new Blob([data], { type })
        if ((window.navigator as any).msSaveOrOpenBlob) {
            // IE10+ case
            ;(window.navigator as any).msSaveOrOpenBlob(file, filename)
        } else {
            const a = document.createElement('a')
            const url = URL.createObjectURL(file)
            a.href = url
            a.download = filename
            document.body.appendChild(a)
            a.click()
            setTimeout(() => {
                document.body.removeChild(a)
                window.URL.revokeObjectURL(url)
            }, 0)
        }
    },

    isWidget() {
        return AppVueObj.app
            ? AppVueObj.app.$store.getters[GeneralGetters.isWidget]
            : false
    },

    getAccessKeyObject() {
        if (`${process.env.VUE_APP_TYPE}` === 'widgets') {
            return {
                accesskey:
                    AppVueObj.app.$store.getters[
                        GeneralGetters.widgetAccessKey
                    ],
            }
        }
        return {}
    },

    getAccessKeyString() {
        if (
            `${process.env.VUE_APP_TYPE}` === 'widgets' &&
            AppVueObj.app.$store.getters[GeneralGetters.widgetAccessKey]
        ) {
            return `&accesskey=${
                AppVueObj.app.$store.getters[GeneralGetters.widgetAccessKey]
            }`
        }
        return ''
    },
}

// window.onpopstate = async (event: any) => {
//     if (!UtilsService.isWidget()) {
//         SpinnerService.showSpinner()
//         setTimeout(() => {
//             UtilsService.popFromHistory(event.state)
//             SpinnerService.hideSpinner()
//         }, 0)
//     }
// }

export default UtilsService
