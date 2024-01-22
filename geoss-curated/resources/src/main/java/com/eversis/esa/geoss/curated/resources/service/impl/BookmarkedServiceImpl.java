package com.eversis.esa.geoss.curated.resources.service.impl;

import jakarta.validation.constraints.NotNull;

import com.eversis.esa.geoss.curated.resources.domain.BookmarkedResult;
import com.eversis.esa.geoss.curated.resources.mapper.BookmarkedResultMapper;
import com.eversis.esa.geoss.curated.resources.model.AddBookmarkedResultModel;
import com.eversis.esa.geoss.curated.resources.model.BookmarkedResultModel;
import com.eversis.esa.geoss.curated.resources.repository.BookmarkedResultRepository;
import com.eversis.esa.geoss.curated.resources.service.BookmarkedService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * The type Bookmarked service.
 */
@Log4j2
@Service
@Transactional(readOnly = true)
public class BookmarkedServiceImpl implements BookmarkedService {

    private final BookmarkedResultRepository bookmarkedResultRepository;

    private final BookmarkedResultMapper bookmarkedResultMapper;

    /**
     * Instantiates a new Bookmarked service.
     *
     * @param bookmarkedResultRepository the bookmarked result repository
     * @param bookmarkedResultMapper the bookmarked result mapper
     */
    public BookmarkedServiceImpl(BookmarkedResultRepository bookmarkedResultRepository,
            BookmarkedResultMapper bookmarkedResultMapper) {
        this.bookmarkedResultRepository = bookmarkedResultRepository;
        this.bookmarkedResultMapper = bookmarkedResultMapper;
    }

    @Override
    public Page<BookmarkedResult> findBookmarks(Pageable pageable) {
        log.info("Finding bookmarks");
        return bookmarkedResultRepository.findAll(pageable);
    }

    @Override
    public BookmarkedResult findBookmark(long bookmarkId) {
        log.info("Finding bookmark with id {}", bookmarkId);
        return bookmarkedResultRepository.findById(bookmarkId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Bookmark entity with id: " + bookmarkId + " does not exist"));
    }

    @Transactional
    @Override
    public void createBookmark(BookmarkedResultModel bookmarkedResultDto) {
        log.info("Creating new bookmark - {}", bookmarkedResultDto);
        BookmarkedResult bookmarkedResult =
                bookmarkedResultRepository.save(bookmarkedResultMapper.mapToBookmarkedResult(bookmarkedResultDto));
        log.info("Created new bookmark with id: {}", bookmarkedResult.getId());
    }

    @Transactional
    @Override
    public void updateBookmark(long bookmarkId, BookmarkedResultModel bookmarkedResultDto) {
        log.info("Updating bookmark with id {}, using model {}", bookmarkId, bookmarkedResultDto);
        final BookmarkedResult bookmarkedResult = bookmarkedResultRepository.findById(bookmarkId).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Bookmark entity with id: " + bookmarkId + " does not exist"));
        bookmarkedResultRepository.save(bookmarkedResultMapper.mapToBookmarkedResult(bookmarkedResultDto, bookmarkedResult));
        log.info("Updated bookmark with id: {}", bookmarkedResult.getId());
    }

    @Transactional
    @Override
    public void deleteBookmark(long bookmarkId) {
        log.info("Deleting bookmark with id: {}", bookmarkId);
        bookmarkedResultRepository.deleteById(bookmarkId);
        log.info("Deleted bookmark with id: {}", bookmarkId);
    }

    @Override
    public Page<BookmarkedResult> findBookmarksByUserId(String userId, @NotNull Pageable pageable) {
        log.info("Finding bookmarks by userId: {}", userId);
        return bookmarkedResultRepository.findByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public void addBookmark(AddBookmarkedResultModel addBookmarkedResultDto) {
        log.info("Adding bookmark - {}", addBookmarkedResultDto);
        BookmarkedResult bookmarkedResult =
                bookmarkedResultRepository.save(bookmarkedResultMapper.mapToAddBookmarkedResult(addBookmarkedResultDto));
        log.info("Added bookmark with id: {}", bookmarkedResult.getId());
    }

    @Transactional
    @Override
    public void deleteBookmarkByTargetIdAndDataSource(String targetId, String dataSource) {
        log.info("Deleting bookmark by targetId: {} and dataSource: {}", targetId, dataSource);
        bookmarkedResultRepository.deleteByTargetIdAndDataSource(targetId, dataSource);
        log.info("Deleted bookmark by targetId: {} and dataSource: {}", targetId, dataSource);
    }

}
