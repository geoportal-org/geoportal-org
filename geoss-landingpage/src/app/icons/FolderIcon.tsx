function FolderIcon({ color = "white", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path
                d="M29.3337 14.6667V22.6667C29.3337 28.0001 28.0003 29.3334 22.667 29.3334H9.33366C4.00033 29.3334 2.66699 28.0001 2.66699 22.6667V9.33341C2.66699 4.00008 4.00033 2.66675 9.33366 2.66675H11.3337C13.3337 2.66675 13.7737 3.25341 14.5337 4.26675L16.5337 6.93341C17.0403 7.60008 17.3337 8.00008 18.667 8.00008H22.667C28.0003 8.00008 29.3337 9.33341 29.3337 14.6667Z"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
            />
            <path
                d="M10.667 2.66675H22.667C25.3337 2.66675 26.667 4.00008 26.667 6.66675V8.50675"
                stroke="white"
                strokeWidth="1.5"
                strokeMiterlimit="10"
                strokeLinecap="round"
                strokeLinejoin="round"
            />
        </svg>
    );
}

export default FolderIcon;
