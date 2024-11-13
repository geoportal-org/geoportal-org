function LeafIcon({ color = "transparent", width = "1em", height = "1em", className = "" }) {
    return (
        <svg viewBox="0 0 32 32" className={className} fill={color} height={height} width={width}>
            <path d="M12.9202 16.0538L6.38659 27.3704" stroke="white" strokeWidth="1.5" strokeLinecap="round" />
            <path
                d="M4.77418 16.4439C5.41361 9.60889 20.3186 3.83638 20.3186 3.83638C20.3186 3.83638 22.772 19.6308 17.1724 23.602C11.3934 27.7006 4.11425 23.498 4.77418 16.4439Z"
                stroke="white"
                strokeWidth="1.5"
                strokeLinecap="round"
            />
        </svg>
    );
}

export default LeafIcon;
