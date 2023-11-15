<?php

use Piwik\Db;
use Piwik\Common;
use \Exception;

try {
	$sql = "CREATE TABLE " . Common::prefixTable('mynewtable') . " (
	            mykey VARCHAR( 10 ) NOT NULL ,
	            mydata VARCHAR( 100 ) NOT NULL ,
	            PRIMARY KEY ( mykey )
	        )  DEFAULT CHARSET=utf8 ";
	Db::exec($sql);
	} catch (Exception $e) {
	// ignore error if table already exists (1050 code is for 'table already exists')
	if (!Db::get()->isErrNo($e, '1050')) {
	    throw $e;
	}