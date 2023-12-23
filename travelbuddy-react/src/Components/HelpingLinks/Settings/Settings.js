import React, {useEffect, useRef, useState} from 'react';
import AnimationFlash from "../../../Containers/Animations/PageTransitionAnimations/AnimationFlash";
import ProfileBackground from "../../General/Background/MyProfilBackground";
import './Settings.scss';
import DarkLightSwitch from "../../../Containers/Themes/DarkLightSwitch";

function Settings() {

    return (
        <AnimationFlash>
            <div className="StartPage-content">
                <ProfileBackground/>
                <div className="user-profile-container">
                    <DarkLightSwitch/>
                </div>
            </div>

        </AnimationFlash>
    );
}

export default Settings;
