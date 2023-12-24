import {useNavigate} from 'react-router-dom';
import './StartPage.scss';
import {useTranslation} from "react-i18next";
import {useAuth} from "../../../Containers/Authentication/AuthProvider";
import {Button} from "../../../Containers/Button/Button";
import BackgroundImage from "../../General/Background/StartingBackground";
import TravelCarousel from "../TravelCarousel/TravelCarousel";
import {motion} from 'framer-motion';
import styled from 'styled-components';
import React, { useState } from 'react';
import AnimationLogo from "../../../Containers/Animations/PageTransitionAnimations/AnimationLogo";

function StartPage() {
    const {t} = useTranslation();
    const navigate = useNavigate();
    const {isLoggedIn} = useAuth();
    const [animationComplete, setAnimationComplete] = useState(false);
    const handleClick = () => {


        if (isLoggedIn) {
            navigate('/travel');
        } else {
            navigate('/register');
        }
    };


    return (
        <>
            {!animationComplete &&
                <AnimationLogo onAnimationComplete={() => setAnimationComplete(true)} />
            }
            <div className="StartPage-content">
                <BackgroundImage/>
                <div className="button-container-Start">
                    <Button buttonStyle="btn--large-image" onClick={handleClick}>
                        {t('home.startingButton')}
                    </Button>
                </div>
                <div className="travel-carousel-container">
                    <TravelCarousel/>
                </div>
            </div>
        </>
    );
}

export default StartPage;
