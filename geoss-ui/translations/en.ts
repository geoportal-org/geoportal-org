export default {
    statistics: {
        statistics: 'Statistics',
        chartOptions: 'CHART OPTIONS:',
        source: 'Source:',
        dataUsage: 'Data usage',
        siteUsage: 'Site usage',
        dataset: 'Dataset:',
        type: 'Type:',
        moreOptions: 'MORE OPTIONS:',
        interval: 'Interval:',
        resultsPresented: 'Results presented:',
        analyzedPeriod: 'ANALYZED PERIOD:',
        hideOptions: 'Collapse chart options',
        showOptions: 'Show chart options',
        showChart: 'Show chart',
        lastWeek: 'Last week',
        lastMonth: 'Last month',
        lastYear: 'Last year',
        exportButton: 'Save',
        period: 'Analyzed period: ',
        generated: 'Genereation date: ',
        dataTypeOptions: {
            numberOfSearches: 'Number of searches',
            popularResources: 'Most popular resources',
            popularKeywords: 'Most popular keywords',
            popularCatalogs: 'Most popular catalogs',
            popularOrganizations: 'Most popular organizations',
            popularAreas: 'Most popular areas',
        },
        siteTypeOptions: {
            numberOfUsers: 'Number of users',
            numberOfSessions: 'Number of sessions',
            bounceRate: 'Bounce rate',
            returningUsers: 'Returning users',
            usersCountries: "Users' countries",
            popularBrowsers: 'Most popular browsers',
        },
        timeUnits: {
            day: 'Day',
            week: 'Week',
            month: 'Month',
            year: 'Year',
        },
        bounceRateLabel: 'Percent of visits leaving the website',
        dateLabel: 'Date',
        newVisitorsLabel: 'New visitors',
        returningVisitorsLabel: 'Returning visitors',
        popularBrowsersTitle: 'Most popular browsers based on number of actions',
        visitorTypeTitle: 'Visitor type',
        country: 'Country',
        numberOfVisits: 'Number of visits',
        numberOfSearches: 'Number of searches'
    },
    placeholders: {
        searchBar: 'Enter search words',
        googlePlaces: 'Enter location',
        w3w: 'Enter words',
        from: 'From...',
        to: 'To...',
        selectContinentOrCountry: 'Select continent or country',
        search: 'Search',
        clearSearch: 'Clear search',
    },
    notifications: {
        newVersionTitle: 'NEW VERSION AVAILABLE',
        newVersionPart1:
            'The GEOSS Portal has been updated for a more intuitive and fluid user experience.',
        newVersionPart2: 'A Quick Tour video of the main features is available',
        newVersionPart3: 'here',
        newVersionPart4: 'Visit the',
        newVersionPart5: 'Release notes',
        newVersionPart6: 'to look at the detailed changes.',
        basemapUnavailable:
            'Default basemap is temporarily unavailable. The portal is changing map to another.',
        basemapUnavailableTitle: 'BASEMAP NOT AVAILABLE',
    },
    welcomePopup: {
        newVersionTitle: 'THE GEOSS PORTAL 3.7 IS AVAILABLE',
        newVersionPart1:
            'The GEOSS Portal has been updated for a more intuitive and fluid user experience.',
        newVersionPart2:
            'A Quick Tour video clip of the main features is available',
        newVersionPart3: 'here',
        newVersionPart4: 'Visit the',
        newVersionPart5: 'Release notes',
        newVersionPart6: 'to look at the detailed changes.',
    },
    menu: {
        toggle: 'Toggle menu',
        languageToggle: 'Toggle language selector',
        signIn: 'Sign in',
        signOff: 'Sign off',
    },
    general: {
        error: 'Error',
        warning: 'Warning',
        queryInProgress: 'Query in progress',
        copyLink: 'Copy link',
        noFiltersAvailable: 'No filters available',
        customDownload: 'Custom download',
        backendError: 'Backend error',
        errorOccurred: 'An error has occurred',
        unableToFindAnyResources: 'We were unable to find any resources.',
        loadingResourcesIsTakingTooLong:
            'Resource loading takes more time than usual.',
        noResourcesFound: 'No resources found',
        noResourcesFoundChooseAnotherSource:
            'No resources have been found for this type of data. Please choose to view resources from another type or go back to previous results',
        showDetails: 'Show details',
        hideDetails: 'Hide details',
        hi: 'Hi',
        yes: 'Yes',
        no: 'No',
        info: 'Info',
        close: 'Close',
        noData: 'No data',
        ignore: 'Ignore',
        disable: 'Disable',
        searchResults: 'Search results',
    },
    dataSources: {
        dab: 'Data',
        information: 'Information',
        services: 'Services',
    },
    pagination: {
        showing: 'Showing',
        visible: 'Visible',
        next: 'Next',
        prev: 'Prev',
        of: 'of',
        results: 'results',
    },
    errors: {
        noMetadata:
            'There was a problem with getting the metadata of the resource',
        google: {
            OVER_QUERY_LIMIT:
                'Sorry, we are not able to translate into geographical terms your request right now. Try tomorrow.',
        },
    },
    map: {
        legend: 'Legend',
        previewUnavailable: 'Preview unavailable',
        layersHandling: 'Layers Handling',
        centerMapOnLayer: 'Center map on layer',
        layerSettings: 'Layer settings',
        removeLayer: 'Remove layer',
        compareLayer:
            'Compare layers (at least 2 layers must be added and visible)',
    },
    searchBar: {
        filters: 'Filters',
        search: 'Search',
        shareSearch: 'Share search',
        clearSearch: 'Clear search',
        saveSearch: 'Save search',
        showHideAllMap: 'Show/hide all map',
        popular: 'Popular',
        seeAlso: 'See also',
        moveAround: 'Move around',
        recommendationsForYou: 'Recommendations for you',
    },
    filters: {
        advanced: 'Advanced',
        filterBy: 'Filter by',
        toggle: 'Toggle filters',
    },
    generalFilters: {
        filters: 'Filters',
        advancedSearch: 'Advanced search',
        revertChanges: 'Revert changes',
        earthObservationCatalogs: 'Earth observations catalogs',
        thematicAreas: 'Thematic areas',
        geolocation: 'Geolocation',
        continentCountry: 'Continent & Country',
        coordinates: 'Coordinates',
        relationToSelectedArea: 'Relation to the selected area',
        overlaps: 'Overlaps',
        contains: 'Contains',
        disjoint: 'Disjoint',
        dateRanges: 'Date range',
        lastWeek: 'Last week',
        lastMonth: 'Last month',
        lastYear: 'Last year',
        last10Years: 'Last 10 years',
        cancel: 'Clear',
        accept: 'Apply',
    },
    customDownloadOptionsPopup: {
        title: 'Choose preferred options',
        format: 'Format',
        outputSize: 'Output size',
        subsetCRS: 'Subset CRS',
        subsetLowerCoordinate: 'Subset lower coordinate',
        subsetUpperCoordinate: 'Subset upper coordinate',
        download: 'Download',
        addToDownloads: 'Add to downloads list',
        outputDownloadFormat: 'Output download format',
        imageResolutionInPixels: 'Image resolution in pixels (250, 500)',
        coordinateReferenceSystemOfSubset:
            'Coordinate Reference System of a subset',
        subsetLowerCoordinateInSpecifiedCRSFormat:
            'Subset lower left coordinate in specified CRS format',
        subsetUpperCoordinateInSpecifiedCRSFormat:
            'Subset upper right coordinate in specified CRS format',
        latLongRule:
            'Allowed latitude values: between -90 and 90 and for longitude: between -180 and 180',
        xYRule: 'Allowed X values: between -20037508.3427892 and 20037508.3427892 and for Y: between -20037508.3427892 and 20037508.3427892',
        scaleFactor: 'Scale factor',
        rangeSubset: 'Range subset',
        none: 'none',
        default: 'default',
        crs: 'CRS',
        coordinatesLowerCorner: 'Coordinates (lower corner)',
        coordinatesUpperCorner: 'Coordinates (upper corner)',
    },
    unitedNationsStatisticsPopup: {
        title: 'Choose preferred options',
        timeSeries: 'Time Series',
        timeSeriesTooltip: 'Available time series',
        timeSeriesPlaceholder: 'Choose time series',
        year: 'Year',
        yearTooltip: 'Available years',
        yearPlaceholder: 'Choose year',
        attributes: 'Attributes',
        attributesTooltip: 'Available attributes',
        attributesPlaceholder: 'Choose attributes',
        addLayer: 'Add layer',
        unavailable: 'United Nations Statistics service unavailable',
    },
    dabResult: {
        organisation: 'Organisation',
        creators: 'Creators',
        exploreExtendedView: 'Explore Extended View',
        recentView: 'recent view',
        recentViews: 'recent views',
        seeMore: 'See more',
        seeOnWikipedia: 'See details on Wikipedia',
        showOnMap: 'Show on map',
        addAsBookmark: 'Add as bookmark',
        removeFromBookmarks: 'Remove from bookmarks',
        showInsideFolder: 'Show inside folder',
        hideInsideFolder: 'Hide inside folder',
        share: 'Share',
        layers: 'Layers',
        downloads: 'Downloads',
        workflow: 'Workflow',
        workflowRedirect: 'Run workflow on GEOSS website',
        statistics: 'Statistics',
        data: 'Switch to Data',
        information: 'Switch to Information',
        services: 'Switch to Services',
        userContributeddata: 'Switch to user contributed Data',
        userContributedinformation: 'Switch to user contributed Information',
        userContributedservices: 'Switch to user contributed Services',
        file: 'file',
        other: 'other',
        downloadNow: 'Download now',
        downloadLater: 'Download later',
    },
    popupTitles: {
        quickSurvey: 'Quick survey',
        resourceDetails: 'Resource details',
        rawMetadata: 'Show raw metadata',
        layers: 'Showing Resources that can be loaded on map',
        customizeDownload: 'Customize your download',
        unsd: 'United Nations Statistics',
        bookmarkResult: 'Bookmark result',
        rateResouce: 'Rate resource',
        sentinelDataAccess: 'Sentinel data access',
        saveSearch: 'Save search',
        confirmSearch: 'Confirm search',
        downloadLinks: 'Download links',
        yourDownloads: 'Your downloads',
        downloadsList: 'Downloads list',
        savedRuns: 'Saved runs',
        workflowandruns: 'Workflow & Runs',
        improveDefinition: 'Improve definition',
        entryRelations: 'Entry relations',
    },
    popupContent: {
        bookmarkSavedSuccess:
            'Result has been saved in My Workspace -> Bookmarked Results.',
        bookmarkSavedFail: 'Failed to bookmark result.',
        bookmarkRemovedSuccess: 'Result has been removed from bookmarks.',
        bookmarkRemovedFail: 'Failed to remove result from bookmarks.',
        saveSearchSuccess:
            'Your search has been saved in My Workspace -> Your saved searches.',
        saveSearchFail: 'Failed to save search.',
        loadOnMap: 'Load on the map',
        loadingLayer: 'Loading layer',
        unavailable: 'Unavailable',
        actions: 'Actions',
        showExpertOptions: 'Show expert options',
        hideExpertOptions: 'Hide expert options',
        requiredFields: 'required fields',
        optionalFields: 'options fields',
        expertOptions: 'expert options',
        thisFieldIsRequired: 'This field is required',
        additionalExpertOption: 'Additional expert option',
        setBoundingBox: 'Set bounding box',
        coordinatesWSEN: 'Coordinates: W,S,E,N',
        cloudPlatformSelection: 'Cloud platform selection',
        optimal: 'optimal',
        fetchingPlatformsData: 'Fetching platforms data',
        entryRelationsStart:
            'You are now in ENTRY RELATIONS setting mode.<br />At first, please choose desired entry as a <b>relation source</b>.',
        entryRelationsSrcSet1: 'Relation source has been set to',
        entryRelationsSrcSet2: 'Please choose <b>destination resources</b>.',
        entryRelationsNoDest:
            'There are no destination resources selected. Choose at least one resource related to the source entry.',
        entryRelationsRedirecting:
            'Entry relations data has been successfuly sent. Redirecting...',

        // metadata
        generalInfo: 'General info',
        platform: 'Platform',
        platformId: 'Platform Id',
        productType: 'Product Type',
        instrument: 'Instrument',
        cloudCoverage: 'Cloud coverage',
        daytimeStart: 'Daytime start',
        daytimeStop: 'Daytime stop',
        sceneParameters: 'Scene parameters',
        size: 'Size',
        relativeOrbit: 'Relative orbit',
        sensorPolarisation: 'Sensor polarisation',
        aquisitionType: 'Acquisition type',
        missionDataTaken: 'Mission data token',
        startOrbitNumber: 'Start orbit number',
        stopOrbitNumber: 'Stop orbit number',
        orbitDirection: 'Orbit direction',
        productClass: 'Product class',
        productConsolidation: 'Product consolidation',
        stopRelativeOrbitNumber: 'Stop Relative obit number',
        sliceNumber: 'Slice number',
        status: 'Status',
        contactInformation: 'Contact information',
        contrubutor: 'Contributor',
        deliveryPoint: 'Delivery point',
        city: 'City',
        postalCode: 'Postal code',
        country: 'Country',
        emailAddress: 'E-mail address',
        organizationName: 'Organization name',
        organizationUrl: 'Organization URL',
        creators: 'Creators',
        role: 'Role',
        dataIdentification: 'Data identification',
        fileIdentifier: 'File identifier',
        parentIdentifier: 'Parent identifier',
        hierarchyLevel: 'Hierarchy level',
        dateStamp: 'Date stamp',
        language: 'Language',
        descriptiveKeywords: 'Descriptive keywords',
        boundingRectangle: 'Bounding Rectangle',
        temporalExtent: 'Temporal Extent',
        onlineResources: 'Online Resources',
        rawOnlineResources: 'Raw Online Resources',
        additionalInfo: 'Additional info',
        publicationDate: 'Publication date',
        doi: 'DOI',
        licenseForFiles: 'License (for files)',
        references: 'References',
        publishedIn: 'Published in',
        mapDetails: 'Map details',

        // rating
        comment: 'Comment',
        rating: 'Rating',
        send: 'Send',

        // confirm search
        lookingForResultInLocation:
            'Are you looking for any result in location',
        lookingFor: 'Are you looking for',
        inLocation: 'in location',

        // dab request too long
        resourceTakesMoreTime: 'Resource loading takes more time than usual.',
        wouldYouLikeToContinue: 'Would you like to continue?',

        // layer not available
        mapLayerUnavailable:
            'Map service is not available at the moment. Portal maintenance team has been informed.\nWould you like to disable presentation of this layer?',

        // workflow
        saveRunSuccess: 'Result has been saved in My Workspace -> Saved Runs.',
        saveFail: 'Failed to Saved RunId.',
        seeThisWorkflow: 'See this Workflow',
        run: 'Run',
        runs: 'RUNS',
        selectResources: 'Select resources',
        selectInput: 'Select input',
        inputName: 'Input name',
        chosenResources: 'Chosen resources',
        add: 'Add',
        name: 'Name',
        id: 'ID',
        messageLog: 'Message Log',
        outputs: 'Outputs',
        cancel: 'Cancel',
        accept: 'Accept',
        removeResource: 'Remove resource',
        mustBeLoggedIn:
            'Please <a href="/c/portal/login">log in</a> to run the service',
        mustBeLoggedIn1: 'Please ',
        mustBeLoggedIn2: 'log in',
        mustBeLoggedIn3: 'to run the service',
        noRunsAvailable: 'No runs available',
        default: 'Default',
        workflowInput: 'Workflow input',
        runName: 'Run name',
        worldwide: 'Worldwide',
        serverResponseTimeout:
            'Server response takes longer than expected - please try again.',

        // resource editor
        advancedView: 'Advanced view',
        keywords: 'Keywords',
        definition: 'Definition',
        link: 'Link',
        html: 'html',
        pdf: 'pdf',
        img: 'img',
        file: 'file',
        editorialNotes: 'Editorial notes',
        passToModerator: 'Pass to moderator',
        addLinkOption: 'Add link option',
        removeLinkOption: 'Remove link option',
        improveDefinitionSuccess:
            'Definition improvement was successfully sent to moderator',
        improveDefinitionFail: 'Failed to send definition improvement',
        improveTheResourceDefinition: 'Improve the resource definition',
        youNeedToBeSignedInToImprove:
            'You need to be signed in to improve the resource definition',

        // user contributed metadata
        userContributedTitleSummary: 'User contributed description',
        userContributedTitleKeywords: 'User contributed keywords',
        userContributedTitleTransferOptions:
            'User contributed online resources',
        userContributedTitleComment: 'User comments',
        userContributedRemoveEntryExtension: 'Remove entry extension',
        userContributedAreYouSure:
            'Are you sure you want to remove following entry extension?',
        userContributedCommentOnYourDecision: 'Comment on your decision',
        userContributedCancel: 'Cancel',
        userContributedDelete: 'Delete',
        userContributedRemoveEntryExtensionSuccess:
            'You have successfuly removed entry extension',
        userContributedRemoveUnavailable:
            'Extension removal is temporary unavailable - entry is currently processed in worklow.<br/>Please try again later.',
    },

    irisFilters: {
        filters: 'Filters',
        magnitudeType: 'Magnitude Type',
        sortBy: 'Sort by',

        legend: {
            depth: 'Depth',
            km: 'km',
            magnitude: 'Magnitude',
        },
    },

    facetedFilters: {
        filters: 'Filters',
        keyword: 'Keyword',
        format: 'Format',
        source: 'Source',
        protocol: 'Protocol',
        organisation: 'Organisation',
        serviceHealth: 'Service Health',
    },

    granulaFilters: {
        filters: 'Filters',
        productType: 'Product type',
        sensorPolarisation: 'Sensor Polarisation',
        sensorMode: 'Sensor Mode',
        sensorSwatch: 'Sensor Swatch',
        instrument: 'Instrument',
        productLevel: 'Product Level',
        timeliness: 'Timeliness',
        relativeOrbit: 'Relative Orbit',
        row: 'Row',
        path: 'Path',
    },

    sentinelLogin: {
        username: 'Username',
        password: 'Password',
        sentinelDataAccessRegistration: 'Sentinel data access registration',
        download: 'Download',
    },

    fileDownloadsPopup: {
        fileName: 'File name',
        status: 'Status',
        action: 'Action',
        download: 'Download',
        clearAll: 'Clear All',
        cancel: 'Cancel',
        score: 'score',
        veryUnreliable: 'Very unreliable',
        frequentlyUnavailable: 'Frequently unavailable',
        sometimesUnavailable: 'Sometimes unavailable',
        mostlyAvailable: 'Mostly available',
        veryReliable: 'Very reliable',
        noInfo: 'No resource availability information',
        preparePackage: 'Prepare package',
        packagesList: 'Packages list',
        listIsEmpty: 'Use search engine and choose links to download',
        files: 'File(s)',
        areCurrentlyUnavailableYouCan: 'are currently unavailable, you can',
        downloadPackageWithoutThisFiles:
            'download package without this file(s)',
        abort: 'abort',
        or: 'or',
        retry: 'retry',
        packagePreparation: 'package preparation.',
        requestTakesLongerThanExpected: 'Request takes longer than expected...',
    },

    survey: {
        question: 'Question',
        generalImpression: {
            question: 'What is your general impression of the portal?',
            positive: 'Very good',
            neutral: 'Fine',
            negative: 'Bad',
        },
        didYouFind: {
            thankYou: 'Thanks for your time!',
            question: 'Did you find what you were looking for?',
            yes: 'Yes',
            no: 'No',
            partially: 'Partially',
        },
        whatWereYouLookingFor: {
            question: 'What were you looking for?',
            placeholder: 'Type here...',
        },
        interest: {
            question1: 'How would you describe your interest',
            question2: 'as an Earth Observation user?',
            options: [
                {
                    label: 'Global - world-wide interest',
                    value: 'global',
                },
                {
                    label: 'National - country-specific interest',
                    value: 'national',
                },
                {
                    label: 'Regional - specific geographic interests within a country',
                    value: 'regional',
                },
                {
                    label: 'Local - community-specific geographic interests',
                    value: 'local',
                },
            ],
        },
        classification: {
            question: 'How would you classify yourself professionally?',
            options: [
                {
                    label: 'Scientist/Researcher',
                    value: 'scientist/researcher',
                },
                {
                    label: 'Decision Support Official',
                    value: 'decision support official',
                },
                {
                    label: 'Policy Analyst',
                    value: 'policy analyst',
                },
            ],
        },
        organized: {
            title: 'How strongly do you agree with the following statement?',
            question: 'I find the Portal site content logically organized',
            stronglyAgree: 'Strongly\nagree',
            stronglyDisagree: 'Strongly\ndisagree',
        },
        adequately: {
            title: 'How strongly do you agree with the following statement?',
            question:
                'I think that the information offered on the site is adequately described',
            stronglyAgree: 'Strongly\nagree',
            stronglyDisagree: 'Strongly\ndisagree',
        },
        search_criteria: {
            question:
                'Are there any other search criteria that would be helpful to you?',
            placeholder: 'Please write them here',
        },
        visualization: {
            question:
                'Are there any visualizations of data that might help you better understand what can be derived from the Portal?',
            placeholder: 'Please describe them here',
        },
        thankYouForCompleting: 'Thank you for completing this survey!',
        ok: 'OK',
        hideInFuture: "Don't show this again",
        submit: 'Submit',
        other: 'Other',
    },
    yellowPages: {
        title: 'Data providers',
        placeholderSearch: 'Search organizations',
        orderBy: 'Order by',
        nameAscending: 'Name ascending',
        nameDescending: 'Name descending',
        registrationDate: 'Registration date',
        readMore: 'Read more',
        readLess: 'Read less',
        sustainableDevelopmentGoals: 'The Sustainable Development Goals',
        societalBenefitAreas: 'The Societal Benefit Areas',
        geoAffiliation: 'The GEO Affiliation',
        dataPolicy: 'The Data Policy',
        geographicalCoverage: 'The geographical coverage',
        discoverable: 'Discoverable',
        accessible: 'Accessible',
        standard_encoding_using: 'Standard encoding using',
        well_documented_metadata: 'Well documented metadata',
        traceable: 'Traceable',
        quality_documented: 'Quality documented',
        preserved: 'Preserved',
        periodically_verified: 'Periodically verified',
        reviewed_and_refreshed: 'Reviewed and refreshed',
        tagged_with_permanent_ID: 'Tagged with permanent ID',
        no_poverty: 'No poverty',
        zero_hunger: 'Zero hunger',
        good_health_and_well_being: 'Good health and well-being',
        quality_education: 'Quality education',
        gender_equality: 'Gender equality',
        clean_water_and_sanitation: 'Clean water and sanitation',
        affordable_and_clean_energy: 'Affordable and clean energy',
        decent_work_and_economic_growth: 'Decent work and economic growth',
        industry_innovation_and_infrastructure:
            'Industry, innovation and infrastructure',
        reduced_inequalities: 'Reduced inequalities',
        sustainable_cities_and_communities:
            'Sustainable cities and communities',
        responsible_consumption_and_production:
            'Responsible consumption and production',
        climate_action: 'Climate action',
        life_below_water: 'Life below water',
        life_on_land: 'Life on land',
        peace_justice_and_strong_institutions:
            'Peace, justice and strong institutions',
        partnerships_for_the_goals: 'Partnerships for the goals',
        biodiversity_and_ecosystem_sustainability:
            'Biodiversity and Ecosystem Sustainability',
        disaster_resilience: 'Disaster Resilience',
        energy_and_mineral_resource_management:
            'Energy and Mineral Resource Management',
        food_security_and_sustainable_agriculture:
            'Food Security and Sustainable Agriculture',
        infrastructure_and_transportation_management:
            'Infrastructure and Transportation Management',
        public_health_surveillance: 'Public Health Surveillance',
        water_resources_management: 'Water Resources Management',
        sustainable_urban_development: 'Sustainable Urban Development',
    },
    bookmarks: {
        title: 'My workspace / Bookmarked results',
        selectAll: 'Select all',
        showOnMap: 'Show on map',
        delete: 'Delete',
        incompatibleDataSources: 'Incompatible data sources',
        desiredSource:
            'You can show on map items of one data source only. Choose your desired data source:',
        noItemsSelected: 'No items selected',
        selectItems: 'Please select at least one item from list.',
        accept: 'OK',
        yourListIsEmpty: 'Your list is empty.',
        recentViews: 'recent views',
        download: 'Download',
        checked: 'checked',
    },
    geoLikes: {
        title: 'My workspace / GEO Likes',
    },
    privacyPolicy: {
        statement:
            'We use cookies to track visits to our website only, no personal information is collected. If you wish to restrict or block our use of cookies, please follow the instructions set out in our Cookies Policy.',
        cookieLink: 'Cookie Notice',
        cookieText: 'for further information or to change your preferences.',
        accept: 'Accept',
        decline: 'Decline',
    },
    sendFeedback: 'Send feedback',
    tutorial: {
        mode: 'Tutorial mode',
        close: 'Close tutorial mode',
        new: 'new',
        tip: 'tip',
    },
    mapControls: {
        zoomIn: 'Zoom in',
        zoomOut: 'Zoom out',
        resetView: 'Reset view to default',
        areaOfInterest: 'Area of interest',
        layers: 'Layers',
        changeBasemap: 'Change basemap',
    },
    seeOtherSources: {
        sources: 'Sources',
    },
    wms: {
        _count: 'Count',
        _mean: 'Mean',
        _sum: 'Sum',
        adm0_code: 'ADM0 code',
        adm0_name: 'ADM0 name',
        area: 'Area',
        fid: 'FID',
        ld: 'LD',
        status: 'Status',
    },
    poweredBy: 'Powered by',
    feedback: {
        title: 'Send Feedback',
        email: 'Email',
        simpleOrEnhanced:
            'Would like to fill-in the Simple or Enhanced GEOSS user questionnaire?',
        start: 'Start',
        submit: 'Submit',
        clear: 'Clear form',
        feedback: 'Feedback',
        placeholder: 'Type here...',
        simpleFormTitle: 'Simple form',
        enhancedFormTitle: 'Enhanced form',
        nextPage: 'Next',
        other: 'Other',
        thankYou:
            'The Questionnaire has been submitted. Thank you for your time!',
        simpleForm: {
            firstName: 'First Name',
            wantTo: 'You want to',
            suggestRadio: 'Suggest an enhancement',
            report: 'Report a problem',
            suggestionTitle: 'Suggestion Title',
            suggestionDescription: 'Suggestion Description',
            problemTitle: 'Problem Title',
            problemDescription: 'Problem Description',
            severity: {
                label: 'Severity',
                options: [
                    {
                        label: 'Low',
                        value: 'low',
                    },
                    {
                        label: 'Medium',
                        value: 'medium',
                    },
                    {
                        label: 'High',
                        value: 'high',
                    },
                ],
            },
            enterText: 'Enter text from the picture',
        },
        enhancedForm: {
            title: 'The Enhanced GEOSS Portal User Questionnaire',
            introduction: 'Introduction',
            interface: 'The GEOSS Portal enhanced interface',
            page1: {
                title: 'General information',
                description:
                    'The GEOSS Portal User Questionnaire has been conceived to conduct a user assessment of the GEOSS Portal. The goal is to introduce users to the portal and evaluate the performance and capabilities of the portal interface.',
                descriptionCd:
                    'This questionnaire will provide the users’ perspective in three main areas of the GEOSS Portal:',
                li1: '• The overall organization and content;',
                li2: '• The navigation capabilities;',
                li3: '• And the functionalities (e.g., search capability, data access, etc.).',
                questionnaireHas: 'This questionnaire has three parts:',
                li4: '• The Entry Questions to determine who the end users are, what are their interests and if they used this Portal already;',
                li5: '• The Operational Questions determine the user’s experience regarding the GEOSS Portal overall organization and content, its navigational capabilities and its functionalities;',
                li6: '• And the Overall Impressions to determine the user’s impression of the GEOSS Portal and its features, and if they plan to use the GEOSS Portal again.',
                measure:
                    'We would like to measure several different usability aspects:',
                li7: '- effectiveness (can users successfully achieve their objectives);',
                li8: '- efficiency (how much effort and resource is expended in achieving those objectives);',
                li9: '- overall satisfaction (was the experience satisfactory).',
                qShouldBe:
                    'The questionnaire should be a living experience, so go ahead and open two tabs in your browser: one pointing to this questionnaire and the other on the actual portal (geoportal.org).',
                estimate:
                    'The estimated time to fulfill this survey is about 20 minutes.',
                thankYou:
                    'We thank you very much in advance for your time and contribution towards making this portal user-centric.',
                email: 'E-Mail Address',
            },
            page2: {
                title: 'ENTRY QUESTIONS – About You',
                description:
                    'The Entry Questions help us to determine who the end users are, what are their interests and if they have used this Portal already',
                interest: {
                    label: '1.1 How would you describe your interest as an Earth Observation user?',
                    options: [
                        {
                            label: 'Global - world-wide interest',
                            value: 'global',
                        },
                        {
                            label: 'National - country-specific interest',
                            value: 'national',
                        },
                        {
                            label: 'Regional - specific geographic interests within a country',
                            value: 'regional',
                        },
                        {
                            label: 'Local - community-specific geographic interests',
                            value: 'local',
                        },
                    ],
                },
                classify: {
                    label: '1.2 How would you classify yourself professionally?',
                    options: [
                        {
                            label: 'Scientist/Researcher',
                            value: 'scientist/researcher',
                        },
                        {
                            label: 'Decision Support Official',
                            value: 'decision support official',
                        },
                        {
                            label: 'Policy Analyst',
                            value: 'policy analyst',
                        },
                    ],
                },
                communityOfPractice: {
                    label: '1.3 What is your community of practice or societal benefit area of interest (e.g., agriculture, biodiversity, climate, disasters, ecosystems, energy, health, water or weather)?',
                },
                applicationInterests: {
                    label: '1.4 What are your application interests?',
                    options: [
                        {
                            label: 'Experienced user',
                            value: 'experienced user',
                        },
                        {
                            label: 'Casual/inexperienced user',
                            value: 'casual/inexperienced user',
                        },
                        {
                            label: 'Software development and/or data integration user',
                            value: 'software development/data integration user',
                        },
                    ],
                },
                visited: {
                    label: '1.5 Have you ever visited the GEOSS Portal?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                used: {
                    label: '1.6 Have you ever used any other Web sources for Earth observation data, services, and/or information?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                specify: {
                    label: '1.7 If yes, please specify other Web sources for Earth observation data, services, and/or information:',
                },
            },
            page3: {
                title: 'OPERATIONAL QUESTIONS',
                description:
                    'The Operational Questions give us an idea of the user’s experience of the GEOSS Portal, the overall organization and content, its navigational capabilities and functionalities',
                goTo: {
                    label: '2.1 Go to the GEOSS Portal',
                },
                tryToLocate: {
                    label: '2.2 Try to locate the "Park inn St. Petersburg" using an advanced query criteria. Were you successful?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                ifYouHaveTime: {
                    label: '2.2.1 If you have time, try to locate any other place, for example, your own or place of work. Were you successful?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                didYouFind: {
                    label: '2.3 Clear all the query criteria. Did you find a suitable icon button to do this?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                howManyResources: {
                    label: '2.4 How many resources can you find in South America dealing with deforestation?',
                },
                withReference: {
                    label: '2.5 With reference to the results from previous point, load on the map these two layers: LBA-ECO LC-14 Modeled Deforestation Scenarios, Amazon Basin, Governance Scenario 2047 and Business As Usual Scenario 2047. What can you conclude by looking at those layers?',
                },
                howManyResults: {
                    label: '2.6 Filter by the keyword "Biodiversity". How many results do you have?',
                },
                succedInDownloading: {
                    label: '2.7 Perform any search you like. Did you succeed in downloading any resources?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                tellUsAbout: {
                    label: '2.8 Tell us about this search experience; what did you search for? What did you download?',
                },
                goBack: {
                    label: '2.9 Go back to the GEOSS Portal',
                },
                tryToSignIn: {
                    label: '2.10 Try to sign in. Did you succeed?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                multiSearch: {
                    label: '2.11 Perform a multi-criteria search and then save the search. Did you find a suitable icon button to do that?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                tryToBookmark: {
                    label: '2.12 Try to bookmark a search result. Did you find a suitable icon button to do that?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                lookToWorkspace: {
                    label: '2.13 Have a look to My Workspace. Did you find whatever you saved in the previous steps?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                informationEasyToFind: {
                    label: '2.14 The information I was looking for was easy to find',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                searchToolsDetailed: {
                    label: '2.15 The search tools are detailed enough for the information/data or services I required',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                iDoNotNeedVideoTut: {
                    label: '2.16 I think I do not need Video Tutorials',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                contentLogicallyOrganized: {
                    label: '2.17 I find the Portal site content logically organized',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                adequatelyDescribed: {
                    label: '2.18 I think that the information offered on the site is adequately described',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                otherHelpfulSearchCriteria: {
                    label: '2.19 Are there any other search criteria that would be helpful to you?',
                },
                portalSpeed: {
                    label: '2.20 I find the Portal site to be responsive (speed)',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                anyVisualizationOfData: {
                    label: '2.21 Is there any visualization of data that might help you better understand what can be derived from the Portal? Please describe.',
                },
            },
            page4: {
                title: 'OVERALL IMPRESSIONS OF THE PORTAL',
                description:
                    'You are now confronted with statements to which you rate your level of agreement on a five-point scale (from “Strongly disagree” to “Strongly agree”).',
                usePortalFrequently: {
                    label: '3.1 I think that I would like to use the Portal frequently',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                unnecessarilyComplex: {
                    label: '3.2 I found the Portal unnecessarily complex',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                easyToUse: {
                    label: '3.3 I think that the Portal is easy to use ',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                needSupport: {
                    label: '3.4 I think that I would need the support of a technical person to be able to use the Portal',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                wellIntegratedFunctions: {
                    label: '3.5 I found the various functions in the Portal were well integrated',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                tooMuchInconsistency: {
                    label: '3.6 I think there is too much inconsistency in this system',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                learnQuickly: {
                    label: '3.7 I would imagine that most people would learn to use the Portal very quickly ',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                cumbersomeToUse: {
                    label: '3.8 I found the Portal very cumbersome to use',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                feltConfident: {
                    label: '3.9 I felt very confident using the Portal',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
                neededToLearn: {
                    label: '3.10 I needed to learn a lot of things before I could get going with the Portal',
                    options: [
                        {
                            label: '1',
                            value: '1',
                        },
                        {
                            label: '2',
                            value: '2',
                        },
                        {
                            label: '3',
                            value: '3',
                        },
                        {
                            label: '4',
                            value: '4',
                        },
                        {
                            label: '5',
                            value: '5',
                        },
                    ],
                },
            },
            page5: {
                portalFeature: {
                    label: '3.11 What single feature of the Portal was the most useful and easiest to use? Please explain.',
                },
                whichFeatures: {
                    label: '3.12 Which feature was the least useful and most difficult to use? Please explain.',
                },
                suggestions: {
                    label: '3.13 What suggestions would you make to improve the Portal?',
                },
                wouldReturn: {
                    label: '3.14 Would you return to this Portal site again?',
                    options: [
                        {
                            label: 'Yes',
                            value: 'yes',
                        },
                        {
                            label: 'No',
                            value: 'no',
                        },
                    ],
                },
                leaveQuote: {
                    label: '3.15 Please, leave us a quote about the Portal',
                },
            },
        },
    },
}
