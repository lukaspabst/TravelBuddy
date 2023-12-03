import {motion} from 'framer-motion';
import React from "react";

const blackBox = {
    initial: {
        height: "100vh",
        bottom: 0,
    },
    animate: {
        height: 0,
    },
};


const AnimatedTripCard = ({children}) => {
    const selectedTrip = JSON.parse(localStorage.getItem('selectedTrip'));
    console.log(selectedTrip);
    return (
        <div className="absolute inset-0 flex items-center justify-center">
            <motion.div
                className="relative z-50 w-full bg-black"
                initial="initial"
                animate="animate"
                variants={blackBox}
            />
        </div>
    );
};

export default AnimatedTripCard;
