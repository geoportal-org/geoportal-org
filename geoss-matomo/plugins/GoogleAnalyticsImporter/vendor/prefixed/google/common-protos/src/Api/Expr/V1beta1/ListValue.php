<?php

# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/api/expr/v1beta1/value.proto
namespace Matomo\Dependencies\GoogleAnalyticsImporter\Google\Api\Expr\V1beta1;

use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\RepeatedField;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBUtil;
/**
 * A list.
 * Wrapped in a message so 'not set' and empty can be differentiated, which is
 * required for use in a 'oneof'.
 *
 * Generated from protobuf message <code>google.api.expr.v1beta1.ListValue</code>
 */
class ListValue extends \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\Message
{
    /**
     * The ordered values in the list.
     *
     * Generated from protobuf field <code>repeated .google.api.expr.v1beta1.Value values = 1;</code>
     */
    private $values;
    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type \Google\Api\Expr\V1beta1\Value[]|\Google\Protobuf\Internal\RepeatedField $values
     *           The ordered values in the list.
     * }
     */
    public function __construct($data = NULL)
    {
        \Matomo\Dependencies\GoogleAnalyticsImporter\GPBMetadata\Google\Api\Expr\V1Beta1\Value::initOnce();
        parent::__construct($data);
    }
    /**
     * The ordered values in the list.
     *
     * Generated from protobuf field <code>repeated .google.api.expr.v1beta1.Value values = 1;</code>
     * @return \Google\Protobuf\Internal\RepeatedField
     */
    public function getValues()
    {
        return $this->values;
    }
    /**
     * The ordered values in the list.
     *
     * Generated from protobuf field <code>repeated .google.api.expr.v1beta1.Value values = 1;</code>
     * @param \Google\Api\Expr\V1beta1\Value[]|\Google\Protobuf\Internal\RepeatedField $var
     * @return $this
     */
    public function setValues($var)
    {
        $arr = GPBUtil::checkRepeatedField($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType::MESSAGE, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Api\Expr\V1beta1\Value::class);
        $this->values = $arr;
        return $this;
    }
}
