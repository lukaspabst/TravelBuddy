import React, {useEffect, useState} from 'react';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
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
import './LinkedSocialMediaGrid.scss';

const iconMapping = {
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

const LinkedSocialMediaGrid = () => {
    const [linkedMedia, setLinkedMedia] = useState([]);

    useEffect(() => {
        const storedLinks = JSON.parse(localStorage.getItem('socialMediaLinks')) || {};
        const mediaArray = Object.entries(storedLinks).map(([platform, link]) => ({
            label: platform,
            link: link,
        }));
        setLinkedMedia(mediaArray);
    }, []);

    return (
        <div className="linked-social-media-grid">
            {linkedMedia.map((media, index) => (
                <div key={index} className="linked-media-item">
                    <div className="icon-container">
                        <FontAwesomeIcon icon={iconMapping[media.label]}
                                         style={{fontSize: '25px', marginRight: '8px'}}/>
                    </div>
                    {media.link}
                </div>
            ))}
        </div>
    );
};

export default LinkedSocialMediaGrid;
