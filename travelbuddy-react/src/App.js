import React, {useEffect, useRef} from 'react';
import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom';
import './App.css';
import Navbar from './Components/General/Navbar/Navbar';
import LoginForm from './Containers/Login/LoginForm';
import RegisterForm from './Containers/Register/RegisterForm';
import {AuthProvider, useAuth} from './Containers/Authentication/AuthProvider';
import StartPage from './Components/StartingHome/StartPage/StartPage';
import Footer from './Components/General/Footer/Footer';
import useContainerHeight from './containerHeightCalc';
import CreateTrip from "./Components/MyTravel/CreateTrip/CreateTrip";
import MyTravels from "./Components/MyTravel/MyTravels/MyTravels";
import OpenTrips from "./Components/MyTravel/OpenTrips/OpenTrips";
import ClosedTrips from "./Components/MyTravel/ClosedTrips/ClosedTrips";
import Trip from "./Components/MyTravel/Trips/Trip";
import LoadingSpinner from "./Containers/PageTransition/LoadingSpinner/LoadingSpinner";
import PageTransition from "./Containers/PageTransition/PageTransition/PageTransition";
function App() {
    const appContainerRef = useRef(null);
    const { containerHeight, updateHeight } = useContainerHeight(appContainerRef);
    const { isLoggedIn } = useAuth();

    if (isLoggedIn === undefined) {
        return <LoadingSpinner />;
    }
    return (
        <Router>
            <AuthProvider>
                <div ref={appContainerRef}>
                    <Navbar />
                    <PageTransition>
                        <Routes>
                            <Route key="home" path="/" element={<StartPage />} />
                            <Route key="login" path="/login" element={<LoginForm />} />
                            <Route key="register" path="/register" element={<RegisterForm />} />
                            <Route key="travel" path="/travel" element={<CreateTrip />} />
                            <Route key="myTrips" path="/MyTrips" element={isLoggedIn ? <MyTravels /> : <Navigate to="/login" />} />
                            <Route key="openTravels" path="/openTravels" element={isLoggedIn ? <OpenTrips /> : <Navigate to="/login" />} />
                            <Route key="pastTravels" path="/pastTravels" element={isLoggedIn ? <ClosedTrips /> : <Navigate to="/login" />} />
                            <Route key="trip" path="/trip/:id" element={isLoggedIn ? <Trip /> : <Navigate to="/login" />} />
                        </Routes>
                    </PageTransition>
                </div>
                <Footer containerHeight={containerHeight} />
            </AuthProvider>
        </Router>
    );
}

export default App;
