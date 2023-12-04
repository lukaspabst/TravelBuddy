import React, {useEffect, useState} from 'react';
import axios from 'axios';
import TravelBackground from "../../General/Background/TravelBackground";
import {API_BASE_URL} from "../../../config";
import {TripDTO} from "../TripDTO";
import './OpenTrips.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPerson} from "@fortawesome/free-solid-svg-icons";
import {useTranslation} from "react-i18next";
import {Link, useNavigate} from "react-router-dom";
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";

function OpenTrips() {
    const [userTrips, setUserTrips] = useState([]);
    const {t, i18n} = useTranslation();
    const navigation = useNavigate();
    const handleTripClick = (trip) => {
        localStorage.setItem('selectedTrip', JSON.stringify(trip));
    };
    useEffect(() => {
        const fetchUserTrips = async () => {
            try {
                const response = await axios.get(`${API_BASE_URL}/api/trips/userTrips/open`, {withCredentials: true});
                const tripsData = response.data;
                const formattedTrips = tripsData.map((trip) => {
                    return new TripDTO(
                        trip.id,
                        trip.nameTrip,
                        trip.maxPersons,
                        trip.startdate,
                        trip.enddate
                    );
                });
                setUserTrips(formattedTrips);

            } catch (error) {
                console.error('Error fetching user trips:', error);
            }
        };

        fetchUserTrips();
    }, []);

    return (
        <AnimationFlash>
            <div className="StartPage-container">
                <TravelBackground/>
                <div className="trips-container">
                    <h2>{t('myTravels.yourTrips')}</h2>
                    <br/>
                    {userTrips.map((trip) => (
                        <Link
                            to={{
                                pathname: `/trip/${trip.id}`
                            }}
                            onClick={() => handleTripClick(trip)}
                        >
                            <div key={trip.id} className="trip-card">
                                <div className="trip-info">
                                    <div className="placeholder">
                                        <img src="/assets/ImagePlaceHolder.png" alt="Placeholder"/>
                                    </div>
                                    <div className="trip-name">{trip.name}</div>
                                    <div className="max-persons">
                                        <i data-number={trip.maxPersons}>
                                            <FontAwesomeIcon icon={faPerson} size="2xl"/>
                                        </i>
                                    </div>
                                    <div className="dates">
                                        <div>{trip.startDate || 'N/A'}</div>
                                        <div>{trip.endDate || 'N/A'}</div>
                                    </div>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            </div>
        </AnimationFlash>
    );
}

export default OpenTrips;
