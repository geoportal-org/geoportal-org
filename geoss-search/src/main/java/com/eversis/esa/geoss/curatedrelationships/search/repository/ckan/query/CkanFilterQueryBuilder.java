package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.DateRange;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanFields.FORMAT;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanFields.ID;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanFields.MODIFIED_DATE;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanFields.ORGANIZATION;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query.CkanFields.TAGS;

class CkanFilterQueryBuilder {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_INSTANT;
    private StringBuilder filterQueryBuilder = new StringBuilder();

    CkanFilterQueryBuilder ids(Iterable<String> ids) {
        StringBuilder idsQueryBuilder = new StringBuilder();
        idsQueryBuilder.append('(');
        idsQueryBuilder.append(String.join(" OR ", ids));
        idsQueryBuilder.append(')');

        filterQueryBuilder
                .append(getQueryFieldsDelimiter())
                .append(ID + CkanQueryParameters.VALUE_DELIMITER)
                .append(idsQueryBuilder.toString());
        return this;
    }

    CkanFilterQueryBuilder dateRange(DateRange dateRange) {
        StringBuilder dateRangeQueryBuilder = new StringBuilder();
        dateRangeQueryBuilder.append('[');
        dateRangeQueryBuilder.append(dateRange.getStartDateTime()
                .map(dateTime -> dateTime.atZone(ZoneOffset.UTC))
                .map(dateTime -> dateTime.format(dateFormatter))
                .orElse("*"));
        dateRangeQueryBuilder.append(" TO ");
        dateRangeQueryBuilder.append(dateRange.getEndDateTime()
                .map(dateTime -> dateTime.atZone(ZoneOffset.UTC))
                .map(dateTime -> dateTime.format(dateFormatter))
                .orElse("*"));
        dateRangeQueryBuilder.append(']');

        filterQueryBuilder
                .append(getQueryFieldsDelimiter())
                .append(MODIFIED_DATE + CkanQueryParameters.VALUE_DELIMITER)
                .append(dateRangeQueryBuilder.toString());
        return this;
    }

    CkanFilterQueryBuilder format(String format) {
        filterQueryBuilder
                .append(getQueryFieldsDelimiter())
                .append(FORMAT + ":")
                .append(format);
        return this;
    }

    CkanFilterQueryBuilder keyword(String keyword) {
        filterQueryBuilder
                .append(getQueryFieldsDelimiter())
                .append(TAGS + ":")
                .append(keyword);
        return this;
    }

    CkanFilterQueryBuilder organization(String organizationName) {
        filterQueryBuilder
                .append(getQueryFieldsDelimiter())
                .append(ORGANIZATION + ":")
                .append(organizationName);
        return this;
    }

    String build() {
        return filterQueryBuilder.toString();
    }

    private String getQueryFieldsDelimiter() {
        return filterQueryBuilder.length() > 0 ? " AND " : "";
    }

}
