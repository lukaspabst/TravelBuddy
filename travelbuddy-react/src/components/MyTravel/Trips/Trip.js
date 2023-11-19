import {useTranslation} from "react-i18next";
import React from "react";
import TravelBackground from "../../General/Background/TravelBackground";
import './Trip.scss';

function Trip() {
    const { t } = useTranslation();

    return (
        <div className="StartPage-content">
            <TravelBackground />
        </div>
    );
}

export default Trip;
