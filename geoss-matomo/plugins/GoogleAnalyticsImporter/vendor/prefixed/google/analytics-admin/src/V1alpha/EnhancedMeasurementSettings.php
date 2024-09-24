<?php

# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/analytics/admin/v1alpha/resources.proto
namespace Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha;

use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\RepeatedField;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBUtil;
/**
 * Singleton resource under a WebDataStream, configuring measurement of
 * additional site interactions and content.
 *
 * Generated from protobuf message <code>google.analytics.admin.v1alpha.EnhancedMeasurementSettings</code>
 */
class EnhancedMeasurementSettings extends \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\Message
{
    /**
     * Output only. Resource name of this Data Stream.
     * Format:
     * properties/{property_id}/webDataStreams/{stream_id}/enhancedMeasurementSettings
     * Example: "properties/1000/webDataStreams/2000/enhancedMeasurementSettings"
     *
     * Generated from protobuf field <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     */
    private $name = '';
    /**
     * Indicates whether Enhanced Measurement Settings will be used to
     * automatically measure interactions and content on this web stream.
     * Changing this value does not affect the settings themselves, but determines
     * whether they are respected.
     *
     * Generated from protobuf field <code>bool stream_enabled = 2;</code>
     */
    private $stream_enabled = \false;
    /**
     * Output only. If enabled, capture a page view event each time a page loads or the
     * website changes the browser history state.
     *
     * Generated from protobuf field <code>bool page_views_enabled = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     */
    private $page_views_enabled = \false;
    /**
     * If enabled, capture scroll events each time a visitor gets to the bottom of
     * a page.
     *
     * Generated from protobuf field <code>bool scrolls_enabled = 4;</code>
     */
    private $scrolls_enabled = \false;
    /**
     * If enabled, capture an outbound click event each time a visitor clicks a
     * link that leads them away from your domain.
     *
     * Generated from protobuf field <code>bool outbound_clicks_enabled = 5;</code>
     */
    private $outbound_clicks_enabled = \false;
    /**
     * If enabled, capture a view search results event each time a visitor
     * performs a search on your site (based on a query parameter).
     *
     * Generated from protobuf field <code>bool site_search_enabled = 7;</code>
     */
    private $site_search_enabled = \false;
    /**
     * If enabled, capture video play, progress, and complete events as visitors
     * view embedded videos on your site.
     *
     * Generated from protobuf field <code>bool video_engagement_enabled = 9;</code>
     */
    private $video_engagement_enabled = \false;
    /**
     * If enabled, capture a file download event each time a link is clicked with
     * a common document, compressed file, application, video, or audio extension.
     *
     * Generated from protobuf field <code>bool file_downloads_enabled = 10;</code>
     */
    private $file_downloads_enabled = \false;
    /**
     * Output only. If enabled, capture a page view event each time a page loads.
     *
     * Generated from protobuf field <code>bool page_loads_enabled = 12 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     */
    private $page_loads_enabled = \false;
    /**
     * If enabled, capture a page view event each time the website changes the
     * browser history state.
     *
     * Generated from protobuf field <code>bool page_changes_enabled = 13;</code>
     */
    private $page_changes_enabled = \false;
    /**
     * Required. URL query parameters to interpret as site search parameters.
     * Max length is 1024 characters. Must not be empty.
     *
     * Generated from protobuf field <code>string search_query_parameter = 16 [(.google.api.field_behavior) = REQUIRED];</code>
     */
    private $search_query_parameter = '';
    /**
     * Additional URL query parameters.
     * Max length is 1024 characters.
     *
     * Generated from protobuf field <code>string uri_query_parameter = 17;</code>
     */
    private $uri_query_parameter = '';
    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type string $name
     *           Output only. Resource name of this Data Stream.
     *           Format:
     *           properties/{property_id}/webDataStreams/{stream_id}/enhancedMeasurementSettings
     *           Example: "properties/1000/webDataStreams/2000/enhancedMeasurementSettings"
     *     @type bool $stream_enabled
     *           Indicates whether Enhanced Measurement Settings will be used to
     *           automatically measure interactions and content on this web stream.
     *           Changing this value does not affect the settings themselves, but determines
     *           whether they are respected.
     *     @type bool $page_views_enabled
     *           Output only. If enabled, capture a page view event each time a page loads or the
     *           website changes the browser history state.
     *     @type bool $scrolls_enabled
     *           If enabled, capture scroll events each time a visitor gets to the bottom of
     *           a page.
     *     @type bool $outbound_clicks_enabled
     *           If enabled, capture an outbound click event each time a visitor clicks a
     *           link that leads them away from your domain.
     *     @type bool $site_search_enabled
     *           If enabled, capture a view search results event each time a visitor
     *           performs a search on your site (based on a query parameter).
     *     @type bool $video_engagement_enabled
     *           If enabled, capture video play, progress, and complete events as visitors
     *           view embedded videos on your site.
     *     @type bool $file_downloads_enabled
     *           If enabled, capture a file download event each time a link is clicked with
     *           a common document, compressed file, application, video, or audio extension.
     *     @type bool $page_loads_enabled
     *           Output only. If enabled, capture a page view event each time a page loads.
     *     @type bool $page_changes_enabled
     *           If enabled, capture a page view event each time the website changes the
     *           browser history state.
     *     @type string $search_query_parameter
     *           Required. URL query parameters to interpret as site search parameters.
     *           Max length is 1024 characters. Must not be empty.
     *     @type string $uri_query_parameter
     *           Additional URL query parameters.
     *           Max length is 1024 characters.
     * }
     */
    public function __construct($data = NULL)
    {
        \Matomo\Dependencies\GoogleAnalyticsImporter\GPBMetadata\Google\Analytics\Admin\V1Alpha\Resources::initOnce();
        parent::__construct($data);
    }
    /**
     * Output only. Resource name of this Data Stream.
     * Format:
     * properties/{property_id}/webDataStreams/{stream_id}/enhancedMeasurementSettings
     * Example: "properties/1000/webDataStreams/2000/enhancedMeasurementSettings"
     *
     * Generated from protobuf field <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @return string
     */
    public function getName()
    {
        return $this->name;
    }
    /**
     * Output only. Resource name of this Data Stream.
     * Format:
     * properties/{property_id}/webDataStreams/{stream_id}/enhancedMeasurementSettings
     * Example: "properties/1000/webDataStreams/2000/enhancedMeasurementSettings"
     *
     * Generated from protobuf field <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @param string $var
     * @return $this
     */
    public function setName($var)
    {
        GPBUtil::checkString($var, True);
        $this->name = $var;
        return $this;
    }
    /**
     * Indicates whether Enhanced Measurement Settings will be used to
     * automatically measure interactions and content on this web stream.
     * Changing this value does not affect the settings themselves, but determines
     * whether they are respected.
     *
     * Generated from protobuf field <code>bool stream_enabled = 2;</code>
     * @return bool
     */
    public function getStreamEnabled()
    {
        return $this->stream_enabled;
    }
    /**
     * Indicates whether Enhanced Measurement Settings will be used to
     * automatically measure interactions and content on this web stream.
     * Changing this value does not affect the settings themselves, but determines
     * whether they are respected.
     *
     * Generated from protobuf field <code>bool stream_enabled = 2;</code>
     * @param bool $var
     * @return $this
     */
    public function setStreamEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->stream_enabled = $var;
        return $this;
    }
    /**
     * Output only. If enabled, capture a page view event each time a page loads or the
     * website changes the browser history state.
     *
     * Generated from protobuf field <code>bool page_views_enabled = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @return bool
     */
    public function getPageViewsEnabled()
    {
        return $this->page_views_enabled;
    }
    /**
     * Output only. If enabled, capture a page view event each time a page loads or the
     * website changes the browser history state.
     *
     * Generated from protobuf field <code>bool page_views_enabled = 3 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @param bool $var
     * @return $this
     */
    public function setPageViewsEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->page_views_enabled = $var;
        return $this;
    }
    /**
     * If enabled, capture scroll events each time a visitor gets to the bottom of
     * a page.
     *
     * Generated from protobuf field <code>bool scrolls_enabled = 4;</code>
     * @return bool
     */
    public function getScrollsEnabled()
    {
        return $this->scrolls_enabled;
    }
    /**
     * If enabled, capture scroll events each time a visitor gets to the bottom of
     * a page.
     *
     * Generated from protobuf field <code>bool scrolls_enabled = 4;</code>
     * @param bool $var
     * @return $this
     */
    public function setScrollsEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->scrolls_enabled = $var;
        return $this;
    }
    /**
     * If enabled, capture an outbound click event each time a visitor clicks a
     * link that leads them away from your domain.
     *
     * Generated from protobuf field <code>bool outbound_clicks_enabled = 5;</code>
     * @return bool
     */
    public function getOutboundClicksEnabled()
    {
        return $this->outbound_clicks_enabled;
    }
    /**
     * If enabled, capture an outbound click event each time a visitor clicks a
     * link that leads them away from your domain.
     *
     * Generated from protobuf field <code>bool outbound_clicks_enabled = 5;</code>
     * @param bool $var
     * @return $this
     */
    public function setOutboundClicksEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->outbound_clicks_enabled = $var;
        return $this;
    }
    /**
     * If enabled, capture a view search results event each time a visitor
     * performs a search on your site (based on a query parameter).
     *
     * Generated from protobuf field <code>bool site_search_enabled = 7;</code>
     * @return bool
     */
    public function getSiteSearchEnabled()
    {
        return $this->site_search_enabled;
    }
    /**
     * If enabled, capture a view search results event each time a visitor
     * performs a search on your site (based on a query parameter).
     *
     * Generated from protobuf field <code>bool site_search_enabled = 7;</code>
     * @param bool $var
     * @return $this
     */
    public function setSiteSearchEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->site_search_enabled = $var;
        return $this;
    }
    /**
     * If enabled, capture video play, progress, and complete events as visitors
     * view embedded videos on your site.
     *
     * Generated from protobuf field <code>bool video_engagement_enabled = 9;</code>
     * @return bool
     */
    public function getVideoEngagementEnabled()
    {
        return $this->video_engagement_enabled;
    }
    /**
     * If enabled, capture video play, progress, and complete events as visitors
     * view embedded videos on your site.
     *
     * Generated from protobuf field <code>bool video_engagement_enabled = 9;</code>
     * @param bool $var
     * @return $this
     */
    public function setVideoEngagementEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->video_engagement_enabled = $var;
        return $this;
    }
    /**
     * If enabled, capture a file download event each time a link is clicked with
     * a common document, compressed file, application, video, or audio extension.
     *
     * Generated from protobuf field <code>bool file_downloads_enabled = 10;</code>
     * @return bool
     */
    public function getFileDownloadsEnabled()
    {
        return $this->file_downloads_enabled;
    }
    /**
     * If enabled, capture a file download event each time a link is clicked with
     * a common document, compressed file, application, video, or audio extension.
     *
     * Generated from protobuf field <code>bool file_downloads_enabled = 10;</code>
     * @param bool $var
     * @return $this
     */
    public function setFileDownloadsEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->file_downloads_enabled = $var;
        return $this;
    }
    /**
     * Output only. If enabled, capture a page view event each time a page loads.
     *
     * Generated from protobuf field <code>bool page_loads_enabled = 12 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @return bool
     */
    public function getPageLoadsEnabled()
    {
        return $this->page_loads_enabled;
    }
    /**
     * Output only. If enabled, capture a page view event each time a page loads.
     *
     * Generated from protobuf field <code>bool page_loads_enabled = 12 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @param bool $var
     * @return $this
     */
    public function setPageLoadsEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->page_loads_enabled = $var;
        return $this;
    }
    /**
     * If enabled, capture a page view event each time the website changes the
     * browser history state.
     *
     * Generated from protobuf field <code>bool page_changes_enabled = 13;</code>
     * @return bool
     */
    public function getPageChangesEnabled()
    {
        return $this->page_changes_enabled;
    }
    /**
     * If enabled, capture a page view event each time the website changes the
     * browser history state.
     *
     * Generated from protobuf field <code>bool page_changes_enabled = 13;</code>
     * @param bool $var
     * @return $this
     */
    public function setPageChangesEnabled($var)
    {
        GPBUtil::checkBool($var);
        $this->page_changes_enabled = $var;
        return $this;
    }
    /**
     * Required. URL query parameters to interpret as site search parameters.
     * Max length is 1024 characters. Must not be empty.
     *
     * Generated from protobuf field <code>string search_query_parameter = 16 [(.google.api.field_behavior) = REQUIRED];</code>
     * @return string
     */
    public function getSearchQueryParameter()
    {
        return $this->search_query_parameter;
    }
    /**
     * Required. URL query parameters to interpret as site search parameters.
     * Max length is 1024 characters. Must not be empty.
     *
     * Generated from protobuf field <code>string search_query_parameter = 16 [(.google.api.field_behavior) = REQUIRED];</code>
     * @param string $var
     * @return $this
     */
    public function setSearchQueryParameter($var)
    {
        GPBUtil::checkString($var, True);
        $this->search_query_parameter = $var;
        return $this;
    }
    /**
     * Additional URL query parameters.
     * Max length is 1024 characters.
     *
     * Generated from protobuf field <code>string uri_query_parameter = 17;</code>
     * @return string
     */
    public function getUriQueryParameter()
    {
        return $this->uri_query_parameter;
    }
    /**
     * Additional URL query parameters.
     * Max length is 1024 characters.
     *
     * Generated from protobuf field <code>string uri_query_parameter = 17;</code>
     * @param string $var
     * @return $this
     */
    public function setUriQueryParameter($var)
    {
        GPBUtil::checkString($var, True);
        $this->uri_query_parameter = $var;
        return $this;
    }
}
