
<template>
    <div class="community-portal-configuration">
        <div class="my-workspace-header">
            Community Portal Tool
        </div>
        <div class="community-portal-configuration__wrapper">
            <div class="my-workspace-tab my-workspace-content community-portal-configuration__sub">

                <img class="creator__logo" src="/img/geoss-logo-blue.png" />
                <h1 class="creator__header">Register Community Portal</h1>
                <p>
                    To register your portal in GEOSS and thus become an officially recognized GEOSS Community Portal, fill
                    in all fields with the answears.
                </p>

                <div class="creator__field required">
                    <label>Mirror Site URL address: <span>*</span></label>
                    <input v-model="address" placeholder="Type here..." type="text" ref="address" />
                </div>
                <div class="creator__field required">
                    <label>Name and surname: <span>*</span></label>
                    <input v-model="name" placeholder="Type here..." type="text" ref="name" />
                </div>
                <div class="creator__field required">
                    <label>E-mail: <span>*</span></label>
                    <input v-model="email" placeholder="Type here..." type="email" ref="email" />
                </div>
                <div class="creator__field">
                    <label>Why do you want to register you Community Portal?</label>
                    <textarea />
                </div>
                <button class="green-btn-default" @click="close()">Submit</button>
            </div>
        </div>
    </div>
</template>

<script>
import { Component, Vue } from 'nuxt-property-decorator';
import PopupCloseService from '@/services/popup-close.service'

@Component
export default class CreatorRegisterPopupComponent extends Vue {
    close() {
        PopupCloseService.closePopup('creator')
    }
}

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
                font-weight: bold;
            }

            input,
            textarea {
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
        margin: 15px 0;
        max-width: 400px;
        text-align: center;
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
