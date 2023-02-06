import { Global } from "@emotion/react";

export const Fonts = () => (
    <Global
        styles={`
        @font-face {
            font-family: 'NotesEsa';
            font-style: normal;
            font-weight: normal;
            src: url("/fonts/NotesEsaReg.eot");
            src: url('/fonts/NotesEsaReg.otf') format('opentype');
        }

        @font-face {
            font-family: 'NotesEsa';
            font-style: italic;
            font-weight: normal;
            src: url('/fonts/NotesEsaRegIta.otf') format('opentype');
        }

        @font-face {
            font-family: 'NotesEsa';
            font-style: normal;
            font-weight: bold;
            src: url('/fonts/NotesEsaBol.otf') format('opentype');
        }

        @font-face {
            font-family: 'NotesEsa';
            font-style: italic;
            font-weight: bold;
            src: url('/fonts/NotesEsaBolIta.otf') format('opentype');
        }
    `}
    />
);
