function MapIcon({ color = "transparent", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path
                d="M29.3337 12V20C29.3337 23.3333 28.667 25.6667 27.1737 27.1733L18.667 18.6666L28.9737 8.35999C29.2137 9.41332 29.3337 10.6133 29.3337 12Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M28.9737 8.35997L8.36031 28.9733C4.34698 28.0533 2.66699 25.28 2.66699 20V12C2.66699 5.33329 5.33366 2.66663 12.0003 2.66663H20.0003C25.2803 2.66663 28.0537 4.34663 28.9737 8.35997Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M27.1737 27.1733C25.667 28.6666 23.3337 29.3333 20.0004 29.3333H12.0004C10.6137 29.3333 9.41368 29.2133 8.36035 28.9733L18.667 18.6666L27.1737 27.1733Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M8.32035 10.64C9.22702 6.73329 15.0937 6.73329 16.0004 10.64C16.5204 12.9333 15.0803 14.88 13.8137 16.08C12.8937 16.96 11.4404 16.96 10.507 16.08C9.24037 14.88 7.78702 12.9333 8.32035 10.64Z"
                stroke="white"
                strokeWidth="1.5"
            />
            <path
                d="M12.1261 11.6H12.1381"
                stroke="white"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        </svg>
    );
}

export default MapIcon;
