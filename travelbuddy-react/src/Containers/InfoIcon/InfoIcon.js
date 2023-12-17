import React from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import Tooltip from 'react-tooltip-lite';
import './InfoIcon.scss';

const InfoIcon = ({ tooltipMessage }) => (
    <div className="info-icon-container">
        <Tooltip
            content={<div className="tooltip-content">{tooltipMessage}</div>}
            arrow={false}
        >
            <FontAwesomeIcon icon={faInfoCircle} className="info-icon"/>
        </Tooltip>
    </div>
);

export default InfoIcon;