import { Field } from "formik";
import { useDropzone, FileRejection, ErrorCode } from "react-dropzone";
import { Center, Text, Box, Flex, Image } from "@chakra-ui/react";
import { AttachmentIcon } from "@chakra-ui/icons";
import { ValidationService } from "@/services";
import { TextContent } from "@/components";
import { cutString } from "@/utils/helpers";
import useCustomToast from "@/utils/useCustomToast";
import useFormatMsg from "@/utils/useFormatMsg";
import { ToastStatus, UploaderProps } from "@/types";
import { uploaderConfig } from "@/data";

export const Uploader = ({ fieldData, value, handleUploaderChange, handleFileUploaderUse, isError }: UploaderProps) => {
    const { showToast } = useCustomToast();
    const { translate } = useFormatMsg();
    const { getRootProps, getInputProps, isDragActive } = useDropzone({
        onDrop: (acceptedFiles) => handleFileAccept(acceptedFiles),
        onDropRejected: (fileRejections) => handleFileReject(fileRejections),
        onError: () => handleGeneralError(),
        ...uploaderConfig,
    });

    const handleFileAccept = (files: File[]) => {
        handleFileUploaderUse();
        if (!files.length) {
            return handleUploaderChange("");
        }
        handleUploaderChange(files[0]);
    };

    const handleFileReject = (fileRejections: FileRejection[]) => {
        console.log(fileRejections);
        const rejectionError = setRejectionError(fileRejections);
        const errorMessageId = Object.values(ErrorCode).includes(rejectionError)
            ? `information.error.${rejectionError}`
            : "information.error.upload";
        showError(errorMessageId);
    };

    const handleGeneralError = () => showError("information.error.upload");

    const setRejectionError = (fileRejections: FileRejection[]): ErrorCode => {
        if (fileRejections.length > 1) {
            return ErrorCode.TooManyFiles;
        }
        const { errors } = fileRejections[0];
        return errors[0].code as ErrorCode;
    };

    const showError = (errorMsgId: string) =>
        showToast({
            title: translate("general.error"),
            description: translate(errorMsgId),
            status: ToastStatus.ERROR,
        });

    return (
        <Box>
            <Center
                p={2}
                minH="100px"
                bg={isDragActive ? "brand.darkSoft" : "inherit"}
                cursor="pointer"
                borderRadius="primary"
                border="2px"
                borderStyle={isDragActive ? "solid" : "dashed"}
                borderColor={isDragActive || !isError ? "brand.dark" : "red"}
                gap={2}
                transitionDuration="slower"
                {...getRootProps()}
            >
                <Field
                    {...getInputProps({ refKey: "innerRef" })}
                    name={fieldData.name}
                    id={fieldData.name}
                    validate={(value: string) => ValidationService.checkForErrors(fieldData, value)}
                    type={fieldData.type}
                    value={undefined}
                />
                {!(value instanceof File) && <AttachmentIcon boxSize={5} />}
                <Flex direction="column" gap={2}>
                    {isDragActive && (
                        <Text>
                            <TextContent id="pages.file-repository.drop" />
                        </Text>
                    )}
                    {!isDragActive && !(value instanceof File) && (
                        <Text>
                            <TextContent id="pages.file-repository.upload" />
                        </Text>
                    )}
                    {!isDragActive && value instanceof File && (
                        <>
                            <Center fontWeight="bold" title={value.name} textAlign="center">
                                <Image
                                    h="20px"
                                    src="/images/file-icon.png"
                                    alt={translate("pages.file-repository.file-icon", { title: value.name })}
                                />
                                {cutString(value.name, 20)}
                            </Center>
                            <Center gap={2}>
                                <AttachmentIcon boxSize={5} />
                                <Text>
                                    <TextContent id="pages.file-repository.upload-new" />
                                </Text>
                            </Center>
                        </>
                    )}
                </Flex>
            </Center>
        </Box>
    );
};
