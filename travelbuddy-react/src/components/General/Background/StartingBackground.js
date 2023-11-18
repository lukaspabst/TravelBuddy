import React from 'react';
import './Background.scss';

function BackgroundImage() {
    return (
        <header className="background-header">
        <div className="Image-Scaler">
            <img src="/assets/BackgroundImages/Placeholder.png" alt="Bild" className="centered-image" />
        </div>
        </header>
    );
}

export default BackgroundImage;
