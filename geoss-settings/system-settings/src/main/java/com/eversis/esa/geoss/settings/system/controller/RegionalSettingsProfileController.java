package com.eversis.esa.geoss.settings.system.controller;

import com.eversis.esa.geoss.settings.system.domain.RegionalSettings;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.jakarta.JsonSchemaGenerator;
import com.fasterxml.jackson.module.jsonSchema.jakarta.types.ObjectSchema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.data.rest.webmvc.ProfileController;
import org.springframework.data.rest.webmvc.RestMediaTypes;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.PropertyUtils;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Descriptor;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Regional settings profile controller.
 */
@Log4j2
@AllArgsConstructor
@BasePathAwareController("/profile")
@ResponseBody
@Tag(name = "profile-controller")
public class RegionalSettingsProfileController {

    private final BaseUri baseUri;

    /**
     * Regional settings alps alps.
     *
     * @return the alps
     */
    @GetMapping(value = "/regional-settings", produces = {MediaType.ALL_VALUE, MediaTypes.ALPS_JSON_VALUE})
    Alps regionalSettingsAlps() {
        String regionalSettings = baseUri.getUriComponentsBuilder()
                .path(ProfileController.PROFILE_ROOT_MAPPING)
                .path("/regional-settings").toUriString();
        List<Descriptor> descriptors = List.of(
                Alps.descriptor()
                        .id("regionalSetting-representation")
                        .href(regionalSettings)
                        .descriptor(Stream
                                .concat(
                                        PropertyUtils
                                                .getExposedProperties(RegionalSettings.class)
                                                .stream()
                                                .map(property -> Alps.descriptor()
                                                        .name(property.getName())
                                                        .type(Type.SEMANTIC)
                                                        .build()),
                                        Stream.empty()
                                // TODO add method descriptions
                                // Stream.of(
                                //         Alps.descriptor()
                                //                 .id("cancel")
                                //                 .name("reservation")
                                //                 .type(Type.IDEMPOTENT)
                                //                 .rt("#reservation")
                                //                 .build())
                                )
                                .collect(Collectors.toList()))
                        .build()
        );
        return Alps.alps().descriptor(descriptors).build();
    }

    /**
     * Regional settings json scheme json schema.
     *
     * @return the json schema
     * @throws JsonMappingException the json mapping exception
     */
    @GetMapping(value = "/regional-settings", produces = RestMediaTypes.SCHEMA_JSON_VALUE)
    public JsonSchema regionalSettingsJsonScheme() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
        JsonSchema schema = schemaGen.generateSchema(RegionalSettings.class);
        if (schema.isObjectSchema()) {
            ObjectSchema objectSchema = schema.asObjectSchema();
            objectSchema.setId(null);
            objectSchema.setTitle(RegionalSettings.class.getSimpleName());
            objectSchema.set$schema("\"$schema\": \"http://json-schema.org/draft-04/schema#\"");
        }
        return schema;
    }
}
