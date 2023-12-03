import React, {useEffect, useState} from 'react';
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";
import ProfileBackground from "../../General/Background/MyProfilBackground";
import './MyUser.scss';
import {useTranslation} from "react-i18next";
import axios from "axios";
import {API_BASE_URL} from "../../../config";
import {Button} from "../../../Containers/Button/Button";
import LinkedSocialMediaGrid from "../../../Containers/SocialMedia/Grid/LinkedSocialMediaGrid";
import SocialMediaInterface from "../../../Containers/SocialMedia/SocialMediaInterface/SocialMediaInterface";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import {Link} from "react-router-dom";
import DigitalClock from "../../../Containers/DigitalUhr/digitalUhr";

function MyUser() {
    const {t} = useTranslation();

    const [userData, setUserData] = useState({
        firstName: '',
        lastName: '',
        location: '',
        birthday: null,
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
                    withCredentials: true,
                });

                if (response.status === 200) {
                    const mapBackendValueToDropdownValue = (backendValue) => {
                        switch (backendValue) {
                            case 'Male':
                                return 'M';
                            case 'Female':
                                return 'W';
                            case 'Other':
                                return 'D';
                            default:
                                return '';
                        }
                    };

                    const userBirthday = response.data.birthday ? new Date(response.data.birthday) : null;

                    setUserData({
                        firstName: response.data.firstName,
                        lastName: response.data.lastName,
                        location: response.data.location,
                        birthday: userBirthday,
                        zipCode: response.data.zipCode,
                        preferences: response.data.preferences,
                        travelDestination: response.data.travelDestination,
                        socialMediaLinks: response.data.socialMediaLinks,
                        gender: mapBackendValueToDropdownValue(response.data.gender), // Mapping hier anwenden
                    });
                } else {
                    console.error('Invalid date format received:');
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };

        fetchUserData();
    }, []);

    const handleSaveProfile = async () => {
        console.log("UserData in handleSave:", userData);
        try {
            const requestData = {
                firstName: userData.firstName,
                lastName: userData.lastName,
                location: userData.location,
                birthday: userData.birthday ? (new Date(userData.birthday)).toISOString() : '',
                zipCode: userData.zipCode,
                preferences: userData.preferences,
                travelDestination: userData.travelDestination,
                socialMediaLinks: JSON.parse(localStorage.getItem('socialMediaLinks')), // Social Media Links aus dem Local Storage
                gender: userData.gender,
            };
            console.error(requestData)
            const response = await axios.post(`${API_BASE_URL}/api/users/register`, requestData, {
                withCredentials: true,
            });

            if (response.status === 200) {
                console.log('Profile updated successfully');
            } else {

                console.error('Error updating profile:', response.data);
            }
        } catch (error) {
            console.error('Error updating profile:', error);
        }
    };

    return (
        <UserProfileContent userData={userData} setUserData={setUserData} t={t} onSaveProfile={handleSaveProfile}/>
    );
}

function UserProfileContent({userData, setUserData, t, onSaveProfile}) {
    const [currentTime, setCurrentTime] = useState(new Date());

    useEffect(() => {
        const intervalId = setInterval(() => {
            setCurrentTime(new Date());
        }, 1000);

        return () => clearInterval(intervalId);
    }, []);
    console.log("UserData in UserProfileContent:", userData);
    const handleInputChange = (e) => {
        const {id, value} = e.target;
        console.log(`Setting ${id} to: ${value}`);
        setUserData((prevUserData) => ({
            ...prevUserData,
            [id]: value,
        }));
    };

    const handleGenderSelection = (e) => {
        const selectedGender = e.target.value;
        console.log(`Selected gender: ${selectedGender}`);
        setUserData((prevUserData) => ({
            ...prevUserData,
            gender: selectedGender,
        }));
    };
    const handleDateChange = (date) => {
        setUserData((prevUserData) => ({
            ...prevUserData,
            birthday: date,
        }));
    };
    return (
        <AnimationFlash>
            <div className="StartPage-content">
                <ProfileBackground/>
                <div className="user-profile-container">
                    <h1>{t('userProfile.title')}</h1>
                    <div className="user-profile-content">
                        <div className="user-profile-image">
                            <img src="/assets/pb_placeholder.png" alt="Placeholder"/>
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
                                    value={userData.firstName}
                                    onChange={handleInputChange}
                                />
                                <label htmlFor="location">{t('userProfile.location')}</label>
                                <input
                                    type="text"
                                    id="location"
                                    value={userData.location}
                                    onChange={handleInputChange}
                                />
                            </div>
                            <div className="user-profile-right">
                                <label htmlFor="lastName">{t('userProfile.surname')}</label>
                                <input
                                    type="text"
                                    id="lastName"
                                    value={userData.lastName}
                                    onChange={handleInputChange}
                                />
                                <label htmlFor="zipCode">{t('userProfile.zipCode')}</label>
                                <input
                                    type="text"
                                    id="zipCode"
                                    value={userData.zipCode}
                                    onChange={handleInputChange}
                                />
                            </div>
                        </div>
                    </div>
                    <div className="app-container">
                        <div className="interface-container">
                            <SocialMediaInterface/>
                        </div>
                        <div className="grid-container">
                            <LinkedSocialMediaGrid/>
                        </div>
                    </div>
                    <div className="preferences-container">
                        <label htmlFor="preferences">{t('userProfile.preferences')}</label>
                        <input
                            type="text"
                            id="preferences"
                            value={userData.preferences}
                            onChange={handleInputChange}
                        />
                    </div>

                    <div className="travel-destination-container">
                        <label htmlFor="travelDestination">{t('userProfile.travelDestinations')}</label>
                        <input
                            type="text"
                            id="travelDestination"
                            value={userData.travelDestination}
                            onChange={handleInputChange}
                        />
                    </div>
                    <div className="gender-and-birthday-container">
                        <div className="gender-container">
                            <div className="selected-gender">
                                <label htmlFor="gender">{t('userProfile.gender')}</label>
                                <select
                                    id="gender"
                                    value={userData.gender}
                                    onChange={handleGenderSelection}
                                >
                                    <option value="">{t('userProfile.selectGender')}</option>
                                    <option value="M">{t('userProfile.male')}</option>
                                    <option value="W">{t('userProfile.female')}</option>
                                    <option value="D">{t('userProfile.other')}</option>
                                </select>
                            </div>
                        </div>
                        <div className="birthday-container">
                            <label htmlFor="birthday">{t('userProfile.birthday')}</label>
                            <DatePicker
                                id="birthday"
                                selected={userData.birthday}
                                onChange={handleDateChange}
                                dateFormat="dd.MM.yyyy"
                                placeholderText={t('userProfile.selectBirthday')}
                                popperPlacement="bottom"
                                showYearDropdown
                                yearDropdownItemNumber={15}
                                scrollableYearDropdown
                                className="custom-datepicker"
                            />
                        </div>
                    </div>
                    <div className="format-button-editProfile">
                        <Button buttonStyle="btn--outline" onClick={onSaveProfile}>
                            {t('userProfile.editProfileButton')}
                        </Button>
                    </div>
                </div>
                <div className="helpingLinks">
                    <div>
                        <ul>
                            <p>Weitere Links für dich</p>
                            <li><Link to="/user-security">Deine User Security</Link></li>
                            <li><Link to="/faq">FAQ</Link></li>
                            <li><Link to="/support">Support</Link></li>
                        </ul>
                        <div className="digital-clock-wrapper-in-helpingLinks">
                            <DigitalClock/>
                        </div>
                    </div>
                </div>
            </div>

        </AnimationFlash>
    );
}

export default MyUser;
