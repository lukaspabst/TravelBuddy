import {useTranslation} from "react-i18next";
import React from "react";
import TravelBackground from "../../General/Background/TravelBackground";
import './Trip.scss';
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";

function Trip() {
    const {t} = useTranslation();

    return (
        <AnimationFlash>
            <div className="StartPage-content">
                <TravelBackground/>
            </div>
        </AnimationFlash>
    );
}

export default Trip;
