import React, {useEffect, useRef, useState} from 'react';
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
import {use} from "i18next";
import InfoIcon from "../../../Containers/InfoIcon/InfoIcon";
//TODO WE NEED COUNTRY FOR ZIPCODE AND LOCATION CHECK AND TEST STH ABOUT PRÄFERENZEN ALSO BACKEND CHECK FOR VALID BIRTHDAY
function MyUser() {
    const {t} = useTranslation();
    const [isEditMode, setIsEditMode] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);

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
        profilePicture:'',
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
                        gender: mapBackendValueToDropdownValue(response.data.gender),

                    });

                    setSelectedImage(response.data.profilePicture);
                    setIsEditMode(true);
                } else if (response.status === 404) {
                    setIsEditMode(false);
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };

        fetchUserData();
    }, []);

    const handleSaveProfile = async () => {
        try {
            const requestData = {
                firstName: userData.firstName,
                lastName: userData.lastName,
                location: userData.location,
                birthday: userData.birthday ? (new Date(userData.birthday)).toISOString() : '',
                zipCode: userData.zipCode,
                preferences: userData.preferences,
                travelDestination: userData.travelDestination,
                socialMediaLinks: JSON.parse(localStorage.getItem('socialMediaLinks')),
                gender: userData.gender,
                profilePicture: JSON.parse(localStorage.getItem("userProfilePicture"))
            };

            let response;
            localStorage.removeItem("userProfilePicture")
            if (isEditMode) {

                response = await axios.post(`${API_BASE_URL}/api/users/update`, requestData, {
                    withCredentials: true,
                });
            } else {
                // Register a new user profile
                response = await axios.post(`${API_BASE_URL}/api/users/register`, requestData, {
                    withCredentials: true,
                });
            }

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
        <UserProfileContent
            userData={userData}
            setUserData={setUserData}
            t={t}
            onSaveProfile={handleSaveProfile}
            selectedImage={selectedImage}
            setSelectedImage={setSelectedImage}
        />
    );
}

function UserProfileContent({ userData, setUserData, t, onSaveProfile, selectedImage, setSelectedImage }) {
    const [currentTime, setCurrentTime] = useState(new Date());
    const [errorInfo, setErrorInfo] = useState(null);
    const fileInputRef = useRef(null);
    const handleUploadButtonClick = () => {
        fileInputRef.current.click();
    };
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                const base64String = reader.result;
                const indexOfComma = base64String.indexOf(',') + 1;
                const trimmedImage = base64String.substring(indexOfComma);

                // Convert base64 to ByteArray
                const byteCharacters = atob(trimmedImage);
                const byteNumbers = new Array(byteCharacters.length);
                for (let i = 0; i < byteCharacters.length; i++) {
                    byteNumbers[i] = byteCharacters.charCodeAt(i);
                }
                const byteArray = new Uint8Array(byteNumbers);

                // Save ByteArray to localStorage
                localStorage.setItem("userProfilePicture", JSON.stringify(Array.from(byteArray)));

                // Set selectedImage state
                setSelectedImage(trimmedImage);
            };
            reader.readAsDataURL(file);
        }
    };

    useEffect(() => {
        const intervalId = setInterval(() => {
            setCurrentTime(new Date());
        }, 1000);

        return () => clearInterval(intervalId);
    }, []);

    const handleInputChange = (e) => {
        const {id, value} = e.target;
        setUserData((prevUserData) => ({
            ...prevUserData,
            [id]: value,
        }));
    };

    const handleGenderSelection = (e) => {
        const selectedGender = e.target.value;
        setUserData((prevUserData) => ({
            ...prevUserData,
            gender: selectedGender,
        }));
    };
    const handleDateChange = (date) => {
        const userAge = new Date().getFullYear() - new Date(date).getFullYear();
        if (userAge < 16) {
            setErrorInfo(t('errorMessages.ageRestriction'));
            return;
        }
        setUserData((prevUserData) => ({
            ...prevUserData,
            birthday: date,
        }));
        setErrorInfo(null);
    };
    return (
        <AnimationFlash>
            <div className="StartPage-content">
                <ProfileBackground/>
                <div className="user-profile-container">
                    <h1>{t('userProfile.title')}</h1>
                    <div className="user-profile-content">
                        <div className="user-profile-image">
                            <img
                                src={selectedImage ? `data:image/png;base64,${selectedImage}` : '/assets/pb_placeholder.png'}
                                alt="Avatar"/>
                            <input
                                type="file"
                                accept="image/*"
                                onChange={handleImageChange}
                                style={{display: 'none'}}
                                ref={fileInputRef}
                            />
                            <label htmlFor="upload-avatar-input">
                                <Button buttonStyle="btn--small-avatar" onClick={handleUploadButtonClick}>
                                    {t('userProfile.uploadAvatar')}
                                </Button>
                            </label>
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
                        {errorInfo ? <InfoIcon tooltipMessage={errorInfo}/> :
                            <Button buttonStyle="btn--outline" onClick={onSaveProfile}>
                                {t('userProfile.editProfileButton')}
                            </Button>
                        }
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
