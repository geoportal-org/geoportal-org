package com.eversis.esa.geoss.curated.resources.service;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.resources.domain.BookmarkedResult;
import com.eversis.esa.geoss.curated.resources.model.AddBookmarkedResultModel;
import com.eversis.esa.geoss.curated.resources.model.BookmarkedResultModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Bookmarked service.
 */
public interface BookmarkedService {

    /**
     * Find bookmarks page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<BookmarkedResult> findBookmarks(@NotNull Pageable pageable);

    /**
     * Find bookmark bookmarked result.
     *
     * @param bookmarkId the bookmark id
     * @return the bookmarked result
     */
    BookmarkedResult findBookmark(long bookmarkId);

    /**
     * Create bookmark.
     *
     * @param bookmarkedResultDto the bookmarked result dto
     */
    void createBookmark(BookmarkedResultModel bookmarkedResultDto);

    /**
     * Update bookmark.
     *
     * @param bookmarkId the bookmark id
     * @param bookmarkedResultDto the bookmarked result dto
     */
    void updateBookmark(long bookmarkId, BookmarkedResultModel bookmarkedResultDto);

    /**
     * Delete bookmark.
     *
     * @param bookmarkId the bookmark id
     */
    void deleteBookmark(long bookmarkId);

    /**
     * Find bookmarks by user id page.
     *
     * @param userId the user id
     * @param pageable the pageable
     * @return the page
     */
    Page<BookmarkedResult> findBookmarksByUserId(String userId, @NotNull Pageable pageable);

    /**
     * Add bookmark.
     *
     * @param addBookmarkedResultDto the add bookmarked result dto
     */
    void addBookmark(AddBookmarkedResultModel addBookmarkedResultDto);

    /**
     * Delete bookmark by target id and data source.
     *
     * @param targetId the target id
     * @param dataSource the data source
     */
    void deleteBookmarkByTargetIdAndDataSource(String targetId, String dataSource);

}
