import React from 'react';
import {motion} from 'framer-motion';

const variants = {
    fade: {
        initial: { opacity: 0 },
        enter: { opacity: 1 },
        exit: { opacity: 0 }
    },
    fancy: {
        initial: { scale: 0.5, opacity: 0 },
        enter: { scale: 1, opacity: 1 },
        exit: { scale: 0.5, opacity: 0 }
    }
};

const PageTransition = ({children, variant = 'fade'}) => (
    <motion.div variants={variants[variant]} initial="initial" animate="enter" exit="exit">
        {children}
    </motion.div>
);

export default PageTransition;