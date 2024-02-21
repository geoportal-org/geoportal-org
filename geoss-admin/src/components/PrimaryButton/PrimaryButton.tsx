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
            color={variant !== ButtonVariant.SOLID ? color || "brand.dark" : "brand.mainLight"}
            bg={variant === ButtonVariant.SOLID ? color : "inherit"}
            whiteSpace='break-spaces'
        >
            {children}
        </Button>
    );
};
