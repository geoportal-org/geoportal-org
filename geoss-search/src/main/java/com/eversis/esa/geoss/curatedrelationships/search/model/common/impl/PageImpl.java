package com.eversis.esa.geoss.curatedrelationships.search.model.common.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Overriden {@link org.springframework.data.domain.PageImpl} to support pagination using startIndex instead of page*
 * number.
 *
 * @param <T> the type parameter
 */
public class PageImpl<T> implements Page<T> {

    private final List<T> content = new ArrayList<>();
    private final Pageable pageable;
    private final long total;

    /**
     * Constructor of {@code PageImpl}.
     *
     * @param content the content of this page
     * @param pageable the paging information
     * @param total the total amount of items available
     */
    public PageImpl(List<T> content, Pageable pageable, long total) {
        if (null == content) {
            throw new IllegalArgumentException("Content must not be null!");
        }
        this.content.addAll(content);
        this.total = total;
        this.pageable = pageable;
    }

    /**
     * Creates a new {@link PageImpl} with the given content. This will result in the created {@link Page} being
     * identical to the entire {@link List}*.
     *
     * @param content the content
     */
    public PageImpl(List<T> content) {
        this(content, null, (null == content) ? 0 : content.size());
    }

    @Override
    public int getStartIndex() {
        return pageable == null ? 0 : pageable.getStartIndex();
    }

    @Override
    public int getSize() {
        return pageable == null ? content.size() : pageable.getPageSize();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public Sort getSort() {
        return pageable == null ? null : pageable.getSort();
    }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page with startIndex %s of %d containing %s instances", getStartIndex(),
                getTotalElements(), contentType);
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageImpl<?>)) {
            return false;
        }

        PageImpl<?> that = (PageImpl<?>) obj;

        boolean totalEqual = this.total == that.total;
        boolean contentEqual = this.content.equals(that.content);
        boolean pageableEqual = Objects.equals(this.pageable, that.pageable);

        return totalEqual && contentEqual && pageableEqual;
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = 31 * result + (int) (total ^ total >>> 32);
        result = 31 * result + (pageable == null ? 0 : pageable.hashCode());
        result = 31 * result + content.hashCode();

        return result;
    }
}
