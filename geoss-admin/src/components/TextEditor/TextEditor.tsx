import dynamic from "next/dynamic";
import { useRouter } from "next/router";
import { SunEditorOptions } from "suneditor/src/options";
import { Loader } from "@/components";
import { TextEditorProps } from "@/types";
import "suneditor/dist/css/suneditor.min.css";

const SunEditor = dynamic(() => import("suneditor-react"), {
    ssr: false,
    loading: () => <Loader />,
});

export const TextEditor = ({ onEditorChange, onEditorBlur, initialContent }: TextEditorProps) => {
    const router = useRouter();
    const locale = (router.locale === "zh" ? "zh_cn" : router.locale) as SunEditorOptions["lang"];

    return (
        <SunEditor
            lang={locale}
            setOptions={{
                buttonList: [
                    ["fontSize", "formatBlock"],
                    ["bold", "underline", "italic", "strike", "subscript", "superscript", "fontColor", "hiliteColor", "outdent", "indent", "align"],
                    ["horizontalRule", "list", "table"],
                    ["image", "link"],
                    ["fullScreen", "showBlocks", "codeView", "preview"]
                ],
                imageFileInput: false,
                charCounter: true,
                defaultStyle: "font-size: 14px",
                height: "auto",
                linkTargetNewWindow: true,
                minHeight: "250px",
                historyStackDelayTime: 0,
                //@ts-ignore
                strictMode: false
            }}
            name="editorData"
            setContents={initialContent || ""}
            onChange={(text) => onEditorChange(text)}
            onBlur={() => onEditorBlur()}
        />
    );
};
