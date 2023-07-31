package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.geographicmetadata.model;

import com.rometools.rome.feed.impl.CloneableBean;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;

@Getter
@Setter
@ToString
public class OnlineResource implements Cloneable {

    private String url;
    private String protocol;
    private String name;
    private String description;
    private String descriptionURL;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneableBean.beanClone(this, Collections.emptySet());
    }

}
