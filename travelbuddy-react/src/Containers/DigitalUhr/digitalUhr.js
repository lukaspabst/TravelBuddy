
import React, {useEffect, useState} from 'react';
import './digitalUhr.scss';

const DigitalClock = () => {
    const [currentTime, setCurrentTime] = useState(new Date());


    useEffect(() => {
        const intervalId = setInterval(() => {
            setCurrentTime(new Date());
        }, 1000);

        return () => clearInterval(intervalId);
    }, []);

    return (

        <div className="digital-clock">
            <div className="container">
                <div className="date">{currentTime.toLocaleDateString()}</div>
                <div className="uhrzeit-helper-wrapper">
                    <div className="hr">{currentTime.getHours().toString().padStart(2, '0')}</div>
                    <div className="colon">:</div>
                    <div className="min">{currentTime.getMinutes().toString().padStart(2, '0')}</div>
                    <div className="colon">:</div>
                    <div className="sec">{currentTime.getSeconds().toString().padStart(2, '0')}</div>
                </div>
            </div>
        </div>
    );
};

export default DigitalClock;
