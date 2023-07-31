package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.dashboard.model;

import com.rometools.rome.feed.impl.CloneableBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;

/**
 * The type Dashboard.
 */
@Getter
@Setter
@ToString
public class Dashboard implements Cloneable {

    private int id;
    /**
     * The Content.
     */
    String content;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneableBean.beanClone(this, Collections.emptySet());
    }
}
