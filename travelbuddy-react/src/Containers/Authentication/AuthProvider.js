import React, {createContext, useContext, useEffect, useState} from 'react';
import Cookies from 'js-cookie';
import axios from "axios";
import {API_BASE_URL} from "../../config";

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({children}) {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const checkToken = async () => {
            try {
                const response = await axios.post(`${API_BASE_URL}/api/authenticate`, {}, {withCredentials: true});

                if (response.status === 200) {
                    setIsLoggedIn(true);
                } else {
                    setIsLoggedIn(false);
                }
            } catch (error) {
                setIsLoggedIn(false);
            } finally {
                setIsLoading(false); // Set loading state to false regardless of the result
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
        <AuthContext.Provider value={{isLoggedIn, isLoading, login, logout}}>
            {children}
        </AuthContext.Provider>
    );
}
