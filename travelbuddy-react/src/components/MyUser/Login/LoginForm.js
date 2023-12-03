import React, {useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import './LoginForm.scss';
import {useTranslation} from "react-i18next";
import axios from "axios";
import {useAuth} from "../../../Containers/Authentication/AuthProvider";
import {Button} from "../../../Containers/Button/Button";
import {API_BASE_URL} from "../../../config";
import BackgroundImage from "../../General/Background/StartingBackground";
import {motion} from 'framer-motion';

function LoginForm() {
    const [state, setState] = useState({
        username: '',
        password: '',
        message: '',
    });

    const navigate = useNavigate();
    const {t} = useTranslation();
    const {login} = useAuth()
    const handleChange = (e) => {
        setState({...state, [e.target.name]: e.target.value});
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        const {username, password} = state;

        try {
            const request = await axios.post(`${API_BASE_URL}/api/login`, {
                username,
                password
            }, {withCredentials: true});
            if (request.status === 200) {
                login();
                navigate('/');
            } else if (request.status === 401) {
                setState({...state, message: t('errorMessages.wrongCredentials')});
            } else if (request.status === 403) {
                setState({...state, message: t('errorMessages.wrongCredentials')});
            } else if (request.status === 500) {
                setState({...state, message: t('errorMessages.internalServerError')});
            }
        } catch (error) {
            setState({...state, message: t('errorMessages.internalServerError')});
        }
    }

    return (
        <LoginFormContent handleSubmit={handleSubmit} handleChange={handleChange} state={state} t={t}
                          navigate={navigate}/>
    );
}

function LoginFormContent({handleSubmit, handleChange, state, t, navigate}) {
    return (
        <motion.div key="uniqueKey" initial={{opacity: 0}} animate={{opacity: 1}} exit={{opacity: 0}}>
            <div className="StartPage-content">
                <BackgroundImage/>
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
                        <Button buttonStyle="btn--outline" type="submit">{t('login.loginButton')}</Button>
                    </form>
                    <p>
                        {t('login.signupMessage')}{' '}
                        <Link to="/register" id="signup-link">
                            {t('login.signupLink')}
                        </Link>
                    </p>
                    <p className="errorMessages">{state.message}</p>
                </div>
            </div>
        </motion.div>
    );
}

export default LoginForm;
