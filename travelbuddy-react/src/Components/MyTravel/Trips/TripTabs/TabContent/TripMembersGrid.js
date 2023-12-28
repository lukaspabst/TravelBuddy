import React, { useEffect, useState } from 'react';
import { API_BASE_URL } from '../../../../../config';
import { Link, useParams } from 'react-router-dom';
import axios from 'axios';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEraser, faPen, faUser } from '@fortawesome/free-solid-svg-icons';
import { Button } from '../../../../../Containers/Button/Button';
import { useTranslation } from 'react-i18next';
import AddMemberModal from '../MemberModals/AddMemberModal';
import '../StylesForModalAndTabContent/Modal.scss';
import ChangeRoleModal from '../MemberModals/ChangeRoleModal';
import RemoveMemberModal from '../MemberModals/RemoveMemberModal';

const TripMembersGrid = (role) => {
    const { tripId } = useParams();
    const [tripMembers, setTripMembers] = useState([]);
    const [isAddMemberModalOpen, setIsAddMemberModalOpen] = useState(false);
    const [visibleMembers, setVisibleMembers] = useState([]);
    const calculateGridPosition = (index) => {
        const row = Math.floor(index / 5) + 1;
        const col = (index % 5) + 1;
        return { row, col };
    };
    const [areMembersVisible, setAreMembersVisible] = useState(false);
    const updateGrid = async () => {
        try {
            const response = await axios.get(`${API_BASE_URL}/api/trips/${tripId}/members`, { withCredentials: true });
            console.log('Fetched trip members data:', response.data);
            setTripMembers(response.data);
        } catch (error) {
            console.error('An error occurred while fetching the trip members:', error);
        }
    };
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

    const { t } = useTranslation();
    const openAddMemberModal = () => {
        setIsAddMemberModalOpen(true);
    };

    const closeAddMemberModal = () => {
        setIsAddMemberModalOpen(false);
    };
    const [isRemoveMemberModalOpen, setRemoveMemberModalOpen] = useState(false);

    // Example usage for the "Remove Member" icon
    const handleRemoveMemberClick = (username) => {
        setSelectedMember({ username });
        setRemoveMemberModalOpen(true);
    };
    const [isChangeRoleModalOpen, setChangeRoleModalOpen] = useState(false);
    const [selectedMember, setSelectedMember] = useState(null);
    // Example usage for the "Change Role" icon
    const handleChangeRoleClick = (username, role) => {
        setSelectedMember({ username, role });
        setChangeRoleModalOpen(true);
    };

    return (
        <>
            <div className="trip-members-wrapper">
                <div className="trip-buttons-wrapper">
                    <div className="left-buttons">
                        <Button
                            buttonStyle="btn--outline"
                            onClick={() => setAreMembersVisible(!areMembersVisible)}
                        >
                            {areMembersVisible
                                ? t('tripMembersGrid.hideMembers')
                                : t('tripMembersGrid.showMembers')}
                        </Button>
                        <Button buttonStyle="btn--outline" onClick={openAddMemberModal}>
                            {t('tripMembersGrid.addMember')}
                        </Button>
                    </div>
                    <div className="right-buttons">
                        <Button
                            buttonStyle="btn--outline"
                            onClick={() => console.log('nerv nicht')}
                        >
                            {t('trip.makeTripPublic')}
                        </Button>
                        <Button
                            buttonStyle="btn--outline"
                            onClick={() => console.log('View Candidates Button Clicked')}
                        >
                            {t('trip.viewCandidates')}
                        </Button>
                    </div>
                </div>
                <div className="trip-members-grid">
                    {areMembersVisible &&
                        tripMembers.map((member, index) => {
                            const { row, col } = calculateGridPosition(index);
                            const memberStyle = {
                                gridArea: `${row} / ${col} / ${row + 1} / ${col + 1}`,
                            };
                            console.log(calculateGridPosition(index));
                            return (
                                <div
                                    key={index}
                                    style={memberStyle}
                                    className={`member-card-trip ${areMembersVisible ? 'enter' : 'exit'}`}
                                >
                                    <div className="trip-member-Role">
                                        <p>{member.role}</p>
                                    </div>
                                    <div className="imageWrapper-trip-member-pb">
                                        <img
                                            src={
                                                member.picture
                                                    ? `data:image/png;base64,${member.picture}`
                                                    : '/assets/pb_placeholder.png'
                                            }
                                            alt={member.name}
                                            className="trip-member-image"
                                        />
                                    </div>
                                    <div className="trip-member-name">
                                        <p style={{ flexGrow: 1 }}>{member.name}</p>
                                        <div className="icon-wrapper">
                                            {member.role !== 'Trip Organizer' &&
                                                role.role === 'Trip Organizer' && (
                                                    <div className="icon">
                                                        <FontAwesomeIcon
                                                            icon={faPen}
                                                            onClick={() =>
                                                                handleChangeRoleClick(
                                                                    member.username,
                                                                    member.role
                                                                )
                                                            }
                                                        />
                                                        <div className="icon-tooltip">
                                                            {t('tripMembersGrid.changeRoleTooltip')}
                                                        </div>
                                                    </div>
                                                )}
                                            {member.role !== 'Trip Organizer' && (
                                                <div className="icon">
                                                    <FontAwesomeIcon
                                                        icon={faEraser}
                                                        onClick={() =>
                                                            handleRemoveMemberClick(member.username)
                                                        }
                                                    />
                                                    <div className="icon-tooltip">
                                                        {t('tripMembersGrid.removeMemberTooltip')}
                                                    </div>
                                                </div>
                                            )}
                                            <div className="icon">
                                                <Link
                                                    to={`/profile/${member.username}`}
                                                    style={{ textDecoration: 'none', color: 'inherit' }}
                                                >
                                                    <FontAwesomeIcon icon={faUser} />
                                                    <div className="icon-tooltip">
                                                        {t('tripMembersGrid.viewProfileTooltip')}
                                                    </div>
                                                </Link>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            );
                        })}
                </div>
            </div>
            {selectedMember && (
                <ChangeRoleModal
                    isOpen={isChangeRoleModalOpen}
                    loggedInRole={role}
                    targetUsername={selectedMember.username}
                    role={selectedMember.role}
                    closeModal={() => {
                        setChangeRoleModalOpen(false);
                    }}
                    updateGrid={updateGrid}
                />
            )}
            {selectedMember && (
                <RemoveMemberModal
                    isOpen={isRemoveMemberModalOpen}
                    targetUsername={selectedMember.username}
                    closeModal={() => setRemoveMemberModalOpen(false)}
                    updateGrid={updateGrid}
                />
            )}
            <AddMemberModal
                isOpen={isAddMemberModalOpen}
                closeModal={closeAddMemberModal}
                updateGrid={updateGrid}
                loggedInRole={role}
            />
        </>
    );
};

export default TripMembersGrid;
