import i18n from 'i18next';
import {initReactI18next} from 'react-i18next';
import languageNames from './translations/languageNames.json';

i18n
    .use(initReactI18next)
    .init({
        resources: {
            gb: {//default language (is English, geändert in gb für Flagge)
                translation: require('./translations/en.json')
            },
            de: {
                translation: require('./translations/de.json')
            },
            jp: {
                translation: require('./translations/jp.json')
            },
        },
        fallbackLng: 'gb',
        interpolation: {
            escapeValue: false,
        },
        lng: localStorage.getItem('selectedLanguage') || 'gb',
    });

export {languageNames};
export default i18n;
