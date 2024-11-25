import Vue from 'vue'

export const $tc = (sign: string) => Vue.prototype.$nuxt.$options.i18n.t(sign)
