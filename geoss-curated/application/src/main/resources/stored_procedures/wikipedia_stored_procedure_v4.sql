CREATE PROCEDURE `add_or_update_wikipedia_entry`(
    IN _title VARCHAR(200),
    IN _summary TEXT,
    IN _logo VARCHAR(800),
    IN _keywords VARCHAR(2000),
    IN _tags VARCHAR(2000),
	IN _code VARCHAR(180),
    IN _typeId INT,
    IN _definitionTypeId INT,
    IN _accessPolicyId INT,
    IN _sourceId INT,
    IN _dataSourceId INT,
    IN _protocolId INT,
    IN _endpointUrl VARCHAR(1024)
)
BEGIN
	DECLARE endpoint_id INT(11);
	DECLARE entry_id INT(11);
	DECLARE EXIT HANDLER FOR SQLEXCEPTION ROLLBACK;
    DECLARE EXIT HANDLER FOR SQLWARNING ROLLBACK;
    START TRANSACTION;


		-- Insert entry
		INSERT INTO entry(title, summary, logo, keywords, tags, code, typeId, definitionTypeId, accessPolicyId, sourceId, dataSourceId, displayDataSourceId)
		VALUES(_title, _summary, _logo, _keywords, _tags, _code, _typeId, _definitionTypeId, _accessPolicyId, _sourceId, _dataSourceId, dataSourceId)
		ON DUPLICATE KEY UPDATE title=_title, summary=_summary, logo=_logo, keywords=_keywords, tags=_tags, code=_code, typeId=_typeId, definitionTypeId=_definitionTypeId, accessPolicyId=_accessPolicyId, sourceId=_sourceId, dataSourceId=_dataSourceId, displayDataSourceId=_dataSourceId;

		SET @entry_id = (SELECT id FROM entry WHERE code = _code LIMIT 1);
        SET @affected_rows = ROW_COUNT();
		-- Insert endpoint
		INSERT INTO endpoint(url, urlType, isCustom)
		SELECT _endpointUrl, 'HTTP', 0 FROM DUAL
		WHERE NOT EXISTS (SELECT id FROM endpoint
						WHERE endpoint.url = _endpointUrl AND endpoint.urlType = 'HTTP' AND endpoint.isCustom = 0)
		LIMIT 1 ;

		SET @endpoint_id = (SELECT id FROM endpoint WHERE url = _endpointUrl AND urlType = 'HTTP' AND isCustom = 0 LIMIT 1);

		-- Insert transfer option
		INSERT INTO transferoptions(name, title, entryId, endpointId, protocolId)
		SELECT _endpointUrl, CONCAT('Wikipedia link: ', _title), @entry_id, @endpoint_id, _protocolId FROM DUAL
		WHERE NOT EXISTS (SELECT id FROM transferoptions ts
							WHERE ts.name = _endpointUrl AND ts.entryId = @entry_id AND ts.endpointId = @endpoint_id AND ts.protocolId = _protocolId);


		COMMIT;

END
$$
