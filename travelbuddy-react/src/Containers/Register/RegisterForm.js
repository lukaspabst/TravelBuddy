import React, { Component } from 'react';
import './RegisterForm.scss';
import {Link} from 'react-router-dom';

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

    }

    render() {
        return (
            <div className="container">
                <div className="register-form-container">
                    <h1>Login</h1>
                    <form onSubmit={this.handleSubmit}>
                        <label htmlFor="username">Username</label>
                        <input type="username" id="username" name="username"
                               required
                               value={this.state.username}
                               onChange={this.handleChange}
                        />
                        <label htmlFor="email">Email</label>
                        <input type="email" id="email" name="email" required
                               value={this.state.email}
                               onChange={this.handleChange}
                        />
                        <label htmlFor="password">Password</label>
                        <input type="password" id="password" name="password" required
                               value={this.state.password}
                               onChange={this.handleChange}
                        />
                        <label htmlFor="handy">Handy</label>
                        <input type="handy" id="handy" name="handy"
                               value={this.state.handy}
                               onChange={this.handleChange}
                        />
                        <button type="submit">Login</button>
                    </form>
                    <p>Already have an account? <Link to="/login" id="register">Login</Link></p>
                </div>
            </div>
        );
    }
}

export default RegisterForm;
