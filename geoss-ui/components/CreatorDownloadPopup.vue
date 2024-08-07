
<template>
    <div class="community-portal-configuration">
        <div class="my-workspace-header">
            Community Portal Tool
        </div>
        <div class="community-portal-configuration__wrapper">
            <div class="my-workspace-tab my-workspace-content community-portal-configuration__sub">

                <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                <h1 class="creator__header">What is Community Portal?</h1>
                <p>
                    The Communit Portal is a section of the GEOSS Portal that enables communities to share specific and
                    predefined data, articles and studies as a thematic area part of the GEOSS Portal, customized se
                    relevant information is directly discoverable and accessible to the specific community.
                </p>
                <p>
                    After download the setup tool, follow the steps given in the instructions file included with the
                    installation files to greate the site.
                </p>
                <p>
                    Later you will have the possibility to decide to publis you site in the GEOSS Community Portal section.
                </p>
                <button class="green-btn-default" @click="">Download</button>
                <p>Any additional questions can be asked on our <a href="/tutorials" target="_blank">Contact Page</a></p>

            </div>
        </div>
    </div>
</template>

<script>
import { Component, Vue } from 'nuxt-property-decorator';

@Component
export default class CreatorDownloadPopupComponent extends Vue { }

</script>


<style lang="scss" scoped>
.community-portal-configuration {
    height: calc(100% - 70px);

    &__sub {
        display: flex;
        flex-direction: column;
        align-items: center;
        max-height: 100%;
    }

    &__wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
    }

    .creator {
        &__logo {
            display: inline-block;
            margin: 15px;
        }

        &__header {
            font-size: 2em;
            margin-bottom: 15px;
        }

        &__field {
            margin-bottom: 15px;

            label {
                display: block;
                font-weight: 700;
                font-size: 0.85em;
                margin: 3px 0;
            }

            input {
                display: block;
                border: 1px solid #ccc;
                padding: 3px 5px;
                width: 400px;
            }

            &.required {
                span {
                    color: $red;
                }
            }

            &.radio {
                display: flex;
                align-items: center;

                input {
                    width: 30px;
                }
            }
        }

        &__hint {
            padding: 10px;
            font-size: 0.85em;
            background: #EEF4F8;
        }

        &__buttons {
            width: 100%;
            display: flex;
            justify-content: space-between;
        }
    }

    p {
        margin-bottom: 15px;
        max-width: 500px;
    }

    .green-btn-default {
        margin: 15px 0 30px;

        &.inverted {
            background: white;
            border: 1px solid #209d90;
            color: #209d90;
        }
    }

    .view-options {
        display: block;
        width: 100%;
    }

}
</style>
