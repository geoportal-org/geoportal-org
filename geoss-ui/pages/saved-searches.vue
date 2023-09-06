<template>
    <div>
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

                    <div>
                        <button class="red-btn-default" @click="deleteSavedSearch(savedSearch.id)">Delete</button>
                        <button class="blue-btn-default">Share</button>
                        <button class="blue-btn-default"
                            @click="highlightSavedSearch(savedSearch.id, savedSearch.phrase)">Publish</button>
                    </div>
                </li>
            </ul>
        </div>
    </div>
</template>

<script>
import UserAPI from '@/api/user';

export default {
    layout() {
        return 'default'
    },

    data() {
        return {
            savedSearches: null,
            showShare: false,
        }
    },

    methods: {
        createdDate(date) {
            return (new Date(date)).toLocaleString();
        },
        async updateList() {
            this.savedSearches = await UserAPI.getSavedSearchesByUser();
        },
        deleteSavedSearch(id) {
            UserAPI.deleteSavedSearch(id)
                .then(() => {
                    this.savedSearches = UserAPI.getSavedSearchesByUser();
                })
                .catch(() => {
                    console.log(error)
                });

        },

        highlightSavedSearch(id, name) {
            UserAPI.highlightSavedSearch(id, name)
                .then(() => {
                    console.log('Saved Search published');
                })
                .catch(() => {
                    console.log(error)
                });
        }
    },

    mounted() {
        this.updateList();
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
}
</style>
