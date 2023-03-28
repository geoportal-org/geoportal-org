/** @type {import('next').NextConfig} */
const nextConfig = {
    reactStrictMode: false,
    i18n: {
        locales: ["en", "pl", "es", "zh", "fr", "ru"],
        defaultLocale: "en",
    },
};

module.exports = nextConfig;
