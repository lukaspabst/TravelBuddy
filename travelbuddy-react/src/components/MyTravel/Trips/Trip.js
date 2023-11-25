import { useTranslation } from "react-i18next";
import React, { useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import TravelBackground from "../../General/Background/TravelBackground";
import './Trip.scss';
import AnimatedTripCard from "../../../Containers/Animations/PageTransitionAnimations/AnimationTrip";
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";

function Trip() {
    const { t } = useTranslation();

    return (
        <AnimationFlash>
            <div className="StartPage-content">
                <TravelBackground />
            </div>
        </AnimationFlash>
    );
}

export default Trip;
