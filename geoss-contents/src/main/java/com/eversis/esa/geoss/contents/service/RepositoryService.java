package com.eversis.esa.geoss.contents.service;

import com.eversis.esa.geoss.contents.domain.Document;
import com.eversis.esa.geoss.contents.domain.Folder;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * The interface Repository service.
 */
public interface RepositoryService {

    /**
     * Init.
     */
    void init();

    /**
     * Add folder folder.
     *
     * @param folder the folder
     * @return the folder
     */
    Folder addFolder(Folder folder);

    /**
     * Remove folder.
     *
     * @param id the id
     */
    void removeFolder(Long id);

    /**
     * Add document document.
     *
     * @param document the document
     * @param file the file
     * @return the document
     */
    Document addDocument(Document document, MultipartFile file);

    /**
     * Remove document.
     *
     * @param id the id
     */
    void removeDocument(Long id);

    /**
     * Gets document content.
     *
     * @param id the id
     * @return the document content
     */
    Resource getDocumentContent(Long id);
}
