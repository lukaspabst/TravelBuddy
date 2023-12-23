import React, {useRef} from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import './App.css';
import Navbar from './Components/General/Navbar/Navbar';
import LoginForm from './Components/MyUser/Login/LoginForm';
import RegisterForm from './Components/MyUser/Register/RegisterForm';
import {AuthProvider, useAuth} from './Containers/Authentication/AuthProvider';
import StartPage from './Components/StartingHome/StartPage/StartPage';
import Footer from './Components/General/Footer/Footer';
import CreateTrip from "./Components/MyTravel/CreateTrip/CreateTrip";
import MyTravels from "./Components/MyTravel/MyTravels/MyTravels";
import OpenTrips from "./Components/MyTravel/OpenTrips/OpenTrips";
import ClosedTrips from "./Components/MyTravel/ClosedTrips/ClosedTrips";
import Trip from "./Components/MyTravel/Trips/Trip";

import {AnimatePresence} from "framer-motion";
import MyUser from "./Components/MyUser/MyUser/MyUser";
import {ThemeProvider} from "./Containers/Themes/ThemeProvider";
import Settings from "./Components/HelpingLinks/Settings/Settings";

function App() {
    const appContainerRef = useRef(null);
    const {isLoggedIn, isLoading} = useAuth();

    return (
        <ThemeProvider>
        <Router>
            <AuthProvider>
                <div ref={appContainerRef}>
                    <Navbar/>
                    <AnimatePresence mode="out-in">
                        <Routes>
                            <Route path="/" element={<StartPage/>}/>
                            <Route path="/login" element={<LoginForm/>}/>
                            <Route path="/register" element={<RegisterForm/>}/>
                            <Route path="/travel" element={<CreateTrip/>}/>
                            <Route path="/MyTrips" element={isLoggedIn ? <MyTravels/> : <Navigate to="/login"/>}/>
                            <Route path="/openTravels" element={isLoggedIn ? <OpenTrips/> : <Navigate to="/login"/>}/>
                            <Route path="/pastTravels" element={isLoggedIn ? <ClosedTrips/> : <Navigate to="/login"/>}/>
                            <Route path="/trip/:id" element={isLoggedIn ? <Trip/> : <Navigate to="/login"/>}/>
                            <Route path="/myProfile" element={isLoggedIn ? <MyUser/> : <Navigate to="/login"/>}/>
                            <Route path="/settings" element={isLoggedIn ? <Settings/> : <Navigate to="/login"/>}/>
                        </Routes>
                    </AnimatePresence>
                </div>
                <Footer/>
            </AuthProvider>
        </Router>
        </ThemeProvider>
    );
}

export default App;
