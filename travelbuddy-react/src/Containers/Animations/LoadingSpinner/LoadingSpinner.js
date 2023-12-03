import React from 'react';
import './LoadingSpinner.scss'; // Include your CSS for the spinner styling

function LoadingSpinner() {
    return (
        <div className="loading-spinner-container">
            <div className="loading-spinner"></div>
        </div>
    );
}

export default LoadingSpinner;
