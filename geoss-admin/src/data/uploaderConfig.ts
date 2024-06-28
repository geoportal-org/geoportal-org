export const uploaderConfig = {
    maxFiles: 1, // accept only single file
    maxSize: 52428800, // up to 10MB per file
    multiple: false, // single file selection from dialog window
    accept: {
        "application/jrxml": [".jrxml"],
        "application/json": [".map"],
        "application/msword": [".doc", ".dot"],
        "application/pdf": [".pdf"],
        "application/rtf": [".rtf"],
        "application/zip": [".zip"],
        "application/vnd.ms-excel": [".xls"],
        "application/vnd.ms-fontobject": [".eot"],
        "application/vnd.ms-powerpoint": [".ppt"],
        "application/vnd.oasis.opendocument.formula": [".odf"],
        "application/vnd.oasis.opendocument.graphics": [".odg"],
        "application/vnd.oasis.opendocument.presentation": [".odp"],
        "application/vnd.oasis.opendocument.spreadsheet": [".ods"],
        "application/vnd.oasis.opendocument.text": [".odt"],
        "application/vnd.openxmlformats-officedocument.presentationml.presentation": [".pptx"],
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": [".xlsx"],
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document": [".docx"],
        "application/vnd.sun.xml.base": [".odb"],
        "application/vnd.sun.xml.calc": [".sxc"],
        "application/vnd.sun.xml.impress": [".sxi"],
        "application/vnd.sun.xml.writer": [".sxw"],
        "application/vnd.visio": [".vsd"],
        "application/x-shockwave-flash": [".swf"],
        "application/xml": [".xml"],
        "audio/mpeg": [".mp3"],
        "font/ttf": [".ttf"],
        "font/woff": [".woff"],
        "image/bmp": [".bmp"],
        "image/gif": [".gif"],
        "image/jpeg": [".jpeg", ".jpg"],
        "image/png": [".png"],
        "image/svg+xml": [".svg"],
        //"image/tiff": [".tiff", ".tif"],
        "text/css": [".css"],
        "text/html": [".htm", ".html"],
        "text/javascript": [".js"],
        "text/plain": [".txt"],
        "text/xml": [".xml"],
        "video/mp4": [".mp4"],
        "video/x-flv": [".flv"],
    }, // accepted files formats: mimeType: [extensions list]
};

export const acceptedLogoExtensions = ["jpeg", "jpg", "png", "svg"];
