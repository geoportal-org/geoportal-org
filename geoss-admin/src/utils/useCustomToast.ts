import { useToast, useBreakpointValue } from "@chakra-ui/react";
import { ToastData, ToastStatus } from "@/types";

const useCustomToast = () => {
    const toast = useToast();
    const position = useBreakpointValue({ base: "top-left", "2xl": "top" }) as any;

    const showToast = ({ title, description, status = ToastStatus.SUCCESS }: ToastData) => {
        toast({
            description,
            duration: 5000,
            isClosable: true,
            position,
            status,
            title,
            variant: "geossAlert",
            containerStyle: {
                margin: "4px 4px 4px 0",
            },
        });
    };

    return { showToast };
};

export default useCustomToast;
