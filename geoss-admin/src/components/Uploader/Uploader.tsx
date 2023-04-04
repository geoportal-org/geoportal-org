import { FileUploader } from "react-drag-drop-files";
import styles from "./Uploader.module.css";

export const Uploader = () => {
    const handleChange = (file: any) => {
        console.log(file);
    };

    return <FileUploader multiple={false} required={true} classes={styles.uploader} handleChange={handleChange} />;
};
