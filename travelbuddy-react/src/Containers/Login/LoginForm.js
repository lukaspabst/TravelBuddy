import React, { Component } from 'react';
import {Link} from 'react-router-dom';
import './LoginForm.scss';

class LoginForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
        };
    }

    handleChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    }

    handleSubmit = (e) => {
        e.preventDefault();

    }

    render() {
        return (
            <div className="container">
            <div className="login-form-container">
                <h1>Login</h1>
                <form onSubmit={this.handleSubmit}>
                        <label htmlFor="username">Username</label>
                        <input type="username" id="username" name="username"
                               required
                               value={this.state.username}
                               onChange={this.handleChange}
                        />
                        <label htmlFor="password">Password</label>
                        <input type="password" id="password" name="password" required
                            value={this.state.password}
                            onChange={this.handleChange}
                        />
                    <button type="submit">Login</button>
                </form>
                <p>Don't have an account? <Link to="/register" id="signup-link">Sign up</Link></p>
            </div>
            </div>
        );
    }
}

export default LoginForm;
