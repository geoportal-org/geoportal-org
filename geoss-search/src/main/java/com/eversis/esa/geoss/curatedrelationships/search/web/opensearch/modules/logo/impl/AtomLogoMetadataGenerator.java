package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.impl;

import com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.modules.logo.AtomLogoModule;

import com.rometools.modules.atom.io.AtomModuleGenerator;
import com.rometools.modules.atom.modules.AtomLinkModule;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.ModuleGenerator;
import org.jdom2.Element;
import org.jdom2.Namespace;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AtomLogoMetadataGenerator implements ModuleGenerator {

    static final Namespace NS = Namespace.getNamespace("atom", AtomLinkModule.URI);
    private static final Set<Namespace> NAMESPACES;

    static {
        final Set<Namespace> nss = new HashSet<>();
        nss.add(NS);
        NAMESPACES = Collections.unmodifiableSet(nss);
    }

    @Override
    public String getNamespaceUri() {
        return AtomLogoModuleConstants.URI;
    }

    @Override
    public Set<Namespace> getNamespaces() {
        return NAMESPACES;
    }

    @Override
    public void generate(Module module, Element element) {
        if (module instanceof AtomLogoModule) {
            AtomLogoModule logoModule = (AtomLogoModule) module;
            Element logo = new Element("logo", NS);
            logo.setText(logoModule.getLink().getHref());
            logo.addNamespaceDeclaration(NS);
            element.addContent(logo);
        } else if (module instanceof AtomLinkModule) {
            AtomModuleGenerator atomModuleGenerator = new AtomModuleGenerator();
            atomModuleGenerator.generate(module, element);
        }
    }
}
