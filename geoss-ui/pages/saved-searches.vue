<template>
    <div v-if="loaded">
        <client-only>
            <div class="sub-page">
                <div class="sub-page__content">
                    <div class="my-workspace-header">
                        My Workspace / Your Saved Searches
                        <NuxtLink to="/" class="close-window">
                            <div class="line-1"></div>
                            <div class="line-2"></div>
                        </NuxtLink>
                    </div>
                    <div class="my-workspace-tab my-workspace-content saved-searches">
                        <div v-if="!savedSearches">Loading...</div>
                        <div v-if="savedSearches && !savedSearches.length">You have no Saved Searches yet.</div>
                        <ul v-else>
                            <li v-for="savedSearch of savedSearches" :key="savedSearch.id">
                                <div>
                                    <a :href="savedSearch.url" class="close-window">
                                        <IconSearchEarth />
                                        <div>
                                            <span>
                                                {{ savedSearch.phrase }}
                                            </span>
                                            <small>
                                                Created on: {{ createdDate(savedSearch.createdOn) }}
                                            </small>
                                        </div>
                                    </a>
                                </div>

                                <div class="saved-searches__buttons-wrapper">
                                    <button class="red-btn-default" @click="deleteSavedSearch(savedSearch.id)">Delete</button>
                                    <button class="blue-btn-default"
                                        @click="highlightSavedSearch(savedSearch.id, savedSearch.phrase)">Publish</button>
                                    <Share :url="$config.baseUrl + savedSearch.url" :survey="false" />
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </client-only>
    </div>
</template>

<script>
import UserAPI from '@/api/user'
import NotificationService from '@/services/notification.service'

export default {
    data() {
        return {
            loaded: false,
            savedSearches: null,
            showShare: false,
        }
    },

    methods: {
        notification(message, type = 'success') {
            const title = type === 'success' ? `Saved Searches` : `${this.$tc('general.errorOccurred')}`
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
            this.savedSearches = await UserAPI.getSavedSearchesByUser();
        },
        deleteSavedSearch(id) {
            UserAPI.deleteSavedSearch(id)
                .then(() => {
                    this.notification('Your Saved Seaerch has beed successfully deleted')
                    this.updateList();
                })
                .catch((error) => {
                    this.notification(this.prepareErrorMessage(error), 'error')
                });
        },

        highlightSavedSearch(id, name) {
            UserAPI.highlightSavedSearch(id, name)
                .then(() => {
                    this.notification('Your Saved Seaerch has beed successfully passed to Highlighted Searches and is waiting for administrator acceptance')
                })
                .catch((error) => {
                    this.notification(this.prepareErrorMessage(error), 'error')
                });
        },
        shareSavedSearch(savedSearchUrl) {
            console.log(savedSearchUrl)
        }
    },

    mounted() {
        this.updateList();
        this.loaded = true;
    }
}
</script>

<style lang="scss" scoped>
.saved-searches {
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

    &__buttons-wrapper {
        display: flex;
        gap: 5px;
    }
}
</style>
