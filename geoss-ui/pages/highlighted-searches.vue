<template>
    <div v-if="loaded">
        <client-only>
            <div class="my-workspace-header">
                Highlighted Searches
                <NuxtLink to="/" class="close-window">
                    <div class="line-1"></div>
                    <div class="line-2"></div>
                </NuxtLink>
            </div>
            <div class="my-workspace-tab my-workspace-content highlighted-searches">
                <div v-if="!highlightedSearches">Loading...</div>
                <div v-if="highlightedSearches && !highlightedSearches.length">There are no Highlighted Searches.</div>
                <ul v-else>
                    <li v-for="highlightedSearch of highlightedSearches" :key="highlightedSearch.id">
                        <div>
                            <a :href="highlightedSearch.url" class="close-window">
                                <IconSearchEarth />
                                <div>
                                    <span>
                                        {{ highlightedSearch.phrase }}
                                    </span>
                                    <small>
                                        Created on: {{ createdDate(highlightedSearch.createdOn) }}
                                    </small>
                                </div>
                            </a>
                        </div>

                        <div v-if="$auth.loggedIn">
                            <button v-if="!highlightedSearch.defaultSearch" class="blue-btn-default"
                                @click="defaultHighlightedSearch(highlightedSearch)">Default</button>
                            <button class="red-btn-default"
                                @click="deleteHighlightedSearch(highlightedSearch.id)">Delete</button>
                            <button v-if="highlightedSearch.enabled" class="blue-btn-default"
                                @click="toggleHighlightedSearch(highlightedSearch)">Disable</button>
                            <button v-if="!highlightedSearch.enabled" class="blue-btn-default"
                                @click="toggleHighlightedSearch(highlightedSearch)">Enable</button>
                        </div>
                    </li>
                </ul>
            </div>
        </client-only>
    </div>
</template>

<script>
import UserAPI from '@/api/user';
import NotificationService from '@/services/notification.service'

export default {
    layout() {
        return 'default'
    },

    data() {
        return {
            loaded: false,
            highlightedSearches: null,
            showShare: false,
        }
    },

    methods: {
        notification(message, type = 'success') {
            const title = type === 'success' ? `Highlighted Searches` : `${this.$tc('general.errorOccurred')}`
            NotificationService.show(
                title,
                message,
                10000,
                null,
                9999,
                type
            );
        },
        prepareErrorMessage(error) {
            if (error.response && error.response.data && error.response.data.cause && error.response.data.cause.cause && error.response.data.cause.cause.message) {
                return error.response.data.cause.cause.message
            } else {
                return error
            }
        },
        createdDate(date) {
            return (new Date(date)).toLocaleString();
        },
        async updateList() {
            this.highlightedSearches = await UserAPI.getHighlightedSearches(this.$auth.loggedIn);
        },
        getUpdateData(highlightedSearch) {
            return {
                id: highlightedSearch.id,
                data: {
                    name: highlightedSearch.name,
                    phrase: highlightedSearch.phrase,
                    url: highlightedSearch.url,
                    enabled: highlightedSearch.enabled,
                    defaultSearch: highlightedSearch.defaultSearch
                }
            }
        },
        deleteHighlightedSearch(id) {
            UserAPI.deleteHighlightedSearch(id)
                .then(() => {
                    this.notification('Your Highlighted Seaerch has beed successfully deleted')
                    this.updateList();
                })
                .catch((error) => {
                    this.notification(this.prepareErrorMessage(error), 'error')
                });
        },
        defaultHighlightedSearch(highlightedSearch) {
            for (const search of this.highlightedSearches) {
                search.defaultSearch = false
                this.updateHighlightedSearch(search)
            }
            highlightedSearch.defaultSearch = !highlightedSearch.defaultSearch;
            if (highlightedSearch.defaultSearch) {
                highlightedSearch.enabled = true
            }
            this.updateHighlightedSearch(highlightedSearch)
        },
        toggleHighlightedSearch(highlightedSearch) {
            highlightedSearch.enabled = !highlightedSearch.enabled;
            this.updateHighlightedSearch(highlightedSearch)
        },
        async updateHighlightedSearch(highlightedSearch) {
            const { id, data } = this.getUpdateData(highlightedSearch);
            await UserAPI.updateHighlightedSearch(id, data);
        }
    },

    mounted() {
        this.updateList();
        this.loaded = true;
    }
}
</script>

<style lang="scss" scoped>
.highlighted-searches {
    ul {
        li {
            padding: 5px 15px;
            border: 1px solid $grey-dark;
            margin-bottom: -1px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;

            >div {
                margin: 10px 0;
            }

            a {
                display: flex;
                align-items: center;
                color: $black;

                &:hover {
                    text-decoration: none;
                    color: $blue;
                }

                svg {
                    width: 32px;
                    height: 32px;
                    margin-right: 10px;
                    fill: $blue;
                }

                div {
                    display: flex;
                    flex-direction: column;

                    span {
                        font-size: 1.2em;
                        margin-bottom: 5px;
                    }

                    small {
                        font-size: 0.85em;
                        color: $grey-darker;
                    }
                }
            }

            button {
                font-size: 1em;
            }
        }
    }
}
</style>
