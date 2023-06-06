package com.eversis.esa.geoss.settings.common.hateoas;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

/**
 * The type Page mapper.
 */
@RequiredArgsConstructor
@Component
public class PageMapper {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;

    @Value("${spring.data.web.pageable.one-indexed-parameters:false}")
    private boolean oneIndexedParameters;

    /**
     * To collection model collection model.
     *
     * @param <T> the type parameter
     * @param collection the collection
     * @param entityLinks the entity links
     * @param collectionLinks the collection links
     * @return the collection model
     */
    public <T> CollectionModel<EntityModel<T>> toCollectionModel(Collection<T> collection,
            Function<T, List<Link>> entityLinks, Supplier<List<Link>> collectionLinks) {
        if (collection == null) {
            return CollectionModel.empty(collectionLinks.get());
        }
        List<EntityModel<T>> entityModels = collection
                .stream()
                .map(entity -> EntityModel.of(entity, entityLinks.apply(entity)))
                .toList();
        return CollectionModel.of(entityModels, collectionLinks.get());
    }

    /**
     * To paged model paged model.
     *
     * @param <T> the type parameter
     * @param page the page
     * @param type the type
     * @param entityLinks the entity links
     * @param collectionLinks the collection links
     * @return the paged model
     */
    public <T> PagedModel<EntityModel<T>> toPagedModel(Page<T> page, Class<T> type,
            Function<T, List<Link>> entityLinks, Supplier<List<Link>> collectionLinks) {
        PagedModel<EntityModel<T>> entityModels;
        if (page.getContent().isEmpty()) {
            entityModels = toEmptyModel(page, type, collectionLinks);
        } else {
            entityModels = toPagedModel(page, entityLinks, collectionLinks);
        }
        entityModels = addPaginationLinks(entityModels, page, Optional.empty());
        return entityModels;
    }

    /**
     * Page to paged model.
     *
     * @param <T> the type parameter
     * @param page the page
     * @param entityLinks the entity links
     * @param collectionLinks the collection links
     * @return the paged model
     */
    public <T> PagedModel<EntityModel<T>> toPagedModel(Page<T> page,
            Function<T, List<Link>> entityLinks, Supplier<List<Link>> collectionLinks) {
        if (page == null) {
            return PagedModel.empty(collectionLinks.get());
        }
        List<EntityModel<T>> entityModels = page.getContent()
                .stream()
                .map(entity -> EntityModel.of(entity, entityLinks.apply(entity)))
                .toList();
        return PagedModel.of(entityModels, asPageMetadata(page), collectionLinks.get());
    }

    /**
     * To empty model paged model.
     *
     * @param <T> the type parameter
     * @param page the page
     * @param type the type
     * @param collectionLinks the collection links
     * @return the paged model
     */
    public <T> PagedModel<EntityModel<T>> toEmptyModel(Page<T> page, Class<T> type,
            Supplier<List<Link>> collectionLinks) {
        List<EntityModel<T>> entityModels = Collections.singletonList(
                EntityModelEmptyCollectionEmbeddedWrapper.of(type));
        return PagedModel.of(entityModels, asPageMetadata(page), collectionLinks.get());
    }

    /**
     * Add pagination links to paged model.
     *
     * @param <R> the type parameter
     * @param resources the resources
     * @param page the page
     * @param link the link
     * @return the paged model
     */
    public <R> PagedModel<R> addPaginationLinks(PagedModel<R> resources, Page<?> page, Optional<Link> link) {
        UriTemplate base = getUriTemplate(link);
        boolean isNavigable = page.hasPrevious() || page.hasNext();
        if (isNavigable) {
            resources.add(createLink(base, PageRequest.of(0, page.getSize(), page.getSort()), IanaLinkRelations.FIRST));
        }
        if (page.hasPrevious()) {
            resources.add(createLink(base, page.previousPageable(), IanaLinkRelations.PREV));
        }
        Link selfLink = link.map(Link::withSelfRel)
                .orElseGet(() -> createLink(base, page.getPageable(), IanaLinkRelations.SELF));
        resources.add(selfLink);
        if (page.hasNext()) {
            resources.add(createLink(base, page.nextPageable(), IanaLinkRelations.NEXT));
        }
        if (isNavigable) {
            int lastIndex = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;
            resources.add(createLink(base, PageRequest.of(lastIndex, page.getSize(), page.getSort()),
                    IanaLinkRelations.LAST));
        }
        return resources;
    }

    private PageMetadata asPageMetadata(Page<?> page) {
        int number = oneIndexedParameters ? page.getNumber() + 1 : page.getNumber();

        return new PageMetadata(page.getSize(), number, page.getTotalElements(), page.getTotalPages());
    }

    private UriTemplate getUriTemplate(Optional<Link> baseLink) {
        return UriTemplate.of(baseLink.map(Link::getHref)
                .orElse(ServletUriComponentsBuilder.fromCurrentRequest().build().toString()));
    }

    private Link createLink(UriTemplate base, Pageable pageable, LinkRelation relation) {
        UriComponentsBuilder builder = fromUri(base.expand());
        pageableResolver.enhance(builder, null, pageable);
        return Link.of(UriTemplate.of(builder.build().toString()), relation);
    }

    private static class EntityModelEmptyCollectionEmbeddedWrapper<T> extends EntityModel<T> implements
            EmbeddedWrapper {

        private final Class<T> type;

        private EntityModelEmptyCollectionEmbeddedWrapper(Class<T> type) {
            this.type = type;
        }

        /**
         * Of entity model.
         *
         * @param <T> the type parameter
         * @param type the type
         * @return the entity model
         */
        public static <T> EntityModel<T> of(Class<T> type) {
            return new EntityModelEmptyCollectionEmbeddedWrapper<>(type);
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.core.EmbeddedWrapper#getRel()
         */
        @Override
        public Optional<LinkRelation> getRel() {
            return Optional.empty();
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.core.EmbeddedWrapper#getValue()
         */
        @Override
        public Object getValue() {
            return Collections.emptySet();
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.core.EmbeddedWrapper#getRelTargetType()
         */
        @Override
        public Class<?> getRelTargetType() {
            return type;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.core.EmbeddedWrapper#isCollectionValue()
         */
        @Override
        public boolean isCollectionValue() {
            return true;
        }

        /*
         * (non-Javadoc)
         * @see org.springframework.hateoas.core.EmbeddedWrapper#hasRel(org.springframework.hateoas.LinkRelation)
         */
        @Override
        public boolean hasRel(LinkRelation rel) {
            return false;
        }
    }
}
