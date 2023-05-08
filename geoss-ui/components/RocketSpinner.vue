<template>
    <div class="earch-rocket-spinner">
        <div class="earch-rocket-spinner-wrapper">
            <img class="earth-animation" src="/img/earth-animation.gif" alt="Earth animation loading" />
            <img class="rocket-animation" src="/img/rocket-animation.png" alt="Rocket animation loading" />
        </div>
    </div>
</template>

<style lang="scss">
@-webkit-keyframes rocket-spinning {
    from {
        -webkit-transform: rotate(0deg);
        transform: rotate(0deg);
    }

    to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
    }
}

@keyframes rocket-spinning {
    from {
        -webkit-transform: rotate(0deg);
        transform: rotate(0deg);
    }

    to {
        -webkit-transform: rotate(360deg);
        transform: rotate(360deg);
    }
}

.earch-rocket-spinner {
    position: fixed;
    width: 100%;
    height: 100%;
    background: black;
    left: 0;
    top: 0;
    z-index: 9999;
    -webkit-transition: opacity 450ms ease-in-out, transform 0 450ms ease-in-out;
    -o-transition: opacity 450ms ease-in-out, transform 0 450ms ease-in-out;
    transition: opacity 450ms ease-in-out, transform 0 450ms ease-in-out;
}

.earch-rocket-spinner.inactive {
    opacity: 1;
    transform: translateX(-200%);
}

.earch-rocket-spinner-wrapper {
    position: absolute;
    width: 350px;
    height: 350px;
    left: calc(50% - 175px);
    top: calc(50% - 175px);
}

.earth-animation {
    position: absolute;
    top: 50%;
    left: 50%;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
}

.rocket-animation {
    position: absolute;
    -webkit-animation: rocket-spinning 4s infinite;
    animation: rocket-spinning 4s infinite;
    -webkit-animation-timing-function: linear;
    animation-timing-function: linear;
}
</style>
