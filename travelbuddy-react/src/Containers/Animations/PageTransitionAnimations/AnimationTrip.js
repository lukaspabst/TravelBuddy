import { useEffect, useState } from "react";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPerson} from "@fortawesome/free-solid-svg-icons";
import styled from "styled-components";
import {motion} from "framer-motion";

const BlackCoverDiv = styled( motion.div )`
    background: var(--bg-color-animation);
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    width: 100%;
    height: 100%;
    position: absolute;
    top: 0;
    left: 0;
    z-index: 2;
`;

const AnimatedTripCard = ({ onAnimationComplete }) => {
    const [trip, setTrip] = useState({});

    const TripCardDiv = styled(motion.div)`
    `;
    // Get trip data from local storage when the component mounts
    useEffect(() => {
        const tripData = localStorage.getItem('selectedTrip');
        if (tripData) {
            setTrip(JSON.parse(tripData));
        }
    }, []);

    return (
        <BlackCoverDiv
            initial={{y: '0%'}}
            animate={{y: '100%'}}
            transition={{
                delay: 0.5,
                when: 'afterChildren',
                duration: 1,
                ease: [0.87, 0, 0.13, 1],
            }}
            onAnimationComplete={onAnimationComplete}>
            <TripCardDiv
                className="trip-card"
                initial={{ scale: 1, opacity: 1 }}
                animate={{ scale: 0, opacity: 0 }}
                transition={{
                    delay: 0.25,
                    duration: 1,
                    ease: [0.87, 0, 0.13, 1],
                }}
            >
                <div className="trip-info">
                    <div className="placeholder">
                        <img src="/assets/ImagePlaceHolder.png" alt="Placeholder"/>
                    </div>
                    <div className="trip-name">{trip.nameTrip}</div>
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
            </TripCardDiv>
        </BlackCoverDiv>
    );
};

export default AnimatedTripCard;