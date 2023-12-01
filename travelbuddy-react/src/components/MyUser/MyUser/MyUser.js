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
import SocialMediaInterface from "../../../Containers/SocialMedia/SocialMediaInterface/SocialMediaInterface";

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
                                    />
                                    <label htmlFor="location">{t('userProfile.location')}</label>
                                    <input
                                        type="text"
                                        id="location"
                                        value={userData.location}
                                    />
                                </div>
                                <div className="user-profile-right">
                                    <label htmlFor="lastName">{t('userProfile.surname')}</label>
                                    <input
                                        type="text"
                                        id="lastName"
                                        value={userData.surname}
                                    />
                                    <label htmlFor="zipCode">{t('userProfile.zipCode')}</label>
                                    <input
                                        type="text"
                                        id="zipCode"
                                        value={userData.zipCode}
                                    />
                                </div>
                                </div>
                            </div>
                                <div className="">
                                    <SocialMediaInterface />
                                </div>

                                <div>
                                    <label htmlFor="preferences">{t('userProfile.preferences')}</label>
                                    <input
                                        type="text"
                                        id="preferences"
                                        value={userData.preferences}
                                    />
                                </div>
                                <div>
                                    <label htmlFor="travelDestination">{t('userProfile.travelDestinations')}</label>
                                    <input
                                        type="text"
                                        id="travelDestination"
                                        value={userData.travelDestination}
                                    />
                                </div>
                                <div>
                                    <label htmlFor="gender">{t('userProfile.gender')}</label>
                                    <input
                                        type="text"
                                        id="gender"
                                        value={userData.gender}
                                    />
                                </div>
                            <Button buttonStyle="btn--outline" onClick={() => console.log('Edit profile clicked')}>
                                {t('userProfile.editProfileButton')}
                            </Button>
                    </div>
                </div>
        </AnimationFlash>
    );
}

export default MyUser;
