// App.js
import React, { useRef } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import './App.css';
import Navbar from './Components/Navbar/Navbar';
import LoginForm from './Containers/Login/LoginForm';
import RegisterForm from './Containers/Register/RegisterForm';
import { AuthProvider } from './Containers/Authentication/AuthProvider';
import StartPage from './Components/StartPage/StartPage';
import Footer from './Components/Footer/Footer';
import useContainerHeight from './containerHeightCalc'; // Importiere die benutzerdefinierte Hook

function App() {
    const appContainerRef = useRef(null);
    const containerHeight = useContainerHeight(appContainerRef);

    return (
        <Router>
            <AuthProvider>
                <div ref={appContainerRef}>
                    <Navbar />
                    <Routes>
                        <Route path="/" element={<StartPage />} />
                        <Route path="/login" element={<LoginForm />} />
                        <Route path="/register" element={<RegisterForm />} />
                    </Routes>
                </div>
                <Footer containerHeight={containerHeight} />
            </AuthProvider>
        </Router>
    );
}

export default App;
