// @ts-nocheck
export default {}

const camelToKebab = (str) => {
    return str.replace(/([a-z])([A-Z])/g, '$1-$2').toLowerCase()
}

const arrayify = (obj) => {
    return obj instanceof Array ? obj : [obj]
}

export { camelToKebab, arrayify }
