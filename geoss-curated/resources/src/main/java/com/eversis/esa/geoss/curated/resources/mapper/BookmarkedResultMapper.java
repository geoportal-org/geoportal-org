package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.BookmarkedResult;
import com.eversis.esa.geoss.curated.resources.model.AddBookmarkedResultModel;
import com.eversis.esa.geoss.curated.resources.model.BookmarkedResultModel;
import org.springframework.stereotype.Component;

/**
 * The type Bookmarked result mapper.
 */
@Component
public class BookmarkedResultMapper {

    /**
     * Map to bookmarked result bookmarked result.
     *
     * @param model the model
     * @return the bookmarked result
     */
    public BookmarkedResult mapToBookmarkedResult(BookmarkedResultModel model) {
        return getBookmarkedResult(model);
    }

    /**
     * Map to bookmarked result bookmarked result.
     *
     * @param model the model
     * @param bookmarkedResult the bookmarked result
     * @return the bookmarked result
     */
    public BookmarkedResult mapToBookmarkedResult(BookmarkedResultModel model, BookmarkedResult bookmarkedResult) {
        return getBookmarkedResult(model, bookmarkedResult);
    }

    /**
     * Map to add bookmarked result bookmarked result.
     *
     * @param addBookmarkedResultDto the add bookmarked result dto
     * @return the bookmarked result
     */
    public BookmarkedResult mapToAddBookmarkedResult(AddBookmarkedResultModel addBookmarkedResultDto) {
        return getBookmarkedResult(addBookmarkedResultDto);
    }

    private BookmarkedResult getBookmarkedResult(BookmarkedResultModel model) {
        if (model == null) {
            return null;
        }
        BookmarkedResult bookmarkedResult = new BookmarkedResult();
        bookmarkedResult.setName(model.getName());
        bookmarkedResult.setTargetId(model.getTargetId());
        bookmarkedResult.setEntryXml(model.getEntryXml());
        bookmarkedResult.setUserId(model.getUserId());
        bookmarkedResult.setCurrMap(model.getCurrMap());
        bookmarkedResult.setGroupId(model.getGroupId());
        bookmarkedResult.setDataSource(model.getDataSource());
        bookmarkedResult.setSourceBaseUrl(model.getSourceBaseUrl());
        bookmarkedResult.setValid(model.getValid());
        return bookmarkedResult;
    }

    private BookmarkedResult getBookmarkedResult(BookmarkedResultModel model, BookmarkedResult bookmarkedResult) {
        if (model == null) {
            return null;
        }
        bookmarkedResult.setName(model.getName());
        bookmarkedResult.setTargetId(model.getTargetId());
        bookmarkedResult.setEntryXml(model.getEntryXml());
        bookmarkedResult.setUserId(model.getUserId());
        bookmarkedResult.setCurrMap(model.getCurrMap());
        bookmarkedResult.setGroupId(model.getGroupId());
        bookmarkedResult.setDataSource(model.getDataSource());
        bookmarkedResult.setSourceBaseUrl(model.getSourceBaseUrl());
        bookmarkedResult.setValid(model.getValid());
        return bookmarkedResult;
    }

    private BookmarkedResult getBookmarkedResult(AddBookmarkedResultModel addBookmarkedResultDto) {
        if (addBookmarkedResultDto == null) {
            return null;
        }
        BookmarkedResult bookmarkedResult = new BookmarkedResult();
        bookmarkedResult.setName(addBookmarkedResultDto.getName());
        bookmarkedResult.setTargetId(addBookmarkedResultDto.getTargetId());
        bookmarkedResult.setUserId(addBookmarkedResultDto.getUserId());
        bookmarkedResult.setCurrMap(addBookmarkedResultDto.getCurrMap());
        bookmarkedResult.setDataSource(addBookmarkedResultDto.getDataSource());
        bookmarkedResult.setValid(1);
        return bookmarkedResult;
    }

}
