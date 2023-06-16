package com.eversis.esa.geoss.proxy.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User agent properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAgentProperties {

    /**
     * The Ua.
     */
    String ua;

    /**
     * The Ua browser name.
     */
    String uaBrowserName;

    /**
     * The Ua browser version.
     */
    String uaBrowserVersion;

    /**
     * The Ua engine name.
     */
    String uaEngineName;

    /**
     * The Ua engine version.
     */
    String uaEngineVersion;

    /**
     * The Ua os name.
     */
    String uaOsName;

    /**
     * The Ua os version.
     */
    String uaOsVersion;

    /**
     * The Ua device model.
     */
    String uaDeviceModel;

    /**
     * The Ua device vendor.
     */
    String uaDeviceVendor;

    /**
     * The Ua device type.
     */
    String uaDeviceType;

    /**
     * The Ua cpu architecture.
     */
    String uaCpuArchitecture;

}
