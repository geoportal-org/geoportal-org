package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper.ElementFactory;
import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.datamodel.DataModelModule;

import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.ModuleGenerator;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DataModelModuleGenerator implements ModuleGenerator {

    static final Namespace NS = Namespace.getNamespace("dm", DataModelModuleConstants.URI);
    private static final Set<Namespace> NAMESPACES;

    static {
        final Set<Namespace> nss = new HashSet<>();
        nss.add(NS);
        NAMESPACES = Collections.unmodifiableSet(nss);
    }

    @Override
    public String getNamespaceUri() {
        return DataModelModuleConstants.URI;
    }

    @Override
    public Set<Namespace> getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public void generate(Module module, Element element) {
        if (module instanceof DataModelModule) {
            DataModelModule dataModelModule = (DataModelModule) module;
            Element root = element;
            Element termFrequency = new Element("termFrequency", NS);
            termFrequency.addNamespaceDeclaration(NS);
            for (Map.Entry<String, List<Facet>> facetEntry : dataModelModule.getFacets().entrySet()) {
                Element facetContainer = new Element(facetEntry.getKey(), NS);
                for (Facet facetItem : facetEntry.getValue()) {
                    Element item = new Element("item", NS);

                    Element freq = ElementFactory.createElement("freq", String.valueOf(facetItem.getCount()), NS);
                    item.addContent(freq);

                    try {
                        String encodedTerm = URLEncoder.encode(facetItem.getDisplayName(), StandardCharsets.UTF_8.name());
                        Element termElement = ElementFactory.createElement("term", encodedTerm, NS);
                        item.addContent(termElement);
                    } catch (UnsupportedEncodingException e) {
                        log.warn("Unable to encode term due to unsupported encoding. Reason: {}", e.getMessage());
                    }

                    Element decodedTermElement = ElementFactory.createElement("decodedTerm", facetItem.getDisplayName(), NS);
                    item.addContent(decodedTermElement);

                    if (facetItem.containsIdentifier()) {
                        Element idElement = ElementFactory.createElement("id", facetItem.getTermId(), NS);
                        item.addContent(idElement);
                    }

                    facetContainer.addContent(item);
                }

                facetContainer.addNamespaceDeclaration(NS);
                termFrequency.addContent(facetContainer);
            }
            root.addContent(termFrequency);
        }

    }
}
