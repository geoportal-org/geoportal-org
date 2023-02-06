export type ToastData = {
    title: string;
    description: string;
    status?: ToastStatus;
};

export enum ToastStatus {
    SUCCESS = "success",
    ERROR = "error",
    WARNING = "warning",
    INFO = "info",
}
