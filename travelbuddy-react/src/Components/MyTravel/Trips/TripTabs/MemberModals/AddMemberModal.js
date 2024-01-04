import React, { useState } from 'react';
import ReactModal from 'react-modal';
import { useTranslation } from 'react-i18next'; // Import translation hook
import '../StylesForModalAndTabContent/Modal.scss';
import {Button} from "../../../../../Containers/Button/Button";
import {faTimes} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import axios from "axios";
import {API_BASE_URL} from "../../../../../config";
import { useParams } from 'react-router-dom';
function AddMemberModal({ isOpen, closeModal, updateGrid,loggedInRole }) {
    const { t } = useTranslation();
    const { tripId } = useParams();
    const [username, setUsername] = useState('');
    const [role, setRole] = useState('');
    const [message, setMessage] = useState('');
    const resetState = () => {
        setUsername('');
        setRole('');
        setMessage('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.patch(
                `${API_BASE_URL}/api/trips/${tripId}/addMember`,
                {
                    username,
                    role,
                    status: 'Invited'
                },
                { withCredentials: true, headers: { 'Content-Type': 'application/json' } }
            );

            if (response.status === 200) {
                console.log('Member added successfully:', response.data);
                const saveMessageResponse = await axios.post(
                    `${API_BASE_URL}/api/messages/save`,
                    {
                        type: 'invite_trip',
                        tripId,
                        username,
                    },
                    { withCredentials: true, headers: { 'Content-Type': 'application/json' } }
                );

                if (saveMessageResponse.status === 201) {
                    console.log('Message saved successfully:', saveMessageResponse.data);
                } else {
                    console.error('Error saving message:', saveMessageResponse.data);
                }

                resetState();
                updateGrid();
                closeModal();
            } else if (response.status === 403) {
                setMessage(t('errorMessages.unauthorized'));
            } else if (response.status === 500) {
                setMessage(t('errorMessages.internalServerError'));
            } else {
                setMessage(t('errorMessages.addMemberError'));
            }
        } catch (error) {
            console.error('Error adding member:', error);
            setMessage(t('errorMessages.addMemberError'));
        }
    };
    const handleClose = () => {
        resetState();
        closeModal();
    };
    const isTripAssistant = loggedInRole.role === 'Trip Assistant';

    return (
        <ReactModal
            ariaHideApp={false}
            isOpen={isOpen}
            className="addMember-modal"
            overlayClassName="addMember-overlay"
            onRequestClose={() => {}}
        >
            <div className="modal-header">
                <h2>{t('tripMembersGrid.addMember')}</h2>
                <div className="close-icon-container">
                    <FontAwesomeIcon icon={faTimes} onClick={handleClose} className="close-icon"/>
                </div>
            </div>
            <form onSubmit={handleSubmit}>
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}
                       placeholder={t('tripMembersGrid.usernamePlaceholder')}/>
                <select
                    className="select-trip-member-role"
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                >
                    <option value="">{t('tripMembersGrid.selectRole')}</option>

                        <option value="Traveler">
                            {t('tripMembersGrid.memberRole')}
                        </option>

                    {!isTripAssistant && (
                        <option value="Assistant">
                            {t('tripMembersGrid.tripAssistantRole')}
                        </option>
                    )}
                </select>
                {message && <p className="errorMessages">{message}</p>}
                <Button buttonStyle="btn--outline" type="submit">{t('tripMembersGrid.addMember')}</Button>
            </form>
        </ReactModal>
    );
}

export default AddMemberModal;
