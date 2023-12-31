import React, { useState } from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronDown, faChevronUp} from "@fortawesome/free-solid-svg-icons";
import InfoIcon from "../../../../../Containers/InfoIcon/InfoIcon";
import {Button} from "../../../../../Containers/Button/Button";
import {useTranslation} from "react-i18next";
import '../../../CreateTrip/CreateTrip.scss';
import '../StylesForModalAndTabContent/TripTab.scss';
import TripMembersGrid from "./TripMembersGrid";
import {API_BASE_URL} from "../../../../../config";
import {useParams} from "react-router-dom";
import axios from "axios";

const TripGeneralInfo = ({ tripData, role }) => {
    const { t } = useTranslation();
    const { tripId } = useParams();
    const [state, setState] = useState({
        nameTrip: tripData.nameTrip,
        startDate: tripData.startDate,
        endDate: tripData.endDate,
        maxPersons: tripData.maxPersons,
        costs: tripData.costs,
        destination: tripData.destination,
    });

    const isEditableAdminOrOrganizer = role === 'Trip Organizer' || role === 'Trip Assistant';

    const [errorInfo, setErrorInfo] = useState(null);
    const incrementPrice = () => {
        setState(prevState => ({
            ...prevState,
            costs: Number(prevState.costs) + 50,
        }));
    }

    const decrementPrice = () => {
        setState(prevState => ({
            ...prevState,
            costs: Number(prevState.costs) - 50,
        }));
    }
    const incrementPersons = () => {
        setState(prevState => ({
            ...prevState,
            maxPersons: Number(prevState.maxPersons) + 1,
        }));
    }

    const decrementPersons = () => {
        setState(prevState => ({
            ...prevState,
            maxPersons: Number(prevState.maxPersons) - 1,
        }));
    }
    const handleChange = (e) => {
        const { name, value } = e.target;
        setState(prevState => ({
            ...prevState,
            [name]: value
        }));

        let today = new Date();
        let maxDate = new Date();
        maxDate.setFullYear(maxDate.getFullYear() + 50);

        let startDate, endDate;

        if (name === "startDate") {
            startDate = new Date(value);
            endDate = state.endDate ? new Date(state.endDate) : null;
        } else if (name === "endDate") {
            endDate = new Date(value);
            startDate = state.startDate ? new Date(state.startDate) : null;
        }

        if(startDate) {
            if(startDate < today || startDate > maxDate) {
                setErrorInfo(t(`errorMessages.invalidStartDate`));
                return;
            }
        }

        if(endDate) {
            if(endDate < today || endDate > maxDate) {
                setErrorInfo(t(`errorMessages.invalidEndDate`));
                return;
            }
        }

        if(startDate && endDate) {
            if(startDate > endDate) {
                setErrorInfo(t(`errorMessages.endDateBeforeStartDate`));
                return;
            }
        }

        setErrorInfo(null);

        if (name === "maxPersons") {
            const maxPersons = parseInt(value);
            if (maxPersons < 2 || maxPersons > 25) {
                setErrorInfo(t(`errorMessages.invalidMaxPersons`));
                return;
            }
        }

        if (name === "costs") {
            const costs = parseFloat(value);
            if (costs <= 0) {
                setErrorInfo(t(`errorMessages.invalidMaxPrice`));
                return;
            }
        }

        if (name === "nameTrip" && !value) {
            setErrorInfo(t(`errorMessages.emptyTripName`));
            return;
        }
        if (['nameTrip', 'startDate', 'endDate', 'destination', 'maxPersons', 'costs'].every(item => state[item])) {
            setErrorInfo(null);
        } else {
            setErrorInfo(t('errorMessages.emptyFields'));
        }
    };

    const handleButtonClick = async () => {
        try {
            const { nameTrip, startDate, endDate, destination, maxPersons, costs } = state;
            const response = await axios.put(
                `${API_BASE_URL}/api/trips/${tripId}`,
                {
                    tripName: nameTrip,
                    startDate,
                    endDate,
                    destination,
                    maxPersons,
                    costs
                },
                { withCredentials: true }
            );
            if(response.status===200){
                localStorage.setItem('selectedTrip',JSON.stringify(state));
            }
            // Handle the response as needed
            console.log('Trip updated successfully:', response);

            // If the update is successful, you may want to show a success message
        } catch (error) {
            // Handle errors, e.g., show an error message
            console.error('Error updating trip:', error);
            setErrorInfo('Failed to update trip. Please try again.');
        }
    };


    return (
        <div className="tab-content-general">
            <form className="form-general-Trip-Info">
                <div>
                    <label htmlFor="nameTrip">{t('createTrip.tripNameLabel')}</label>
                    <input
                        type="text"
                        id="nameTrip"
                        name="nameTrip"
                        required
                        value={state.nameTrip}
                        onChange={handleChange}
                        readOnly={!isEditableAdminOrOrganizer}
                    />
                </div>
                <div>
                    <label htmlFor="startDate">{t('createTrip.startDateLabel')}</label>
                    <input
                        type="date"
                        id="startDate"
                        name="startDate"
                        required
                        value={state.startDate}
                        onChange={handleChange}
                        readOnly={!isEditableAdminOrOrganizer}
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
                        readOnly={!isEditableAdminOrOrganizer}
                    />
                </div>
                <div>
                    <label htmlFor="destination">{t('createTrip.destinationLabel')}</label>
                    <input
                        type="text"
                        id="destination"
                        name="destination"
                        required
                        value={state.destination}
                        onChange={handleChange}
                        readOnly={!isEditableAdminOrOrganizer}
                    />
                </div>
                <div>
                    <label htmlFor="maxPersons">{t('createTrip.maxPersonsLabel')}</label>
                    <div className="input-holder-for-spinButton-and-Input">
                        <input
                            type="number"
                            id="maxPersons"
                            name="maxPersons"
                            required
                            value={state.maxPersons}
                            onChange={handleChange}
                            readOnly={!isEditableAdminOrOrganizer}
                        />
                        <div className="button-holder-spinButtons">
                            <button type="button" onClick={incrementPersons} className="button-style-spin-buttons">
                                <FontAwesomeIcon
                                    icon={faChevronUp}/></button>
                            <button type="button" onClick={decrementPersons} className="button-style-spin-buttons">
                                <FontAwesomeIcon
                                    icon={faChevronDown}/></button>
                        </div>
                    </div>
                </div>
                <div>
                    <label htmlFor="costs">{t('createTrip.maxPriceLabel')}</label>
                    <div className="input-holder-for-spinButton-and-Input">
                        <input
                            type="number"
                            id="costs"
                            name="costs"
                            required
                            value={state.costs}
                            onChange={handleChange}
                            step="50"
                            readOnly={!isEditableAdminOrOrganizer}

                        />
                        <div className="button-holder-spinButtons">
                            <button type="button" onClick={incrementPrice} className="button-style-spin-buttons">
                                <FontAwesomeIcon
                                    icon={faChevronUp}/></button>
                            <button type="button" onClick={decrementPrice} className="button-style-spin-buttons">
                                <FontAwesomeIcon
                                    icon={faChevronDown}/></button>
                        </div>
                    </div>
                </div>
            </form>
            <div className="container-for-error-and-button">
                {errorInfo ? (
                    <InfoIcon tooltipMessage={errorInfo}/>
                ) : (
                    <Button
                        buttonStyle="btn--outline"
                        type="button"
                        onClick={handleButtonClick}
                        disabled={!isEditableAdminOrOrganizer}
                    >
                        {t('trip.updateTripGeneral')}
                    </Button>
                )}
            </div>
            <TripMembersGrid role={role}/>
        </div>
    );
};
export default TripGeneralInfo;