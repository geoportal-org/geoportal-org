package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.service;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.ConceptDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.model.TermDto;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query.VocabularyServerQueryFactory;
import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.eosterm.config.EostermThesaurusProperties;
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
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;

/**
 * The type Eosterm thesaurus service integration tests.
 */
public class EostermThesaurusServiceIntegrationTests {

    private MockWebServer server;
    private EostermThesaurusService eostermThesaurusService;

    /**
     * Before each.
     */
    @Before
    public void beforeEach() {
        server = new MockWebServer();
        WebClient webClient = WebClient.create();
        VocabularyServerQueryFactory queryFactory = new VocabularyServerQueryFactory();
        EostermThesaurusProperties configuration = new EostermThesaurusProperties();
        configuration.setBaseUri(server.url("/").toString());
        eostermThesaurusService = new EostermThesaurusService(webClient, queryFactory, configuration);
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
                Files.readAllBytes(Paths.get("src/integration-test/resources/eosterm/fetch_top_terms_body.xml")),
                StandardCharsets.UTF_8);
        prepareResponse(fetchTopTermsMockResponse -> fetchTopTermsMockResponse
                .setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                .setBody(fetchTopTermsBody));

        StepVerifier.create(eostermThesaurusService.fetchTopTerms())
                .expectSubscription()
                .expectNextCount(4)
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
        String termId = "105120";
        String fetchTermBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/eosterm/fetch_term_105120_body.xml")),
                StandardCharsets.UTF_8);
        String fetchRelatedTermsBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/eosterm/fetch_related_terms_105120_body.xml")),
                StandardCharsets.UTF_8);
        String fetchBroaderTermsBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/eosterm/fetch_broader_terms_105120_body.xml")),
                StandardCharsets.UTF_8);
        String fetchNarrowerTermsBody = new String(Files.readAllBytes(
                Paths.get("src/integration-test/resources/eosterm/fetch_narrower_terms_105120_body.xml")),
                StandardCharsets.UTF_8);
        final Dispatcher dispatcher = new Dispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                switch (request.getPath()) {
                    case "/?task=fetchTerm&arg=105120":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchTermBody);
                    case "/?task=fetchRelated&arg=105120":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchRelatedTermsBody);
                    case "/?task=fetchDown&arg=105120":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchNarrowerTermsBody);
                    case "/?task=fetchUp&arg=105120":
                        return new MockResponse().setHeader(CONTENT_TYPE, TEXT_XML_UTF8_CONTENT_TYPE)
                                .setBody(fetchBroaderTermsBody);
                    default:
                }
                return new MockResponse().setResponseCode(404);
            }
        };
        server.setDispatcher(dispatcher);

        ConceptDto expectedConcept = new ConceptDto(
                ThesaurusType.EOSTERM,
                "color",
                termId,
                false,
                Arrays.asList(
                        new TermDto("113040", "color balance", false),
                        new TermDto("113060", "color temperature", false)
                ),
                Arrays.asList(
                        new TermDto("91630", "additive colour", false),
                        new TermDto("113080", "complementary colors", false),
                        new TermDto("115610", "primary colors", false),
                        new TermDto("89860", "subtractive primary colors", false),
                        new TermDto("104360", "water colour", false)
                ),
                Arrays.asList(
                        new TermDto("1010", "ATTRIBUTES", true),
                        new TermDto("1040", "PROPERTIES", true),
                        new TermDto("117120", "physical properties", false)
                ));
        StepVerifier.create(eostermThesaurusService.fetchConcept(termId))
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
