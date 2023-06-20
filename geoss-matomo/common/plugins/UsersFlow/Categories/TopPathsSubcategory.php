<?php
/**
 * Copyright (C) InnoCraft Ltd - All rights reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of InnoCraft Ltd.
 * The intellectual and technical concepts contained herein are protected by trade secret or copyright law.
 * Redistribution of this information or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from InnoCraft Ltd.
 *
 * You shall use this code only in accordance with the license agreement obtained from InnoCraft Ltd.
 *
 * @link https://www.innocraft.com/
 * @license For license details see https://www.innocraft.com/license
 *
 */
namespace Piwik\Plugins\UsersFlow\Categories;

use Piwik\Category\Subcategory;
use Piwik\Piwik;

class TopPathsSubcategory extends Subcategory
{
    protected $categoryId = 'General_Actions';
    protected $id = 'UsersFlow_TopPaths';
    protected $order = 48;

    public function getHelp()
    {
        return '<p>' . Piwik::translate('UsersFlow_TopPathsSubcategoryHelp') . '</p>';
    }
}
