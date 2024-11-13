function WindIcon({ color = "transparent", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path
                d="M2.66699 20H24.667C27.2403 20 29.3337 22.0933 29.3337 24.6667C29.3337 27.24 27.2403 29.3333 24.667 29.3333C22.0937 29.3333 20.0003 27.24 20.0003 24.6667V24"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M2.66699 16.0001H24.667C27.227 16.0001 29.3337 13.9067 29.3337 11.3334C29.3337 8.77341 27.227 6.66675 24.667 6.66675C22.107 6.66675 20.0003 8.76008 20.0003 11.3334V12.0001"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M2.66699 12H12.4137C14.4003 12 16.0003 10.3867 16.0003 8.41333C16.0003 6.42666 14.387 4.82666 12.4137 4.82666C10.427 4.82666 8.82699 6.43999 8.82699 8.41333V8.91999"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        </svg>
    );
}

export default WindIcon;
