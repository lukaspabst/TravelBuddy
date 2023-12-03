import React, {useEffect, useState} from 'react';
import Select from 'react-select';
import './SocialMediaInterface.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {
    faBitbucket,
    faFacebook,
    faGithub,
    faGitlab,
    faInstagram,
    faLinkedin,
    faPinterest,
    faReddit,
    faSnapchat,
    faTumblr,
    faTwitch,
    faTwitter,
    faXing,
    faYoutube
} from "@fortawesome/free-brands-svg-icons";
import {Button} from "../../Button/Button";
import {validateLink} from "../LinkValidationUtils";
import {faInfoCircle} from "@fortawesome/free-solid-svg-icons";
import Tooltip from 'react-tooltip-lite';
import {useTranslation} from "react-i18next";

const SocialMediaInterface = () => {
    const {t} = useTranslation();
    const socialMediaOptions = [
        {value: "Facebook", icon: faFacebook},
        {value: "Twitter", icon: faTwitter},
        {value: "Instagram", icon: faInstagram},
        {value: "LinkedIn", icon: faLinkedin},
        {value: "Xing", icon: faXing},
        {value: "YouTube", icon: faYoutube},
        {value: "Twitch", icon: faTwitch},
        {value: "Snapchat", icon: faSnapchat},
        {value: "Pinterest", icon: faPinterest},
        {value: "Reddit", icon: faReddit},
        {value: "Tumblr", icon: faTumblr},
        {value: "GitHub", icon: faGithub},
        {value: "GitLab", icon: faGitlab},
        {value: "Bitbucket", icon: faBitbucket},

    ];

    const [selectedPlatform, setSelectedPlatform] = useState('');
    const [socialMediaLinks, setSocialMediaLinks] = useState({});
    const [errorInfo, setErrorInfo] = useState(null);


    useEffect(() => {
        const storedLinks = JSON.parse(localStorage.getItem('socialMediaLinks')) || {};
        setSocialMediaLinks(storedLinks);
    }, []);



    const handlePlatformChange = (selectedOption) => {
        setSelectedPlatform(selectedOption.value);
    };

    const handleLinkChange = (event) => {
        setSocialMediaLinks({...socialMediaLinks, [selectedPlatform]: event.target.value});
    };

    const customStyles = {
        control: (provided) => ({
            ...provided,
            background: '#555',
        }),
        option: (provided, state) => ({
            ...provided,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            padding: '8px',
            background: state.isFocused ? '#444' : '#555',
            borderBottom: 'solid black 1px',
            transition: 'color 0.3s',
            cursor: 'pointer',
            transform: state.isFocused ? 'scale(1.01)' : 'scale(1)',
        }),
        menu: (provided) => ({
            ...provided,
            zIndex: 9999,
            height: 'auto',
            background: '#555', // Hintergrundfarbe für das Menü
        }),
        menuList: (provided) => ({
            ...provided,
            maxHeight: '150px',
            overflowY: 'auto',
        }),
    };


    const handleSave = () => {
        if (selectedPlatform && socialMediaLinks[selectedPlatform]) {
            const isValidLink = validateLink(selectedPlatform, socialMediaLinks[selectedPlatform]);

            if (isValidLink) {
                const updatedLinks = {...socialMediaLinks};
                updatedLinks[selectedPlatform] = socialMediaLinks[selectedPlatform];
                setSocialMediaLinks(updatedLinks);
                setSelectedPlatform('');
                setErrorInfo(null);
                localStorage.setItem('socialMediaLinks', JSON.stringify(updatedLinks));
            } else {
                setErrorInfo('Ungültiger Link für die ausgewählte Plattform');
            }
        } else {
            setErrorInfo('Link oder Plattform fehlt');
        }
    };

    return (
        <div className="social-media-container">
            <label htmlFor="platformDropdown">{t('socialMedia.editLinksLabel')}</label>
            <Select
                id="platformDropdown"
                value={{
                    value: selectedPlatform,
                    label: (
                        <div className="platform-Dropdown-options">
                            {selectedPlatform && (
                                <FontAwesomeIcon
                                    icon={socialMediaOptions.find(option => option.value === selectedPlatform).icon}
                                    style={{color: "#2e59a3", marginRight: '8px'}}/>
                            )}
                            {selectedPlatform}
                        </div>
                    ),
                }}
                onChange={handlePlatformChange}
                options={socialMediaOptions.map((platform) => ({
                    value: platform.value,
                    label: (
                        <div className="platform-Dropdown-options">
                            <div className="icon-container">
                                <FontAwesomeIcon icon={platform.icon} style={{color: "#2e59a3", marginRight: '8px'}}/>
                            </div>
                            <div>
                                {platform.value}
                            </div>
                        </div>
                    ),
                }))}
                styles={customStyles}
            />
            {selectedPlatform && (
                <div className="padding-for-button">
                    <label htmlFor="linkInput">{t('socialMedia.enterLinkLabel', {platform: selectedPlatform})}:</label>
                    <input
                        type="text"
                        id="linkInput"
                        placeholder={t('socialMedia.linkPlaceholder', {platform: selectedPlatform})}
                        value={socialMediaLinks[selectedPlatform] || ''}
                        onChange={handleLinkChange}
                    />
                </div>
            )}
            <div className="button-and-tooltip-row">
                <div
                    className={`padding-if-dropdown-no-value-selected ${!selectedPlatform ? 'padding-for-button' : ''}`}>
                    <Button buttonStyle="btn--small-avatar" onClick={handleSave}>
                        {t('socialMedia.saveButton')}
                    </Button>
                </div>
                <div className="tooltip-info-button-socialMedia">
                    {errorInfo && (
                        <Tooltip
                            content={<div className="tooltip-content">{errorInfo}</div>}
                            arrow={false}
                        >
                            <FontAwesomeIcon icon={faInfoCircle} style={{color: '#ff6666', cursor: 'pointer'}}/>
                        </Tooltip>
                    )}
                </div>
            </div>
        </div>
    );
};

export default SocialMediaInterface;
