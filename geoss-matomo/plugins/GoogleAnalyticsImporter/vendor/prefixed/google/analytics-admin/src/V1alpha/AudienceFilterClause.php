<?php

# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/analytics/admin/v1alpha/audience.proto
namespace Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha;

use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\RepeatedField;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBUtil;
/**
 * A clause for defining either a simple or sequence filter. A filter can be
 * inclusive (i.e., users satisfying the filter clause are included in the
 * Audience) or exclusive (i.e., users satisfying the filter clause are
 * excluded from the Audience).
 *
 * Generated from protobuf message <code>google.analytics.admin.v1alpha.AudienceFilterClause</code>
 */
class AudienceFilterClause extends \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\Message
{
    /**
     * Required. Specifies whether this is an include or exclude filter clause.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceFilterClause.AudienceClauseType clause_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     */
    private $clause_type = 0;
    protected $filter;
    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type \Google\Analytics\Admin\V1alpha\AudienceSimpleFilter $simple_filter
     *           A simple filter that a user must satisfy to be a member of the Audience.
     *     @type \Google\Analytics\Admin\V1alpha\AudienceSequenceFilter $sequence_filter
     *           Filters that must occur in a specific order for the user to be a member
     *           of the Audience.
     *     @type int $clause_type
     *           Required. Specifies whether this is an include or exclude filter clause.
     * }
     */
    public function __construct($data = NULL)
    {
        \Matomo\Dependencies\GoogleAnalyticsImporter\GPBMetadata\Google\Analytics\Admin\V1Alpha\Audience::initOnce();
        parent::__construct($data);
    }
    /**
     * A simple filter that a user must satisfy to be a member of the Audience.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceSimpleFilter simple_filter = 2;</code>
     * @return \Google\Analytics\Admin\V1alpha\AudienceSimpleFilter|null
     */
    public function getSimpleFilter()
    {
        return $this->readOneof(2);
    }
    public function hasSimpleFilter()
    {
        return $this->hasOneof(2);
    }
    /**
     * A simple filter that a user must satisfy to be a member of the Audience.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceSimpleFilter simple_filter = 2;</code>
     * @param \Google\Analytics\Admin\V1alpha\AudienceSimpleFilter $var
     * @return $this
     */
    public function setSimpleFilter($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha\AudienceSimpleFilter::class);
        $this->writeOneof(2, $var);
        return $this;
    }
    /**
     * Filters that must occur in a specific order for the user to be a member
     * of the Audience.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceSequenceFilter sequence_filter = 3;</code>
     * @return \Google\Analytics\Admin\V1alpha\AudienceSequenceFilter|null
     */
    public function getSequenceFilter()
    {
        return $this->readOneof(3);
    }
    public function hasSequenceFilter()
    {
        return $this->hasOneof(3);
    }
    /**
     * Filters that must occur in a specific order for the user to be a member
     * of the Audience.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceSequenceFilter sequence_filter = 3;</code>
     * @param \Google\Analytics\Admin\V1alpha\AudienceSequenceFilter $var
     * @return $this
     */
    public function setSequenceFilter($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha\AudienceSequenceFilter::class);
        $this->writeOneof(3, $var);
        return $this;
    }
    /**
     * Required. Specifies whether this is an include or exclude filter clause.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceFilterClause.AudienceClauseType clause_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     * @return int
     */
    public function getClauseType()
    {
        return $this->clause_type;
    }
    /**
     * Required. Specifies whether this is an include or exclude filter clause.
     *
     * Generated from protobuf field <code>.google.analytics.admin.v1alpha.AudienceFilterClause.AudienceClauseType clause_type = 1 [(.google.api.field_behavior) = REQUIRED];</code>
     * @param int $var
     * @return $this
     */
    public function setClauseType($var)
    {
        GPBUtil::checkEnum($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Analytics\Admin\V1alpha\AudienceFilterClause\AudienceClauseType::class);
        $this->clause_type = $var;
        return $this;
    }
    /**
     * @return string
     */
    public function getFilter()
    {
        return $this->whichOneof("filter");
    }
}
