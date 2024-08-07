import Vue from 'vue'
import { Timers } from '@/data/timers'

const TutorialTagsService = {
    eventBus: new Vue(),

    isWindow(object: { window: any } | null) {
        return object != null && object === object.window
    },

    getWindow(element: any) {
        return TutorialTagsService.isWindow(element)
            ? element
            : element.nodeType === 9 && element.defaultView
    },

    offset(element: Element, tagPlacement: string) {
        let window
        let box = {
            top: 0,
            left: 0,
            width: 0,
            height: 0
        }

        const document = element && element.ownerDocument
        const documentElem = document.documentElement

        if (typeof element.getBoundingClientRect !== typeof undefined) {
            box = element.getBoundingClientRect()
        }

        let addBoxWidth = false
        let addBoxHeight = false
        if (tagPlacement) {
            const place = tagPlacement.split('-')
            if (place.includes('right')) {
                addBoxWidth = true
            }
            if (place.includes('bottom')) {
                addBoxHeight = true
            }
            if (place.includes('center')) {
                if (place.includes('right') || place.includes('left')) {
                    box.height = box.height * 0.5
                    addBoxHeight = true
                }
                if (place.includes('top') || place.includes('bottom')) {
                    box.width = box.width * 0.5
                    addBoxWidth = true
                }
                if (place.filter((e) => e === 'center').length === 2) {
                    box.height = box.height * 0.5
                    box.width = box.width * 0.5
                    addBoxHeight = true
                    addBoxWidth = true
                }
            }
        }

        window = TutorialTagsService.getWindow(document)

        return {
            top:
                box.top +
                window.pageYOffset -
                documentElem.clientTop +
                (addBoxHeight ? box.height : 0),
            left:
                box.left +
                window.pageXOffset -
                documentElem.clientLeft +
                (addBoxWidth ? box.width : 0)
        }
    },

    getDescriptionPositionHorizontal(left: number) {
        const descWidth = 280
        const descOffset = 15
        const descRange = left + descWidth + descOffset
        let position = 'left'
        if (descRange > window.innerWidth) {
            position = 'right'
        }
        return `${position}: 0;`
    },

    getDescriptionPositionVertical(top: number) {
        const descHeight = 124
        const descOffset = 15
        const descRange = top + descHeight + descOffset
        let position = 'top'
        if (descRange > window.innerHeight) {
            position = 'bottom'
        }
        return `${position}: calc(100% + 5px);`
    },

    refreshTagsGroup(group: any, show: any, timeout = 500) {
        let tagAction = 'close'
        if (show) {
            tagAction = 'open'
            setTimeout(() => {
                this.eventBus.$emit('getPositions', {
                    group,
                    action: tagAction
                })
            }, timeout + 100)
        } else {
            this.eventBus.$emit('getPositions', { group, action: tagAction })
        }
    },

    refreshTagsAll(timeout = true) {
        if (timeout) {
            setTimeout(() => {
                TutorialTagsService.eventBus.$emit('getPositions')
            }, Timers.collapseTransition + 100)
        } else {
            TutorialTagsService.eventBus.$emit('getPositions')
        }
    },

    getPositions(
        tag: { id: any; placement: string; startPosition: any },
        event: { action: any } | null = null
    ) {
        const eventAction = event && event.action ? event.action : null
        const tagId = tag.id
        const tagPlacement = tag.placement || 'top-left'
        const element = document.querySelector(
            '[data-tutorial-tag="' + tagId + '"]'
        )
        if (element) {
            if (eventAction === 'close' && tag.startPosition) {
                return tag.startPosition
            }

            if (
                tagId.indexOf('result-') === 0 &&
                !this.isResultTagInView(element)
            ) {
                return {
                    top: 0,
                    left: 0
                }
            }

            return {
                top: TutorialTagsService.offset(
                    element,
                    tagPlacement
                ).top.toFixed(),
                left: TutorialTagsService.offset(
                    element,
                    tagPlacement
                ).left.toFixed()
            }
        } else {
            return {
                top: 0,
                left: 0
            }
        }
    },

    isResultTagInView(element: Element) {
        const searchResultsBox = document
            .querySelector('.search-container__wrapper')!
            .querySelector('.vb')
        const filtersBox = searchResultsBox!
            .querySelector('.trigger')!
            .getBoundingClientRect()
        const filtersPosTop = filtersBox.top
        const filtersHeight = filtersBox.height
        const tagsBorderTop = filtersPosTop + filtersHeight

        const resultsBox = searchResultsBox!.getBoundingClientRect()
        const resultsPosTop = resultsBox.top
        const resultsHeight = resultsBox.height
        const tagsBorderBottom = resultsPosTop + resultsHeight

        const tagBox = element.getBoundingClientRect()
        const tagPosTop = tagBox.top
        const tagPosBottom = tagPosTop + tagBox.height

        const isTagInView = !!(
            tagsBorderTop < tagPosTop && tagsBorderBottom > tagPosBottom
        )

        return isTagInView
    }
}

export default TutorialTagsService
