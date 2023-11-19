import React from 'react';
import './Footer.scss';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faFacebook, faInstagram, faLinkedin, faWhatsapp} from "@fortawesome/free-brands-svg-icons";
import {useTranslation} from "react-i18next";


function Footer({}) {
    const {t, i18n} = useTranslation();
    return (
        <footer className="footer">
            <div className="footer-content">
                <div className="footer-links">
                       <a href="/">{t('footer.home')}</a>
                        <a href="/dienstleistungen">{t('footer.ourServices')}</a>
                        <a href="/kontakt">{t('footer.paymentOptions')}</a>
                        <a href="/über-uns">{t('footer.aboutUs')}</a>
                        <a href="/kontakt">{t('footer.contact')}</a>
                </div>
                <div className="footer-links">
                    <a href='URL für Instagram'>
                        <FontAwesomeIcon icon={faInstagram} />
                    </a>
                    <a href='URL für Facebook'>
                        <FontAwesomeIcon icon={faFacebook} />
                    </a>
                    <a href='URL für LinkedIn'>
                        <FontAwesomeIcon icon={faLinkedin} />
                    </a>
                    <a href='URL für WhatsApp'>
                        <FontAwesomeIcon icon={faWhatsapp} />
                    </a>
                </div>
            </div>
            <div className="footer-bottom">
                &copy; {new Date().getFullYear()} TravelBuddy GmbH. {t('footer.rightsReserved')}
            </div>
        </footer>
    );
}

export default Footer;
