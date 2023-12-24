import React from 'react';

const TripGeneralInfo = ({ tripData }) => {
    return (
        <div className="tab-content-general">
            <label>Trip Name:</label>
            <input type="text" value={tripData.nameTrip} readOnly/>

            <label>Start Date:</label>
            <input type="text" value={tripData.startDate} readOnly/>

            <label>End Date:</label>
            <input type="text" value={tripData.endDate} readOnly/>

            <label>Max Persons:</label>
            <input type="text" value={tripData.maxPersons} readOnly/>

            <label>Max Persons:</label>
            <input type="text" value={tripData.ma} readOnly/>
        </div>
    );
};

export default TripGeneralInfo;
