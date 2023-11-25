import {useTranslation} from "react-i18next";
import {Link} from "react-router-dom";
import {Button} from "../../../Containers/Button/Button";
import React from "react";
import TravelBackground from "../../General/Background/TravelBackground";
import './MyTravels.scss';
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";
function MyTravels() {
    const { t } = useTranslation();

    return (
        <AnimationFlash>
        <div className="StartPage-content">
            <TravelBackground />
            <div className="button-container">
                <Link to="/openTravels">
                    <Button buttonStyle="btn--large-image">{t('myTravels.openTravels')}</Button>
                </Link>
                <Link to="/travel">
                    <Button buttonStyle="btn--large-image">{t('myTravels.createTrip')}</Button>
                </Link>
                <Link to="/pastTravels">
                    <Button buttonStyle="btn--large-image">{t('myTravels.closedTravels')}</Button>
                </Link>
            </div>
        </div>
        </AnimationFlash>
    );
}

export default MyTravels;
