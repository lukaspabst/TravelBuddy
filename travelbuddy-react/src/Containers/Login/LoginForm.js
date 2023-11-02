import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import './LoginForm.scss';
import { useTranslation } from "react-i18next";
import axios from "axios";

function LoginForm() {
    const [state, setState] = useState({
        username: '',
        password: '',
        message: '',
    });

    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleChange = (e) => {
        setState({ ...state, [e.target.name]: e.target.value });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const { username, password } = state;

        try {
            const request = await axios.post('http://localhost:8080/login', { username, password }, { withCredentials: true });
            if (request.status === 200) {
                navigate('/');
            } else if (request.status === 401 || request.status === 403) {
                setState({ ...state, message: 'Username oder Password falsch.' });
            } else if (request.status === 500) {
                setState({ ...state, message: 'Fehler: Interner Serverfehler.' });
            }
        } catch (error) {
            setState({ ...state, message: 'Fehler: Interner Serverfehler.' });
        }
    }

    return (
        <LoginFormContent handleSubmit={handleSubmit} handleChange={handleChange} state={state} t={t} navigate={navigate} />
    );
}

function LoginFormContent({ handleSubmit, handleChange, state, t, navigate }) {
    return (
        <div className="container">
            <div className="login-form-container">
                <h1>{t('login.title')}</h1>
                <form onSubmit={handleSubmit}>
                    <label htmlFor="username">{t('login.usernameLabel')}</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                        required
                        value={state.username}
                        onChange={handleChange}
                    />
                    <label htmlFor="password">{t('login.passwordLabel')}</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        required
                        value={state.password}
                        onChange={handleChange}
                    />
                    <button type="submit">{t('login.loginButton')}</button>
                </form>
                <p>
                    {t('login.signupMessage')}{' '}
                    <Link to="/register" id="signup-link">
                        {t('login.signupLink')}
                    </Link>
                </p>
                <p>{state.message}</p>
            </div>
        </div>
    );
}

export default LoginForm;
