import React, {createContext, useContext, useEffect, useState} from 'react';
import Cookies from 'js-cookie';

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export function AuthProvider({ children }) {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        const jwtToken = Cookies.get('jwtToken');
        console.log("Funktion wird ausgeführt"+jwtToken);
        if (jwtToken) {
            try {
                const decodedToken = jwtToken.split('.')[1];
                const decodedPayload = JSON.parse(atob(decodedToken));
                if (decodedPayload.exp * 1000 > Date.now()) {
                    setIsLoggedIn(true);
                } else {
                    setIsLoggedIn(false);
                }
            } catch (error) {
            }
        }
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
