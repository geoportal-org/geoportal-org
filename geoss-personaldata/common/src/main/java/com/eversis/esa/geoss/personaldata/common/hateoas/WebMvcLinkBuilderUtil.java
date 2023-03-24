package com.eversis.esa.geoss.personaldata.common.hateoas;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

/**
 * The type Web mvc link builder util.
 */
public class WebMvcLinkBuilderUtil {

    /**
     * Uri with base path string.
     *
     * @param webMvcLinkBuilder the web mvc link builder
     * @param basePath the base path
     * @return the string
     */
    public static String uriWithBasePath(WebMvcLinkBuilder webMvcLinkBuilder, String basePath) {
        var originalUri = webMvcLinkBuilder.toUri();
        var uriComponentsBuilder = webMvcLinkBuilder.toUriComponentsBuilder();
        uriComponentsBuilder = uriComponentsBuilder.replacePath(basePath + originalUri.getPath());
        var uri = uriComponentsBuilder.toUriString();
        return uri;
    }

}
