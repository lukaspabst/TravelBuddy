import React from 'react';
import './StartingBackground.scss';

function BackgroundImage() {
    return (
        <header className="background-header">
        <div className="Image-Scaler">
            <img src="/assets/Placeholder.png" alt="Bild" className="centered-image" />
        </div>
        </header>
    );
}

export default BackgroundImage;
