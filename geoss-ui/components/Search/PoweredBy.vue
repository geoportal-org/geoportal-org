<template>
	<div class="powered-by">
		<span class="powered-by__title">{{ $t('poweredBy') }}</span>
        <img class="powered-by__image" :src="`${staticPath()}/img/geoss-portal-small.png`" :alt="`${$t('poweredBy')} GEOSS`" />
	</div>
</template>

<script lang="ts">
import { Component, Prop, Vue, Emit, Watch } from 'vue-property-decorator';

@Component
export default class PoweredByComponent extends Vue {

}
</script>

<style lang="scss" scoped>
.powered-by {
    display: flex;
    padding: 10px 0;
    flex-wrap: nowrap;
    align-items: center;
    flex-grow: 1;
    flex-basis: 0;
    justify-content: center;
    
    @media (max-width: $breakpoint-md) {
        order: 3;
        min-width: 100%;
    }

    &__title {
        font-size: 12px;
        color: $white;
        margin-right: 5px;
        white-space: nowrap;
    }

    &__image {
        max-width: 50px;
    }
}
</style>
