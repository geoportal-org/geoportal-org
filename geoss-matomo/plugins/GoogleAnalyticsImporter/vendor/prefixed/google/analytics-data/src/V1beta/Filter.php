<?php

# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/analytics/data/v1beta/data.proto
namespace Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Data\V1beta;

use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\RepeatedField;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBUtil;
/**
 * An expression to filter dimension or metric values.
 *
 * Generated from protobuf message <code>google.analytics.data.v1beta.Filter</code>
 */
class Filter extends \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\Message
{
    /**
     * The dimension name or metric name.
     * In most methods, dimensions & metrics can be used for the first time in
     * this field. However in a RunPivotReportRequest, this field must be
     * additionally specified by name in the RunPivotReportRequest's dimensions or
     * metrics.
     *
     * Generated from protobuf field <code>string field_name = 1;</code>
     */
    private $field_name = '';
    protected $one_filter;
    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type string $field_name
     *           The dimension name or metric name.
     *           In most methods, dimensions & metrics can be used for the first time in
     *           this field. However in a RunPivotReportRequest, this field must be
     *           additionally specified by name in the RunPivotReportRequest's dimensions or
     *           metrics.
     *     @type \Google\Analytics\Data\V1beta\Filter\StringFilter $string_filter
     *           Strings related filter.
     *     @type \Google\Analytics\Data\V1beta\Filter\InListFilter $in_list_filter
     *           A filter for in list values.
     *     @type \Google\Analytics\Data\V1beta\Filter\NumericFilter $numeric_filter
     *           A filter for numeric or date values.
     *     @type \Google\Analytics\Data\V1beta\Filter\BetweenFilter $between_filter
     *           A filter for two values.
     * }
     */
    public function __construct($data = NULL)
    {
        \Matomo\Dependencies\GoogleAnalyticsImporter\GPBMetadata\Google\Analytics\Data\V1Beta\Data::initOnce();
        parent::__construct($data);
    }
    /**
     * The dimension name or metric name.
     * In most methods, dimensions & metrics can be used for the first time in
     * this field. However in a RunPivotReportRequest, this field must be
     * additionally specified by name in the RunPivotReportRequest's dimensions or
     * metrics.
     *
     * Generated from protobuf field <code>string field_name = 1;</code>
     * @return string
     */
    public function getFieldName()
    {
        return $this->field_name;
    }
    /**
     * The dimension name or metric name.
     * In most methods, dimensions & metrics can be used for the first time in
     * this field. However in a RunPivotReportRequest, this field must be
     * additionally specified by name in the RunPivotReportRequest's dimensions or
     * metrics.
     *
     * Generated from protobuf field <code>string field_name = 1;</code>
     * @param string $var
     * @return $this
     */
    public function setFieldName($var)
    {
        GPBUtil::checkString($var, True);
        $this->field_name = $var;
        return $this;
    }
    /**
     * Strings related filter.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.StringFilter string_filter = 3;</code>
     * @return \Google\Analytics\Data\V1beta\Filter\StringFilter|null
     */
    public function getStringFilter()
    {
        return $this->readOneof(3);
    }
    public function hasStringFilter()
    {
        return $this->hasOneof(3);
    }
    /**
     * Strings related filter.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.StringFilter string_filter = 3;</code>
     * @param \Google\Analytics\Data\V1beta\Filter\StringFilter $var
     * @return $this
     */
    public function setStringFilter($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Data\V1beta\Filter\StringFilter::class);
        $this->writeOneof(3, $var);
        return $this;
    }
    /**
     * A filter for in list values.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.InListFilter in_list_filter = 4;</code>
     * @return \Google\Analytics\Data\V1beta\Filter\InListFilter|null
     */
    public function getInListFilter()
    {
        return $this->readOneof(4);
    }
    public function hasInListFilter()
    {
        return $this->hasOneof(4);
    }
    /**
     * A filter for in list values.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.InListFilter in_list_filter = 4;</code>
     * @param \Google\Analytics\Data\V1beta\Filter\InListFilter $var
     * @return $this
     */
    public function setInListFilter($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Data\V1beta\Filter\InListFilter::class);
        $this->writeOneof(4, $var);
        return $this;
    }
    /**
     * A filter for numeric or date values.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.NumericFilter numeric_filter = 5;</code>
     * @return \Google\Analytics\Data\V1beta\Filter\NumericFilter|null
     */
    public function getNumericFilter()
    {
        return $this->readOneof(5);
    }
    public function hasNumericFilter()
    {
        return $this->hasOneof(5);
    }
    /**
     * A filter for numeric or date values.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.NumericFilter numeric_filter = 5;</code>
     * @param \Google\Analytics\Data\V1beta\Filter\NumericFilter $var
     * @return $this
     */
    public function setNumericFilter($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Data\V1beta\Filter\NumericFilter::class);
        $this->writeOneof(5, $var);
        return $this;
    }
    /**
     * A filter for two values.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.BetweenFilter between_filter = 6;</code>
     * @return \Google\Analytics\Data\V1beta\Filter\BetweenFilter|null
     */
    public function getBetweenFilter()
    {
        return $this->readOneof(6);
    }
    public function hasBetweenFilter()
    {
        return $this->hasOneof(6);
    }
    /**
     * A filter for two values.
     *
     * Generated from protobuf field <code>.google.analytics.data.v1beta.Filter.BetweenFilter between_filter = 6;</code>
     * @param \Google\Analytics\Data\V1beta\Filter\BetweenFilter $var
     * @return $this
     */
    public function setBetweenFilter($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Data\V1beta\Filter\BetweenFilter::class);
        $this->writeOneof(6, $var);
        return $this;
    }
    /**
     * @return string
     */
    public function getOneFilter()
    {
        return $this->whichOneof("one_filter");
    }
}
