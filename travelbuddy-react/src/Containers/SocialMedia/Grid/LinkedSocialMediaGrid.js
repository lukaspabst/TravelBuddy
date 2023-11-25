import React from 'react';
import './LinkedSocialMediaGrid.scss';
const LinkedSocialMediaGrid = ({ linkedMedia }) => (
    <div className="linked-social-media-grid">
        {linkedMedia.map((media, index) => (
            <div key={index} className="linked-media-item">
                {media.label}: {media.link}
            </div>
        ))}
    </div>
);

export default LinkedSocialMediaGrid;
