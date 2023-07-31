package com.eversis.esa.geoss.curatedrelationships.search.model;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.BoundingBox;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    public void setQueryType(SearchQueryType queryType) {
        this.queryType = queryType != null ? queryType : SearchQueryType.FULL_TEXT;
    }

    public SearchQueryType getQueryType() {
        return queryType;
    }

    public void setOptionalPhrase(String phrase) {
        this.optionalPhrase = Optional.ofNullable(phrase);
    }

    public Optional<String> getOptionalPhrase() {
        return optionalPhrase;
    }

    public Optional<DateRange> getOptionalDateRange() {
        return optionalDateRange;
    }

    public Optional<BoundingBox> getOptionalBoundingBox() {
        return optionalBoundingBox;
    }

    public Optional<BoundingBoxRelation> getOptionalBoundingBoxRelation() {
        return optionalBoundingBoxRelation;
    }

    public Set<String> getSources() {
        return sources;
    }

    public Set<EntryType> getEntryTypes() {
        return entryTypes;
    }

    public Set<String> getParents() {
        return parents;
    }

    public Optional<String> getOptionalOrganizationName() {
        return organizationName;
    }

    public Optional<String> getOptionalProtocol() {
        return protocol;
    }

    public Optional<String> getOptionalKeyword() {
        return keyword;
    }

    public Optional<String> getOptionalFormat() {
        return format;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "dataSource=" + dataSource +
                ", queryType=" + queryType +
                ", optionalPhrase=" + optionalPhrase +
                ", optionalDateRange=" + optionalDateRange +
                ", optionalBoundingBox=" + optionalBoundingBox +
                ", optionalBoundingBoxRelation=" + optionalBoundingBoxRelation +
                ", sources=" + sources +
                ", resourceEntryTypes=" + entryTypes +
                ", parents=" + parents +
                ", organizationName=" + organizationName +
                ", protocol=" + protocol +
                ", keyword=" + keyword +
                ", format=" + format +
                '}';
    }

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

        public SearchQueryBuilder setPhrase(String phrase) {
            this.optionalPhrase = Optional.ofNullable(phrase).map(String::trim).filter(s -> !s.isEmpty());
            return this;
        }

        public SearchQueryBuilder setDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
            if (startDateTime != null || endDateTime != null) {
                this.optionalDateRange = Optional.of(new DateRange(startDateTime, endDateTime));
            }
            return this;
        }

        public SearchQueryBuilder setBoundingBox(BoundingBox boundingBox) {
            this.optionalBoundingBox = Optional.ofNullable(boundingBox);
            return this;
        }

        public SearchQueryBuilder setBoundingBoxRelation(BoundingBoxRelation boundingBoxRelation) {
            this.optionalBoundingBoxRelation = Optional.ofNullable(boundingBoxRelation);
            return this;
        }

        public SearchQueryBuilder setSources(Set<String> sources) {
            this.sources = sources != null ? sources : Collections.emptySet();
            return this;
        }

        public SearchQueryBuilder setEntryTypes(Set<EntryType> entryTypes) {
            this.entryTypes = entryTypes != null ? entryTypes : Collections.emptySet();
            return this;
        }

        public SearchQueryBuilder setParents(Set<String> parents) {
            this.parents = parents != null ? parents : Collections.emptySet();
            return this;
        }

        public SearchQueryBuilder setDataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public SearchQueryBuilder setOrganizationName(String organizationName) {
            this.organizationName = Optional.ofNullable(organizationName);
            return this;
        }

        public SearchQueryBuilder setProtocol(String protocol) {
            this.protocol = Optional.ofNullable(protocol);
            return this;
        }

        public SearchQueryBuilder setKeyword(String keyword) {
            this.keyword = Optional.ofNullable(keyword);
            return this;
        }

        public SearchQueryBuilder setFormat(String format) {
            this.format = Optional.ofNullable(format);
            return this;
        }

        public SearchQuery build() {
            return new SearchQuery(this);
        }

    }

}
