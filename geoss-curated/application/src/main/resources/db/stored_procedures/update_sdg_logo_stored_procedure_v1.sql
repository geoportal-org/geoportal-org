CREATE PROCEDURE `update_sdg_goal_logos` (
	IN logoOldBaseUrl VARCHAR(40),
    IN logoNewBaseUrl VARCHAR(40))
BEGIN
UPDATE entry
SET logo = REPLACE(logo, logoOldBaseUrl, logoNewBaseUrl)
WHERE code REGEXP 'geoss_cr_un_sd_[0-9]{1,2}$';
END
$$
