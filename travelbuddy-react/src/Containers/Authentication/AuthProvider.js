import React, { createContext, useContext, useEffect, useState } from 'react';
import Cookies from 'js-cookie';
import axios from 'axios';
import { API_BASE_URL } from '../../config';

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    // Check if there is a saved isLoggedIn state in sessionStorage, or default to false
    const savedIsLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';
    const [isLoggedIn, setIsLoggedIn] = useState(savedIsLoggedIn);
    const [isLoading, setIsLoading] = useState(true);

    const checkToken = async () => {
        setIsLoading(true);
        try {
            const response = await axios.post(`${API_BASE_URL}/api/authenticate`, {}, { withCredentials: true });

            if (response.status === 200) {
                setIsLoggedIn(true);
                sessionStorage.setItem('isLoggedIn', 'true');
            } else {
                setIsLoggedIn(false);
                sessionStorage.setItem('isLoggedIn', 'false');
            }
        } catch (error) {
            setIsLoggedIn(false);
            sessionStorage.setItem('isLoggedIn', 'false');
        } finally {
            setIsLoading(false);
        }
    };

    useEffect(() => {
        checkToken();
    }, []);

    const logout = () => {
        setIsLoggedIn(false);
        sessionStorage.setItem('isLoggedIn', 'false');
        Cookies.remove('jwtToken');
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, isLoading, checkToken, logout }}>
            {children}
        </AuthContext.Provider>
    );
}
