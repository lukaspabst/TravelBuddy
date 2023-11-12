import React, {createContext, useContext, useEffect, useState} from 'react';
import Cookies from 'js-cookie';
import axios from "axios";
import { API_BASE_URL } from "../../config";

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const checkToken = async () => {
            try {
                const response = await axios.post(`${API_BASE_URL}/api/authenticate`, {}, { withCredentials: true });

                if (response.status === 200) {
                    setIsLoggedIn(true);
                } else {
                    setIsLoggedIn(false);
                }
            } catch (error) {
                setIsLoggedIn(false);
            }
        };

        checkToken();
    }, []);

    const login = () => {
        setIsLoggedIn(true);
    }

    const logout = () => {
            setIsLoggedIn(false);
            Cookies.remove('jwtToken');
        };

    return (
        <AuthContext.Provider value={{ isLoggedIn, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}
