import React from 'react';
import { useNavigate } from 'react-router-dom';
import './StartPage.scss';
import {useTranslation} from "react-i18next";
import {useAuth} from "../../../Containers/Authentication/AuthProvider";
import {Button} from "../../../Containers/Button/Button";
import BackgroundImage from "../../General/Background/StartingBackground";
import TravelCarousel from "../TravelCarousel/TravelCarousel";
import { motion } from 'framer-motion';

function StartPage() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const {isLoggedIn} = useAuth();

    const handleClick = () => {


        if (isLoggedIn) {
            navigate('/travel');
        } else {

            navigate('/register');
            window.location.reload();
        }
    };


    return (
        <motion.div key="uniqueKey" initial={{ opacity: 0 }} animate={{ opacity: 1 }} exit={{ opacity: 0 }}>
        <div className="StartPage-content">
            <BackgroundImage />
            <div className="button-container-Start">
                <Button buttonStyle="btn--large-image" onClick={handleClick}>{t('home.startingButton')}</Button>
            </div>
            <div className="travel-carousel-container">
                <TravelCarousel />
            </div>
        </div>
        </motion.div>
    );
}

export default StartPage;
