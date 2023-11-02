import { faHouse } from '@fortawesome/free-solid-svg-icons';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, {useState} from 'react';
import { Link } from 'react-router-dom';
import './Navbar.css'
import { Button } from '../Button/Button'
import { useTranslation } from 'react-i18next';
import {useAuth} from "../../Containers/Authentication/AuthProvider";



function Navbar() {
    const [click, setClick] = useState(false);
    const [button, setButton] = useState(true);
    const handleClick = () => setClick(!click);
    const closeMobileMenu = () => setClick(false);
    const { isLoggedIn, logout } = useAuth();

    const {t, i18n} = useTranslation();

    const changeLanguage = (lng) => {
        i18n.changeLanguage(lng);
    };
    const handleLogout = () => {
        logout();
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
            <nav className='navbar'>
                <div className='navbar-container'>
                    <Link to='/' className='navbar-logo'>
                        <img src="/favicon.png" className="logo-image" />
                        TravelBuddy
                    </Link>
                    <div className='menu-icon' onClick={handleClick}>
                        <FontAwesomeIcon icon={faBars} />
                    </div>
                    <ul className={click ? 'nav-menu active' : 'nav-menu'}>
                            <div className="language-selector">
                                <select className="language-select" onChange={(e) => changeLanguage(e.target.value)}
                                        value={i18n.language}>
                                    <option value="en" className="language-option">English</option>
                                    <option value="de" className="language-option">Deutsch</option>
                                    <option value="jap" className="language-option">Japanese</option>
                                </select>
                            </div>
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
                    </ul>
                    {isLoggedIn ?
                        (button && <Button onClick={handleLogout} buttonStyle='btn--outline'>{t('navbar.logout')}</Button>)
                        : (
                        <Link to="/login">
                            {(button && <Button buttonStyle="btn--outline">{t('navbar.login')}</Button>)}
                        </Link>
                    )}
                </div>
            </nav>
        </>
    )
}

export default Navbar
