function SearchIcon({ color = "white", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path
                d="M15.3337 28.0001C22.3293 28.0001 28.0003 22.329 28.0003 15.3334C28.0003 8.33781 22.3293 2.66675 15.3337 2.66675C8.33805 2.66675 2.66699 8.33781 2.66699 15.3334C2.66699 22.329 8.33805 28.0001 15.3337 28.0001Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
            <path
                d="M29.3337 29.3333L26.667 26.6666"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        </svg>
    );
}

export default SearchIcon;
