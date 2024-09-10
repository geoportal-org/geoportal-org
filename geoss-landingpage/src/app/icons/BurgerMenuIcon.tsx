function BurgerMenuIcon({
  color = "white",
  width = "1em",
  height = "1em",
  className = "",
}) {
  return (
    <svg
      viewBox="0 0 15 15"
      className={className}
      fill={color}
      height={height}
      width={width}
    >
      <path
        fill="currentColor"
        fillRule="evenodd"
        d="M1.5 3a.5.5 0 000 1h12a.5.5 0 000-1h-12zM1 7.5a.5.5 0 01.5-.5h12a.5.5 0 010 1h-12a.5.5 0 01-.5-.5zm0 4a.5.5 0 01.5-.5h12a.5.5 0 010 1h-12a.5.5 0 01-.5-.5z"
        clipRule="evenodd"
      />
    </svg>
  );
}

export default BurgerMenuIcon;
