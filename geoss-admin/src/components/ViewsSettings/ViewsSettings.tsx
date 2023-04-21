import { Loader, MainContent } from "@/components";
import { ViewsService } from "@/services/api/settings/ViewsService";
import { useEffect, useState } from "react";

//TBD

export const ViewsSettings = () => {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        handlePaginationParamsChange();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    const handlePaginationParamsChange = async () => {
        try {
            const {
                _embedded: { views },
                page,
            } = await ViewsService.getViewsList();
            console.log(views);
            console.log(page);
        } catch (e) {
            console.error(e);
        } finally {
            setIsLoading(false);
        }
    };

    const onAddViewAction = () => {
        console.log("add");
    };

    const headingActions = [
        {
            titleId: "pages.views.add",
            onClick: () => onAddViewAction(),
        },
    ];

    if (isLoading) {
        return <Loader />;
    }

    return (
        <MainContent titleId="nav.settings.section.views" actions={headingActions}>
            Views Settings
        </MainContent>
    );
};
