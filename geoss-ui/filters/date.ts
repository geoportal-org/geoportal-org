export default (date: string | number | Date, format: string) => {
    let dateObj: Date
    const splitArr = format.split(/(YYYY|MMM|MM|DDth|DD|hh|mm|ss)+/)

    if (typeof date !== 'object') {
        dateObj = new Date(date)
    } else {
        dateObj = date
    }

    return splitArr
        .map((item) => {
            if (item === 'YYYY') {
                return dateObj.getFullYear()
            }

            if (item === 'MM') {
                return padStart(dateObj.getMonth() + 1, 2, 0)
            }

            if (item === 'MMM') {
                return monthes[dateObj.getMonth()]
            }

            if (item === 'DDth') {
                return dateObj.getDate() + getDayEnding(dateObj.getDate())
            }

            if (item === 'DD') {
                return padStart(dateObj.getDate(), 2, 0)
            }

            if (item === 'hh') {
                return padStart(dateObj.getHours(), 2, 0)
            }
            if (item === 'mm') {
                return padStart(dateObj.getMinutes(), 2, 0)
            }
            if (item === 'ss') {
                return padStart(dateObj.getSeconds(), 2, 0)
            }

            return item
        })
        .join('')
}

const monthes = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'Novemner',
    'December',
]

const getDayEnding = (day: number) => {
    switch (day) {
        case 1:
            return 'st'
        case 2:
            return 'nd'
        case 3:
            return 'rd'
        default:
            return 'th'
    }
}

const padStart = (
    value: string | number | any[],
    length: number,
    char?: string | number | undefined
) => {
    value = value + ''
    const len = length - value.length

    if (len <= 0) {
        return value
    } else {
        return Array(len + 1).join(char + '') + value
    }
}
