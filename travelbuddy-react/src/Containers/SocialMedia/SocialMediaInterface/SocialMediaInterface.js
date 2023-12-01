import React, { useState } from 'react';
import './SocialMediaInterface.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFacebook, faInstagram, faTwitter} from "@fortawesome/free-brands-svg-icons";
import {solid} from "@fortawesome/fontawesome-svg-core/import.macro";
import {faLink} from "@fortawesome/free-solid-svg-icons";
import {Button} from "../../Button/Button";

const SocialMediaInterface = () => {
    const socialMediaOptions = [
        { value: "Facebook", icon: faFacebook },
        { value: "Twitter", icon: faTwitter },
        { value: "Instagram", icon: faInstagram }
    ];

    const [selectedPlatform, setSelectedPlatform] = useState('');
    const [socialMediaLinks, setSocialMediaLinks] = useState({});

    const handlePlatformChange = (event) => {
        setSelectedPlatform(event.target.value);
    };

    const handleLinkChange = (event) => {
        setSocialMediaLinks({ ...socialMediaLinks, [selectedPlatform]: event.target.value });
    };
    const handleSave = () => {
        // Hier kannst du die Links speichern oder an das Backend senden
        console.log('Gespeicherte Links:', socialMediaLinks);
    };

    return (
        <div className="social-media-container">
            <div>
                <label htmlFor="platformDropdown">Deine Social Media Links:</label>
                <select id="platformDropdown" value={selectedPlatform} onChange={handlePlatformChange}>
                    <option value="">-- Plattform auswählen --</option>
                    {socialMediaOptions.map((platform) => (
                        <option key={platform.value} value={platform.value}>
                            <p>{platform.value}
                            <FontAwesomeIcon
                                icon={faFacebook}
                                style={{ color: "#2e59a3" }}
                            />{' '}</p>
                        </option>
                    ))}
                </select>
            </div>

            {selectedPlatform && (
                <div>
                    <input
                        type="text"
                        id="linkInput"
                        placeholder={`Link für ${selectedPlatform}`}
                        value={socialMediaLinks[selectedPlatform] || ''}
                        onChange={handleLinkChange}
                    />
                </div>
            )}

            <Button buttonStyle="btn--small-avatar" onClick={handleSave}>Speichern</Button>
        </div>
    );
};

export default SocialMediaInterface;
