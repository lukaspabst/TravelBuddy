import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import './RegisterForm.scss';
import {useTranslation} from "react-i18next";
import {useAuth} from "../../../Containers/Authentication/AuthProvider";
import axios from "axios";
import {API_BASE_URL} from "../../../config";
import {Button} from "../../../Containers/Button/Button";
import BackgroundImage from "../../General/Background/StartingBackground";
import {motion} from 'framer-motion';

function RegisterForm() {
    const {t} = useTranslation();
    const {checkToken} = useAuth()
    const [state, setState] = useState({
        username: '',
        email: '',
        password: '',
        handy: '',
    });

    const handleChange = (e) => {
        setState({...state, [e.target.name]: e.target.value});
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const {username, email, password, handy} = state;

        try {
            const response = await axios.post(
                `${API_BASE_URL}/api/register`,
                {username, email, password, handy},
                {withCredentials: true}
            );

            if (response.status === 201) {
                await checkToken();
                window.location.href = "/";
            } else if (response.status === 400) {
                setState({...state, message: t('errorMessages.badRequest')});
            } else if (response.status === 500) {
                setState({...state, message: t('errorMessages.internalServerError')});
            } else {
                setState({...state, message: t('errorMessages.registerError')});
            }
        } catch (error) {
            setState({...state, message: t('errorMessages.registerError')});
        }
    }

    return (
        <RegisterFormContent
            handleSubmit={handleSubmit}
            handleChange={handleChange}
            state={state}
            t={t}
        />
    );
}

function RegisterFormContent({handleSubmit, handleChange, state, t}) {
    return (
        <motion.div key="uniqueKey" initial={{opacity: 0}} animate={{opacity: 1}} exit={{opacity: 0}}>
            <div className="StartPage-content">
                <BackgroundImage/>
                <div className="register-form-container">
                    <h1>{t('register.title')}</h1>
                    <form onSubmit={handleSubmit}>
                        <label htmlFor="username">{t('register.usernameLabel')}</label>
                        <input
                            type="text"
                            id="username"
                            name="username"
                            required
                            value={state.username}
                            onChange={handleChange}
                        />
                        <label htmlFor="email">{t('register.emailLabel')}</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            required
                            value={state.email}
                            onChange={handleChange}
                        />
                        <label htmlFor="password">{t('register.passwordLabel')}</label>
                        <input
                            type="password"
                            id="password"
                            name="password"
                            required
                            value={state.password}
                            onChange={handleChange}
                        />
                        <label htmlFor="handy">{t('register.handyLabel')}</label>
                        <input
                            type="text"
                            id="handy"
                            name="handy"
                            value={state.handy}
                            onChange={handleChange}
                        />
                        <Button buttonStyle="btn--outline" type="submit">{t('register.registerButton')}</Button>
                    </form>
                    <p>
                        {t('register.loginMessage')}{' '}
                        <Link to="/login" id="register">
                            {t('register.loginLink')}
                        </Link>
                    </p>
                </div>
            </div>
        </motion.div>
    );
}

export default RegisterForm;
