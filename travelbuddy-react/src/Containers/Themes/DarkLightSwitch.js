import React, { useContext } from 'react';
import { ThemeContext } from './ThemeContext';
import {faLightbulb, faMoon} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './DarkLightSwitch.scss';

const DarkModeSwitch = () => {
    const { theme, toggleTheme } = useContext(ThemeContext);

    const toggleClassName = theme === 'dark' ? 'switch-dark-mode' : 'switch-light-mode';

    return (
        <label className={`dark-mode-switch ${theme}`}>
            <input
                type="checkbox"
                onChange={toggleTheme}
                checked={theme === 'dark'}
            />
            <div className="moon-icon">
                <FontAwesomeIcon icon={faMoon}/>
            </div>
            <div className={`switch-toggle ${toggleClassName}`}>
                <div className="ball"></div>
            </div>
            <div className="lightbulb-icon">
                <FontAwesomeIcon icon={faLightbulb}/>
            </div>
        </label>
    );
};

export default DarkModeSwitch;