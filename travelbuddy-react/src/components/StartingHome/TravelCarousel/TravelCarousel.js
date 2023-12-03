import React, {useState} from 'react';
import './TravelCarousel.scss';
import {faChevronLeft, faChevronRight} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {Link} from "react-router-dom";

const TravelCarousel = () => {
    const travelData = [
        {
            title: 'Amsterdam',
            image: '/assets/Städte/Amsterdam.png',
            travelId: 'amsterdam',
        },
        {
            title: 'Berlin',
            image: '/assets/Städte/Berlin.png',
            travelId: 'berlin',
        },
        {
            title: 'London',
            image: '/assets/Städte/London.png',
            travelId: 'london',
        },
        {
            title: 'Paris',
            image: '/assets/Städte/Paris.png',
            travelId: 'paris',
        },
        {
            title: 'Warschau',
            image: '/assets/Städte/Warschau.png',
            travelId: 'warschau',
        },
    ];

    const itemsToShow = 3; // Anzahl der anzuzeigenden Elemente

    const [currentIndex, setCurrentIndex] = useState(0);

    const handleNext = () => {
        setCurrentIndex((currentIndex + 1) % (travelData.length - itemsToShow + 1));
    };

    const handlePrev = () => {
        setCurrentIndex((currentIndex - 1 + (travelData.length - itemsToShow + 1)) % (travelData.length - itemsToShow + 1));
    };

    return (
        <div className="travel-carousel">
            <div className="carousel-container">
                <button onClick={handlePrev} className="carousel-button"><FontAwesomeIcon icon={faChevronLeft}/>
                </button>
                {travelData.slice(currentIndex, currentIndex + itemsToShow).map((travel, index) => (
                    <div className="travel-card" key={index}>
                        <Link to={`/travel/${travel.travelId}`} className="travel-card-link">
                            <div className="card-content">
                                <img src={travel.image} alt={travel.title} className="travel-image"/>
                                <p className="travel-title">{travel.title}</p>
                            </div>
                        </Link>
                    </div>
                ))}
                <button onClick={handleNext} className="carousel-button"><FontAwesomeIcon icon={faChevronRight}/>
                </button>
            </div>
        </div>
    );
};


export default TravelCarousel;
