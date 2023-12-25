import React, {useEffect, useState} from 'react';
import {API_BASE_URL} from "../../../../config";
import {useParams} from "react-router-dom";
import axios from "axios";

const TripMembersGrid = () => {
    const { tripId } = useParams();
    const [tripMembers, setTripMembers] = useState([]);
    const calculateGridPosition = (index) => {
        // Hier können Sie die Logik für die Berechnung der Rasterposition implementieren
        const row = Math.floor(index / 5) + 1;
        const col = (index % 5) + 1;
        return { row, col };
    };
    useEffect(() => {
        const fetchTripMembers = async () => {
            console.log('Current tripId:', tripId);  // logging the tripId
            try {
                const response = await axios.get(`${API_BASE_URL}/api/trips/${tripId}/members`, {withCredentials: true});
                console.log('Fetched trip members data:', response.data);
                setTripMembers(response.data);
            } catch (error) {
                console.error('An error occurred while fetching the trip members:', error);
            }
        };

        fetchTripMembers();
    }, [tripId]);

    return (
        <div className="trip-members-grid">
            {tripMembers.map((member, index) => {
                const { row, col } = calculateGridPosition(index);
                return (
                    <div key={index} style={{ gridArea: `${row} / ${col} / ${row + 1} / ${col + 1}` }}>
                        <img
                            src={member.picture ? `data:image/png;base64,${member.picture}` : '/assets/pb_placeholder.png'}
                            alt={member.name}
                            className="trip-member-image"
                        />
                        <div>{member.name}</div>
                    </div>
                );
            })}
        </div>
    );
};

export default TripMembersGrid;
