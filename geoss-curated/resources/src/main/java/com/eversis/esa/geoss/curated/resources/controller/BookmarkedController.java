package com.eversis.esa.geoss.curated.resources.controller;

import com.eversis.esa.geoss.curated.resources.domain.BookmarkedResult;
import com.eversis.esa.geoss.curated.resources.model.AddBookmarkedResultModel;
import com.eversis.esa.geoss.curated.resources.model.BookmarkedResultModel;
import com.eversis.esa.geoss.curated.resources.service.BookmarkedService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;

/**
 * The type Bookmarked controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/bookmarked")
@ResponseBody
@Tag(name = "bookmarked")
public class BookmarkedController {

    private final BookmarkedService bookmarkedService;

    /**
     * Find bookmarks page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<BookmarkedResult> findBookmarks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find bookmarks page: {}, size: {}", page, size);
        return bookmarkedService.findBookmarks(PageRequest.of(page, size));
    }

    /**
     * Find bookmark bookmarked result.
     *
     * @param bookmarkId the bookmark id
     * @return the bookmarked result
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{bookmarkId}")
    public BookmarkedResult findBookmark(@PathVariable long bookmarkId) {
        log.info("Find bookmark");
        return bookmarkedService.findBookmark(bookmarkId);
    }

    /**
     * Create bookmark.
     *
     * @param bookmarkedResultDto the bookmarked result dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createBookmark(@RequestBody @Valid BookmarkedResultModel bookmarkedResultDto) {
        log.info("Create bookmark");
        bookmarkedService.createBookmark(bookmarkedResultDto);
    }

    /**
     * Update bookmark.
     *
     * @param bookmarkId the bookmark id
     * @param bookmarkedResultDto the bookmarked result dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{bookmarkId}")
    public void updateBookmark(
            @PathVariable long bookmarkId,
            @RequestBody @Valid BookmarkedResultModel bookmarkedResultDto) {
        log.info("Update bookmark");
        bookmarkedService.updateBookmark(bookmarkId, bookmarkedResultDto);
    }

    /**
     * Delete bookmark.
     *
     * @param bookmarkId the bookmark id
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{bookmarkId}")
    public void deleteBookmark(@PathVariable long bookmarkId) {
        log.info("Delete bookmark");
        bookmarkedService.deleteBookmark(bookmarkId);
    }

    /**
     * Find bookmarks by user id page.
     *
     * @param userId the user id
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/search/findBookmarksByUserId")
    public Page<BookmarkedResult> findBookmarksByUserId(
            @RequestParam String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Find bookmarks by userId: {}, page: {}, size: {}", userId, page, size);
        return bookmarkedService.findBookmarksByUserId(userId, PageRequest.of(page, size));
    }

    /**
     * Add bookmark.
     *
     * @param addBookmarkedResultDto the add bookmarked result dto
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public void addBookmark(@RequestBody @Valid AddBookmarkedResultModel addBookmarkedResultDto) {
        log.info("Add bookmark");
        bookmarkedService.addBookmark(addBookmarkedResultDto);
    }

    /**
     * Delete bookmark by target id and data source.
     *
     * @param targetId the target id
     * @param dataSource the data source
     */
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/delete/byTargetIdAndDataSource")
    public void deleteBookmarkByTargetIdAndDataSource(@RequestParam String targetId,
            @RequestParam String dataSource) {
        log.info("Delete bookmark by targetId and dataSource");
        bookmarkedService.deleteBookmarkByTargetIdAndDataSource(targetId, dataSource);
    }

}
