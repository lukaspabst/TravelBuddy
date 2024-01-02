import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { API_BASE_URL } from '../../../config';
import AnimationFlash from '../../../Containers/Animations/PageTransitionAnimations/AnimationFlash';
import ProfileBackground from '../../General/Background/MyProfilBackground';
import './PublicUserProfile.scss';
import { useParams } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
    faFacebook,
    faTwitter,
    faInstagram,
    faLinkedin,
    faXing,
    faYoutube,
    faTwitch,
    faSnapchat,
    faPinterest,
    faReddit,
    faTumblr,
    faGithub,
    faGitlab,
    faBitbucket} from "@fortawesome/free-brands-svg-icons";

function PublicUserProfile() {
    const { username } = useParams();
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
        profilePicture: '',
        country: '',
        age: null,
    });

    function calculateAge(birthday) {
        const currentDate = new Date();
        const birthDate = new Date(birthday);
        let age = currentDate.getFullYear() - birthDate.getFullYear();

        if (currentDate.getMonth() < birthDate.getMonth() ||
            (currentDate.getMonth() === birthDate.getMonth() && currentDate.getDate() < birthDate.getDate())) {
            age--;
        }

        return age;
    }

    useEffect(() => {
        const fetchUserData = async () => {
            try {
                const response = await axios.get(
                    `${API_BASE_URL}/api/users/${username}`,{withCredentials: true}
                );

                if (response.status === 200) {
                    const userBirthday = response.data.birthday
                        ? new Date(response.data.birthday)
                        : null;

                    const userAge = userBirthday ? calculateAge(userBirthday) : null;

                    setUserData({
                        firstName: response.data.firstName,
                        lastName: response.data.lastName,
                        location: response.data.location,
                        birthday: userBirthday,
                        country: response.data.country,
                        zipCode: response.data.zipCode,
                        preferences: response.data.preferences,
                        travelDestination: response.data.travelDestination,
                        socialMediaLinks: response.data.socialMediaLinks,
                        gender: response.data.gender,
                        profilePicture: response.data.profilePicture,
                        age: userAge
                    });
                } else if (response.status === 404) {
                    // Handle user not found
                    console.log('User not found');
                }
            } catch (error) {
                console.error('Error fetching user data:', error);
            }
        };

        fetchUserData();
    }, [username]);  // Make sure to include username in the dependency array
    function SocialMediaIcon({ platform, link }) {
        const iconProps = {
            'Facebook': faFacebook,
            'Twitter': faTwitter,
            'Instagram': faInstagram,
            'LinkedIn': faLinkedin,
            'Xing': faXing,
            'YouTube': faYoutube,
            'Twitch': faTwitch,
            'Snapchat': faSnapchat,
            'Pinterest': faPinterest,
            'Reddit': faReddit,
            'Tumblr': faTumblr,
            'GitHub': faGithub,
            'GitLab': faGitlab,
            'Bitbucket': faBitbucket,
        };

        const icon = iconProps[platform];

        return (
            <a href={link} target="_blank" rel="noopener noreferrer">
                <FontAwesomeIcon icon={icon} className="social-media-icon"/>
            </a>
        );
    }

    return (
        <AnimationFlash>
            <div className="StartPage-content">
                <ProfileBackground />
                <div className="public-user-profile-container">
                    <h1>{username} Profile</h1>
                    <div className="public-user-profile-content">
                        <div className="public-user-profile-image">
                            <img
                                src={
                                    userData.profilePicture
                                        ? `data:image/png;base64,${userData.profilePicture}`
                                        : '/assets/pb_placeholder.png'
                                }
                                alt="Avatar"
                            />
                        </div>
                        <div className="public-user-profile-name">
                            <p>
                                {userData.firstName} {userData.lastName}
                            </p>
                        </div>
                        <div className="public-user-profile-location">
                            <p>
                                {userData.zipCode} {userData.location} <br/> {userData.country}
                            </p>
                        </div>
                        <div className="public-user-profile-age">
                            <p>
                                {userData.age} Years
                            </p>
                            <div className="age-hover-birthday">
                                {`Birthday: ${userData.birthday ? userData.birthday.toLocaleDateString() : 'N/A'}`}
                            </div>

                        </div>

                        <div
                            className="public-user-profile-social-media"
                            style={{
                                visibility:
                                    userData.socialMediaLinks &&
                                    Object.keys(userData.socialMediaLinks).length > 0
                                        ? 'visible'
                                        : 'hidden',
                            }}
                        >
                            <p>Social Media</p>
                            <div className="social-media-icons">
                                {Object.entries(userData.socialMediaLinks).map(([platform, link], index) => (
                                    <SocialMediaIcon key={index} platform={platform} link={link}/>
                                ))}
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </AnimationFlash>
    );
}

export default PublicUserProfile;
