import { useEffect, useState } from "react";
import { Formik, FormikHelpers, FormikValues } from "formik";
import { Box, Flex } from "@chakra-ui/react";
import { FormField, Loader, PrimaryButton, TextContent } from "@/components";
import { MenuService } from "@/services/api";
import { addChildMenuItemForm, addMenuItemForm } from "@/data/forms";
import { areObjectsEqual, setExistingFormValues, setFormInitialValues } from "@/utils/helpers";
import { ButtonType, MenuContentManageProps } from "@/types";
import { scrollbarStyles } from "@/theme/commons";

export const MenuContentManage = ({
    parentMenuId,
    menuItemId,
    manageMenuItem,
    isMainMenuItem,
}: MenuContentManageProps) => {
    const itemForm = isMainMenuItem ? addMenuItemForm : addChildMenuItemForm;
    const [isLoading, setIsLoading] = useState(true);
    const [initValues, setInitValues] = useState<FormikValues>(setFormInitialValues(itemForm));

    useEffect(() => {
        menuItemId ? getMenuItemInfo(menuItemId) : setIsLoading(false);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const getMenuItemInfo = async (itemId: number) => {
        try {
            const editedMenuItem = await MenuService.getMenuItem(itemId);
            setInitValues(setExistingFormValues(itemForm, editedMenuItem));
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const handleFormSubmit = (values: FormikValues, actions: FormikHelpers<FormikValues>) =>
        manageMenuItem(parentMenuId, values, actions, setInitValues, menuItemId);

    const renderFormFields = () => {
        const formFields = itemForm.map((field) => <FormField key={field.name} fieldData={field} />);
        return (
            <Flex direction="column" gap={6} mb={6}>
                {formFields}
            </Flex>
        );
    };

    const renderFormFooter = (values: FormikValues) => (
        <Flex justifyContent="flex-end" py={1} w="full">
            <PrimaryButton
                type={ButtonType.SUBMIT}
                disabled={menuItemId !== undefined && areObjectsEqual(initValues, values)}
            >
                <TextContent id="general.save" />
            </PrimaryButton>
        </Flex>
    );

    if (isLoading) {
        return <Loader />;
    }

    return (
        <Formik initialValues={initValues} onSubmit={handleFormSubmit}>
            {(formikProps) => {
                const { handleSubmit, values } = formikProps;
                return (
                    <Box overflowY="auto" minH="full" h="full" pr={1} sx={scrollbarStyles}>
                        <form onSubmit={handleSubmit} noValidate>
                            {renderFormFields()}
                            {renderFormFooter(values)}
                        </form>
                    </Box>
                );
            }}
        </Formik>
    );
};
