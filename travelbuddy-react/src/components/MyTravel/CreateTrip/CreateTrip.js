import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './CreateTrip.scss';
import { useTranslation } from 'react-i18next';
import axios from 'axios';

import TravelBackground from "../../General/Background/TravelBackground";
import {API_BASE_URL} from "../../../config";
import {Button} from "../../../Containers/Button/Button";

function CreateTrip() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const [state, setState] = useState({
        tripName: '',
        startDate: '',
        endDate: '',
        destination: '',
        maxPersons: '',
        maxPrice: '',
    });

    const handleChange = (e) => {
        setState({ ...state, [e.target.name]: e.target.value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const { tripName, startDate, endDate, destination, maxPersons, maxPrice } = state;



        try {
            const response = await axios.post(
                `${API_BASE_URL}/api/trips`,
                {
                    tripName,
                    startDate,
                    endDate,
                    destination,
                    maxPersons,
                    maxPrice
                },
                { withCredentials: true }
            );

            if (response.status === 201) {

                navigate('/MyTrips');
            } else if (response.status === 400) {
                setState({ ...state, message: t('errorMessages.badRequest') });
            } else if (response.status === 500) {
                setState({ ...state, message: t('errorMessages.internalServerError') });
            } else {
                setState({ ...state, message: t('errorMessages.createTripError') });
            }
        } catch (error) {
            setState({ ...state, message: t('errorMessages.createTripError') });
        }
    };

    return (
        <CreateTripFormContent
            handleSubmit={handleSubmit}
            handleChange={handleChange}
            state={state}
            t={t}
        />
    );
}

function CreateTripFormContent({ handleSubmit, handleChange, state, t }) {
    return (
        <div className="StartPage-content">
            <TravelBackground />
            <div className="create-trip-form-container">
                <h1>{t('createTrip.title')}</h1>
                <form onSubmit={handleSubmit}>
                    <label htmlFor="tripName">{t('createTrip.tripNameLabel')}</label>
                    <input
                        type="text"
                        id="tripName"
                        name="tripName"
                        required
                        value={state.tripName}
                        onChange={handleChange}
                    />
                    <div className="create-trip-form-container-nextto">
                        <div>
                            <label htmlFor="startDate">{t('createTrip.startDateLabel')}</label>
                            <input
                                type="date"
                                id="startDate"
                                name="startDate"
                                required
                                value={state.startDate}
                                onChange={handleChange}
                            />
                        </div>
                        <div>
                            <label htmlFor="endDate">{t('createTrip.endDateLabel')}</label>
                            <input
                                type="date"
                                id="endDate"
                                name="endDate"
                                required
                                value={state.endDate}
                                onChange={handleChange}
                            />
                        </div>
                    </div>
                    <label htmlFor="destination">{t('createTrip.destinationLabel')}</label>
                    <input
                        type="text"
                        id="destination"
                        name="destination"
                        required
                        value={state.destination}
                        onChange={handleChange}
                    />
                    <label htmlFor="maxPersons">{t('createTrip.maxPersonsLabel')}</label>
                    <input
                        type="number"
                        id="maxPersons"
                        name="maxPersons"
                        required
                        value={state.maxPersons}
                        onChange={handleChange}
                    />
                    <label htmlFor="maxPrice">{t('createTrip.maxPriceLabel')}</label>
                    <input
                        type="number"
                        id="maxPrice"
                        name="maxPrice"
                        required
                        value={state.maxPrice}
                        onChange={handleChange}
                    />
                    <Button buttonStyle="btn--outline" type="submit" >
                        {t('createTrip.createTripButton')}
                    </Button>
                </form>
                <p>
                    {t('createTrip.goBackMessage')}{' '}
                    <Link to="/MyTrips" id="goBack">
                        {t('createTrip.goBackLink')}
                    </Link>
                </p>
            </div>
        </div>
    );
}

export default CreateTrip;
