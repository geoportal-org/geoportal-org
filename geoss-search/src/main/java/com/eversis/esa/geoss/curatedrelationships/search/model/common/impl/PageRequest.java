/*
 * Copyright 2008-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eversis.esa.geoss.curatedrelationships.search.model.common.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

/**
 * Reimplementation of Spring {@link org.springframework.data.domain.PageRequest} to support pagination using startIndex
 * instead of page number.
 *
 * @author Oliver Gierke
 */
public class PageRequest implements Pageable {

    private final int startIndex;
    private final int size;
    private final Sort sort;

    /**
     * Creates a new {@link PageRequest}.
     *
     * @param startIndex the start index
     * @param size the size
     */
    public PageRequest(int startIndex, int size) {
        this(startIndex, size, null);
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param startIndex the start index
     * @param size the size
     * @param direction the direction
     * @param properties the properties
     */
    public PageRequest(int startIndex, int size, Direction direction, String... properties) {
        this(startIndex, size, new Sort(direction, properties));
    }

    /**
     * Creates a new {@link PageRequest} with sort parameters applied.
     *
     * @param startIndex the start index
     * @param size the size
     * @param sort the sort
     */
    public PageRequest(int startIndex, int size, Sort sort) {
        if (0 > startIndex) {
            throw new IllegalArgumentException("Start index must not be less than zero!");
        }

        if (0 >= size) {
            throw new IllegalArgumentException("Page size must not be less than or equal to zero!");
        }

        this.startIndex = startIndex;
        this.size = size;
        this.sort = sort;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public int getStartIndex() {
        return startIndex;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PageRequest)) {
            return false;
        }

        PageRequest that = (PageRequest) obj;

        boolean pageEqual = this.startIndex == that.startIndex;
        boolean sizeEqual = this.size == that.size;

        boolean sortEqual = this.sort == null ? that.sort == null : this.sort.equals(that.sort);

        return pageEqual && sizeEqual && sortEqual;
    }

    @Override
    public int hashCode() {

        int result = 17;

        result = 31 * result + startIndex;
        result = 31 * result + size;
        result = 31 * result + (null == sort ? 0 : sort.hashCode());

        return result;
    }

}
