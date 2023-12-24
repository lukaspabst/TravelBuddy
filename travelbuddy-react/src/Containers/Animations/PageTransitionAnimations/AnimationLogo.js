import React from 'react';
import { motion } from 'framer-motion';
import styled from 'styled-components';
import './Animation.scss';
import './GlitchNoise.scss';
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

const AnimationLogo = ({ onAnimationComplete }) => (
    <BlackCoverDiv
        initial={{ y: '0%' }}
        animate={{ y: '100%' }}
        transition={{
            delay: 1.75,
            when: 'afterChildren',
            duration: 1.5,
            ease: [0.87, 0, 0.13, 1],
        }}
        onAnimationComplete={onAnimationComplete}
    >
        <div className="animation-wrapper-for-logo">
            <img src="/favicon.png" alt='favicon' className='logo-image-animation'/>
            <motion.h1
                className='travelBuddy'
                initial={{ opacity: 0 }}
                animate={{ opacity: 1, transition: { delay: 0.5, duration: 0.5, ease: [0.87, 0, 0.13, 1] }}}
            >
                <div className="shine">TravelBuddy</div>
            </motion.h1>
        </div>
    </BlackCoverDiv>
);

export default AnimationLogo;