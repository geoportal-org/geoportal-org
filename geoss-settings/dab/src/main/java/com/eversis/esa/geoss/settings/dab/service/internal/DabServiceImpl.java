package com.eversis.esa.geoss.settings.dab.service.internal;

import com.eversis.esa.geoss.settings.common.properties.Constants;
import com.eversis.esa.geoss.settings.dab.properties.DabProperties;
import com.eversis.esa.geoss.settings.dab.service.DabService;
import com.eversis.esa.geoss.settings.instance.domain.Catalog;
import com.eversis.esa.geoss.settings.instance.repository.CatalogRepository;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet;
import com.eversis.esa.geoss.settings.system.domain.ApiSettingsSet.Dab;
import com.eversis.esa.geoss.settings.system.repository.ApiSettingsRepository;

import com.rometools.modules.opensearch.OpenSearchModule;
import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Feed;
import com.rometools.rome.feed.module.Module;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.XmlReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The type Dab service.
 */
@Log4j2
@RequiredArgsConstructor
@Service
@Transactional
public class DabServiceImpl implements DabService {

    private final DabProperties dabProperties;

    private final ApiSettingsRepository apiSettingsRepository;

    private final CatalogRepository catalogRepository;

    @Override
    public Page<Catalog> getCatalogs(Pageable pageable) {
        log.debug("pageable:{},offset:{}", pageable, pageable.getOffset());
        String url = getDabCatalogUrl(pageable);
        log.debug("url:{}", url);
        try {
            InputStream inputStream = httpCall(url);
            Page<Catalog> catalogs = readFeed(inputStream, pageable);
            log.debug("catalogs:{}", catalogs);
            return catalogs;
        } catch (ExecutionException | InterruptedException | TimeoutException | IOException | FeedException e) {
            throw new DataAccessResourceFailureException(e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void syncCatalogs() {
        syncCatalogsFromDab();
    }

    @Scheduled(cron = "${geoss.settings.dab.catalog-sync-cron-expression}")
    @SchedulerLock(name = "syncCatalogsFromDab")
    void syncCatalogsFromDab() {
        try {
            String url = getDabCatalogUrl(null);
            log.debug("url:{}", url);
            InputStream inputStream = httpCall(url);
            List<Catalog> catalogs = readFeed(inputStream);
            catalogs = catalogRepository.saveAll(catalogs);
            log.debug("Sync catalogs:{}", catalogs.size());
        } catch (IllegalStateException e) {
            log.warn("Sync disabled. " + e.getMessage());
        } catch (ExecutionException | InterruptedException | TimeoutException | IOException | FeedException e) {
            throw new DataAccessResourceFailureException(e.getMessage(), e);
        }
    }

    private String getDabCatalogUrl(Pageable pageable) {
        return apiSettingsRepository.findBySiteIdAndSetAndKey(Constants.DEFAULT_SITE_ID, ApiSettingsSet.DAB,
                        Dab.DAB_BASE_URL)
                .map(apiSettings -> {
                    String url = apiSettings.getValue() + dabProperties.getCatalogEndpoint();
                    if (pageable != null) {
                        url += "&si=" + pageable.getOffset();
                        url += "&ct=" + pageable.getPageSize();
                    }
                    return url;
                })
                .orElseThrow(() -> new IllegalStateException("NO DAB API configuration."));
    }

    private InputStream httpCall(String url) throws ExecutionException, InterruptedException, TimeoutException {
        HttpClient client = HttpClient.newBuilder()
                .version(Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(dabProperties.getConnectTimeout()))
                .build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .version(HttpClient.Version.HTTP_2)
                .timeout(Duration.ofSeconds(dabProperties.getRequestTimeout()))
                .GET()
                .build();
        return client.sendAsync(request, BodyHandlers.ofInputStream())
                .thenApply(response -> {
                    String sb = response.request().method() + " "
                                + response.uri() + " "
                                + response.version() + " "
                                + response.statusCode();
                    log.debug(sb);
                    return response;
                })
                .thenApply(HttpResponse::body)
                .get(dabProperties.getResponseTimeout(), TimeUnit.SECONDS);
    }

    private Page<Catalog> readFeed(InputStream inputStream, Pageable pageable) throws IOException, FeedException {
        WireFeedInput wireFeedInput = new WireFeedInput();
        WireFeed wireFeed = wireFeedInput.build(new XmlReader(inputStream));
        if (wireFeed instanceof Feed feed) {
            log.debug("feedType:{},title:{},id:{}", feed.getFeedType(), feed.getTitle(), feed.getId());

            List<Entry> entries = feed.getEntries();
            int total = entries.size();

            Module module = feed.getModule(OpenSearchModule.URI);
            if (module instanceof OpenSearchModule openSearchModule) {
                total = openSearchModule.getTotalResults();
                int size = entries.size();
                if (size == total && size > pageable.getPageSize()) {
                    int start = Math.min((int) pageable.getOffset(), size);
                    int end = Math.min(start + pageable.getPageSize(), size);
                    log.debug("start:{},end:{},size:{},total:{}", start, end, size, total);
                    entries = entries.subList(start, end);
                }
            }

            List<Catalog> catalogs = entries.stream().map(this::toCatalog).toList();
            catalogs = catalogRepository.saveAll(catalogs);

            return new PageImpl<>(catalogs, pageable, total);
        } else {
            log.warn("Invalid wireFeed type: " + wireFeed.getClass());
        }
        return Page.empty(pageable);
    }

    private List<Catalog> readFeed(InputStream inputStream) throws IOException, FeedException {
        WireFeedInput wireFeedInput = new WireFeedInput();
        WireFeed wireFeed = wireFeedInput.build(new XmlReader(inputStream));
        if (wireFeed instanceof Feed feed) {
            log.debug("feedType:{},title:{},id:{}", feed.getFeedType(), feed.getTitle(), feed.getId());

            List<Entry> entries = feed.getEntries();

            return entries.stream().map(this::toCatalog).toList();
        } else {
            log.warn("Invalid wireFeed type: " + wireFeed.getClass());
        }
        return Collections.emptyList();
    }

    private Catalog toCatalog(Entry entry) {
        String id = entry.getId();
        Catalog catalog = catalogRepository.findByDabId(id).orElse(new Catalog());
        catalog.setDabId(id);
        catalog.setTitle(entry.getTitle());
        entry.getCategories().stream().findFirst().ifPresent(category -> {
            catalog.setLabel(category.getLabel());
            catalog.setValue(category.getTerm());
        });
        return catalog;
    }
}
