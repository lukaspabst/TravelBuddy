import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import './RegisterForm.scss';
import { useTranslation } from "react-i18next";

class RegisterForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            email: '',
            password: '',
            handy: '',
        };
    }

    handleChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    }

    handleSubmit = (e) => {
        e.preventDefault();
        const { username, email, password, handy } = this.state;

    }

    render() {
        return (
            <RegisterFormContent
                handleSubmit={this.handleSubmit}
                handleChange={this.handleChange}
                state={this.state}
            />
        );
    }
}

function RegisterFormContent({ handleSubmit, handleChange, state }) {
    const { t } = useTranslation();

    return (
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
                    <button type="submit">{t('register.registerButton')}</button>
                </form>
                <p>
                    {t('register.loginMessage')}{' '}
                    <Link to="/login" id="register">
                        {t('register.loginLink')}
                    </Link>
                </p>
            </div>
        </div>
    );
}

export default RegisterForm;
