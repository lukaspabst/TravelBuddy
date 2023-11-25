import React, { useEffect, useState } from 'react';
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";
import ProfileBackground from "../../General/Background/MyProfilBackground";
import './MyUser.scss';
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {API_BASE_URL} from "../../../config";
import {Button} from "../../../Containers/Button/Button";
import SocialMediaDropdown from "../../../Containers/SocialMedia/Dropdown/SocialMediaDropdown";
import LinkedSocialMediaGrid from "../../../Containers/SocialMedia/Grid/LinkedSocialMediaGrid";

function MyUser() {
    const { t } = useTranslation();
    const navigate = useNavigate();

    const [userData, setUserData] = useState({
        name: '',
        surname: '',
        location: '',
        zipCode: '',
        preferences: '',
        travelDestination: '',
        socialMediaLinks: '',
        gender: '',
    });

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get(`${API_BASE_URL}/api/users/profile`, {
                    withCredentials: true
                });

                if (response.status === 200) {
                    setUserData(response.data);
                } else {
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };

        fetchUserData();
    }, []);


    return (
        <UserProfileContent userData={userData} t={t} />
    );
}
function UserProfileContent({ userData, t }) {
    const [selectedSocialMedia, setSelectedSocialMedia] = useState('');
    const [linkedSocialMedia, setLinkedSocialMedia] = useState([]);

    const socialMediaOptions = [
        { value: 'facebook', label: 'Facebook' },
        { value: 'twitter', label: 'Twitter' },
        { value: 'instagram', label: 'Instagram' },
        // Weitere soziale Medien hinzufügen
    ];

    const handleSocialMediaChange = (value) => {
        setSelectedSocialMedia(value);
    };

    const handleAddSocialMedia = () => {
        if (selectedSocialMedia) {
            setLinkedSocialMedia([...linkedSocialMedia, { label: selectedSocialMedia, link: '' }]);
            setSelectedSocialMedia('');
        }
    };
    return (
        <AnimationFlash>
            <div className="StartPage-content">

                    <ProfileBackground />
                    <div className="user-profile-container">
                            <h1>{t('userProfile.title')}</h1>
                            <div className="user-profile-content">
                            <div className="user-profile-image">
                                <img src="/assets/pb_placeholder.png" alt="Placeholder" />
                                <Button buttonStyle="btn--small-avatar">
                                    {t('userProfile.uploadAvatar')}
                                </Button>
                            </div>
                            <div className="user-profile-info-important">
                                <div className="user-profile-left">
                                    <label htmlFor="firstName">{t('userProfile.name')}</label>
                                    <input
                                        type="text"
                                        id="firstName"
                                        value={userData.name}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                    <label htmlFor="location">{t('userProfile.location')}</label>
                                    <input
                                        type="text"
                                        id="location"
                                        value={userData.location}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                </div>
                                <div className="user-profile-right">
                                    <label htmlFor="lastName">{t('userProfile.surname')}</label>
                                    <input
                                        type="text"
                                        id="lastName"
                                        value={userData.surname}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                    <label htmlFor="zipCode">{t('userProfile.zipCode')}</label>
                                    <input
                                        type="text"
                                        id="zipCode"
                                        value={userData.zipCode}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                </div>
                                </div>
                                <div>
                                    <label htmlFor="socialMediaLinks">{t('userProfile.socialMedia')}</label>
                                    <SocialMediaDropdown
                                        options={socialMediaOptions}
                                        value={selectedSocialMedia}
                                        onChange={handleSocialMediaChange}
                                    />
                                    <Button buttonStyle="btn--small-avatar" onClick={handleAddSocialMedia}>
                                        {t('userProfile.addSocialMedia')}
                                    </Button>
                                </div>
                                <LinkedSocialMediaGrid linkedMedia={linkedSocialMedia} />

                                <div>
                                    <label htmlFor="preferences">{t('userProfile.preferences')}</label>
                                    <input
                                        type="text"
                                        id="preferences"
                                        value={userData.preferences}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                </div>
                                <div>
                                    <label htmlFor="travelDestination">{t('userProfile.travelDestinations')}</label>
                                    <input
                                        type="text"
                                        id="travelDestination"
                                        value={userData.travelDestination}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                </div>
                                <div>
                                    <label htmlFor="gender">{t('userProfile.gender')}</label>
                                    <input
                                        type="text"
                                        id="gender"
                                        value={userData.gender}
                                        // Hier können Sie eine Funktion zum Aktualisieren des Datenmodells hinzufügen
                                    />
                                </div>
                            <Button buttonStyle="btn--outline" onClick={() => console.log('Edit profile clicked')}>
                                {t('userProfile.editProfileButton')}
                            </Button>
                        </div>
                    </div>
                </div>
        </AnimationFlash>
    );
}

export default MyUser;
