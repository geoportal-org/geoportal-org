package com.eversis.esa.geoss.curatedrelationships.search.model;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The type Search query.
 */
public class SearchQuery {

    private SearchQueryType queryType = SearchQueryType.FULL_TEXT;
    private Optional<String> optionalPhrase;
    private Optional<DateRange> optionalDateRange;
    private Optional<BoundingBox> optionalBoundingBox;
    private Optional<BoundingBoxRelation> optionalBoundingBoxRelation;
    private Set<String> sources;
    private Set<EntryType> entryTypes;
    private Set<String> parents;
    private Optional<String> organizationName;
    private Optional<String> protocol;
    private Optional<String> keyword;
    private Optional<String> format;
    private DataSource dataSource;

    private SearchQuery(SearchQueryBuilder searchQueryBuilder) {
        this.dataSource = searchQueryBuilder.dataSource;
        this.optionalPhrase = searchQueryBuilder.optionalPhrase;
        this.optionalDateRange = searchQueryBuilder.optionalDateRange;
        this.optionalBoundingBox = searchQueryBuilder.optionalBoundingBox;
        this.optionalBoundingBoxRelation = searchQueryBuilder.optionalBoundingBoxRelation;
        this.sources = searchQueryBuilder.sources;
        this.entryTypes = searchQueryBuilder.entryTypes;
        this.parents = searchQueryBuilder.parents;
        this.organizationName = searchQueryBuilder.organizationName;
        this.protocol = searchQueryBuilder.protocol;
        this.keyword = searchQueryBuilder.keyword;
        this.format = searchQueryBuilder.format;
    }

    /**
     * Sets query type.
     *
     * @param queryType the query type
     */
    public void setQueryType(SearchQueryType queryType) {
        this.queryType = queryType != null ? queryType : SearchQueryType.FULL_TEXT;
    }

    /**
     * Gets query type.
     *
     * @return the query type
     */
    public SearchQueryType getQueryType() {
        return queryType;
    }

    /**
     * Sets optional phrase.
     *
     * @param phrase the phrase
     */
    public void setOptionalPhrase(String phrase) {
        this.optionalPhrase = Optional.ofNullable(phrase);
    }

    /**
     * Gets optional phrase.
     *
     * @return the optional phrase
     */
    public Optional<String> getOptionalPhrase() {
        return optionalPhrase;
    }

    /**
     * Gets optional date range.
     *
     * @return the optional date range
     */
    public Optional<DateRange> getOptionalDateRange() {
        return optionalDateRange;
    }

    /**
     * Gets optional bounding box.
     *
     * @return the optional bounding box
     */
    public Optional<BoundingBox> getOptionalBoundingBox() {
        return optionalBoundingBox;
    }

    /**
     * Gets optional bounding box relation.
     *
     * @return the optional bounding box relation
     */
    public Optional<BoundingBoxRelation> getOptionalBoundingBoxRelation() {
        return optionalBoundingBoxRelation;
    }

    /**
     * Gets sources.
     *
     * @return the sources
     */
    public Set<String> getSources() {
        return sources;
    }

    /**
     * Gets entry types.
     *
     * @return the entry types
     */
    public Set<EntryType> getEntryTypes() {
        return entryTypes;
    }

    /**
     * Gets parents.
     *
     * @return the parents
     */
    public Set<String> getParents() {
        return parents;
    }

    /**
     * Gets optional organization name.
     *
     * @return the optional organization name
     */
    public Optional<String> getOptionalOrganizationName() {
        return organizationName;
    }

    /**
     * Gets optional protocol.
     *
     * @return the optional protocol
     */
    public Optional<String> getOptionalProtocol() {
        return protocol;
    }

    /**
     * Gets optional keyword.
     *
     * @return the optional keyword
     */
    public Optional<String> getOptionalKeyword() {
        return keyword;
    }

    /**
     * Gets optional format.
     *
     * @return the optional format
     */
    public Optional<String> getOptionalFormat() {
        return format;
    }

    /**
     * Gets data source.
     *
     * @return the data source
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String toString() {
        return "SearchQuery{" + "dataSource=" + dataSource + ", queryType=" + queryType + ", optionalPhrase="
               + optionalPhrase + ", optionalDateRange=" + optionalDateRange + ", optionalBoundingBox="
               + optionalBoundingBox + ", optionalBoundingBoxRelation=" + optionalBoundingBoxRelation + ", sources="
               + sources + ", resourceEntryTypes=" + entryTypes + ", parents=" + parents + ", organizationName="
               + organizationName + ", protocol=" + protocol + ", keyword=" + keyword + ", format=" + format + '}';
    }

    /**
     * The type Search query builder.
     */
    public static class SearchQueryBuilder {

        private Optional<String> optionalPhrase = Optional.empty();
        private Optional<DateRange> optionalDateRange = Optional.empty();
        private Optional<BoundingBox> optionalBoundingBox = Optional.empty();
        private Optional<BoundingBoxRelation> optionalBoundingBoxRelation = Optional.empty();
        private Set<String> sources = new HashSet<>();
        private Set<EntryType> entryTypes = Collections.emptySet();
        private Set<String> parents = Collections.emptySet();
        private Optional<String> organizationName = Optional.empty();
        private Optional<String> protocol = Optional.empty();
        private Optional<String> keyword = Optional.empty();
        private Optional<String> format = Optional.empty();
        private DataSource dataSource = DataSource.GEOSS_CR;

        /**
         * Sets phrase.
         *
         * @param phrase the phrase
         * @return the phrase
         */
        public SearchQueryBuilder setPhrase(String phrase) {
            this.optionalPhrase = Optional.ofNullable(phrase).map(String::trim).filter(s -> !s.isEmpty());
            return this;
        }

        /**
         * Sets date range.
         *
         * @param startDateTime the start date time
         * @param endDateTime the end date time
         * @return the date range
         */
        public SearchQueryBuilder setDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            if (startDateTime != null || endDateTime != null) {
                this.optionalDateRange = Optional.of(new DateRange(startDateTime, endDateTime));
            }
            return this;
        }

        /**
         * Sets bounding box.
         *
         * @param boundingBox the bounding box
         * @return the bounding box
         */
        public SearchQueryBuilder setBoundingBox(BoundingBox boundingBox) {
            this.optionalBoundingBox = Optional.ofNullable(boundingBox);
            return this;
        }

        /**
         * Sets bounding box relation.
         *
         * @param boundingBoxRelation the bounding box relation
         * @return the bounding box relation
         */
        public SearchQueryBuilder setBoundingBoxRelation(BoundingBoxRelation boundingBoxRelation) {
            this.optionalBoundingBoxRelation = Optional.ofNullable(boundingBoxRelation);
            return this;
        }

        /**
         * Sets sources.
         *
         * @param sources the sources
         * @return the sources
         */
        public SearchQueryBuilder setSources(Set<String> sources) {
            this.sources = sources != null ? sources : Collections.emptySet();
            return this;
        }

        /**
         * Sets entry types.
         *
         * @param entryTypes the entry types
         * @return the entry types
         */
        public SearchQueryBuilder setEntryTypes(Set<EntryType> entryTypes) {
            this.entryTypes = entryTypes != null ? entryTypes : Collections.emptySet();
            return this;
        }

        /**
         * Sets parents.
         *
         * @param parents the parents
         * @return the parents
         */
        public SearchQueryBuilder setParents(Set<String> parents) {
            this.parents = parents != null ? parents : Collections.emptySet();
            return this;
        }

        /**
         * Sets data source.
         *
         * @param dataSource the data source
         * @return the data source
         */
        public SearchQueryBuilder setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        /**
         * Sets organization name.
         *
         * @param organizationName the organization name
         * @return the organization name
         */
        public SearchQueryBuilder setOrganizationName(String organizationName) {
            this.organizationName = Optional.ofNullable(organizationName);
            return this;
        }

        /**
         * Sets protocol.
         *
         * @param protocol the protocol
         * @return the protocol
         */
        public SearchQueryBuilder setProtocol(String protocol) {
            this.protocol = Optional.ofNullable(protocol);
            return this;
        }

        /**
         * Sets keyword.
         *
         * @param keyword the keyword
         * @return the keyword
         */
        public SearchQueryBuilder setKeyword(String keyword) {
            this.keyword = Optional.ofNullable(keyword);
            return this;
        }

        /**
         * Sets format.
         *
         * @param format the format
         * @return the format
         */
        public SearchQueryBuilder setFormat(String format) {
            this.format = Optional.ofNullable(format);
            return this;
        }

        /**
         * Build search query.
         *
         * @return the search query
         */
        public SearchQuery build() {
            return new SearchQuery(this);
        }

    }

}
