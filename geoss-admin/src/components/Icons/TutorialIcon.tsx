import { IconProps } from "@/types";

export const TutorialIcon = ({ color, width, height }: IconProps) => (
    <svg
        width={width}
        height={height}
        viewBox="0 0 10 10"
        xmlns="http://www.w3.org/2000/svg"
        fill={color}
        stroke={color}
        strokeWidth={0.3}
    >
        <g strokeWidth={0} />
        <g strokeLinecap="round" strokeLinejoin="round" />
        <g />
        <path d="M5 .835a4.165 4.165 0 1 0 0 8.33 4.165 4.165 0 0 0 0-8.33zm0 7.997C2.887 8.832 1.168 7.113 1.168 5S2.887 1.168 5 1.168 8.832 2.887 8.832 5A3.836 3.836 0 0 1 5 8.832z" />
        <path d="M4.986 2.817c-.846 0-1.318.522-1.324 1.351h.367c-.012-.601.303-1.041.939-1.041.455 0 .834.321.834.787 0 .303-.163.548-.379.752-.443.411-.567.605-.59 1.168h.372c.022-.511.011-.501.457-.947.297-.28.507-.56.507-.992 0-.676-.537-1.079-1.183-1.079zM5 6.5a.333.333 0 1 0 0 .666.333.333 0 0 0 0-.666z" />
    </svg>
);
