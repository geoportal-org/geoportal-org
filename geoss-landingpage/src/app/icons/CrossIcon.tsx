function CrossIcon({
  color = "white",
  width = "1em",
  height = "1em",
  className = "",
}) {
  return (
    <svg
      viewBox="0 0 512 512"
      className={className}
      fill={color}
      height={height}
      width={width}
    >
      <path
        fill="none"
        stroke="currentColor"
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth={32}
        d="M368 368L144 144M368 144L144 368"
      />
    </svg>
  );
}

export default CrossIcon;
