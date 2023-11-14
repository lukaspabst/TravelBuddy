import React, { useState } from 'react';
import './TravelCarousel.scss';
import { faChevronLeft, faChevronRight } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

const TravelCarousel = () => {
    const travelData = [
        {
            title: 'Amsterdam',
            image: '/assets/Städte/Amsterdam.png',
        },
        {
            title: 'Berlin',
            image: '/assets/Städte/Berlin.png',
        },
        {
            title: 'London',
            image: '/assets/Städte/London.png',
        },
        {
            title: 'Paris',
            image: '/assets/Städte/Paris.png',
        },
        {
            title: 'Warschau',
            image: '/assets/Städte/Warschau.png',
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
                <button onClick={handlePrev} className="carousel-button"><FontAwesomeIcon icon={faChevronLeft} /></button>
                {travelData.slice(currentIndex, currentIndex + itemsToShow).map((travel, index) => (
                    <div className="travel-card" key={index}>
                        <div className="card-content">
                            <img src={travel.image} alt={travel.title} className="travel-image" />
                            <p className="travel-title">{travel.title}</p>
                        </div>
                    </div>
                ))}
                <button onClick={handleNext} className="carousel-button"><FontAwesomeIcon icon={faChevronRight} /></button>
            </div>
        </div>
    );
};

export default TravelCarousel;
