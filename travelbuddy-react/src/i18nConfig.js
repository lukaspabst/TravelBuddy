import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

i18n
    .use(initReactI18next)
    .init({
        resources: {
            en: {
                translation: require('./translations/en.json')
            },
            de: {
                translation: require('./translations/de.json')
            },
            jap: {
                translation: require('./translations/jap.json')
            },
        },
        fallbackLng: 'en',
        interpolation: {
            escapeValue: false,
        },
    });

export default i18n;
