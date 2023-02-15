import { AppVueObj, Liferay } from '@/data/global'
import { UserActions } from '@/store/user/user-actions'

const LiferayService = {
    init() {
        AppVueObj.app.$store.dispatch(
            UserActions.setId,
            Liferay.ThemeDisplay.getUserId()
        )
        AppVueObj.app.$store.dispatch(
            UserActions.setIsSignedIn,
            Liferay.ThemeDisplay.isSignedIn()
        )
        AppVueObj.app.$store.dispatch(
            UserActions.setAuthToken,
            Liferay.authToken
        )
        AppVueObj.app.$store.dispatch(
            UserActions.setGroupId,
            Liferay.ThemeDisplay.getScopeGroupId()
        )
    },
}

export default LiferayService
