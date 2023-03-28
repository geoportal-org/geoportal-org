export type TextEditorProps = {
    onEditorChange: (text: string) => void;
    onEditorBlur: () => void;
    initialContent?: string;
};
