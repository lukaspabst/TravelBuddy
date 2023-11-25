import React from 'react';
import './SocialMediaDropdown.scss';
const SocialMediaDropdown = ({ options, value, onChange }) => (
    <div className="social-media-dropdown">
        <select
            value={value}
            onChange={(e) => onChange(e.target.value)}
        >
            <option value="" disabled>
                Select Social Media
            </option>
            {options.map((option) => (
                <option key={option.value} value={option.value}>
                    {option.label}
                </option>
            ))}
        </select>
        {/* Hier können Sie ein passendes Icon je nach Auswahl anzeigen */}
    </div>
);

export default SocialMediaDropdown;
