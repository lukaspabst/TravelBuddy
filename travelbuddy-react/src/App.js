import React from 'react';
import Navbar from './Components/Navbar/Navbar';
import LoginForm from './Containers/Login/LoginForm';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import RegisterForm from "./Containers/Register/RegisterForm";

function App() {
    return (
            <Router>
                <Navbar />
                    <Routes>
                        <Route path="/login" element={<LoginForm />} />
                        <Route path="/register" element={<RegisterForm />} />
                    </Routes>
            </Router>
    );
}

export default App;
