package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query.VocabularyServerQueryFactory;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.earth.config.EarthThesaurusProperties;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.ThesaurusType;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * The type Earth thesaurus service integration tests.
 */
public class EarthThesaurusServiceIntegrationTests {

    private MockWebServer server;
    private EarthThesaurusService earthThesaurusService;

    /**
     * Before each.
     */
    @Before
    public void beforeEach() {
        server = new MockWebServer();
        WebClient webClient = WebClient.create();
        VocabularyServerQueryFactory queryFactory = new VocabularyServerQueryFactory();
        EarthThesaurusProperties configuration = new EarthThesaurusProperties();
        configuration.setBaseUri(server.url("/").toString());
        earthThesaurusService = new EarthThesaurusService(webClient, queryFactory, configuration);
    }

    /**
     * After each.
     *
     * @throws IOException the io exception
     */
    @After
    public void afterEach() throws IOException {
        server.shutdown();
    }

    /**
     * When fetching top terms then all results are parsed.
     *
     * @throws IOException the io exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void whenFetchingTopTerms_thenAllResultsAreParsed() throws IOException, InterruptedException {
        String fetchTopTermsBody = new String(
                Files.readAllBytes(Paths.get("src/integration-test/resources/earth/fetch_top_terms_body.xml")),
                StandardCharsets.UTF_8);
        prepareResponse(fetchTopTermsMockResponse -> fetchTopTermsMockResponse
                .setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                .setBody(fetchTopTermsBody));

        StepVerifier.create(earthThesaurusService.fetchTopTerms())
                .expectSubscription()
                .expectNextCount(5)
                .expectComplete()
                .verify();

        RecordedRequest request = server.takeRequest();
        assertEquals("/?task=fetchTopTerms", request.getPath());
    }

    /**
     * When fetching concept by term id then concept is returned.
     *
     * @throws IOException the io exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void whenFetchingConceptByTermId_thenConceptIsReturned() throws IOException, InterruptedException {
        String termId = "94730";
        String fetchTermBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/earth/fetch_term_94730_body.xml")), StandardCharsets.UTF_8);
        String fetchRelatedTermsBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/earth/fetch_related_terms_94730_body.xml")),
                StandardCharsets.UTF_8);
        String fetchBroaderTermsBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/earth/fetch_broader_terms_94730_body.xml")),
                StandardCharsets.UTF_8);
        String fetchNarrowerTermsBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/earth/fetch_narrower_terms_94730_body.xml")),
                StandardCharsets.UTF_8);
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                switch (request.getPath()) {
                    case "/?task=fetchTerm&arg=94730":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchTermBody);
                    case "/?task=fetchRelated&arg=94730":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchRelatedTermsBody);
                    case "/?task=fetchDown&arg=94730":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchNarrowerTermsBody);
                    case "/?task=fetchUp&arg=94730":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchBroaderTermsBody);
                    default:
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);

        ConceptDto expectedConcept = new ConceptDto(
                ThesaurusType.EARTH,
                "anomaly",
                termId,
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                Arrays.asList(
                        new TermDto("140", "ACCESSORY TERMS", true),
                        new TermDto("270", "GENERAL TERMS", false)
                ));
        StepVerifier.create(earthThesaurusService.fetchConcept(termId))
                .expectNext(expectedConcept)
                .expectComplete()
                .verify();
    }

    private void prepareResponse(Consumer<MockResponse> consumer) {
        MockResponse response = new MockResponse();
        consumer.accept(response);
        this.server.enqueue(response);
    }

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String TEXT_XML_UTF8_CONTENT_TYPE = "text/xml;utf-8;charset=utf-8";

}
