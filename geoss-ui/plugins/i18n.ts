import Vue from 'vue'
import { AppVueObj } from '~/data/global'

export const $tc = (sign: string) => Vue.prototype.$nuxt.$options.i18n.t(sign)
