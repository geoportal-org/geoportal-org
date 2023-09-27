package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.processor.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.TransferOption;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticle;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticleEntry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.util.StringBuilderUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.validation.constraints.NotNull;

/**
 * The type Wiki article entry factory.
 */
@Component
public class WikiArticleEntryFactory {

    private static final int ARTICLE_TITLE_MAX_LENGTH = 200;
    private static final int ARTICLE_SUMMARY_MAX_LENGTH = 6000;
    private static final int ARTICLE_IMAGE_URL_MAX_LENGTH = 800;
    private static final int ARTICLE_KEYWORDS_MAX_LENGTH = 2000;
    private static final int ARTICLE_TAGS_MAX_LENGTH = 2000;
    @Value("${geoss.curated.wikipedia.code-prefix}")
    private String articleEntryCodePrefix;
    @Value("${geoss.curated.wikipedia.keywords-delimiter}")
    private String keywordsDelimiter;

    /**
     * Creates database entry object, which corresponds to wikipedia article object. It does not save data to database,
     * it only creates pre-formatted data object.
     *
     * @param wikiArticle article's data
     * @param typeId foreign key value
     * @param definitionTypeId foreign key value
     * @param accessPolicyId foreign key value
     * @param sourceId foreign key value
     * @param dataSourceId foreign key value
     * @param protocolId foreign key value
     * @return the wiki article entry
     */
    public WikiArticleEntry createWikiArticleEntry(WikiArticle wikiArticle,
            Integer typeId, Integer definitionTypeId, Integer accessPolicyId, Integer sourceId, Integer dataSourceId,
            Integer protocolId) {
        return new WikiArticleEntry(
                getWikiArticleTitle(wikiArticle.getTitle(), ARTICLE_TITLE_MAX_LENGTH),
                getWikiArticleSummary(wikiArticle.getDescription(), ARTICLE_SUMMARY_MAX_LENGTH),
                getWikiArticleImageUrl(wikiArticle.getImageUrl(), ARTICLE_IMAGE_URL_MAX_LENGTH),
                getWikiArticleKeywords(wikiArticle.getCategories(), ARTICLE_KEYWORDS_MAX_LENGTH),
                getWikiArticleKeywords(wikiArticle.getCategories(), ARTICLE_TAGS_MAX_LENGTH).toLowerCase(),
                typeId,
                definitionTypeId,
                accessPolicyId,
                getWikiArticleEntryCode(wikiArticle.getPageId()),
                sourceId,
                dataSourceId,
                new TransferOption(protocolId.toString(), wikiArticle.getUrl())
        );

    }

    private String getWikiArticleEntryCode(String pageId) {
        return articleEntryCodePrefix + pageId;
    }

    private String getWikiArticleTitle(String title, int maxLength) {
        if (!StringUtils.hasLength(title)) {
            return title;
        }
        return title.substring(0, Math.min(title.length(), maxLength));
    }

    private String getWikiArticleSummary(String summary, int maxLength) {
        if (!StringUtils.hasLength(summary)) {
            return summary;
        }
        return summary.substring(0, Math.min(summary.length(), maxLength));
    }

    private String getWikiArticleImageUrl(String imageUrl, int maxLength) {
        if (!StringUtils.hasLength(imageUrl)) {
            return null;
        }
        if (imageUrl.length() > maxLength) {
            return null;
        }
        return imageUrl;
    }

    private String getWikiArticleKeywords(@NotNull Iterable<String> keywords, int maxLength) {
        return StringBuilderUtil.joinElementsUnlessMaxLength(keywords, keywordsDelimiter, maxLength);
    }

}
