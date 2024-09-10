function DropdownArrow({
  color = "white",
  width = "1em",
  height = "1em",
  className = "",
}) {
  return (
    <svg
      viewBox="0 0 24 24"
      className={className}
      fill={color}
      height={height}
      width={width}
    >
      <path fill="none" d="M0 0h24v24H0z" />
      <path d="M12 13.172l4.95-4.95 1.414 1.414L12 16 5.636 9.636 7.05 8.222z" />
    </svg>
  );
}

export default DropdownArrow;
