import React, { useEffect, useState } from 'react';
import { API_BASE_URL } from '../../../../config';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEraser, faPen, faUser } from '@fortawesome/free-solid-svg-icons';
import { Button } from '../../../../Containers/Button/Button';
import { useTranslation } from 'react-i18next';
import AddMemberModal from "./AddMemberModal"; // Import the useTranslation hook

const TripMembersGrid = () => {
    const { tripId } = useParams();
    const [tripMembers, setTripMembers] = useState([]);
    const [isAddMemberModalOpen, setIsAddMemberModalOpen] = useState(false);
    const calculateGridPosition = (index) => {
        const row = Math.floor(index / 5) + 1;
        const col = (index % 5) + 1;
        return { row, col };
    };
    const [areMembersVisible, setAreMembersVisible] = useState(false);

    useEffect(() => {
        const fetchTripMembers = async () => {
            console.log('Current tripId:', tripId);
            try {
                const response = await axios.get(`${API_BASE_URL}/api/trips/${tripId}/members`, { withCredentials: true });
                console.log('Fetched trip members data:', response.data);
                setTripMembers(response.data);
            } catch (error) {
                console.error('An error occurred while fetching the trip members:', error);
            }
        };

        fetchTripMembers();
    }, [tripId]);

    const { t } = useTranslation(); // Initialize the useTranslation hook
    const openAddMemberModal = () => {
        setIsAddMemberModalOpen(true);
    };

    const closeAddMemberModal = () => {
        setIsAddMemberModalOpen(false);
    };
    return (
        <>
            <div className="trip-members-wrapper">
                <div className="trip-buttons-wrapper">
                    <div className="left-buttons">
                        <Button buttonStyle="btn--outline" onClick={() => setAreMembersVisible(!areMembersVisible)}>
                            {areMembersVisible ? t('tripMembersGrid.hideMembers') : t('tripMembersGrid.showMembers')}
                        </Button>
                        <Button buttonStyle="btn--outline" onClick={openAddMemberModal}>
                            {t('tripMembersGrid.addMember')}
                        </Button>
                    </div>
                    <div className="right-buttons">
                        <Button buttonStyle="btn--outline" onClick={() => console.log('Make Trip Public Button Clicked')}>
                            {t('trip.makeTripPublic')}
                        </Button>
                        <Button buttonStyle="btn--outline" onClick={() => console.log('View Candidates Button Clicked')}>
                            {t('trip.viewCandidates')}
                        </Button>
                    </div>
                </div>
                <div className="trip-members-grid">
                    {areMembersVisible &&
                        tripMembers.map((member, index) => {
                            const { row, col } = calculateGridPosition(index);
                            return (
                                <div key={index} style={{ gridArea: `${row} / ${col} / ${row + 1} / ${col + 1}` }} className="member-card-trip">
                                    <div className="trip-member-Role">
                                        <p>{member.role}</p>
                                    </div>
                                    <div className="imageWrapper-trip-member-pb">
                                        <img
                                            src={member.picture ? `data:image/png;base64,${member.picture}` : '/assets/pb_placeholder.png'}
                                            alt={member.name}
                                            className="trip-member-image"
                                        />
                                    </div>
                                    <div className="trip-member-name">
                                        <p style={{ flexGrow: 1 }}>{member.name}</p>
                                        <div className="icon-wrapper">
                                            {member.role !== 'Trip Organizer' && (
                                                <div className="icon">
                                                    <FontAwesomeIcon icon={faPen} onClick={() => console.log('Change Role Icon Clicked')} />
                                                    <div className="icon-tooltip">{t('tripMembersGrid.changeRoleTooltip')}</div>
                                                </div>
                                            )}
                                            {member.role !== 'Trip Organizer' && (
                                                <div className="icon">
                                                    <FontAwesomeIcon icon={faEraser} onClick={() => console.log('Remove Member Icon Clicked')} />
                                                    <div className="icon-tooltip">{t('tripMembersGrid.removeMemberTooltip')}</div>
                                                </div>
                                            )}
                                            <div className="icon">
                                                <FontAwesomeIcon icon={faUser} onClick={() => console.log('View Profile Icon Clicked')} />
                                                <div className="icon-tooltip">{t('tripMembersGrid.viewProfileTooltip')}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                </div>
            </div>
            <AddMemberModal isOpen={isAddMemberModalOpen} closeModal={closeAddMemberModal} />
        </>
    );
};

export default TripMembersGrid;
