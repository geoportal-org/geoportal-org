import Script from "next/script";
import React from "react";
import { v4 as uuidv4 } from "uuid";

type Props = {
    type?: string;
    data?: any;
    internalUrl?: boolean;
    portalUrl?: string;
};

const SchemaHeader = ({ type, data, internalUrl = false, portalUrl }: Props) => {
    const pageUrl = process.env.NEXT_PUBLIC_PAGE_URL || portalUrl || '';
    switch (type) {
        case "article":
            const parsedArticleBody = data.data.toString().replace(/"/g, '\\"');
            return (
                <Script
                    id={uuidv4()}
                    type="application/ld+json"
                    dangerouslySetInnerHTML={{
                        __html: `
                        {
                            "@context": "https://schema.org",
                            "@type": "Article",
                            "mainEntityOfPage": {
                                "@type": "WebPage",
                                "@id": "${pageUrl + "/news/" + data.slug}"
                            },
                            "headline": "${data.title}",
                            "datePublished": "${data.date.substring(0, 10)}",
                            "image": "${data.img}",
                            "publisher": {
                                "@type": "Organization",
                                "url": "${pageUrl}",
                                "logo": "${pageUrl + "/_next/image?url=%2Flogo.webp&w=64&q=75"}"
                            },
                            "articleBody": "${parsedArticleBody}"
                        }
                        `,
                    }}
                />
            );
        case "articles-list":
            return (
                <Script
                    id={uuidv4()}
                    type="application/ld+json"
                    dangerouslySetInnerHTML={{
                        __html: `
                        {
                            "@context": "https://schema.org",
                            "@type": "ItemList",
                            "itemListElement": [
                            ${data.map((news: any, index: number) => {
                                return `                                {
                                "@type": "ListItem",
                                "position": ${index + 1},
                                "item": {
                                    "@type": "Article",
                                    "headline": "${news.title}",
                                    "datePublished": "${news.date.substring(0, 10)}",
                                    "image": "${news.img}",
                                    "url": "${pageUrl + "/news/" + news.id}",
                                    "publisher": {
                                        "@type": "Organization",
                                        "url": "${pageUrl}",
                                        "logo": "${pageUrl + "/_next/image?url=%2Flogo.webp&w=64&q=75"}"
                                    },
                                    "description": "${news.text}"
                                    }
                                }`;
                            })}
                            ]
                        }
                        `,
                    }}
                />
            );

        case "items-list":
            return (
                <Script
                    id={uuidv4()}
                    type="application/ld+json"
                    dangerouslySetInnerHTML={{
                        __html: `
                            {
                                "@context": "https://schema.org",
                                "@type": "ItemList",
                                "itemListElement": [
                                    ${data.map((el: any, index: number) => {
                                        if (internalUrl) {
                                            return `                                
                                            {
                                            "@type": "ListItem",
                                            "position": ${index + 1},
                                            "name": "${el.text}",
                                            "url": "${pageUrl + el.href}"
                                            }
                                        `;
                                        } else {
                                            return `                                
                                            {
                                            "@type": "ListItem",
                                            "position": ${index + 1},
                                            "name": "${el.text}",
                                            "url": "${el.href}"
                                            }
                                        `;
                                        }
                                    })}
                                ]
                            }
                        `,
                    }}
                />
            );

        case "providers-list":
            return (
                <Script
                    id={uuidv4()}
                    type="application/ld+json"
                    dangerouslySetInnerHTML={{
                        __html: `
                                {
                                    "@context": "https://schema.org",
                                    "@type": "ItemList",
                                    "itemListElement": [
                                        ${data.map((el: any, index: number) => {
                                            const url = el.data.websiteInstitutionURL;
                                            return `                                
                                                {
                                                "@type": "ListItem",
                                                "position": ${index + 1},
                                                "name": "${el.name}",
                                                "url": "${url}"
                                                }
                                            `;
                                        })}
                                    ]
                                }
                            `,
                    }}
                />
            );
        case "faq":
            return (
                <Script
                    id={uuidv4()}
                    type="application/ld+json"
                    dangerouslySetInnerHTML={{
                        __html: `
                            {
                                "@context": "https://schema.org",
                                "@type": "FAQPage",
                                "mainEntity": [
                                    ${data.map((data: any, index: number) => {
                                        return `                                {
                                        "@type": "Question",
                                        "name": "${data.text}",
                                        "acceptedAnswer": {
                                            "@type": "Answer",
                                            "text": "${data.content}"
                                        }
                                        }`;
                                    })}
                                ]
                            }
                        `,
                    }}
                />
            );
        default:
            return;
    }
};

export default SchemaHeader;
