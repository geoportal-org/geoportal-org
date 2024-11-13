function TreeIcon({ color = "transparent", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path
                d="M21.5595 13.4133H10.4394C8.86611 13.4133 8.31945 12.3599 9.23945 11.0799L14.7995 3.29326C15.4528 2.35993 16.5461 2.35993 17.1861 3.29326L22.7461 11.0799C23.6795 12.3599 23.1328 13.4133 21.5595 13.4133Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M23.4531 24H8.55981C6.45314 24 5.73314 22.6 6.97314 20.8933L12.2931 13.4133H19.7198L25.0398 20.8933C26.2798 22.6 25.5598 24 23.4531 24Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path d="M16 29.3333V24" stroke="white" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
    );
}

export default TreeIcon;
