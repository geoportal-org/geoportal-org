<?php

/**
 * ECPoint
 *
 * PHP version 5
 *
 * @author    Jim Wigginton <terrafrost@php.net>
 * @copyright 2016 Jim Wigginton
 * @license   http://www.opensource.org/licenses/mit-license.html  MIT License
 * @link      http://phpseclib.sourceforge.net
 */
namespace Matomo\Dependencies\GoogleAnalyticsImporter\phpseclib3\File\ASN1\Maps;

use Matomo\Dependencies\GoogleAnalyticsImporter\phpseclib3\File\ASN1;
/**
 * ECPoint
 *
 * @author  Jim Wigginton <terrafrost@php.net>
 */
abstract class ECPoint
{
    const MAP = ['type' => ASN1::TYPE_OCTET_STRING];
}
