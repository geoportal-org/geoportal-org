export const en = {
    general: {
        close: "Close",
        type: "Type here...",
        confirm: "Confirm",
        cancel: "Cancel",
        published: "Published",
        draft: "Draft",
        title: "Title",
        save: "Save",
        url: "URL",
        created: "Created",
        deleted: "Deleted",
        updated: "Updated",
        error: "Error occured",
        upload: "Upload",
        yes: "Yes",
        no: "No",
        "creation-date": "Creation date",
        "modification-date": "Modification date",
        author: "Author",
        actions: "Actions",
        id: "ID",
        description: "Description",
        "not-applicable": "n/a",
        "invalid-id": "Invalid ID",
    },
    form: {
        placeholders: {
            type: "Type here...",
            "select-one": "Select one...",
            "no-options": "No options",
        },
        errors: {
            "invalid-email": "Invalid e-mail address",
            "invalid-url": "Invalid URL",
            "invalid-username": "Invalid username",
            "invalid-password":
                "Invalid password (at least 8 characters including a lowercase letter, an uppercase letter, number and special character)",
            "invalid-slug": "Invalid friendly URL (spaces not allowed)",
            required: "Required field",
            "passwords-not-match": "Passwords don't match",
            "required-file": "File is required",
            "invalid-relative-url": "Invalid relative URL",
            "invalid-zero-positive-integer": "An integer between 0 - 20 is required",
            "invalid-latitude": "Invalid latitude (from -90 to 90, up to six decimal places)",
            "invalid-longitude": "Invalid longitude (from -180 to 180, up to six decimal places)",
        },
        actions: {
            submit: "Submit",
            "save-draft": "Save as draft",
            "submit-changes": "Submit changes",
            "save-changes-draft": "Save changes as draft",
        },
    },
    information: {
        error: {
            loading: "Oops, an error occured. Please refresh the page and if the problem persists, contact support.",
            upload: "An error occurred while wploading the file - please try again",
            "file-invalid-type": "Invalid file type",
            "file-too-large": "Too large file - maximum file size is 10MB",
            "file-too-small": "Too small file - minimum file size is 1B",
            "too-many-files": "Too many files - single file is accepted",
            "not-unique-file-name": "The file name is not unique within the folder. To upload a file, rename it.",
            "new-layer": "An error occurred while adding the layer - please try again",
            "updated-layer": "An error occurred while updating the layer - please try again",
            "updated-web": "An error occurred while updating the web settings - please refresh the page and try again",
            "updated-api": "An error occurred while updating the API settings - please refresh the page and try again",
            "new-tag": "An error occurred while adding the tag - please try again",
            "updated-tag": "An error occurred while updating the tag - please try again",
            "not-unique-layer-name": "The layer name is not unique, to add layer please rename it",
            "not-unique-layer-name-update": "The layer name is not unique, to update layer please rename it",
            "not-unique-tag-id": "The tag ID is not unique, to add tag please change ID",
            "not-unique-tag-id-update": "The tag ID is not unique, to update tag please change ID",
            "new-view": "An error occurred while adding the view - please try again",
            "new-nested-view": "An error occurred while adding the nested view - please try again",
            "update-view": "An error occurred while updating the view - please try again",
            "update-nested-view": "An error occurred while updating the nested view - please try again",
            "404": "Please try again. Contact support if the problem persists.",
            "load-web": "An error occurred while loading the web settings - please refresh the page and try again",
            logout: "Logout failed. Please try again and if the problem persists, contact support",
            "content-save": "Content has not been saved - please try again",
            "content-submit": "Content has not been created - please try again",
            "page-save": "Page has not been saved - please try again",
            "page-submit": "Page has not been created - please try again",
            general: "An error occured. Please refresh the page and if the problem persists, contact support.",
        },
        success: {},
        info: {
            "no-contents": "No website content added",
            "no-pages": "No page added",
            "no-layers": "No layer added",
            "no-changes": "No changes",
            "no-tags": "No tutorial tag added",
            "no-views": "No views added",
            "no-menu-items": "No menu item added",
        },
    },
    pages: {
        error: {
            info: "Oops! That page does not exist.",
        },
        contents: {
            "content-title": "Title",
            "creation-date": "Creation date",
            "delete-content-body": "Are you sure you want to remove content <b>{title}</b> (ID: {itemId})?",
            "delete-selected-info": "Are you sure you want to remove selected contents (<b>IDs: {itemId}</b>)?",
            "delete-selected-single-info": "Are you sure you want to remove selected content (<b>ID: {itemId}</b>)?",
            "delete-content-title": "Content deletion",
            "delete-selected": "Delete selected",
            "modification-date": "Modification date",
            "preview-content-body": "Preview",
            "preview-content-title": "Content preview (ID: {id})",
            actions: "Actions",
            add: "Add content",
            author: "Author",
            content: "Content",
            status: "Status",
            preview: "Content preview",
            "selected-deleted": "Selected content(s) has been deleted",
            "content-deleted": "Content {title} (ID: {itemId}) has been deleted",
        },
        "manage-content": {
            "add-title": "Add content",
            preview: "Show preview",
            "edit-title": "Edit content",
            "not-exist": "The content with the specified ID does not exist",
            "content-created": "Content created",
            "content-updated": "Content updated",
            "content-saved-info": "Content {title} has been created",
            "content-updated-info": "Content {title} has been updated",
            "draft-saved": "Content {title} has been saved as draft",
        },
        page: {
            "creation-date": "Creation date",
            "delete-page-body": "Are you sure you want to remove page <b>{title}</b> (ID: {itemId})?",
            "delete-page-title": "Page deletion",
            "delete-selected": "Delete selected",
            "modification-date": "Modification date",
            "page-title": "Title",
            actions: "Actions",
            add: "Add page",
            author: "Author",
            content: "Content",
            description: "Description (abstract)",
            slug: "Friendly URL",
            "selected-deleted": "Selected page(s) has been deleted",
            "delete-selected-info": "Are you sure you want to remove selected pages (<b>IDs: {itemId}</b>)?",
            "delete-selected-single-info": "Are you sure you want to remove selected page (<b>ID: {itemId}</b>)?",
            deleted: "Page {title} (ID: {itemId}) has been deleted",
        },
        "manage-page": {
            "add-title": "Add page",
            "edit-title": "Edit page",
            translate: "Add translation",
            "not-exist": "The page with the specified ID does not exist",
            "page-created": "Page created",
            "page-updated": "Page updated",
            "page-created-info": "Page {title} has been created",
            "page-updated-info": "Page {title} has been updated",
        },
        "file-repository": {
            "add-file": "Upload file",
            "add-folder": "Create folder",
            "delete-file-body": "Are you sure you want to remove file <b>{title}</b>?",
            "delete-file-header": "File deletion",
            "delete-folder-body": "Are you sure you want to remove folder <b>{title}</b>?",
            "delete-folder-header": "Folder deletion",
            "empty-folder": "No content",
            "file-createdBy": "Author",
            "file-createdDate": "Creation date",
            "file-deleted-info": "<b>{title}</b> file has been deleted",
            "file-deleted": "File deleted",
            "file-editing": "Edit file",
            "file-extension": "Extension",
            "file-fileName": "Name",
            "file-icon": "File {title} icon",
            "file-info-modal-header": "File information",
            "file-modifiedDate": "Modification date",
            "file-preview": "Show {title} file preview",
            "file-title": "Title",
            "file-link": "Link",
            "folder-deleted-info": "<b>{title}</b> folder has been deleted",
            "folder-deleted": "Folder deleted",
            "folder-editing": "Edit folder",
            "folder-icon": "Folder {title} icon",
            "open-folder": "Open folder {title}",
            "root-folder": "Home",
            file: "File",
            upload: "<b>Drop</b> single file here or <b>click</b> to upload",
            "no-file": "File not added",
            drop: "Drop file here",
            "upload-new": "To replace an added file, add a new one by <b>drop</b> or <b>click</b>",
            "delete-folder-error": "Folder was not removed - please refresh the page and try again",
            copy: "Copy link to clipboard",
            copied: "Link copied!",
            "not-copied": "Oops, link not copied",
        },
        menu: {
            add: "Add menu item",
            "add-item": "Add main menu item",
            "add-subitem": "Add child menu item",
            edit: "Edit menu item",
            "edit-subitem": "Edit child menu item",
            "edit-on": "Edit menu",
            "edit-off": "Save menu",
            "delete-info": "Are you sure you want to remove menu item <b>{title}</b>?",
            save: "Are you sure you want to save the current menu structure",
            "delete-title": "Menu item deletion",
            "save-title": "Saving the menu",
            delete: "Remove menu item",
            "img-title": "Image title",
            "img-url": "Image URL",
            created: "{title} menu item has been created",
            "deleted-single": "{title} menu item has been removed",
            "deleted-multi": "{title} menu item has been removed along with its subitems",
            "updated-item": "{title} menu item has been updated",
            "update-error": "The menu structure has not been updated - please refresh the page and try again",
            "delete-error": "The menu item was not removed - please refresh the page and try again",
            "update-item-error": "The menu item was not updated - please refresh the page and try again",
            "create-error": "The menu item was not created - please refresh the page and try again",
        },
        api: {
            "dab-base": "Base URL",
            "dab-data-providers": "Data Providers URL",
            "dab-view-api": "View API key",
            "dab-view-base": "View base URL",
            "geoss-opensearch": "Opensearch URL",
            "geoss-relations-limit": "Entry relations limit",
            "geoss-sync-manager-key": "SyncManager secret key",
            "geoss-sync-manager": "SyncManager URL",
            "geoss-transfer-limit": "Transfer options limit",
            google: "Google Maps API key",
            "kp-api": "API key",
            "kp-base": "Base URL",
            "other-next-geoss-base": "Next GEOSS base URL",
            w3w: "w3w key",
            external: "External",
            dab: "DAB",
            "knowledge-producer": "Knowledge producer",
            geoss: "GEOSS CR",
            other: "Other",
            "no-changes-info": "Make changes to save API settings",
            updated: "API settings has been updated",
        },
        web: {
            source: "Source",
            "default-source-name": "Default source name",
            longitude: "Longitude",
            latitude: "Latitude",
            zoom: "Zoom",
            logo: "Logo",
            "default-source": "Default source",
            map: "Map",
            "no-changes-info": "Make changes to save web settings",
            updated: "Web settings has been updated",
        },
        layer: {
            add: "Add layer",
            edit: "Edit layer",
            name: "Name",
            visibility: "Visibility",
            visible: "Visible",
            invisible: "Invisible",
            "delete-layer-body": "Are you sure you want to remove layer <b>{title}</b> (<b>ID: {itemId}</b>)?",
            "delete-layer-title": "Layer deletion",
            "delete-confirmation": "Layer {title} (ID: {itemId}) has been deleted",
            "delete-error": "Layer {title} (ID: {itemId}) has not been deleted - please try again",
            "layer-added": "Layer {title} has been created",
            "layer-updated": "Layer {title} has been updated",
        },
        catalogs: {},
        views: {
            add: "Add view",
            "add-nested": "Add nested view",
            name: "View name",
            id: "View ID",
            parent: "Parent view name",
            default: "Default option",
            edit: "Edit view",
            "edit-nested": "Edit nested view",
            expand: "Expand the table row to see the nested views",
            "expand-all": "Expand all table rows to see the nested views",
            "delete-nested-body": "Are you sure you want to remove nested view <b>{title}</b>?",
            "delete-view-with-nested-body":
                "Are you sure you want to remove view <b>{title}</b> with its nested views?",
            "delete-view-body": "Are you sure you want to remove view <b>{title}</b>?",
            "delete-nested-title": "Nested view deletion",
            "delete-view-title": "View deletion",
            "delete-confirmation": "View {title} has been deleted",
            "delete-with-nested-confirmation": "View {title} has been deleted with its nested views",
            "delete-nested-confirmation": "Nested view {title} has been deleted",
            "delete-error": "View {title} has not been deleted - please refresh the page and try again",
            "delete-nested-error": "Nested view {title} has not been deleted - please refresh the page and try again",
            added: "View {title} has been created",
            updated: "View {title} has been updated",
            "added-nested": "Nested view {title} has been created",
            "updated-nested": "Nested view {title} has been updated",
        },
        tags: {
            type: "Type",
            tip: "Tip",
            new: "New",
            show: "Show",
            placement: "Placement",
            "left-top": "left-top",
            "left-bottom": "left-bottom",
            "left-center": "left-center",
            "right-top": "right-top",
            "right-bottom": "right-bottom",
            "right-center": "right-center",
            "top-center": "top-center",
            "bottom-center": "bottom-center",
            "center-center": "center-center",
            add: "Add tutorial tag",
            edit: "Edit tutorial tag",
            "tag-added": "Tag {title} has been created",
            "tag-updated": "Tag {title} has been updated",
            "delete-tag-body": "Are you sure you want to remove tag <b>{title}</b>?",
            "delete-tag-title": "Tag deletion",
            "delete-confirmation": "Tag {title} has been deleted",
            "delete-error": "Tag {title} has not been deleted - please try again",
        },
    },
    nav: {
        open: "Open menu",
        close: "Close menu",
        contents: {
            title: "Contents",
            section: {
                website: "Website contents",
                page: "Site pages",
                repository: "File repository",
                menu: "Menu",
            },
        },
        settings: {
            title: "Settings",
            section: {
                api: "API",
                catalogs: "Catalogs",
                layer: "Default layer",
                tutorial: "Tutorial tags",
                views: "Views",
                web: "Web",
            },
        },
        profile: {
            title: "Profile",
            section: {
                profile: "My profile",
                logout: "Logout",
            },
        },
    },
    languageSwitcher: {
        en: "United States of America flag",
        es: "Spanish flag",
        fr: "French flag",
        pl: "Polish flag",
        ru: "Russia flag",
        toggle: "Toggle language selector",
        zh: "China flag",
    },
    translationSwitcher: {
        add: "Add translation",
        en: "Add english translation",
        es: "Add spanish translation",
        fr: "Add french translation",
        pl: "Add Polish translation",
        ru: "Add russian translation",
        zh: "Add chinese translation",
    },
    pagination: {
        page: "Page",
        show: "Show",
    },
    loader: {
        status: "Loading...",
    },
};
