import React from 'react';
import './StartPage.scss';
import { Button } from '../Button/Button'
import {useTranslation} from "react-i18next";
import BackgroundImage from "../Background/StartingBackground";
function StartPage() {

    const { t } = useTranslation();

    return (
        <div className="StartPage-content">
            <BackgroundImage />
            <div className="button-container">
                <Button buttonStyle="btn--large-image">{t('home.startingButton')}</Button>
            </div>
       </div>
    );
}

export default StartPage;
