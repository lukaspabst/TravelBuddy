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
function RemoveMemberModal({ isOpen,targetUsername, closeModal, updateGrid }) {
    const { t } = useTranslation(); // Translation hook
    const { tripId } = useParams();
    const [message, setMessage] = useState('');
    const resetState = () => {
        setMessage('');
    };
    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await axios.patch(
                `${API_BASE_URL}/api/trips/${tripId}/removeMember/${targetUsername}`,{},
                { withCredentials: true}
            );

            if (response.status === 200) {
                console.log('Member removed successfully:', response.data);
                resetState();
                updateGrid();
                closeModal();
            } else if (response.status === 403) {
                setMessage(t('errorMessages.unauthorized'));
            } else if (response.status === 500) {
                setMessage(t('errorMessages.internalServerError'));
            } else {
                setMessage(t('errorMessages.removeMemberError'));
            }
        } catch (error) {
            console.error('Error remove member:', error);
            setMessage(t('errorMessages.removeMemberError'));
        }
    };
    const handleClose = () => {
        resetState();
        closeModal();
    };
    return (
        <ReactModal
            ariaHideApp={false}
            isOpen={isOpen}
            className="addMember-modal"
            overlayClassName="addMember-overlay"
            onRequestClose={() => {}}
        >
            <div className="modal-header removeMember-headWrapper">
                <h2>{t('tripMembersGrid.removeMember')}</h2>
                <div className="close-icon-container">
                    <FontAwesomeIcon icon={faTimes} onClick={handleClose} className="close-icon"/>
                </div>
            </div>
            <form onSubmit={handleSubmit}>
                <input type="text" value={targetUsername} readOnly/>
                {message && <p className="errorMessages">{message}</p>}
                <div className="removeMember-button-wrapper">
                <Button buttonStyle="btn--outline"  type="submit">{t('tripMembersGrid.removeMemberYes')}</Button>
                <Button buttonStyle="btn--outline"  onClick={handleClose}>{t('tripMembersGrid.removeMemberNo')}</Button>
                </div>
            </form>
        </ReactModal>
    );
}

export default RemoveMemberModal;
