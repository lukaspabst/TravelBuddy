import {useTranslation} from "react-i18next";
import React, {useEffect, useState} from "react";
import TravelBackground from "../../../General/Background/TravelBackground";
import './Trip.scss';
import AnimationFlash from "../../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";
import AnimationTrip from "../../../../Containers/Animations/PageTransitionAnimations/AnimationTrip";
import AnimationLogo from "../../../../Containers/Animations/PageTransitionAnimations/AnimationLogo";
import TripGeneralInfo from "../TripTabs/TripGeneralInfo";
import TripDayInfo from "../TripTabs/TripDayInfo";
import {TripDTO} from "../../TripDTO";

function calculateDurationAndDays(startDate, endDate) {
    const oneDay = 1000 * 60 * 60 * 24;
    const diffInMilliseconds = Math.abs(new Date(endDate) - new Date(startDate));
    return Math.round(diffInMilliseconds / oneDay);
}

function Trip() {
    const { t } = useTranslation();
    const [animationComplete, setAnimationComplete] = useState(false);
    const [startDate, setStartDate] = useState(null);
    const [endDate, setEndDate] = useState(null);
    const [activeTab, setActiveTab] = useState("General");
    const [tripDTO, setTripDTO] = useState(null);

    useEffect(() => {
        const selectedTrip = localStorage.getItem('selectedTrip');
        if (selectedTrip) {
            const tripData = JSON.parse(selectedTrip);
            const tripDTO = new TripDTO(
                tripData.id,
                tripData.nameTrip,
                tripData.maxPersons,
                tripData.startDate,
                tripData.endDate
            );
            setTripDTO(tripDTO);
            setStartDate(tripDTO.startDate);
            setEndDate(tripDTO.endDate);
        }
    }, []);


    const durationInDays = calculateDurationAndDays(startDate, endDate);
    const handleTabClick = (tabName) => {
        setActiveTab(tabName);
    };

    return (
        <>
            {!animationComplete &&
                <AnimationTrip onAnimationComplete={() => setAnimationComplete(true)}/>
            }
            <div className="StartPage-content">
                <TravelBackground/>
                <div className="trip-overview-container">
                    <div className="header-of-tripOverview">
                        <div className={activeTab === 'General' ? 'active' : ''}
                             onClick={() => handleTabClick("General")}> General
                        </div>
                        {[...Array(durationInDays).keys()].map(day =>
                            <div key={day} className={activeTab === `Day ${day + 1}` ? 'active' : ''}
                                 onClick={() => handleTabClick(`Day ${day + 1}`)}>
                                Day {day + 1}
                            </div>
                        )}
                    </div>
                    <div className="trip-overview-content">
                        {activeTab === "General" && tripDTO && (
                            <TripGeneralInfo tripData={tripDTO}/>
                        )}
                        {activeTab.startsWith("Day") && (
                            <TripDayInfo dayNumber={activeTab.split(" ")[1]}/>
                        )}
                    </div>
                </div>
            </div>
        </>
    )
        ;
}

export default Trip;