import React, { useState } from 'react';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronDown, faChevronUp} from "@fortawesome/free-solid-svg-icons";
import InfoIcon from "../../../../Containers/InfoIcon/InfoIcon";
import {Button} from "../../../../Containers/Button/Button";
import {useTranslation} from "react-i18next";
import '../../CreateTrip/CreateTrip.scss';

const TripGeneralInfo = ({ tripData, role }) => {
    const { t } = useTranslation();
    const [state, setState] = useState({
        tripName: tripData.nameTrip,
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

        if (name === "startDate" || name === "endDate") {
            let today = new Date();
            let maxDate = new Date();
            maxDate.setFullYear(maxDate.getFullYear() + 50);

            let selectedDate = new Date(value);

            if (selectedDate < today || selectedDate > maxDate) {
                setErrorInfo(t(`errorMessages.invalid${name.charAt(0).toUpperCase() + name.slice(1)}`));
                return;
            } else {
                setErrorInfo(null);
            }
            if ((name === "startDate" && state.endDate) || (name === "endDate" && state.startDate)) {
                let startDate = new Date(name === "startDate" ? value : state.startDate);
                startDate.setHours(0, 0, 0, 0);

                let endDate = new Date(name === "endDate" ? value : state.endDate);
                endDate.setHours(0, 0, 0, 0);

                if (startDate > endDate) {
                    setErrorInfo(t(`errorMessages.endDateBeforeStartDate`));
                    return;
                } else {
                    setErrorInfo(null);
                }
            }

        }
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

        if (name === "tripName" && !value) {
            setErrorInfo(t(`errorMessages.emptyTripName`));
            return;
        }
        if (['tripName', 'startDate', 'endDate', 'destination', 'maxPersons', 'costs'].every(item => state[item])) {
            setErrorInfo(null);
        } else {
            setErrorInfo(t('errorMessages.emptyFields'));
        }
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        // This method should be passed as a prop from parent and should handle form submission
    }
    return (
        <div className="tab-content-general">
            <form onSubmit={handleSubmit}>
                <label htmlFor="tripName">{t('createTrip.tripNameLabel')}</label>
                <input
                    type="text"
                    id="tripName"
                    name="tripName"
                    required
                    value={state.tripName}
                    onChange={handleChange}
                    readOnly={!isEditableAdminOrOrganizer}
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
                </div>
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
                <div className="container-for-error-and-button">
                    {errorInfo ? <InfoIcon tooltipMessage={errorInfo}/> :
                        <Button buttonStyle="btn--outline" type="submit">
                            {t('createTrip.updateTripGeneral')}
                        </Button>
                    }
                </div>
            </form>
        </div>
    );
};
export default TripGeneralInfo;