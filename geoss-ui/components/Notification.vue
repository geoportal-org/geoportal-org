<template>
    <div class="notifications" :style="zIndex">
        <transition-group name="notification">
            <div class="notification" v-for="notification of notifications" :key="notification.id" :style="`top: ${notification.top}px;`" :class="notification.style">
                <div v-if="notification.title" class="notification__title">
                    <span>{{ notification.title }}</span>
                    <button class="cross" @click="closeNotification(notification.id)"></button>
                </div>
                <div v-html="notification.text"></div>
            </div>
        </transition-group>
    </div>
</template>

<script lang="ts">
// @ts-nocheck
import { Component, Vue } from 'nuxt-property-decorator';

import NotificationService from '@/services/notification.service';

@Component
export default class NotificationComponent extends Vue {
    public notifications: any = [];

    get zIndex() {
        const zIndexNoitifcations = this.notifications.filter(notification => !!notification.zIndex).map(notification => notification.zIndex);
        const maxZIndex = Math.max(...zIndexNoitifcations);
        if (zIndexNoitifcations.length && maxZIndex) {
            return `z-index: ${maxZIndex}`;
        }
        return '';
    }

    public closeNotification(id) {
        this.notifications = this.notifications.filter(notification => notification.id !== id);

        clearTimeout(this.notifications.timeout);

        this.$nextTick(() => {
            for (let i = 0; i < this.notifications.length; i++) {
                this.notifications[i].top = this.calculateTopPosition(i);
            }
        });
    }

    private mounted() {
        NotificationService.eventBus.$on('show', data => {
            const existingNotification = this.notifications.find(notification => notification.id === data.id);
            if (existingNotification) {
                clearTimeout(existingNotification.timeout);
                existingNotification.timeout = setTimeout(() => {
                    this.closeNotification(existingNotification.id);
                }, data.duration);
            } else {
                data.top = this.calculateTopPosition();

                data.id = data.id || Math.random();

                this.notifications.push(data);

                data.timeout = setTimeout(() => {
                    this.closeNotification(data.id);
                }, data.duration);
            }
        });
    }

    private calculateTopPosition(index?: number) {
        const notificationElems = this.$el.querySelectorAll('.notification');
        let top = 0;
        for (let i = 0; i < notificationElems.length; i++) {
            if (typeof index !== 'undefined' && index === i) {
                break;
            }

            const elem = notificationElems[i] as HTMLElement;
            top += elem.offsetHeight;

            const marginBottom = parseInt(window.getComputedStyle(elem).marginBottom, 10);

            if (!Number.isNaN(marginBottom)) {
                top += marginBottom;
            }
        }

        return top;
    }
}
</script>

<style lang="scss">
.notifications {
    position: absolute;
    left: 15px;
    top: 100px;
    z-index: 9994;
}

.notification {
    background: rgba(254, 254, 254, 0.9);
    box-shadow: 0px 0px 8px black;
    border: 1px solid black;
    border-radius: 3px;
    color: black;
    padding: 10px;
    width: 280px;
    margin-bottom: 15px;
    position: absolute;
    line-height: 23px;
    left: 0;
    top: 0;
    transition:
        transform 450ms ease-in-out,
        opacity 450ms ease-in-out,
        top 450ms ease-in-out;

    &-enter,
    &-leave-to {
        opacity: 0;
        transform: translateY(-100%);
    }

    &.info {
        background-color: $blue-transparent;
    }

    &.success {
        background-color: $green-transparent;
    }

    &.error {
        background-color: $red-transparent;
    }

    &.info,
    &.success,
    &.error {
        color: white;

        a {
            color: white;
            text-decoration: underline;
        }
    }

    &__title {
        font-size: 20px;
        margin-bottom: 5px;
        font-weight: bold;
        position: relative;
        padding-right: 30px;

        button {
            right: -5px;
            top: -2px;
            position: absolute;

            &:before,
            &:after {
                background-color: $grey-darker;
                width: 20px;

                .notification.info &,
                .notification.success &,
                .notification.error & {
                    background-color: white;
                }
            }
        }
    }
}
</style>
