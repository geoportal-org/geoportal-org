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

    /**
     * Gets org name.
     *
     * @return the org name
     */
    public String getOrgName() {
        return orgName;
    }

    /**
     * Sets org name.
     *
     * @param orgName the org name
     */
    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    /**
     * Gets ind name.
     *
     * @return the ind name
     */
    public String getIndName() {
        return indName;
    }

    /**
     * Sets ind name.
     *
     * @param indName the ind name
     */
    public void setIndName(String indName) {
        this.indName = indName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * Gets modules.
     *
     * @return the modules
     */
    @Override
    public List<Module> getModules() {
        modules = Lists.createWhenNull(modules);
        return modules;
    }

    /**
     * Sets modules.
     *
     * @param modules the modules
     */
    @Override
    public void setModules(final List<Module> modules) {
        this.modules = modules;
    }

    /**
     * Gets module.
     *
     * @param uri the uri
     * @return the module
     */
    @Override
    public Module getModule(final String uri) {
        return ModuleUtils.getModule(modules, uri);
    }

}
