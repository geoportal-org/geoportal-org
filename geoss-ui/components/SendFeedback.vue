<template>
  <a class="send-feedback" href="/community/guest/feedback">
    <i class="icomoon-envelope"></i>
    <span>{{ $t("sendFeedback") }}</span>
  </a>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';

@Component
export default class SendFeedbackComponent extends Vue {}
</script>

<style lang="scss" scoped>
.send-feedback {
  position: fixed;
  bottom: 10px;
  right: 41px;
  z-index: 10;
  color: white;
  display: flex;
  align-items: center;
  font-size: 13px;
  text-decoration: none;

  @media (max-width: $breakpoint_sm) {
    bottom: 40px;
  }

  @media (max-width: $breakpoint_xs) {
    bottom: 50px;
    font-size: 0;
    right: 12px;
  }

  span,
  i {
    text-shadow: $text-shadow-black-darker;
  }

  i {
    margin-right: 5px;
    font-size: 18px;

    @media (max-width: $breakpoint_xs) {
      margin-right: 0px;
      font-size: 32px;
    }
  }

  &:hover {
    span {
      text-decoration: underline;
      color: #fff;
    }

    i {
      animation: wobble 0.5s both;
    }
  }
}

.yellow-pages,
.bookmarks {
  .send-feedback {
    @media (max-width: $breakpoint_xs) {
      font-size: 13px;
      bottom: 10px;
      right: 41px;
    }

    i {
      @media (max-width: $breakpoint_xs) {
        margin-right: 5px;
        font-size: 18px;
      }
    }
  }
}
</style>
