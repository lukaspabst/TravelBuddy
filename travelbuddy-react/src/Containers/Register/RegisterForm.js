import React, { useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import './RegisterForm.scss';
import { useTranslation } from "react-i18next";
import { useAuth } from "../../Containers/Authentication/AuthProvider";
import axios from "axios";
import { API_BASE_URL } from "../../config";
import {Button} from "../../Components/Button/Button";
import BackgroundImage from "../../Components/Background/StartingBackground";

function RegisterForm() {
    const { t } = useTranslation();
    const { login } = useAuth();
    const navigate = useNavigate();
    const [state, setState] = useState({
        username: '',
        email: '',
        password: '',
        handy: '',
    });

    const handleChange = (e) => {
        setState({ ...state, [e.target.name]: e.target.value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const { username, email, password, handy } = state;

        try {
            const response = await axios.post(
                `${API_BASE_URL}/api/register`,
                { username, email, password, handy },
                { withCredentials: true }
            );

            if (response.status === 201) {
                login();
                navigate('/');
            } else if (response.status === 400) {
                // Handle Bad Request (status 400) - Display an error message to the user
                setState({ ...state, message: t('errorMessages.badRequest') });
            } else if (response.status === 500) {
                // Handle Internal Server Error (status 500) - Display an error message to the user
                setState({ ...state, message: t('errorMessages.internalServerError') });
            } else {
                // Handle other cases if needed
                setState({ ...state, message: t('errorMessages.registerError') });
            }
        } catch (error) {
            // Handle network or other errors
            setState({ ...state, message: t('errorMessages.registerError') });
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
function RegisterFormContent({ handleSubmit, handleChange, state, t }) {
    return (
        <div className="StartPage-content">
        <BackgroundImage />
        <div className="container">
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
        </div>
    );
}

export default RegisterForm;
