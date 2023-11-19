
import React from 'react';
import { CSSTransition, TransitionGroup } from 'react-transition-group';
import './PageTransition.css';

const PageTransition = ({ children }) => {
    return (
        <TransitionGroup className="transition-group">
            <CSSTransition
                key={window.location.key}
                timeout={{ enter: 300, exit: 300 }}
                classNames="fade"
            >
                {children}
            </CSSTransition>
        </TransitionGroup>
    );
};

export default PageTransition;
