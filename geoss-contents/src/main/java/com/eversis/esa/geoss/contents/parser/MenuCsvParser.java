package com.eversis.esa.geoss.contents.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.eversis.esa.geoss.contents.domain.Menu;
import com.eversis.esa.geoss.contents.parser.model.MenuCsv;
import com.eversis.esa.geoss.contents.parser.model.MenuImageTitleCsv;
import com.eversis.esa.geoss.contents.parser.model.MenuTitleCsv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

/**
 * Service class for parsing CSV files related to menu.
 */
@Slf4j
@Service
public class MenuCsvParser extends CsvParser {

    // CSV file paths
    @Value("classpath:db/init-data/menu.csv")
    private Resource menuCsvFile;

    @Value("classpath:db/init-data/menu-title.csv")
    private Resource menuTitleCsvFile;

    @Value("classpath:db/init-data/menu-image-title.csv")
    private Resource menuImageTitleCsvFile;

    public List<Menu> getMenusFromCsv(Long siteId) {

        // Parse the title CSV and group by menu ID
        Map<Long, Map<Locale, String>> titlesMap = parseCsv(MenuTitleCsv.class, menuTitleCsvFile).stream()
                .collect(Collectors.groupingBy(MenuTitleCsv::getMenuId,
                        Collectors.toMap(MenuTitleCsv::getLocale, MenuTitleCsv::getTitle)));

        // Parse the image title CSV and group by menu ID
        Map<Long, Map<Locale, String>> imageTitlesMap =
                parseCsv(MenuImageTitleCsv.class, menuImageTitleCsvFile).stream()
                        .collect(Collectors.groupingBy(MenuImageTitleCsv::getMenuId,
                                Collectors.toMap(MenuImageTitleCsv::getLocale, MenuImageTitleCsv::getImageTitle)));

        // Parse the menu CSV and map to Menu objects
        return parseCsv(MenuCsv.class, menuCsvFile).stream().map(menuCsv -> {
            Menu menu = new Menu();
            menu.setImageSource(menuCsv.getImageSource());
            menu.setUrl(menuCsv.getUrl());
            menu.setPriority(menuCsv.getPriority());
            menu.setLevelId(menuCsv.getLevelId());
            menu.setSiteId(siteId);
            menu.setTitle(titlesMap.getOrDefault(menuCsv.getId(), new HashMap<>()));
            menu.setImageTitle(imageTitlesMap.getOrDefault(menuCsv.getId(), new HashMap<>()));
            return menu;
        }).collect(Collectors.toList());
    }

}
