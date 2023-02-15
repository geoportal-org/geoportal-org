export const isFunction = (func: any) => {
    return (
        typeof func === 'function' ||
        Object.prototype.toString.call(func) === '[object Function]'
    )
}
