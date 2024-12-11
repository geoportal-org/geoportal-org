import { default as Router } from "next/router";

declare global {
  interface Window {
    _paq: any;
  }
}

interface InitSettings {
  url: string;
  siteId: string;
}

interface Dimensions {
  dimension1?: string;
  dimension2?: string;
  dimension3?: string;
  dimension4?: string;
  dimension5?: string;
  dimension6?: string;
  dimension7?: string;
  dimension8?: string;
  dimension9?: string;
  dimension10?: string;
}

// to push custom events
export function push(
  args: (
    | Dimensions
    | number[]
    | string[]
    | number
    | string
    | null
    | undefined
  )[]
): void {
  if (!window._paq) {
    window._paq = [];
  }
  window._paq.push(args);
}

export function initMatomo({url, siteId} : InitSettings){
  const _paq = (window._paq = window._paq || []);
  _paq.push(['trackPageView']);
  _paq.push(['enableLinkTracking']);
  _paq.push(['setTrackerUrl', `${url}/matomo.php`]);
  _paq.push(['setSiteId', siteId]);

  const script = document.createElement('script');
  script.async = true;
  script.src = `${url}/matomo.js`;
  document.head.appendChild(script);
}