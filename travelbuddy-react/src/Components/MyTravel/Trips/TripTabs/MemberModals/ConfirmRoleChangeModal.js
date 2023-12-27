import React, { useState } from 'react';
import ReactModal from 'react-modal';
import { useTranslation } from 'react-i18next';
import { Button } from '../../../../../Containers/Button/Button';
import { faTimes } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import axios from 'axios';
import { API_BASE_URL } from '../../../../../config';
import {useParams} from "react-router-dom";

function ConfirmRoleChangeModal({ isOpen, targetUsername, newRole, closeModal, updateGrid, loggedInRole }) {
    const { t } = useTranslation();
    const [message, setMessage] = useState('');
    const {tripId} = useParams();
    const handleConfirm = async () => {
        try {
            // Make the API call to confirm the role change...
            const response = await axios.patch(
                `${API_BASE_URL}/api/trips/${tripId}/changeUserRole`,
                {
                    targetUsername,
                    newRole,
                },
                { withCredentials: true, headers: { 'Content-Type': 'application/json' } }
            );

            console.log('Member changed successfully:', response.data);
            setMessage('');
            updateGrid();
            closeModal();

        } catch (error) {
            console.error('Error confirming role change:', error);
            setMessage(t('errorMessages.confirmRoleChangeError'));
        }
    };

    const handleClose = () => {
        setMessage('');
        closeModal();
    };
    const confirmationMessage = t('confirmRoleChange.areYouSure', { username: targetUsername });
    return (
        <ReactModal
            ariaHideApp={false}
            isOpen={isOpen}
            className="addMember-modal"
            overlayClassName="addMember-overlay"
            onRequestClose={handleClose}
        >
            <div className="confirmation-message">
                <p>{confirmationMessage}</p>
                <p>{t('confirmRoleChange.youWillLoseOrganizer')}</p>
            </div>
            <div className="removeMember-button-wrapper">
                <Button buttonStyle="btn--outline" onClick={handleConfirm}>
                    {t('confirmRoleChange.confirm')}
                </Button>
                <Button buttonStyle="btn--outline" onClick={handleClose}>
                    {t('confirmRoleChange.cancel')}
                </Button>
            </div>
        </ReactModal>
    );
}

export default ConfirmRoleChangeModal;
