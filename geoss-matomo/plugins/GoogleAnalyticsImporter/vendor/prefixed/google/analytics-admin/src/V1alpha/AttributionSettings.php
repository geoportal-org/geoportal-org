<?php

# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/analytics/admin/v1alpha/resources.proto
namespace Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha;

use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\RepeatedField;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBUtil;
/**
 * The attribution settings used for a given property. This is a singleton
 * resource.
 *
 * Generated from protobuf message <code>google.analytics.admin.v1alpha.AttributionSettings</code>
 */
class AttributionSettings extends \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\Message
{
    /**
     * Output only. Resource name of this attribution settings resource.
     * Format: properties/{property_id}/attributionSettings
     * Example: "properties/1000/attributionSettings"
     *
     * Generated from protobuf field <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     */
    private $name = '';
    /**
     * Required. The lookback window configuration for acquisition conversion
     * events. The default window size is 30 days.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.AcquisitionConversionEventLookbackWindow acquisition_conversion_event_lookback_window = 2 [(.google.api.field_behavior) = REQUIRED];</code>
     */
    private $acquisition_conversion_event_lookback_window = 0;
    /**
     * Required. The lookback window for all other, non-acquisition conversion
     * events. The default window size is 90 days.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.OtherConversionEventLookbackWindow other_conversion_event_lookback_window = 3 [(.google.api.field_behavior) = REQUIRED];</code>
     */
    private $other_conversion_event_lookback_window = 0;
    /**
     * Required. The reporting attribution model used to calculate conversion
     * credit in this property's reports.
     * Changing the attribution model will apply to both historical and future
     * data. These changes will be reflected in reports with conversion and
     * revenue data. User and session data will be unaffected.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.ReportingAttributionModel reporting_attribution_model = 4 [(.google.api.field_behavior) = REQUIRED];</code>
     */
    private $reporting_attribution_model = 0;
    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type string $name
     *           Output only. Resource name of this attribution settings resource.
     *           Format: properties/{property_id}/attributionSettings
     *           Example: "properties/1000/attributionSettings"
     *     @type int $acquisition_conversion_event_lookback_window
     *           Required. The lookback window configuration for acquisition conversion
     *           events. The default window size is 30 days.
     *     @type int $other_conversion_event_lookback_window
     *           Required. The lookback window for all other, non-acquisition conversion
     *           events. The default window size is 90 days.
     *     @type int $reporting_attribution_model
     *           Required. The reporting attribution model used to calculate conversion
     *           credit in this property's reports.
     *           Changing the attribution model will apply to both historical and future
     *           data. These changes will be reflected in reports with conversion and
     *           revenue data. User and session data will be unaffected.
     * }
     */
    public function __construct($data = NULL)
    {
        \Matomo\Dependencies\GoogleAnalyticsImporter\GPBMetadata\Google\Analytics\Admin\V1Alpha\Resources::initOnce();
        parent::__construct($data);
    }
    /**
     * Output only. Resource name of this attribution settings resource.
     * Format: properties/{property_id}/attributionSettings
     * Example: "properties/1000/attributionSettings"
     *
     * Generated from protobuf field <code>string name = 1 [(.google.api.field_behavior) = OUTPUT_ONLY];</code>
     * @return string
     */
    public function getName()
    {
        return $this->name;
    }
    /**
     * Output only. Resource name of this attribution settings resource.
     * Format: properties/{property_id}/attributionSettings
     * Example: "properties/1000/attributionSettings"
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
     * Required. The lookback window configuration for acquisition conversion
     * events. The default window size is 30 days.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.AcquisitionConversionEventLookbackWindow acquisition_conversion_event_lookback_window = 2 [(.google.api.field_behavior) = REQUIRED];</code>
     * @return int
     */
    public function getAcquisitionConversionEventLookbackWindow()
    {
        return $this->acquisition_conversion_event_lookback_window;
    }
    /**
     * Required. The lookback window configuration for acquisition conversion
     * events. The default window size is 30 days.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.AcquisitionConversionEventLookbackWindow acquisition_conversion_event_lookback_window = 2 [(.google.api.field_behavior) = REQUIRED];</code>
     * @param int $var
     * @return $this
     */
    public function setAcquisitionConversionEventLookbackWindow($var)
    {
        GPBUtil::checkEnum($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha\AttributionSettings\AcquisitionConversionEventLookbackWindow::class);
        $this->acquisition_conversion_event_lookback_window = $var;
        return $this;
    }
    /**
     * Required. The lookback window for all other, non-acquisition conversion
     * events. The default window size is 90 days.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.OtherConversionEventLookbackWindow other_conversion_event_lookback_window = 3 [(.google.api.field_behavior) = REQUIRED];</code>
     * @return int
     */
    public function getOtherConversionEventLookbackWindow()
    {
        return $this->other_conversion_event_lookback_window;
    }
    /**
     * Required. The lookback window for all other, non-acquisition conversion
     * events. The default window size is 90 days.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.OtherConversionEventLookbackWindow other_conversion_event_lookback_window = 3 [(.google.api.field_behavior) = REQUIRED];</code>
     * @param int $var
     * @return $this
     */
    public function setOtherConversionEventLookbackWindow($var)
    {
        GPBUtil::checkEnum($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha\AttributionSettings\OtherConversionEventLookbackWindow::class);
        $this->other_conversion_event_lookback_window = $var;
        return $this;
    }
    /**
     * Required. The reporting attribution model used to calculate conversion
     * credit in this property's reports.
     * Changing the attribution model will apply to both historical and future
     * data. These changes will be reflected in reports with conversion and
     * revenue data. User and session data will be unaffected.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.ReportingAttributionModel reporting_attribution_model = 4 [(.google.api.field_behavior) = REQUIRED];</code>
     * @return int
     */
    public function getReportingAttributionModel()
    {
        return $this->reporting_attribution_model;
    }
    /**
     * Required. The reporting attribution model used to calculate conversion
     * credit in this property's reports.
     * Changing the attribution model will apply to both historical and future
     * data. These changes will be reflected in reports with conversion and
     * revenue data. User and session data will be unaffected.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AttributionSettings.ReportingAttributionModel reporting_attribution_model = 4 [(.google.api.field_behavior) = REQUIRED];</code>
     * @param int $var
     * @return $this
     */
    public function setReportingAttributionModel($var)
    {
        GPBUtil::checkEnum($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha\AttributionSettings\ReportingAttributionModel::class);
        $this->reporting_attribution_model = $var;
        return $this;
    }
}
