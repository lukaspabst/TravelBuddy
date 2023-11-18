import React, {useEffect, useRef} from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.css';
import Navbar from './Components/General/Navbar/Navbar';
import LoginForm from './Containers/Login/LoginForm';
import RegisterForm from './Containers/Register/RegisterForm';
import { AuthProvider } from './Containers/Authentication/AuthProvider';
import StartPage from './Components/StartingHome/StartPage/StartPage';
import Footer from './Components/General/Footer/Footer';
import useContainerHeight from './containerHeightCalc';
import CreateTrip from "./Components/MyTravel/CreateTrip/CreateTrip";
import MyTravels from "./Components/MyTravel/MyTravels/MyTravels";
function App() {
    const appContainerRef = useRef(null);
    const { containerHeight, updateHeight } = useContainerHeight(appContainerRef);

    return (
        <Router>
            <AuthProvider>
                <div ref={appContainerRef}>
                    <Navbar />
                    <Routes>
                        <Route path="/" element={<StartPage />} />
                        <Route path="/login" element={<LoginForm />} />
                        <Route path="/register" element={<RegisterForm />} />
                        <Route path="/travel" element={<CreateTrip />} />
                        <Route path="/MyTrips" element={<MyTravels />} />
                    </Routes>
                </div>
                <Footer containerHeight={containerHeight} />
            </AuthProvider>
        </Router>
    );
}

export default App;
