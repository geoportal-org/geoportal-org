<?php

# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: google/api/expr/v1alpha1/checked.proto
namespace Matomo\Dependencies\GoogleAnalyticsImporter\Google\Api\Expr\V1alpha1\Decl\FunctionDecl;

use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\RepeatedField;
use Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBUtil;
/**
 * An overload indicates a function's parameter types and return type, and
 * may optionally include a function body described in terms of [Expr][google.api.expr.v1alpha1.Expr]
 * values.
 * Functions overloads are declared in either a function or method
 * call-style. For methods, the `params[0]` is the expected type of the
 * target receiver.
 * Overloads must have non-overlapping argument types after erasure of all
 * parameterized type variables (similar as type erasure in Java).
 *
 * Generated from protobuf message <code>google.api.expr.v1alpha1.Decl.FunctionDecl.Overload</code>
 */
class Overload extends \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\Message
{
    /**
     * Required. Globally unique overload name of the function which reflects
     * the function name and argument types.
     * This will be used by a [Reference][google.api.expr.v1alpha1.Reference] to indicate the `overload_id` that
     * was resolved for the function `name`.
     *
     * Generated from protobuf field <code>string overload_id = 1;</code>
     */
    private $overload_id = '';
    /**
     * List of function parameter [Type][google.api.expr.v1alpha1.Type] values.
     * Param types are disjoint after generic type parameters have been
     * replaced with the type `DYN`. Since the `DYN` type is compatible with
     * any other type, this means that if `A` is a type parameter, the
     * function types `int<A>` and `int<int>` are not disjoint. Likewise,
     * `map<string, string>` is not disjoint from `map<K, V>`.
     * When the `result_type` of a function is a generic type param, the
     * type param name also appears as the `type` of on at least one params.
     *
     * Generated from protobuf field <code>repeated .google.api.expr.v1alpha1.Type params = 2;</code>
     */
    private $params;
    /**
     * The type param names associated with the function declaration.
     * For example, `function ex<K,V>(K key, map<K, V> map) : V` would yield
     * the type params of `K, V`.
     *
     * Generated from protobuf field <code>repeated string type_params = 3;</code>
     */
    private $type_params;
    /**
     * Required. The result type of the function. For example, the operator
     * `string.isEmpty()` would have `result_type` of `kind: BOOL`.
     *
     * Generated from protobuf field <code>.google.api.expr.v1alpha1.Type result_type = 4;</code>
     */
    private $result_type = null;
    /**
     * Whether the function is to be used in a method call-style `x.f(...)`
     * of a function call-style `f(x, ...)`.
     * For methods, the first parameter declaration, `params[0]` is the
     * expected type of the target receiver.
     *
     * Generated from protobuf field <code>bool is_instance_function = 5;</code>
     */
    private $is_instance_function = \false;
    /**
     * Documentation string for the overload.
     *
     * Generated from protobuf field <code>string doc = 6;</code>
     */
    private $doc = '';
    /**
     * Constructor.
     *
     * @param array $data {
     *     Optional. Data for populating the Message object.
     *
     *     @type string $overload_id
     *           Required. Globally unique overload name of the function which reflects
     *           the function name and argument types.
     *           This will be used by a [Reference][google.api.expr.v1alpha1.Reference] to indicate the `overload_id` that
     *           was resolved for the function `name`.
     *     @type \Google\Api\Expr\V1alpha1\Type[]|\Google\Protobuf\Internal\RepeatedField $params
     *           List of function parameter [Type][google.api.expr.v1alpha1.Type] values.
     *           Param types are disjoint after generic type parameters have been
     *           replaced with the type `DYN`. Since the `DYN` type is compatible with
     *           any other type, this means that if `A` is a type parameter, the
     *           function types `int<A>` and `int<int>` are not disjoint. Likewise,
     *           `map<string, string>` is not disjoint from `map<K, V>`.
     *           When the `result_type` of a function is a generic type param, the
     *           type param name also appears as the `type` of on at least one params.
     *     @type string[]|\Google\Protobuf\Internal\RepeatedField $type_params
     *           The type param names associated with the function declaration.
     *           For example, `function ex<K,V>(K key, map<K, V> map) : V` would yield
     *           the type params of `K, V`.
     *     @type \Google\Api\Expr\V1alpha1\Type $result_type
     *           Required. The result type of the function. For example, the operator
     *           `string.isEmpty()` would have `result_type` of `kind: BOOL`.
     *     @type bool $is_instance_function
     *           Whether the function is to be used in a method call-style `x.f(...)`
     *           of a function call-style `f(x, ...)`.
     *           For methods, the first parameter declaration, `params[0]` is the
     *           expected type of the target receiver.
     *     @type string $doc
     *           Documentation string for the overload.
     * }
     */
    public function __construct($data = NULL)
    {
        \Matomo\Dependencies\GoogleAnalyticsImporter\GPBMetadata\Google\Api\Expr\V1Alpha1\Checked::initOnce();
        parent::__construct($data);
    }
    /**
     * Required. Globally unique overload name of the function which reflects
     * the function name and argument types.
     * This will be used by a [Reference][google.api.expr.v1alpha1.Reference] to indicate the `overload_id` that
     * was resolved for the function `name`.
     *
     * Generated from protobuf field <code>string overload_id = 1;</code>
     * @return string
     */
    public function getOverloadId()
    {
        return $this->overload_id;
    }
    /**
     * Required. Globally unique overload name of the function which reflects
     * the function name and argument types.
     * This will be used by a [Reference][google.api.expr.v1alpha1.Reference] to indicate the `overload_id` that
     * was resolved for the function `name`.
     *
     * Generated from protobuf field <code>string overload_id = 1;</code>
     * @param string $var
     * @return $this
     */
    public function setOverloadId($var)
    {
        GPBUtil::checkString($var, True);
        $this->overload_id = $var;
        return $this;
    }
    /**
     * List of function parameter [Type][google.api.expr.v1alpha1.Type] values.
     * Param types are disjoint after generic type parameters have been
     * replaced with the type `DYN`. Since the `DYN` type is compatible with
     * any other type, this means that if `A` is a type parameter, the
     * function types `int<A>` and `int<int>` are not disjoint. Likewise,
     * `map<string, string>` is not disjoint from `map<K, V>`.
     * When the `result_type` of a function is a generic type param, the
     * type param name also appears as the `type` of on at least one params.
     *
     * Generated from protobuf field <code>repeated .google.api.expr.v1alpha1.Type params = 2;</code>
     * @return \Google\Protobuf\Internal\RepeatedField
     */
    public function getParams()
    {
        return $this->params;
    }
    /**
     * List of function parameter [Type][google.api.expr.v1alpha1.Type] values.
     * Param types are disjoint after generic type parameters have been
     * replaced with the type `DYN`. Since the `DYN` type is compatible with
     * any other type, this means that if `A` is a type parameter, the
     * function types `int<A>` and `int<int>` are not disjoint. Likewise,
     * `map<string, string>` is not disjoint from `map<K, V>`.
     * When the `result_type` of a function is a generic type param, the
     * type param name also appears as the `type` of on at least one params.
     *
     * Generated from protobuf field <code>repeated .google.api.expr.v1alpha1.Type params = 2;</code>
     * @param \Google\Api\Expr\V1alpha1\Type[]|\Google\Protobuf\Internal\RepeatedField $var
     * @return $this
     */
    public function setParams($var)
    {
        $arr = GPBUtil::checkRepeatedField($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType::MESSAGE, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Api\Expr\V1alpha1\Type::class);
        $this->params = $arr;
        return $this;
    }
    /**
     * The type param names associated with the function declaration.
     * For example, `function ex<K,V>(K key, map<K, V> map) : V` would yield
     * the type params of `K, V`.
     *
     * Generated from protobuf field <code>repeated string type_params = 3;</code>
     * @return \Google\Protobuf\Internal\RepeatedField
     */
    public function getTypeParams()
    {
        return $this->type_params;
    }
    /**
     * The type param names associated with the function declaration.
     * For example, `function ex<K,V>(K key, map<K, V> map) : V` would yield
     * the type params of `K, V`.
     *
     * Generated from protobuf field <code>repeated string type_params = 3;</code>
     * @param string[]|\Google\Protobuf\Internal\RepeatedField $var
     * @return $this
     */
    public function setTypeParams($var)
    {
        $arr = GPBUtil::checkRepeatedField($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Protobuf\Internal\GPBType::STRING);
        $this->type_params = $arr;
        return $this;
    }
    /**
     * Required. The result type of the function. For example, the operator
     * `string.isEmpty()` would have `result_type` of `kind: BOOL`.
     *
     * Generated from protobuf field <code>.google.api.expr.v1alpha1.Type result_type = 4;</code>
     * @return \Google\Api\Expr\V1alpha1\Type
     */
    public function getResultType()
    {
        return $this->result_type;
    }
    /**
     * Required. The result type of the function. For example, the operator
     * `string.isEmpty()` would have `result_type` of `kind: BOOL`.
     *
     * Generated from protobuf field <code>.google.api.expr.v1alpha1.Type result_type = 4;</code>
     * @param \Google\Api\Expr\V1alpha1\Type $var
     * @return $this
     */
    public function setResultType($var)
    {
        GPBUtil::checkMessage($var, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Api\Expr\V1alpha1\Type::class);
        $this->result_type = $var;
        return $this;
    }
    /**
     * Whether the function is to be used in a method call-style `x.f(...)`
     * of a function call-style `f(x, ...)`.
     * For methods, the first parameter declaration, `params[0]` is the
     * expected type of the target receiver.
     *
     * Generated from protobuf field <code>bool is_instance_function = 5;</code>
     * @return bool
     */
    public function getIsInstanceFunction()
    {
        return $this->is_instance_function;
    }
    /**
     * Whether the function is to be used in a method call-style `x.f(...)`
     * of a function call-style `f(x, ...)`.
     * For methods, the first parameter declaration, `params[0]` is the
     * expected type of the target receiver.
     *
     * Generated from protobuf field <code>bool is_instance_function = 5;</code>
     * @param bool $var
     * @return $this
     */
    public function setIsInstanceFunction($var)
    {
        GPBUtil::checkBool($var);
        $this->is_instance_function = $var;
        return $this;
    }
    /**
     * Documentation string for the overload.
     *
     * Generated from protobuf field <code>string doc = 6;</code>
     * @return string
     */
    public function getDoc()
    {
        return $this->doc;
    }
    /**
     * Documentation string for the overload.
     *
     * Generated from protobuf field <code>string doc = 6;</code>
     * @param string $var
     * @return $this
     */
    public function setDoc($var)
    {
        GPBUtil::checkString($var, True);
        $this->doc = $var;
        return $this;
    }
}
// Adding a class alias for backwards compatibility with the previous class name.
class_alias(Overload::class, \Matomo\Dependencies\GoogleAnalyticsImporter\Google\Api\Expr\V1alpha1\Decl_FunctionDecl_Overload::class);
