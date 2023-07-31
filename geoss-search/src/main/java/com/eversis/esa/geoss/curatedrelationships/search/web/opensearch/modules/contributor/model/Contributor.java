package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.contributor.model;

import com.rometools.rome.feed.impl.CloneableBean;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.rometools.rome.feed.module.Extendable;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.feed.module.impl.ModuleUtils;
import com.rometools.utils.Lists;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Bean for contributor elements of Atom feeds.
 */
public class Contributor implements Cloneable, Serializable, Extendable {

    private static final long serialVersionUID = 1L;

    private String orgName;
    private String indName;
    private String email;
    private List<Module> modules;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return CloneableBean.beanClone(this, Collections.<String>emptySet());
    }

    @Override
    public boolean equals(final Object other) {
        return EqualsBean.beanEquals(this.getClass(), this, other);
    }

    @Override
    public int hashCode() {
        return EqualsBean.beanHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBean.toString(this.getClass(), this);
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIndName() {
        return indName;
    }

    public void setIndName(String indName) {
        this.indName = indName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public List<Module> getModules() {
        modules = Lists.createWhenNull(modules);
        return modules;
    }

    @Override
    public void setModules(final List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public Module getModule(final String uri) {
        return ModuleUtils.getModule(modules, uri);
    }

}
