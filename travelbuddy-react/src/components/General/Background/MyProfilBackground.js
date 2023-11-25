import React from 'react';
import './Background.scss';

function ProfileBackground() {
    return (
        <header className="background-header">
            <div className="Image-Scaler">
                <img src="/assets/BackgroundImages/PB_Placeholder.png" alt="Bild" className="centered-image" />
            </div>
        </header>
    );
}

export default ProfileBackground;
