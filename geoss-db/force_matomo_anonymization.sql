DELIMITER // 
CREATE TRIGGER tr_enable_ipanonymization_matomo 
BEFORE INSERT ON matomo_option FOR EACH ROW 
BEGIN 
	IF new.option_name='PrivacyManager.ipAnonymizerEnabled' THEN 
		SET new.option_value=1; 
	END IF; 
END; // 
DELIMITER ;

DELIMITER // 
CREATE TRIGGER tr_force_ipanonymization_matomo 
BEFORE UPDATE ON matomo_option FOR EACH ROW 
BEGIN 
	IF new.option_name='PrivacyManager.ipAnonymizerEnabled' THEN 
		SET new.option_value=1; 
	END IF; 
END; // 
DELIMITER ;