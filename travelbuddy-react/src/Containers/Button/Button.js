import React from 'react'
import './Button.css'

const STYLES = ['btn--primary', 'btn--outline', 'btn--large-image', 'btn--small-avatar','btn--small-delete'];

const SIZES = ['btn--medium', 'btn--large', 'btn--extra-large'];


export const Button = ({children, type, onClick, buttonStyle, buttonSize}) => {
    const checkButtonStyle = STYLES.includes(buttonStyle) ? buttonStyle : STYLES[0];
    const checkButtonSize = SIZES.includes(buttonSize) ? buttonSize : SIZES[0];

    return (
        <button className={`btn ${checkButtonStyle} ${checkButtonSize}`} onClick={onClick} type={type}>
            {children}
        </button>
    );
};
