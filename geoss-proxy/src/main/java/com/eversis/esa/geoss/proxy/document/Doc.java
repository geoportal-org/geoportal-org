package com.eversis.esa.geoss.proxy.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * The type Doc.
 */
@Document(indexName = "geoss_index", createIndex = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doc {

    /**
     * The Id.
     */
    @Id
    String id;

    /**
     * The Session login.
     */
    @Field(type = FieldType.Keyword, name = "session_login")
    String sessionLogin;

    /**
     * The Session id.
     */
    @Field(type = FieldType.Keyword, name = "session_id")
    String sessionId;

    /**
     * The Session timestamp.
     */
    @Field(type = FieldType.Long, name = "session_timestamp")
    long sessionTimestamp;

    /**
     * The Session date.
     */
    @Field(type = FieldType.Date, name = "session_date")
    Date sessionDate;

    /**
     * The Ua.
     */
    @Field(type = FieldType.Text, name = "ua")
    String ua;

    /**
     * The Ua browser name.
     */
    @Field(type = FieldType.Keyword, name = "ua_browser_name")
    String uaBrowserName;

    /**
     * The Ua browser version.
     */
    @Field(type = FieldType.Keyword, name = "ua_browser_version")
    String uaBrowserVersion;

    /**
     * The Ua engine name.
     */
    @Field(type = FieldType.Keyword, name = "ua_engine_name")
    String uaEngineName;

    /**
     * The Ua engine version.
     */
    @Field(type = FieldType.Keyword, name = "ua_engine_version")
    String uaEngineVersion;

    /**
     * The Ua os name.
     */
    @Field(type = FieldType.Keyword, name = "ua_os_name")
    String uaOsName;

    /**
     * The Ua os version.
     */
    @Field(type = FieldType.Keyword, name = "ua_os_version")
    String uaOsVersion;

    /**
     * The Ua device model.
     */
    @Field(type = FieldType.Keyword, name = "ua_device_model")
    String uaDeviceModel;

    /**
     * The Ua device vendor.
     */
    @Field(type = FieldType.Keyword, name = "ua_device_vendor")
    String uaDeviceVendor;

    /**
     * The Ua device type.
     */
    @Field(type = FieldType.Keyword, name = "ua_device_type")
    String uaDeviceType;

    /**
     * The Ua cpu architecture.
     */
    @Field(type = FieldType.Keyword, name = "ua_cpu_architecture")
    String uaCpuArchitecture;

    /**
     * The Win width.
     */
    @Field(type = FieldType.Integer, name = "win_width")
    int winWidth;

    /**
     * The Win height.
     */
    @Field(type = FieldType.Integer, name = "win_height")
    int winHeight;

}
