package com.eversis.esa.geoss.personaldata.common.hateoas;

import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.server.core.LinkBuilderSupport;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import java.util.List;

/**
 * The type Simple link builder.
 * <br/>
 * This is a simple link builder to use, because spring hateoas have several bugs, therefore
 * {@link org.springframework.hateoas.server.mvc.WebMvcLinkBuilder} cannot be used directly in a project.
 * <br/>
 * Spring hateoas issues:
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/1855">#1855</a>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/1816">#1816</a>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/1728">#1728</a>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/1406">#1406</a>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/1360">#1360</a>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/795">#795</a>
 * <a href="https://github.com/spring-projects/spring-hateoas/issues/434">#434</a>
 */
public class SimpleLinkBuilder extends LinkBuilderSupport<SimpleLinkBuilder> {

    /**
     * Instantiates a new Simple link builder.
     *
     * @param components the components
     * @param affordances the affordances
     */
    SimpleLinkBuilder(UriComponents components, List<Affordance> affordances) {
        super(components, affordances);
    }

    /**
     * Instantiates a new Simple link builder.
     *
     * @param components the components
     */
    SimpleLinkBuilder(UriComponents components) {
        super(components);
    }

    @Override
    protected SimpleLinkBuilder getThis() {
        return this;
    }

    @Override
    protected SimpleLinkBuilder createNewInstance(UriComponents components, List<Affordance> affordances) {
        return new SimpleLinkBuilder(components, affordances);
    }

    /**
     * Link to simple link builder.
     *
     * @param paths the paths
     * @return the simple link builder
     */
    public static SimpleLinkBuilder linkTo(String... paths) {
        ServletUriComponentsBuilder servletUriComponentsBuilder = ServletUriComponentsBuilder.fromCurrentContextPath();
        for (String path : paths) {
            servletUriComponentsBuilder.path(path);
        }
        UriComponents uriComponents = servletUriComponentsBuilder.build();
        return new SimpleLinkBuilder(uriComponents);
    }
}
