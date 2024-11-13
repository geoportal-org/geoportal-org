function DropletIcon({ color = "transparent", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path
                d="M5.2002 18.5333C5.2002 24.48 10.0402 29.3333 16.0002 29.3333C21.9602 29.3333 26.8002 24.4933 26.8002 18.5467C26.8402 11.3067 19.3335 4.89332 16.8135 2.94666"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M16.8133 2.94663C16.3333 2.57329 15.6666 2.57329 15.1866 2.94663C13.3466 4.34663 8.89328 8.05329 6.61328 12.8"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        </svg>
    );
}

export default DropletIcon;
