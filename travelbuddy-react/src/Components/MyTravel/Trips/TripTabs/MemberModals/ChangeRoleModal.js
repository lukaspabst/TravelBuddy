import React, {useEffect, useState} from 'react';
import ReactModal from 'react-modal';
import { useTranslation } from 'react-i18next'; // Import translation hook
import '../Modal.scss';
import {Button} from "../../../../../Containers/Button/Button";
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import axios from "axios";
import {API_BASE_URL} from "../../../../../config";
import { useParams } from 'react-router-dom';
import ConfirmRoleChangeModal from "./ConfirmRoleChangeModal";
import PropTypes from 'prop-types';
function ChangeRoleModal({ isOpen, targetUsername, role, closeModal,updateGrid,loggedInRole  }) {
    const {t} = useTranslation(); // Translation hook
    const {tripId} = useParams();
    const [message, setMessage] = useState('');
    const [newRole, setNewRole] = useState('');
    const [showConfirmModal, setShowConfirmModal] = useState(false); // State for showing the confirm modal

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (newRole === 'Organizer') {
            setShowConfirmModal(true);
            closeModal();
        } else {
            // If not 'Organizer', proceed with the role change
            try {
                // Make the API call and handle the response...
                const response = await axios.patch(
                    `${API_BASE_URL}/api/trips/${tripId}/changeUserRole`,
                    {
                        targetUsername,
                        newRole,
                    },
                    {withCredentials: true, headers: {'Content-Type': 'application/json'}}
                );


                if (response.status === 200) {
                    console.log('Member changed successfully:', response.data);
                    setMessage('');
                    updateGrid();
                    closeModal();
                } else if (response.status === 403) {
                    setMessage(t('errorMessages.unauthorized'));
                } else if (response.status === 500) {
                    setMessage(t('errorMessages.internalServerError'));
                } else {
                    setMessage(t('errorMessages.changeRoleError'));
                }
            } catch (error) {
                console.error('Error adding member:', error);
                setMessage(t('errorMessages.changeRoleError'));
            }
        }
    };
        const resetState = () => {
            setMessage('');
            setNewRole('');
        };
        const handleClose = () => {
            resetState();
            closeModal();
        };
        const isTripAssistant = loggedInRole.role === 'Trip Assistant'

        return (
            <>
                <ReactModal
                    ariaHideApp={false}
                    isOpen={isOpen}
                    className="addMember-modal"
                    overlayClassName="addMember-overlay"
                    onRequestClose={() => {
                    }}
                >
                    <div className="modal-header">
                        <h2>{t('tripMembersGrid.changeUserrole')}</h2>
                        <div className="close-icon-container">
                            <FontAwesomeIcon icon={faTimes} onClick={handleClose} className="close-icon"/>
                        </div>
                    </div>
                    <form onSubmit={handleSubmit}>
                        <input type="text" value={targetUsername} readOnly/>
                        <select
                            className="select-trip-member-role"
                            value={newRole}
                            onChange={(e) => setNewRole(e.target.value)}
                            disabled={isTripAssistant}
                        >
                            <option value="">{t('tripMembersGrid.selectRole')}</option>
                            {!isTripAssistant && (
                                <>
                                    <option value="Traveler">
                                        {t('tripMembersGrid.memberRole')}
                                    </option>
                                    <option value="Assistant">
                                        {t('tripMembersGrid.tripAssistantRole')}
                                    </option>
                                    <option value="Organizer">
                                        {t('tripMembersGrid.tripOrganizerRole')}
                                    </option>
                                </>
                            )}
                        </select>
                        {message && <p className="errorMessages">{message}</p>}
                        <Button buttonStyle="btn--outline" type="submit">{t('tripMembersGrid.changeUserrole')}</Button>
                    </form>
                </ReactModal>
                {showConfirmModal && (
                    <ConfirmRoleChangeModal
                        isOpen={showConfirmModal}
                        targetUsername={targetUsername}
                        newRole={newRole}
                        closeModal={() => setShowConfirmModal(false)}
                        updateGrid={updateGrid}
                        loggedInRole={loggedInRole}
                    />
                )}
            </>
        );
}
export default ChangeRoleModal;
