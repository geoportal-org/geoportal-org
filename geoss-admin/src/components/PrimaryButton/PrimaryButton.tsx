import { Button } from "@chakra-ui/react";
import { PrimaryButtonProps, ButtonType, ButtonVariant } from "@/types";

export const PrimaryButton = ({ children, color, disabled, onClick, type, variant }: PrimaryButtonProps) => {
    return (
        <Button
            isDisabled={!!disabled}
            onClick={onClick}
            size="sm"
            type={type || ButtonType.BUTTON}
            variant={variant || ButtonVariant.OUTLINE}
            color={color || "brand.dark"}
        >
            {children}
        </Button>
    );
};
