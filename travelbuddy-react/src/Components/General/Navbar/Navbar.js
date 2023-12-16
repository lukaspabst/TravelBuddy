import {faBars} from '@fortawesome/free-solid-svg-icons';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import './Navbar.css'
import {Button} from '../../../Containers/Button/Button'
import {useTranslation} from 'react-i18next';
import {useAuth} from "../../../Containers/Authentication/AuthProvider";
import axios from "axios";
import {API_BASE_URL} from "../../../config";
import {languageNames} from "../../../i18nConfig";


function Navbar() {
    const [click, setClick] = useState(false);
    const [button, setButton] = useState(true);

    const [profilePicture, setProfilePicture] = useState(null);

    const handleClick = () => setClick(!click);
    const closeMobileMenu = () => setClick(false);
    const {isLoggedIn, logout} = useAuth();

    const {t, i18n} = useTranslation();
    const languageName = languageNames[i18n.language];

    const [profileMenu, setProfileMenu] = useState(false);

    const changeLanguage = (language) => {
        i18n.changeLanguage(language);
        localStorage.setItem('selectedLanguage', language);
    };
    useEffect(() => {
        const fetchUserProfile = async () => {
            try {
                if (isLoggedIn) {
                    // Make an API request to fetch user profile information
                    const response = await axios.get(`${API_BASE_URL}/api/users/profile-picture`, { withCredentials: true });
                    if (response.data && response.data.profilePicture) {
                        setProfilePicture(response.data.profilePicture);
                    }
                }
            } catch (error) {
            }
        };
        fetchUserProfile();
    }, [isLoggedIn]);

    const handleLogout = () => {
        const checkToken = async () => {
            try {
                const response = await axios.post(`${API_BASE_URL}/api/logout`, {}, {withCredentials: true});

                if (response.status === 200) {
                    logout();
                }
            } catch (error) {
            }
        };
        checkToken();
    };
    const showButton = () => {
        if (window.innerWidth <= 960) {
            setButton(false);
        } else {
            setButton(true);
        }
    };

    window.addEventListener('resize', showButton);

    return (
        <>
            <header>
                <nav className='navbar'>
                    <div className='navbar-container'>
                        <Link to='/' className='navbar-logo'>
                            <img src="/favicon.png" className="logo-image"/>
                            TravelBuddy
                        </Link>
                        <div className='menu-icon' onClick={handleClick}>
                            <FontAwesomeIcon icon={faBars}/>
                        </div>
                        <ul className={click ? 'nav-menu active' : 'nav-menu'}>
                            <li className='nav-item'>
                                <div className="language-selector">
                                    <div className="selected-language" onClick={() => changeLanguage(i18n.language)}>
                                        <img src={`/assets/Flaggs/${i18n.language}.png`} alt={i18n.language}/>
                                        <span>{t(`language.${languageName}`)}</span>
                                    </div>
                                    <div className="language-dropdown">
                                        <ul className="language-list">
                                            <li className="language-list-item" onClick={() => changeLanguage('de')}>
                                                <img src="/assets/Flaggs/de.png" alt="German"/>
                                                <span> {t('language.german')}</span>
                                            </li>
                                            <li className="language-list-item" onClick={() => changeLanguage('jp')}>
                                                <img src="/assets/Flaggs/jp.png" alt="Japanese"/>
                                                <span> {t('language.japanese')}</span>
                                            </li>
                                            <li className="language-list-item" onClick={() => changeLanguage('gb')}>
                                                <img src="/assets/Flaggs/gb.png" alt="English"/>
                                                <span> {t('language.english')}</span>
                                            </li>
                                        </ul>
                                    </div>
                                </div>
                            </li>
                            <li className='nav-item'>
                                <Link to='/' className='nav-links' onClick={closeMobileMenu}>
                                    {t('navbar.home')}
                                </Link>
                            </li>
                            <li className='nav-item'>
                                <Link to='/' className='nav-links' onClick={closeMobileMenu}>
                                    {t('navbar.aboutUs')}
                                </Link>
                            </li>
                            {isLoggedIn && (
                                <li className='nav-item'>
                                    <div className="user-profile">
                                        <img src={profilePicture ? `data:image/png;base64,${profilePicture}` : '/assets/pb_placeholder.png'} alt="Avatar" className="profile-image"/>
                                        <div className="profile-dropdown">
                                            <ul className="profile-menu">
                                                <li className="profile-menu-item">
                                                    <Link to='/MyProfile' className='nav-links'
                                                          onClick={closeMobileMenu}>
                                                        {t('navbar.MyProfile')}
                                                    </Link>
                                                </li>
                                                <li className="profile-menu-item">
                                                    <Link to='/MyTrips' className='nav-links' onClick={closeMobileMenu}>
                                                        {t('navbar.MyTrips')}
                                                    </Link>
                                                </li>
                                                <li className="profile-menu-item">
                                                    <Link to='/Settings' className='nav-links'
                                                          onClick={closeMobileMenu}>
                                                        {t('navbar.Settings')}
                                                    </Link>
                                                </li>
                                                <li className="profile-menu-item">
                                                    <Link to='/Support' className='nav-links' onClick={closeMobileMenu}>
                                                        {t('navbar.Support')}
                                                    </Link>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </li>
                            )}
                            <li>
                                <div className="nav-item">
                                    {isLoggedIn ?
                                        <Button onClick={handleLogout}
                                                buttonStyle='btn--outline'>{t('navbar.logout')}</Button>
                                        : (
                                            <Link to="/login">
                                                {(button &&
                                                    <Button buttonStyle="btn--outline">{t('navbar.login')}</Button>)}
                                            </Link>
                                        )}
                                </div>
                            </li>
                        </ul>
                    </div>
                </nav>
            </header>
        </>
    )
}

export default Navbar
