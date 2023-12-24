import React, { useState, useEffect } from 'react';
import { ThemeContext } from './ThemeContext';

export const ThemeProvider = ({ children }) => {
    // Check if there is a saved theme in localStorage, or default to 'dark'
    const savedTheme = localStorage.getItem('theme') || 'dark';
    const [theme, setTheme] = useState(savedTheme);

    // Effect to update the CSS variables and save the theme when it changes
    useEffect(() => {
        const root = window.document.documentElement;

        root.style.setProperty('--bg-color', theme === 'light' ? 'var(--bg-color-light)' : 'var(--bg-color-dark)');
        root.style.setProperty('--font-color', theme === 'light' ? 'var(--font-color-light)' : 'var(--font-color-dark)');
        root.style.setProperty('--diff-font-color', theme === 'light' ? 'var(--font-color-dark)' : 'var(--font-color-light)');
        root.style.setProperty('--bg-color-input', theme === 'light' ? 'var(--bg-color-input-light)' : 'var(--bg-color-input-dark)');
        root.style.setProperty('--bg-color-animation', theme === 'light' ? 'var(--bg-color-animation-light)' : 'var(--bg-color-animation-dark)');

        // Save the theme in localStorage
        localStorage.setItem('theme', theme);
    }, [theme]);

    // Define a method to toggle theme
    const toggleTheme = () => {
        setTheme((theme) => (theme === 'light' ? 'dark' : 'light'));
    };

    return (
        <ThemeContext.Provider value={{ theme, toggleTheme }}>
            {children}
        </ThemeContext.Provider>
    );
};
