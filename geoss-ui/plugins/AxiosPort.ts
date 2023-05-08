import { setClient } from '@/api/apiClient'

export default ({ app, store }: any) => {
    setClient(app.$axios)
}
