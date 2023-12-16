import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import './CreateTrip.scss';
import {useTranslation} from 'react-i18next';
import axios from 'axios';
import TravelBackground from "../../General/Background/TravelBackground";
import {API_BASE_URL} from "../../../config";
import {Button} from "../../../Containers/Button/Button";
import InfoIcon from "../../../Containers/InfoIcon/InfoIcon";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faChevronDown, faChevronUp} from "@fortawesome/free-solid-svg-icons";

function CreateTrip() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const [state, setState] = useState({
        tripName: '',
        startDate: '',
        endDate: '',
        destination: '',
        maxPersons: '',
        maxPrice: '',
    });
    const [errorInfo, setErrorInfo] = useState(null);
    const incrementPrice = () => {
        setState(prevState => ({
            ...prevState,
            maxPrice: Number(prevState.maxPrice) + 50,
        }));
    }

    const decrementPrice = () => {
        setState(prevState => ({
            ...prevState,
            maxPrice: Number(prevState.maxPrice) - 50,
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

        if (name === "maxPrice") {
            const maxPrice = parseFloat(value);
            if (maxPrice <= 0) {
                setErrorInfo(t(`errorMessages.invalidMaxPrice`));
                return;
            }
        }

        if (name === "tripName" && !value) {
            setErrorInfo(t(`errorMessages.emptyTripName`));
            return;
        }
        if (['tripName', 'startDate', 'endDate', 'destination', 'maxPersons', 'maxPrice'].every(item => state[item])) {
            setErrorInfo(null);
        } else {
            setErrorInfo(t('errorMessages.emptyFields'));
        }
    };


    const handleSubmit = async (e) => {
        e.preventDefault();
        const {tripName, startDate, endDate, destination, maxPersons, maxPrice} = state;
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
                {withCredentials: true}
            );

            if (response.status === 201) {

                navigate('/MyTrips');
            } else if (response.status === 400) {
                setState({...state, message: t('errorMessages.badRequest')});
            } else if (response.status === 500) {
                setState({...state, message: t('errorMessages.internalServerError')});
            } else {
                setState({...state, message: t('errorMessages.createTripError')});
            }
        } catch (error) {
            setState({...state, message: t('errorMessages.createTripError')});
        }
    };

    return (
        <CreateTripFormContent
            handleSubmit={handleSubmit}
            handleChange={handleChange}
            state={state}
            t={t}
            errorInfo={errorInfo}
            incrementPersons={incrementPersons}
            decrementPersons={decrementPersons}
            incrementPrice={incrementPrice}
            decrementPrice={decrementPrice}
        />
    );
}

function CreateTripFormContent({handleSubmit, handleChange, state, t,errorInfo,incrementPersons,decrementPersons,incrementPrice,decrementPrice}) {

    return (
        <div className="StartPage-content">
            <TravelBackground/>
            <div className="create-trip-container">
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
                        <div className="input-holder-for-spinButton-and-Input">
                            <input
                                type="number"
                                id="maxPersons"
                                name="maxPersons"
                                required
                                value={state.maxPersons}
                                onChange={handleChange}
                            />
                            <div className="button-holder-spinButtons">
                                <button type="button" onClick={incrementPersons} className="button-style-spin-buttons"><FontAwesomeIcon
                                    icon={faChevronUp}/></button>
                                <button type="button" onClick={decrementPersons} className="button-style-spin-buttons"><FontAwesomeIcon
                                    icon={faChevronDown}/></button>
                            </div>
                        </div>
                        <label htmlFor="maxPrice">{t('createTrip.maxPriceLabel')}</label>
                        <div className="input-holder-for-spinButton-and-Input">
                            <input
                                type="number"
                                id="maxPrice"
                                name="maxPrice"
                                required
                                value={state.maxPrice}
                                onChange={handleChange}
                                step="50"
                            />
                            <div className="button-holder-spinButtons">
                                <button type="button" onClick={incrementPrice} className="button-style-spin-buttons"><FontAwesomeIcon
                                    icon={faChevronUp}/></button>
                                <button type="button"  onClick={decrementPrice} className="button-style-spin-buttons"><FontAwesomeIcon
                                    icon={faChevronDown}/></button>
                            </div>
                        </div>
                        <div className="container-for-error-and-button">
                            {errorInfo ? <InfoIcon tooltipMessage={errorInfo}/> :
                                <Button buttonStyle="btn--outline" type="submit">
                                    {t('createTrip.createTripButton')}
                                </Button>
                            }
                        </div>
                    </form>
                    <p>
                        {t('createTrip.goBackMessage')}{' '}
                <Link to="/MyTrips" id="goBack">
                    {t('createTrip.goBackLink')}
                </Link>
            </p>
        </div>
</div>
</div>
)
    ;
}

export default CreateTrip;
