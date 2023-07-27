export const getRandomColorsArray = (length : number) => {
    let colors: string[] = []
    for(let i = 0 ; i <= length ; i++){
        colors.push(
            '#' +
                (((1 << 24) * Math.random()) | 0)
                    .toString(16)
                    .padStart(6, '0')
        ) 
    }
    return colors;
}